package com.lg.customRedisLocker;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

public class WeakLocker {
    /** 仅仅只是为了给这个key添加一个强引用 */
    private final Integer key;
    private final ReentrantLock lock;
    public WeakLocker(Integer key,ReentrantLock lock){
        this.key=key;
        this.lock=lock;
    }
    public ReentrantLock getLock(){
        return lock;
    }
    public boolean tryLock(long timeout, TimeUnit unit)
            throws InterruptedException{
        return lock.tryLock(timeout,unit);
    }
    public void unlock(){
        lock.unlock();
    }
}
