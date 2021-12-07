package com.lg.customRedisLocker;

import java.lang.ref.WeakReference;
import java.util.*;

public class a {
    public static void main(String[] args) throws InterruptedException {
        List<WeakLocker> list=new ArrayList<>(1000);
        for (int i = 0; i < 10000; i++) {
            WeakLocker locker = LockerManager.getInstance().getLocker(new QueryRequest(String.valueOf(i), null));
            if (i<1000){
                list.add(locker);
            }
        }
        System.gc();
        Thread.sleep(10000);
        System.out.println(LockerManager.getInstance().lockMap.size());

//        WeakHashMap<Integer, WeakReference<B>> map = new WeakHashMap<Integer, WeakReference<B>>();
//        me(map);
//
//        B b = map.get(1).get();
//        System.gc();
//        System.out.println(b.value);
//        System.out.println(map);
    }

    private static void me(WeakHashMap<Integer, WeakReference<B>> map) {
        Integer a = new Integer(1);
        Integer b = new Integer(2);
        Integer c = new Integer(3);
        Integer d = new Integer(4);
        B b1 = new B(a, b);
        B b2 = new B(c, d);
        map.put(a, new WeakReference<>(b1));
        map.put(c, new WeakReference<>(b2));
    }

    static class B {
        Integer key;
        Integer value;

        public B(Integer key, Integer value) {
            this.key = key;
            this.value = value;
        }
    }

}
