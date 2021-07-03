package com.xing.gfox.hl_study.handler;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class SMessageQueue {

    //阻塞队列
    BlockingQueue<SMessage> blockingQueue = new ArrayBlockingQueue<>(50);

    public void enqueueMessage(SMessage message) {
        try {
            blockingQueue.put(message);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //从消息队列中取出消息
    public SMessage next() {
        try {
            return blockingQueue.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return  null;
    }
}
