package com.lg.redisQueue;

/**
 * @author liug132055
 */
public abstract class Request<T> {
    String key;
    T result;
    Exception requestException;
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

    public Exception getRequestException() {
        return requestException;
    }
}
