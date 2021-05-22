package com.xing.gfox.util;

import android.content.Context;

import com.xing.gfox.log.ViseLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

public class U_assets {
    //assets对应src/main/assets目录

    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String  原文件路径  如：3dtest/winter
     * @param newPath String  复制后路径  如：/3dtest
     */
    public static boolean copyFolderFromAssets(Context context, String oldPath, String newPath) {
        try {
            String[] fileNames = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
            if (fileNames != null) {
                if (fileNames.length > 0) {//如果是目录
                    File file = new File(newPath);
                    file.mkdirs();//如果文件夹不存在，则递归
                    for (String fileName : fileNames) {
                        copyFolderFromAssets(context, oldPath + "/" + fileName, newPath + "/" + fileName);
                    }
                } else {//如果是文件
                    InputStream is = context.getAssets().open(oldPath);
                    FileOutputStream fos = new FileOutputStream(new File(newPath));
                    byte[] buffer = new byte[1024];
                    int byteCount;
                    while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                        fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
                    }
                    fos.flush();//刷新缓冲区
                    is.close();
                    fos.close();
                }
                return true;
            } else {
                ViseLog.e("请检查文件或目录名是否正确！");
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            ViseLog.e("复制文件出现异常！\n" + e);
            return false;
        }
    }

    /**
     * 拷贝资源文件夹中的文件到默认地址. 如果文件已经存在,则直接返回文件路径
     *
     * @return 返回 拷贝文件的目标路径
     */
    public static boolean copyFileFromAssets(Context context, String assetsName, String newPath) {
        if (!U_file.isFolderExist(newPath)) {
            U_file.createFileFolder(newPath);
        }
        try {
            InputStream is = context.getAssets().open(assetsName);
            FileOutputStream fos = new FileOutputStream(newPath);
            byte[] buffer = new byte[7168];
            int count = 0;
            while ((count = is.read(buffer)) > 0) {
                fos.write(buffer, 0, count);
            }
            fos.close();
            is.close();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
