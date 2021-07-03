package com.xing.gfox.util.DialogTest;

import com.xing.gfox.log.ViseLog;

public class myObjectPool extends ObjectPool {
    public myObjectPool(int initialCapacity, int maxCapacity) {
        super(initialCapacity, maxCapacity);
    }

    public myObjectPool(int maxCapacity) {
        super(maxCapacity);
    }

    @Override
    protected Object create() {
        return new Object();
    }

    public void test() {
        myObjectPool pool = new myObjectPool(2, 4);
        Object o1 = pool.acquire();
        Object o2 = pool.acquire();
        Object o3 = pool.acquire();
        Object o4 = pool.acquire();

        ViseLog.d(o1.hashCode());
        ViseLog.d(o2.hashCode());
        ViseLog.d(o3.hashCode());
        ViseLog.d(o4.hashCode());
    }
}
