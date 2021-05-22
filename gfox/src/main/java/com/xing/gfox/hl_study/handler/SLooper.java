package com.xing.gfox.hl_study.handler;

/**
 * 循环
 */
public class SLooper {
    static final ThreadLocal<SLooper> sThreadLocal = new ThreadLocal<>();
    final SMessageQueue mQueue;

    private SLooper() {
        //构造唯一消息队列
        mQueue = new SMessageQueue();
    }

    public static void prepare() {
        if (sThreadLocal.get() != null) {
            throw new RuntimeException("Only one sLooper may be created per thread");
        }
        sThreadLocal.set(new SLooper());
    }

    public static SLooper myLooper() {
        return sThreadLocal.get();
    }

    public static void loop() {
        //从全局ThreadLocalMap中获取唯一， looper对象
        SLooper sLooper = myLooper();
        SMessageQueue mQueue = sLooper.mQueue;

        while (true) {
            //循环获取消息队列中的消息
            SMessage message = mQueue.next();
            if (message != null) {
                message.target.dispatchMessage(message);
            }
        }
    }
}
