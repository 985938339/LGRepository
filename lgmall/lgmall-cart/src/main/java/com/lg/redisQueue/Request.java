package com.lg.redisQueue;

/**
 * @author liug132055
 */
public abstract class Request<T> {
    String key;
    T result;
    Throwable requestThrowable;
    /**
     * 判断请求是否完成
     */
    boolean isDone;

    /**
     * 执行请求
     */
    abstract void execute();

    public boolean isDone() {
        return isDone;
    }

    public String getKey() {
        return key;
    }

    public T getResult() {
        return result;
    }

    public Throwable getRequestThrowable() {
        return requestThrowable;
    }
}
