package com.xing.gfox.util;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.os.EnvironmentCompat;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import com.xing.gfox.base.app.AppInit;

public class U_img {
    // 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
    public static String imgToStr64(InputStream in) throws IOException {
        if (in == null) return "";
        byte[] data = new byte[in.available()];
        in.read(data);
        in.close();
        // 对字节数组Base64编码
        return com.xing.gfox.util.U_base64.encode(data);// 返回Base64编码过的字节数组字符串
    }

    public static String imgToStr64(String imgFile) throws IOException {
        if (com.xing.gfox.util.U_file.isFileExist(imgFile)) {
            return imgToStr64(new FileInputStream(imgFile));
        }
        return "";
    }

    /**
     * 增加base64头部信息，应该是要用不到才对
     *
     * @param imgStr 图片base64后的数据
     * @return 图片base64后的数据
     */
    public static String addBase64ImgHeader(String imgStr) {
        return "data:image/jpeg;base64," + imgStr;
    }

    /**
     * 创建一条图片地址uri,用于保存拍照后的照片
     *
     * @return 图片的uri
     */
    public static Uri createImagePathUri() {
        String status = Environment.getExternalStorageState();
        SimpleDateFormat timeFormatter = new SimpleDateFormat(com.xing.gfox.util.U_time.yyyyMMdd_HHmmss, Locale.getDefault());
        long time = System.currentTimeMillis();
        String imageName = timeFormatter.format(new Date(time));
        // ContentValues是我们希望这条记录被创建时包含的数据信息
        ContentValues values = new ContentValues(2);
        values.put(MediaStore.Images.Media.DISPLAY_NAME, imageName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return AppInit.getAppContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        } else {
            return AppInit.getAppContext().getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, values);
        }
    }

    public static File createImageFile() {
        String timeStamp = new SimpleDateFormat(com.xing.gfox.util.U_time.yyyyMMdd_HHmmss, Locale.getDefault()).format(new Date());
        String imageFileName = String.format("JPEG_%s.jpg", timeStamp);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        if (!storageDir.exists()) {
            storageDir.mkdir();
        }
        File tempFile = new File(storageDir, imageFileName);
        if (!Environment.MEDIA_MOUNTED.equals(EnvironmentCompat.getStorageState(tempFile))) {
            return null;
        }
        return tempFile;
    }
}
