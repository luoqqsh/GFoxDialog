package com.xing.gfox.hl_study.handler;

public class SHandler {

    private SLooper dnLooper;
    private SMessageQueue dnMessageQueue;

    public SHandler() {
        //全局looper对象
        dnLooper = SLooper.myLooper();
        if (dnLooper == null) {
            throw new RuntimeException(
                    "Can't create handler inside thread " + Thread.currentThread()
                            + " that has not called Looper.prepare()");
        }
        dnMessageQueue = dnLooper.mQueue;
    }

    //重写使用
    public void handleMessage(SMessage msg) {
    }

    public void sendMessage(SMessage message) {
        //将消息放入消息队列
        enqueueMessage(message);
    }

    private void enqueueMessage(SMessage message) {
        //赋值当前消息
        message.target = this;

        //使用dnMessageQueue，将消息传入-入队
        dnMessageQueue.enqueueMessage(message);
    }

    public void dispatchMessage(SMessage message) {
        //调度消息
        handleMessage(message);
    }
}
