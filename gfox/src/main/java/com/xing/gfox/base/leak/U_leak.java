package com.xing.gfox.base.leak;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.lang.reflect.Field;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;

import com.xing.gfox.util.U_device;

public class U_leak {

    //外部调用
    public static void fixHuaWeiLeak(Activity mActivity) {
        if (U_device.INSTANCE.isHuaWeiPhone()) {
            fixInputMethodManagerLeak(mActivity);
            reflectGC(mActivity);
        }
    }

    //解决华为手机HwChangeButtonWindowCtrl导致的内存泄漏
    public static void reflectGC(Activity activity) {
        try {
            @SuppressLint("PrivateApi")
            Class clazz = Class.forName("android.app.HwChangeButtonWindowCtrl");
            Field field = clazz.getDeclaredField("mInstanceMap");
            field.setAccessible(true);

            HashMap hashMap = (HashMap) field.get(field.getType().newInstance());
//            HashMap h = (HashMap) field.getType().newInstance();
            for (Object key : hashMap.keySet()) {
                Object o = hashMap.get(key);
                if (o != null) {
                    Field f = o.getClass().getDeclaredField("mActivity");
                    f.setAccessible(true);

                    Object mCurRootView = f.get(o);
                    if (mCurRootView instanceof Activity) {
                        Context context = (Context) mCurRootView;
                        if (context == activity) {
                            f.set(o, null);
                        }
                    }
                }
            }
        } catch (Exception e) {
//            e.printStackTrace();
        }
    }

    //修复华为手机输入法管理导致的内存泄漏
    public static void fixInputMethodManagerLeak(Context destContext) {
        if (destContext == null) {
            return;
        }

        InputMethodManager imm = (InputMethodManager) destContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm == null) {
            return;
        }

        String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView", "mLastSrvView"};
        Field f;
        Object obj_get;
        for (String param : arr) {
            try {
                f = imm.getClass().getDeclaredField(param);
                if (!f.isAccessible()) {
                    setFieldAccessible(f);
                }
                obj_get = f.get(imm);
                if (obj_get instanceof View) {
                    View v_get = (View) obj_get;
                    //不判断的话，可能导致有输入框的页面无法弹出软键盘的问题
//                    if (v_get.getContext() == destContext) { // 被InputMethodManager持有引用的context是想要目标销毁的
                    f.set(imm, null); // 置空，破坏掉path to gc节点
//                    }
                }
            } catch (Throwable t) {
//                t.printStackTrace();
            }
        }
    }

    private static void setFieldAccessible(final Field field) {
        AccessController.doPrivileged((PrivilegedAction) () -> {
            field.setAccessible(true);
            return null;
        });
    }
}
