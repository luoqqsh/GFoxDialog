package com.xing.gfox.hl_study.eventbus;

import java.lang.reflect.Method;

//注册类中的注册方法信息
public class SubscribleMethod {
    private Method method;//注册方法
    private SThreadMode threadMode;//线程类型
    private Class<?> eventType;//参数类型

    public SubscribleMethod(Method method, SThreadMode threadMode, Class<?> eventType) {
        this.method = method;
        this.threadMode = threadMode;
        this.eventType = eventType;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public SThreadMode getThreadMode() {
        return threadMode;
    }

    public void setThreadMode(SThreadMode threadMode) {
        this.threadMode = threadMode;
    }

    public Class<?> getEventType() {
        return eventType;
    }

    public void setEventType(Class<?> eventType) {
        this.eventType = eventType;
    }
}
