package com.xing.gfoxdialog.zuoye.javajvm;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class 引用单例 {
    //强引用，若想中断，设置其值为null
    Object obj = new Object();
    //软引用：在系统将要发出内存溢出异常之前，会把这些对象进行回收
    SoftReference<String> softRef = new SoftReference<>("str");
    //弱引用：在下一次进行垃圾回收的时候，无论内存是否足够，都会进行回收
    WeakReference<String> weakRef = new WeakReference<>("str");
    //虚引用：用来跟踪对象被垃圾回收器回收的活动，必须和下面的引用队列一起使用。
    ReferenceQueue<String> refer = new ReferenceQueue<>();

    //单例模式-懒汉式的延迟加载
    private 引用单例() {
    }

    public static 引用单例 getInstance() {
        return JavaJvmHolder.sInstance;
    }

    //静态内部类
    private static class JavaJvmHolder {
        private static final 引用单例 sInstance = new 引用单例();
    }
//------------------------------------------------------------------------------------------------//
//    //单例模式2-安全，防反射
//    public class MyJavaJVM implements Serializable {
//        private static final long serialVersionUID = 7525352357068359999L;
//        private static volatile MyJavaJVM sInstance;
//        private static boolean isFirst = true;
//
//        private MyJavaJVM() {
//            if (isFirst) {
//                isFirst = false;
//            } else {
//                throw new RuntimeException("破坏了单例，第二个实例创建失败");
//            }
//        }
//
//        public static MyJavaJVM getInstance() {
//            if (sInstance == null) {
//                synchronized (MyJavaJVM.class) {
//                    if (sInstance == null) {
//                        sInstance = new MyJavaJVM();
//                    }
//                }
//            }
//            return sInstance;
//        }
//
//        //序列化安全
//        private Object readResolve() {
//            return sInstance;
//        }
//    }
}
