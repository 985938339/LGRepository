package com.lg.customRedisLocker;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liug132055
 */
public class LockerManager {
    public final Map<Integer, WeakReference<WeakLocker>> lockMap = new WeakHashMap<>();
    private static final LockerManager manager = new LockerManager();

    public static LockerManager getInstance() {
        return manager;
    }


    /**
     * 获取该请求对应的锁
     *
     * @param request 请求
     * @return
     */
    public WeakLocker getLocker(Request request) {
        int h = request.getKey().hashCode();
        int index = request.getKey() == null ? 0 : h ^ (h >>> 16);
        WeakReference<WeakLocker> reference;
        synchronized (this) {
            reference = lockMap.get(index);
            if (Objects.isNull(reference)) {
                ReentrantLock lock = new ReentrantLock();
                Integer key = new Integer(index);
                WeakLocker locker = new WeakLocker(key, lock);
                reference = new WeakReference<>(locker);
                lockMap.put(key, reference);
            }
        }
        return reference.get();
    }
}
