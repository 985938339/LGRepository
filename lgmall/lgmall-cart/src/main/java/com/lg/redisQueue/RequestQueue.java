package com.lg.redisQueue;

import java.util.concurrent.ArrayBlockingQueue;

/**
 * @author liug132055
 */
public class RequestQueue {
    private ArrayBlockingQueue<Request> queue;

    public RequestQueue() {
        queue = new ArrayBlockingQueue(100);
    }

    /**
     * 处理请求
     */
    public void process() {
        while (true) {
            Request request = null;
            try {
                request = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            request.execute();
        }
    }

    /**
     * 如果队列中有该读请求，那么就不插入，直接返回队列中的请求
     * @param request 请求
     * @return
     * @throws InterruptedException
     */
    public<T> Request<T> enqueue(Request<T> request) throws InterruptedException {
        Request<T> request1 = getIfPresent(request);
        if (request1==null){
            queue.put(request);
            return request;
        }
        return request1;
    }

    private<T> Request<T> getIfPresent(Request<T> request){
        if (!(request instanceof QueryRequest)){
            return null;
        }
        for (Request request2:queue){
            //就是因为这里没有判断请求类型，把队列中的更新当做读请求，导致大量读请求丢失了
            if (request2.getKey().equals(request.getKey())&&(request2 instanceof QueryRequest)){
                return request2;
            }
        }
        return null;
    }
}
