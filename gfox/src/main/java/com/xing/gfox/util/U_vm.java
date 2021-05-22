package com.xing.gfox.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Process;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.security.auth.x500.X500Principal;

public class U_vm {
    /**
     * 判断CPU是否支持64位
     * 安卓5.0以上
     *
     * @return 结果
     */
    public static boolean is64bit() {
        boolean is64bit = false;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            is64bit = Build.SUPPORTED_64_BIT_ABIS.length > 0;
        } else {
            try {
                BufferedReader localBufferedReader = new BufferedReader(new FileReader("/proc/cpuinfo"));
                if (localBufferedReader.readLine().contains("aarch64")) {
                    is64bit = true;
                }
                localBufferedReader.close();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
        return is64bit;
    }

    /**
     * 判断CPU是否支持64位
     * 安卓6.0以上
     *
     * @return 结果
     */
    public static boolean is64bit60() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Process.is64Bit();
        }
        return false;
    }

    /**
     * 5.0、6.0均可用
     *
     * @param context 上下文
     * @return 结果
     */
    public static boolean isART64(Context context) {
        final String fileName = "art";
        try {
            ClassLoader classLoader = context.getClassLoader();
            Class<?> cls = ClassLoader.class;
            Method method = cls.getDeclaredMethod("findLibrary", String.class);
            Object object = method.invoke(classLoader, fileName);
            if (object != null) {
                return ((String) object).contains("lib64");
            }
        } catch (Exception e) {

        }

        return false;
    }

    /**
     * 检测系统使用的是art还是dalvik
     *
     * @return 结果
     */
    public static boolean getIsArtInUse() {
        final String vmVersion = System.getProperty("java.vm.version");
        return vmVersion != null && vmVersion.startsWith("2");
    }

    /**
     * 获取手机当前的Runtime
     *
     * @return 正常情况下可能取值Dalvik, ART, ART debug build;
     */
    public static String getCurrentRuntimeValue() {
        try {
            @SuppressLint("PrivateApi")
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            try {
                Method get = systemProperties.getMethod("get", String.class,
                        String.class);
                try {
                    final String value = (String) get.invoke(systemProperties,
                            "persist.sys.dalvik.vm.lib",
                            /* Assuming default is */"Dalvik");
                    if ("libdvm.so".equals(value)) {
                        return "Dalvik";
                    } else if ("libart.so".equals(value)) {
                        return "ART";
                    } else if ("libartd.so".equals(value)) {
                        return "ART debug build";
                    }
                    return value;
                } catch (IllegalAccessException e) {
                    return "IllegalAccessException";
                } catch (IllegalArgumentException e) {
                    return "IllegalArgumentException";
                } catch (InvocationTargetException e) {
                    return "InvocationTargetException";
                }
            } catch (NoSuchMethodException e) {
                return "SystemProperties.get(String key, String def) method is not found";
            }
        } catch (ClassNotFoundException e) {
            return "SystemProperties class is not found";
        }
    }



}
