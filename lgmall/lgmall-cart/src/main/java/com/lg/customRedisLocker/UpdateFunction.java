package com.lg.customRedisLocker;

/**
 * @author liug132055
 */
public interface UpdateFunction<T> {
    /**
     * 需要执行查询的动作,一般是查数据库和写入redis的操作
     *
     * @param key   要更新的key
     * @param value 要更新的value
     */
    public void updateExecution(String key, T value);
}
