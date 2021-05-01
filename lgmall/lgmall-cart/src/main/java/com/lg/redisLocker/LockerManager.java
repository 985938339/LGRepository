package com.lg.redisLocker;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liug132055
 */
public class LockerManager {
    private static final int LOCKER_NUM = 200;
    private List<ReentrantLock> lockList;
    private static LockerManager manager = new LockerManager();

    public static LockerManager getInstance() {
        return manager;
    }

    private LockerManager() {
        initial();
    }

    private void initial() {
        lockList = new ArrayList<>(LOCKER_NUM);
        for (int i = 0; i < LOCKER_NUM; i++) {
            lockList.add(new ReentrantLock(true));
        }
    }

    /**
     * 请求路由,发送队列
     *
     * @param request 请求
     * @return
     */
    public ReentrantLock getLocker(Request request) {
        int h = request.getKey().hashCode();
        int hash = request.getKey() == null ? 0 : h ^ (h >>> 16);
        int index = hash & (LOCKER_NUM - 1);
        return lockList.get(index);
    }
}
