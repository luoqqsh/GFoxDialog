package com.xing.gfox.util.DialogTest;

import android.util.SparseArray;

/**
 * 对象复用池
 */
public abstract class ObjectPool<T> {
    private SparseArray<T> freepool;//空闲-可用
    private SparseArray<T> lentpool;//正在使用-不可用
    //池的最大值
    private int maxCapacity;


    public ObjectPool(int initialCapacity, int maxCapacity) {
        //初始化对象池
        initalize(initialCapacity);
        this.maxCapacity = maxCapacity;
    }

    private void initalize(int initialCapacity) {
        lentpool = new SparseArray<>();
        freepool = new SparseArray<>();
        for (int i = 0; i < initialCapacity; i++) {
            freepool.put(i, create());
        }
    }

    protected abstract T create();

    public ObjectPool(int maxCapacity) {
        this(maxCapacity / 2, maxCapacity);
    }

    /**
     * 申请对象
     */
    public T acquire() {
        T t = null;
        synchronized (freepool) {
            int freesize = freepool.size();
            for (int i = 0; i < freesize; i++) {
                int key = freepool.keyAt(i);
                t = freepool.get(key);
                if (t != null) {
                    this.lentpool.put(key, t);
                    this.freepool.remove(key);
                    return t;
                }
            }
            //如果没对象可取
            if (t == null && lentpool.size() + freesize < maxCapacity) {
                t = create();
                lentpool.put(lentpool.size() + freesize, t);
            }
        }
        return t;
    }

    /**
     * 释放对象
     */
    public void release(T t) {
        if (t == null) {
            return;
        }
        int key = lentpool.indexOfValue(t);
        //释放前把这个对象交给用户处理
        restore(t);
        this.freepool.put(key, t);
        this.lentpool.remove(key);
    }

    protected void restore(T t) {

    }
}
