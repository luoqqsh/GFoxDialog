package com.xing.gfox.util;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class U_gson {
    private Gson mGson;
    private static U_gson gsonUtil;

    private U_gson() {
    }

    public void release() {
        mGson = null;
        gsonUtil = null;
    }

    public Gson getGson() {
        if (mGson == null) {
            mGson = new GsonBuilder()
                    .setLenient()
                    .disableHtmlEscaping()//禁止html转义，不然对url地址会转成unicode编码
                    .create();
        }
        return mGson;
    }

    public static U_gson instance() {
        if (gsonUtil == null) {
            gsonUtil = new U_gson();
        }
        return gsonUtil;
    }

    public <T> T fromJson(String json, Class<T> classOfT) {
        if (TextUtils.isEmpty(json) || classOfT == null) {
            return null;
        }
        try {
            return getGson().fromJson(json, classOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T fromJson(String json, Type type) {
        if (TextUtils.isEmpty(json) || type == null) {
            return null;
        }
        try {
            return getGson().fromJson(json, type);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String toJson(Object object) {
        if (object == null) {
            return null;
        }
        return getGson().toJson(object);
    }
}