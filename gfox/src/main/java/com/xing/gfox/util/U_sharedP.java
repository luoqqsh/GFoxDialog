package com.xing.gfox.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.IOException;

/**
 * @author luoqqsh
 */
public class U_sharedP {
    /**
     * 保存在手机里面的文件名
     */
    private static final String FILE_NAME = "share_date";

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param context 上下文
     * @param key     key
     * @param object  类型
     */
    public static boolean setParamCommit(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        try {
            editor.putString(key, U_stream.objectToStream(object));
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return editor.commit();
    }

    public static void setParam(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        try {
            editor.putString(key, U_stream.objectToStream(object));
            editor.apply();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * 注意：默认值和你返回的object类型要同一类型
     *
     * @param context       上下文
     * @param key           key
     * @param defaultObject 默认
     * @return object数据
     */
    public static <T> T getParam(Context context, String key, T defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        String o = sp.getString(key, "");
        try {
            if (!TextUtils.isEmpty(o)) {
                return U_stream.streamToObject(o);
            } else {
                return defaultObject;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return defaultObject;
        }
    }

    public static boolean clear(Context context) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.edit().clear().commit();
    }

    public static boolean remove(Context context, String key) {
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        if (TextUtils.isEmpty(key)) {
            return false;
        }
        return sp.edit().remove(key).commit();
    }
}