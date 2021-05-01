package com.lg.redisQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * @author liug132055
 */
public class QueueManager {
    private static final int QUEUE_NUM = 20;
    private ExecutorService threadPool;
    private List<RequestQueue> queues;
    private static QueueManager manager = new QueueManager();

    public static QueueManager getInstance() {
        return manager;
    }

    private QueueManager() {
        initial();
    }

    private void initial() {
        ThreadFactory threadFactory = Executors.defaultThreadFactory();
        threadPool = new ThreadPoolExecutor(QUEUE_NUM, 20, 200, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(),
                threadFactory,
                new ThreadPoolExecutor.AbortPolicy());
        queues = new ArrayList<>(QUEUE_NUM);
        for (int i = 0; i < QUEUE_NUM; i++) {
            RequestQueue queue = new RequestQueue();
            queues.add(queue);
            threadPool.execute(queue::process);
        }
    }

    /**
     * 请求路由,发送队列
     *
     * @param request 请求
     * @return
     */
    public <T> Request<T> dispatch(Request<T> request) {
        int h = request.getKey().hashCode();
        int hash = request.getKey() == null ? 0 : h ^ (h >>> 16);
        int index = hash & (QUEUE_NUM - 1);
        try {
            request = queues.get(index).enqueue(request);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return request;
    }

    public void shunDown() {

    }
}
