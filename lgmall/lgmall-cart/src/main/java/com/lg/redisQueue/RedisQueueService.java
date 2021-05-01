package com.lg.redisQueue;

import com.lg.exception.TimeOutException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 用于给队列发送消息，使更新和查询操作串行化
 *
 * @author liug132055
 */
@Service
public class RedisQueueService {
    @Resource
    PlatformTransactionManager platformTransactionManager;
    @Resource
    TransactionDefinition definition;

    /**
     * 先从redis缓存获取，获取不到，再执行该方法
     *
     * @param key           要查询的key
     * @param timeout       超时时间，以毫秒为单位
     * @param queryFunction 定义查询操作
     * @return
     */
    public <T> T query(String key, long timeout, QueryFunction<T> queryFunction) throws Throwable {
        Request<T> request = new QueryRequest<>(key, queryFunction);
        request = QueueManager.getInstance().dispatch(request);
        try {
            long deadline = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) + timeout;
            synchronized (request) {
                while (!request.isDone()) {
                    if (timeout <= 0)
                        break;
                    request.wait(timeout);
                    timeout = deadline - TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (request.getRequestThrowable() != null) {
            throw request.getRequestThrowable();
        }
        return request.getResult();
    }

    /**
     * 执行更新操作
     *
     * @param key            要更新的key
     * @param value          要更新的值
     * @param timeout        超时时间，以毫秒为单位
     * @param updateFunction 定义更新操作
     */
    public <T> void update(String key, T value, long timeout, UpdateFunction<T> updateFunction) throws Throwable {
        //将传进来的更新操作再包装一层事务，生成请求
        Request<T> request = new UpdateRequest<>(key, value, (key1, value1) -> {
            TransactionStatus status = platformTransactionManager.getTransaction(definition);
            try {
                if (updateFunction != null) {
                    updateFunction.updateExecution(key1, value1);
                }
                System.out.println("提交");
                platformTransactionManager.commit(status);
            } catch (Throwable e) {
                System.out.println("回滚");
                platformTransactionManager.rollback(status);
                throw e;
            }
        });
        QueueManager.getInstance().dispatch(request);
        try {
            long deadline = TimeUnit.NANOSECONDS.toMillis(System.nanoTime()) + timeout;
            synchronized (request) {
                while (!request.isDone()) {
                    if (timeout <= 0)
                        break;
                    request.wait(timeout);
                    timeout = deadline - TimeUnit.NANOSECONDS.toMillis(System.nanoTime());
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        //在发生异常以及更新超时的时候需要给controller抛出异常
        if (request.getRequestThrowable() != null || !request.isDone()) {
            throw request.isDone() ? request.getRequestThrowable() : new TimeOutException("更新超时");
        }
    }
}
