package com.lg.customRedisLocker;

/**
 * @author liug132055
 */
public abstract class Request<T> {
    String key;
    T result;

    /**
     * 执行请求
     */
    abstract void execute();

    public String getKey() {
        return key;
    }

    public T getResult() {
        return result;
    }
}
