package com.xing.gfox_glide;

import android.content.Context;
import android.os.Looper;

import com.bumptech.glide.Glide;

import java.io.File;
import java.math.BigDecimal;

/**
 * @author : Created by ChenJiaLiang on 2017/1/16.
 * Email : 576507648@qq.com
 */
public class GlideCacheUtil {
    private static GlideCacheUtil instance;
    private final Context context;
    // 图片缓存最大容量，100M，根据自己的需求进行修改  getMaxCacheSize
    public static int GLIDE_CACHE_SIZE = 100 * 1024 * 1024;
    // 图片缓存子目录
    public static final String GLIDE_CACHE_DIR = "glide_cache";

    private GlideCacheUtil(Context context) {
        this.context = context.getApplicationContext();
    }

    public static int getMaxCacheSize() {
        return GLIDE_CACHE_SIZE;
    }

    public static GlideCacheUtil getInstance(Context context) {
        if (null == instance) {
            instance = new GlideCacheUtil(context);
        }
        return instance;
    }

    // 获取Glide磁盘缓存大小
    public String getCacheSize() {
        return getFormatSize(getCacheFileSize(), 2);
    }

    public long getCacheFileSize() {
        return getFileSize(getCacheFile());
    }

    public File getCacheFile() {
        return new File(context.getCacheDir() + "/" + GLIDE_CACHE_DIR);
    }

    // 获取Glide磁盘缓存大小
    public long getCacheLongSize() {
        return getCacheFileSize();
    }

    // 清除Glide磁盘缓存，自己获取缓存文件夹并删除方法
    public boolean cleanCatchDisk() {
        return deleteFile(getCacheFile());
    }

    // 清除图片磁盘缓存，调用Glide自带方法
    public boolean clearCacheDiskSelf() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 清除Glide内存缓存
    public boolean clearCacheMemory() {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean deleteFile(File file) {
        boolean isSuccess = true;
        if (file == null || !file.exists()) return true;
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            for (File f : fileList) {
                if (!deleteFile(f)) {
                    isSuccess = false;
                }
            }
        } else {
            return file.delete();
        }
        return isSuccess;
    }

    // 获取指定文件夹内所有文件大小的和
    private long getFileSize(File... files) {
        long size = 0;
        for (File file : files) {
            if (file == null || !file.exists()) return size;
            if (file.isDirectory()) {
                File[] fileList = file.listFiles();
                for (File f : fileList) {
                    size = size + getFileSize(f);
                }
            } else {
                size += file.length();
            }
        }
        return size;
    }

    // 格式化单位
    private String getFormatSize(long size, int dotSize) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "B";
        }
        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(dotSize, BigDecimal.ROUND_HALF_UP).toPlainString() + "KB";
        }
        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(dotSize, BigDecimal.ROUND_HALF_UP).toPlainString() + "M";
        }
        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(dotSize, BigDecimal.ROUND_HALF_UP).toPlainString() + "G";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(dotSize, BigDecimal.ROUND_HALF_UP).toPlainString() + "T";
    }
}
