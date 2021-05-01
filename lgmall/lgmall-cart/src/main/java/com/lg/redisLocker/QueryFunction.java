package com.lg.redisLocker;

/**
 * @author liug132055
 */
public interface QueryFunction<T> {
    /**
     * 需要执行查询的动作,一般是查数据库和写入redis的操作
     *
     * @param key 要查询的key
     * @return 查询得到的值
     */
    public T queryExecution(String key);
}
