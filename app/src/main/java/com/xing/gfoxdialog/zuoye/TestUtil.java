package com.xing.gfoxdialog.zuoye;

import android.util.Log;

public class TestUtil {

    public static volatile int i = 0;

    public static Object obj = new Object();

    public static boolean flag_A = true;
    public static boolean flag_B = false;
    public static boolean flag_C = false;

    static class PrintThread_A implements Runnable{
        public void run(){
            while(i < 26){
                if(flag_A){
                    synchronized (obj){
                        System.out.println(Thread.currentThread().getName() + (char)('A' + i));
                        ++i;
                        flag_A = false;
                        flag_B = true;
                    }
                }
            }
        }
    }

    static class PrintThread_B implements Runnable{
        public void run(){
            while(i < 26){
                if(flag_B){
                    synchronized (obj){
                        System.out.println(Thread.currentThread().getName() + (char)('A' + i));
                        ++i;
                        flag_B = false;
                        flag_C = true;
                    }
                }
            }
        }
    }

    static class PrintThread_C implements Runnable{
        public void run(){
            while(i < 26){
                if(flag_C){
                    synchronized (obj){
                        System.out.println(Thread.currentThread().getName() + (char)('A' + i));
                        ++i;
                        flag_C = false;
                        flag_A = true;
                    }
                }
            }
        }
    }
    public static void printABC() {
        String[] a = {"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
        for (int i = 0; i < a.length; i++) {
            print(a[i]);
        }


        Thread t1 = new Thread(new PrintThread_A(), "1");
        Thread t2 = new Thread(new PrintThread_B(), "2");
        Thread t3 = new Thread(new PrintThread_C(), "3");

        t1.start();
        t2.start();
        t3.start();

        System.out.println("OK");
    }

    private static void print(String a) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.e("print", a);
            }
        }).start();
    }
}
