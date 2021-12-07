package com.lg.customRedisLocker;


import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * 用于给队列发送消息，使更新和查询操作串行化
 *
 * @author liug132055
 */
@Service("redisQueueService2")
public class RedisQueueService {

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
        WeakLocker lock = LockerManager.getInstance().getLocker(request);
        try {
            if (lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
                request.execute();
            } else {
                System.out.println("超时");
                throw new Throwable("查询超时");
            }
        } finally {
            lock.unlock();
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
        Request<T> request = new UpdateRequest<>(key, value, updateFunction);
        WeakLocker lock = LockerManager.getInstance().getLocker(request);
        try {
            if (lock.tryLock(timeout, TimeUnit.MILLISECONDS)) {
                request.execute();
            } else {
                System.out.println("超时");
                throw new Throwable("更新超时");
            }
        } finally {
            lock.unlock();
        }
    }
}
