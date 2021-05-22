package com.xing.gfox.util;

import android.content.Context;
import android.text.TextUtils;
import android.util.Base64;


import com.xing.gfox.log.ViseLog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class U_cachefile {

    private static U_cachefile cachefile;
    private String parentPath;

    private U_cachefile(Context context) {
        parentPath = U_file.getAppCacheFolder(context);
        // /storage/emulated/0/Android/data/x.com.dialogdialog/cache
    }

    public static U_cachefile instance(Context context) {
        if (cachefile == null) {
            cachefile = new U_cachefile(context);
        }
        return cachefile;
    }

    public <T> T readObject(String key) {
        return readObject(key, null);
    }

    public <T> T readObject(String key, T defaultValue) {
        if (TextUtils.isEmpty(key)) {
            return defaultValue;
        }
        File file = new File(parentPath, U_encrypt.Md5_16(key));
        if (file.isFile() && file.exists() && file.length() > 0) {
            String string = U_file.readStringFromFile(file);
            try {
                if (TextUtils.isEmpty(string)) {
                    return defaultValue;
                }
                byte[] base64 = Base64.decode(string.getBytes(), Base64.DEFAULT);
                ByteArrayInputStream is = new ByteArrayInputStream(base64);
                T object1;
                ObjectInputStream bis = new ObjectInputStream(is);
                Object object = bis.readObject();
                object1 = (T) object;
                return object1;
            } catch (Exception e) {
                e.printStackTrace();
                remove(key);
                ViseLog.e("UError", "读取异常");
            }
        }
        return defaultValue;
    }

    public void saveObject(String key, Object object) {
        if (TextUtils.isEmpty(key)) {
            return;
        }
        if (object == null) {
            remove(key);
            return;
        }
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            oos.writeObject(object);
            String text = new String(Base64.encode(os.toByteArray(), Base64.DEFAULT));
            U_file.writeStringToFile(parentPath, U_encrypt.Md5_16(key), text);
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.e("UError", "写入异常");
        }
    }

    public void remove(String key) {
        if (!TextUtils.isEmpty(key)) {
            File file = new File(parentPath, U_encrypt.Md5_16(key));
            if (file.exists()) {
                if (!file.delete()) {
                    ViseLog.e("UError", "删除旧缓存失败");
                }
            }
        }
    }

    public void clear() {
        U_file.deleteFolder(parentPath);
    }
}
