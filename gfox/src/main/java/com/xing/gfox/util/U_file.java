package com.xing.gfox.util;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.xing.gfox.R;
import com.xing.gfox.log.ViseLog;
import com.xing.gfox.media.U_media;

public class U_file {
//  data目录        Environment.getDataDirectory() = /data
//  下载缓存         Environment.getDownloadCacheDirectory()= /cache
//  外置SD卡目录     Environment.getExternalStorageDirectory() = /mnt/sdcard
//    Environment.getExternalStoragePublicDirectory(“test”)= /mnt/sdcard/test
//    SYSTEM       Environment.getRootDirectory() = /system
//    getPackageCodePath() = /data/app/com.my.app-1.apk
//    getPackageResourcePath() = /data/app/com.my.app-1.apk
//    getCacheDir() = /data/data/com.my.app/cache
//    getDatabasePath("test")= /data/data/com.my.app/databases/test
//    getDir("test", Context.MODE_PRIVATE)= /data/data/com.my.app/app_test
//    getExternalCacheDir()	= /mnt/sdcard/Android/data/com.my.app/cache
//    getExternalFilesDir("test")= /mnt/sdcard/Android/data/com.my.app/files/test
//    getExternalFilesDir(null) = /mnt/sdcard/Android/data/com.my.app/files
//    getFilesDir()	= /data/data/com.my.app/files

    //以下方法在安卓Q之后的版本能获取得到，但无法使用
    public static final String SDROOT = Environment.getExternalStorageDirectory().getAbsolutePath();
    public static final String DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getAbsolutePath();
    public static final String MUSIC = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).getAbsolutePath();
    public static final String PICTURES = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).getAbsolutePath();
    public static final String MOVIE = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES).getAbsolutePath();
    public static final String ALARMS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_ALARMS).getAbsolutePath();
    public static final String RINGTONES = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_RINGTONES).getAbsolutePath();
    public static final String SYSTEM = Environment.getRootDirectory().getAbsolutePath();
    public static final String DOWNLOADS = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
    public static final String CAMERA = DCIM + File.separator + "Camera";
    public static final String DIALOG = SDROOT + File.separator + "dialog";
    public static final String QQFILE1 = SDROOT + File.separator + "Tencent" + File.separator + "QQfile_recv";
    public static final String QQFILE2 = SDROOT + File.separator + "Android/data/com.tencent.mobileqq/Tencent/QQfile_recv";

    public static final int TYPE_DOC = 0;
    public static final int TYPE_APK = 1;
    public static final int TYPE_ZIP = 2;

    /**
     * 安卓Q获取沙盒目录
     *
     * @param context 上下文
     * @return 文件夹
     */
    public static File getQdir(Context context, String environment) {
        return context.getExternalFilesDir(environment);
    }

    /**
     * 获取APP缓存路径
     *
     * @param context 上下文
     * @return 例子：/storage/emulated/0/Android/data/com.xing.gfox.dialogdialog/cache
     */
    public static String getAppCacheFolder(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            if (context.getExternalCacheDir() != null) {
                return context.getExternalCacheDir().getPath();
            }
        }
        return context.getCacheDir().getPath();
    }
    ////////////////存在判断////////////

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     */
    public static boolean isFileExist(String path) {
        if (TextUtils.isEmpty(path)) {
            ViseLog.e("UError", path + " 文件路径为空");
            return false;
        }
        return isFileExist(new File(path));
    }

    public static boolean isFileExist(File file) {
        if (file == null) throw new NullPointerException("File不能为空");
        if (file.length() == 0) {
            ViseLog.showLog("UError", file.getAbsolutePath() + " 文件存在，但大小为0B");
        }
        return file.exists() && file.isFile();
    }

    /**
     * 判断文件夹是否存在
     *
     * @param path 文件路径
     * @return 是否存在
     */

    public static boolean isFolderExist(String path) {
        if (TextUtils.isEmpty(path)) {
            ViseLog.e("UError", "path为空");
            return false;
        }
        return isFolderExist(new File(path));
    }

    public static boolean isFolderExist(File file) {
        if (file == null) throw new NullPointerException("File不能为空");
        return file.exists() && file.isDirectory();
    }

    /**
     * 判断文件夹是否是空文件夹
     *
     * @param path 文件路径
     * @return 是否是空文件夹
     */
    public static boolean isFolderNull(String path) {
        if (isFolderExist(path)) {
            File file = new File(path);
            String[] list = file.list();
            return list == null || list.length == 0;
        }
        return false;
    }

    ////////////////新建/////////////////
    //创建文件夹-文件路径
    public static boolean createFolder(String filePath) {
        return createFolder(new File(filePath));
    }

    //创建文件夹-文件夹路径+文件夹名
    public static boolean createFolder(String parentPath, String dirName) {
        return createFolder(new File(parentPath, dirName));
    }

    //创建文件夹-文件路径
    public static boolean createFileFolder(String filePath) {
        return createFileFolder(new File(filePath));
    }

    //创建文件夹-文件路径+文件名
    public static boolean createFileFolder(String parentPath, String dirName) {
        File dirFile = new File(parentPath, dirName);
        return createFileFolder(dirFile);
    }

    public static boolean createFolder(File folder) {
        if (!isFolderExist(folder)) {
            return folder.mkdirs();
        } else {
            return true;
        }
    }

    public static boolean createFileFolder(File file) {
        return createFolder(file.getParentFile());
    }

    /**
     * 创建文件
     *
     * @param parentPath 文件目录
     * @param dirName    文件名（含后缀）
     * @param isReplace  是否替换
     * @return 创建结果
     */
    public static File createFile(String parentPath, String dirName, boolean isReplace) {
        ViseLog.showInfo(U_file.class.getName(), "新建文件：" + parentPath + "/" + dirName);
        try {
            File file = new File(parentPath, dirName);
            if (createFileFolder(file)) {
                //开始创建文件
                if (!file.exists()) {
                    if (!file.createNewFile()) {
                        ViseLog.e("UError", "新建文件失败");
                    }
                } else {//文件已存在
                    if (isReplace) {
                        if (file.delete()) {
                            if (file.createNewFile()) {
                                return file;
                            } else {
                                ViseLog.e("UError", "删除后新建文件失败");
                            }
                        } else {
                            ViseLog.e("UError", "文件已存在且无法删除");
                        }
                    } else {
                        ViseLog.e("UError", "文件已存在");
                    }
                }
            } else ViseLog.e("UError", file.getAbsolutePath() + "文件目录创建失败");
            return file;
        } catch (Exception e) {
            ViseLog.e("UError", "新建文件失败" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }
    ////////////////删除/////////////////

    /**
     * 删除文件
     *
     * @param filePath 文件路径
     * @return 删除结果
     */
    public static boolean deleteFile(String filePath) {
        ViseLog.showInfo(U_file.class.getName(), "删除文件：" + filePath);
        if (isFileExist(filePath)) {
            return deleteFile(new File(filePath));
        }
        ViseLog.e("UError", filePath + "文件不存在");
        return false;
    }

    /**
     * 删除文件夹
     *
     * @param filePath 文件夹路径
     * @return 删除结果
     */
    public static boolean deleteFolder(String filePath) {
        ViseLog.showInfo(U_file.class.getName(), "删除文件夹：" + filePath);
        if (isFolderExist(filePath)) {
            return deleteFile(new File(filePath));
        }
        ViseLog.e("UError", filePath + "文件夹不存在");
        return false;
    }

    /**
     * 清空文件夹
     *
     * @param filePath 文件夹路径
     * @return 删除结果
     */
    public static boolean clearFolder(String filePath) {
        ViseLog.showInfo(U_file.class.getName(), "清空文件夹：" + filePath);
        if (isFolderExist(filePath)) {
            return deleteFile(new File(filePath), true);
        }
        ViseLog.e("UError", filePath + "文件夹不存在");
        return false;
    }

    public static boolean deleteFile(File file) {
        return deleteFile(file, false);
    }

    public static boolean deleteFile(File file, boolean isClear) {
        if (file == null || !file.exists()) {
            ViseLog.e("UError", "文件不存在");
            return false;
        }
        ViseLog.showInfo(U_file.class.getName(), "文件删除：" + file.toString());
        if (file.isDirectory()) {
            File[] fileList = file.listFiles();
            if (fileList != null) {
                for (File f : fileList) {
                    if (!deleteFile(f)) {
                        ViseLog.e("UError", f.getName() + "删除失败");
                    }
                }
            }
        }
        if (isClear) {
            return true;
        } else {
            return file.delete();
        }
    }

    ////////////////复制移动/////////////////
    public static InputStream getStreamFromURI(Context context, Uri uri) throws FileNotFoundException {
        ContentResolver resolver = context.getContentResolver();
        return resolver.openInputStream(uri);
    }

    /**
     * 文件复制
     *
     * @param context        context
     * @param fileUri        fileUri
     * @param outputFilePath 输出文件
     * @return 结果
     */
    public static boolean copyFile(Context context, Uri fileUri, String outputFilePath) {
        ViseLog.showInfo(U_file.class.getName(), "文件复制：" + fileUri.toString() + " -> " + outputFilePath);
        try {
            return copyFile(getStreamFromURI(context, fileUri), new FileOutputStream(outputFilePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyFile(String inputFilePath, String outputFilePath, boolean isReplace) {
        ViseLog.showInfo(U_file.class.getName(), "文件复制：" + inputFilePath + " -> " + outputFilePath);
        try {
            if (!isFileExist(inputFilePath)) {
                ViseLog.e("UError", inputFilePath + "源文件不存在");
                return false;
            }
            File file = new File(outputFilePath);
            if (!file.exists()) {
                createFile(file.getParent(), file.getName(), isReplace);
            } else {
                if (isReplace) {
                    if (file.delete()) {
                        createFile(file.getParent(), file.getName(), true);
                    } else {
                        ViseLog.e("UError", "文件删除失败");
                        return false;
                    }
                } else {
                    ViseLog.e("UError", "目标文件已存在，请使用强制复制");
                    return false;
                }
            }
            return copyFile(new FileInputStream(inputFilePath), new FileOutputStream(outputFilePath));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //复制沙盒私有文件到Picture公共目录下
    //orgFilePath是要复制的文件私有目录路径
    public static boolean copyImgToPicture(Context context, String orgFilePath) {
        return copyFileToDownload(context, orgFilePath, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    }

    public static boolean copyVideoToMovie(Context context, String orgFilePath) {
        return copyFileToDownload(context, orgFilePath, MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
    }

    public static boolean copyFileToDownload(Context context, String orgFilePath) {
        return copyFileToDownload(context, orgFilePath, null);
    }

    private static boolean copyFileToDownload(Context context, String inputFile, Uri external) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (external == null) external = MediaStore.Downloads.EXTERNAL_CONTENT_URI;
            return copyFileByContentResolver(context, inputFile, external);
        } else {
            String fileName = System.currentTimeMillis() + getFileNameFromUrl(inputFile);
            String filePath = "";
            if (external == MediaStore.Images.Media.EXTERNAL_CONTENT_URI) {
                filePath = PICTURES + File.separator + fileName;
            } else if (external == MediaStore.Video.Media.EXTERNAL_CONTENT_URI) {
                filePath = MOVIE + File.separator + fileName;
            } else {
                filePath = DOWNLOADS + File.separator + fileName;
            }
            U_file.copyFile(inputFile, filePath, true);
            U_media.updateMedia(context, filePath);
            return true;
        }
    }

    private static boolean copyFileByContentResolver(Context context, String orgFilePath, Uri external) {
        try {
            ContentValues values = new ContentValues();
            long currentTimeMillis = System.currentTimeMillis();
            values.put(MediaStore.Files.FileColumns.DISPLAY_NAME, currentTimeMillis + getExtFromFileName(orgFilePath));
            values.put(MediaStore.Files.FileColumns.MIME_TYPE, getMIMETypeByFile(orgFilePath));//MediaStore对应类型名
            values.put(MediaStore.Files.FileColumns.TITLE, currentTimeMillis + "");
//            values.put(MediaStore.Files.FileColumns.RELATIVE_PATH, "DCIM/Camera");//指定存放文件夹名，需要系统大于29
            ContentResolver resolver = context.getContentResolver();
            Uri insertUri = resolver.insert(external, values);//使用ContentResolver创建需要操作的文件
            return U_file.copyFile(new FileInputStream(orgFilePath), resolver.openOutputStream(insertUri));
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean copyFile(InputStream inputStream, OutputStream outputStream) {
        try {
            byte[] data = new byte[1024];
            int length;
            while ((length = inputStream.read(data)) != -1) {
                outputStream.write(data, 0, length);
            }
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (outputStream != null) {
                    outputStream.close();
                }
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return true;
    }

    public static boolean moveFile(String inputFilePath, String outputFilePath, boolean isReplace) {
        ViseLog.showInfo(U_file.class.getName(), "文件剪切：" + inputFilePath + " -> " + outputFilePath);
        if (copyFile(inputFilePath, outputFilePath, isReplace)) {
            return deleteFile(new File(inputFilePath));
        }
        return false;
    }

    public static boolean moveFolder(String inputFilePath, String outputFilePath) {
        ViseLog.showInfo(U_file.class.getName(), "文件夹剪切：" + inputFilePath + " -> " + outputFilePath);
        if (copyFolder(inputFilePath, outputFilePath)) {
            return deleteFolder(inputFilePath);
        }
        return false;
    }

    public static boolean copyFolder(String inputFilePath, String outputFilePath) {
        File in = new File(inputFilePath);
        createFolder(outputFilePath, in.getName());
        File file = new File(outputFilePath, in.getName());
        return copyFolder(inputFilePath, file.getAbsolutePath(), true);
    }

    private static boolean copyFolder(String inputFilePath, String outputFilePath, boolean isReplace) {
        ViseLog.showInfo(U_file.class.getName(), "文件夹复制：" + inputFilePath + " -> " + outputFilePath);
        try {
            if (!isFolderExist(inputFilePath)) {
                ViseLog.e("UError", inputFilePath + "源文件夹不存在");
                return false;
            }
            File in = new File(inputFilePath);
            File[] listFiles = in.listFiles();
            ViseLog.d(listFiles);
            File out = new File(outputFilePath);
            if (!out.exists()) {
                out.mkdirs();
            }
            if (listFiles != null) {
                for (File f : listFiles) {
                    if (f.isFile()) {
                        //文件
                        copyFile(f.getPath(), outputFilePath + File.separator + f.getName(), isReplace); //调用文件拷贝的方法
                    } else if (f.isDirectory()) {
                        //文件夹
                        copyFolder(f.getPath(), outputFilePath + File.separator + f.getName(), isReplace);//继续调用复制方法      递归的地方,自己调用自己的方法,就可以复制文件夹的文件夹了
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //////////////////////string读取写入//////////////////////////////

    /**
     * 把string写入文件
     *
     * @param context 上下文用于获取默认缓存目录
     * @param dstName 文件名
     * @param text    文本内容
     */
    public void writeStringToFile(Context context, String dstName, String text) {
        writeStringToFile(getAppCacheFolder(context), dstName, text);
    }

    /**
     * 把string写入文件
     *
     * @param dstPath 文件目录
     * @param dstName 文件名
     * @param text    文本内容
     */
    public static void writeStringToFile(String dstPath, String dstName, String text) {
        File file = U_file.createFile(dstPath, dstName, true);
        if (file != null) {
            try {
                FileOutputStream fos = new FileOutputStream(file, false);
                fos.write(text.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 从文件读取string
     *
     * @param filePath 文件路径
     * @return string
     */
    public static String readStringFromFile(String filePath) {
        return readStringFromFile(filePath, "UTF-8");
    }

    public static String readStringFromFile(String filePath, String code) {
        if (TextUtils.isEmpty(filePath)) {
            ViseLog.e("UError", "路径不可为空");
            return null;
        }
        File file = new File(filePath);
        return readStringFromFile(file, code);
    }

    /**
     * 从文件读取string
     *
     * @param file 文件
     * @return string
     */
    public static String readStringFromFile(File file) {
        return readStringFromFile(file, "UTF-8");
    }

    public static String readStringFromFile(File file, String code) {
        if (!file.exists()) {
            ViseLog.e("UError", file.getAbsolutePath() + "读取的文件不存在");
            return null;
        }
        try {
            InputStreamReader isr = new InputStreamReader(new FileInputStream(file), code);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String mimeTypeLine;
            while ((mimeTypeLine = br.readLine()) != null) {
                sb.append(mimeTypeLine);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 重命名文件
     *
     * @param path    地址
     * @param newName 文件名
     * @return 结果
     */
    public static boolean renameFile(String path, String newName) {
        if (TextUtils.isEmpty(path)) {
            ViseLog.e("UError", "文件不存在");
            return false;
        }
        File newFile = new File(path);
        return renameFile(newFile, newName);
    }

    public static boolean renameFile(File file, String newName) {
        ViseLog.d(file.getAbsolutePath() + " " + newName);
        if (!file.exists()) {
            ViseLog.e("UError", "文件不存在");
            return false;
        }
        File newFile = new File(file.getParent(), newName);
        ViseLog.d(newFile.getAbsolutePath());
        return file.renameTo(newFile);
    }

    /**
     * 获取文件MD5值
     *
     * @param path 路径
     * @return md5
     */
    public static String getFileMD5(String path) {
        return getFileMD5(new File(path));
    }

    public static String getFileMD5(File file) {
        if (!file.exists() || !file.isFile()) {
            return "ERROR,文件不存在";
        }
        MessageDigest digest;
        FileInputStream in;
        byte[] buffer = new byte[1024];
        int len;
        try {
            digest = MessageDigest.getInstance("MD5");
            in = new FileInputStream(file);
            while ((len = in.read(buffer, 0, 1024)) != -1) {
                digest.update(buffer, 0, len);
            }
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        BigInteger bigInt = new BigInteger(1, digest.digest());
        return bigInt.toString(16);
    }

    /**
     * 递归取得文件夹大小
     *
     * @param file 文件
     * @return long 大小
     */
    public static long getFolderSize(File file) {
        long size = 0;
        if (isFolderExist(file.getPath())) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File value : files) {
                    if (value.isDirectory()) {
                        size = size + getFolderSize(value);
                    } else {
                        size = size + value.length();
                    }
                }
            }
        }
        return size;
    }

    public static long getFileSize(File file) {
        long size = 0;
        if (isFileExist(file.getPath())) {
            FileInputStream fis;
            try {
                fis = new FileInputStream(file);
                size = fis.available();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return size;
    }

    /**
     * 获取文件编码类型
     *
     * @param path 路径
     * @return 编码类型，UTF-8，GBK，ANSI。。。
     */
    public static String getEncode(String path) {
        return EncodingDetect.getJavaEncode(path);
    }

////////////////////////////////////////////////////////////////////////////////////////////////////

    public static int getFileType(String path) {
        path = getExtFromFileName(path.toLowerCase());
        switch (path) {
            case ".doc":
            case ".docx":
            case ".xls":
            case ".xlsx":
            case ".ppt":
            case ".pptx":
                return TYPE_DOC;
            case ".apk":
                return TYPE_APK;
            case ".zip":
            case ".rar":
            case ".tar":
            case ".gz":
                return TYPE_ZIP;
            default:
                return -1;
        }
    }

    /**
     * 获取文件名
     *
     * @param path 文件地址
     * @return 文件名
     */
    public static String getFileName(String path) {
        if (isFileExist(path)) {
            return new File(path).getName();
        } else {
            return "";
        }
    }

    /**
     * 通过文件名获取文件图标
     */
    public static int getFileIconByPath(String path) {
        path = getExtFromFileName(path.toLowerCase());
        int iconId = R.mipmap.icon_error;

        switch (path) {
            case ".txt":
                iconId = R.mipmap.icon_error;
                break;
            case ".doc":
            case ".docx":
                iconId = R.mipmap.icon_error;
                break;
            case ".xls":
            case ".xlsx":
                iconId = R.mipmap.icon_error;
                break;
            case ".ppt":
            case ".pptx":
                iconId = R.mipmap.icon_error;
                break;
            case ".xml":
                iconId = R.mipmap.icon_error;
                break;
            case ".htm":
            case ".html":
                iconId = R.mipmap.icon_error;
                break;
        }
        return iconId;
    }

    /**
     * 是否是图片文件
     */
    public static boolean isPicFile(String path) {
        path = getExtFromFileName(path.toLowerCase());
        return path.equals(".jpg") || path.equals(".jpeg") || path.equals(".png");
    }

    /**
     * 判断SD卡是否挂载
     */
    public static boolean isSDCardAvailable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }

    //从文件的全名得到文件的拓展名
    public static String getExtFromFileName(String filename) {
        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition != -1) {
            return filename.substring(dotPosition).toLowerCase();
        }
        return "";
    }

    //获取url里的/后面的文件名
    public static String getFileNameFromUrl(String url) {
        int index = url.lastIndexOf("/");
        return url.substring(index + 1);
    }

    //根据后缀获取类型
    public static String getMIMETypeByFile(String fileName) {
        if (TextUtils.isEmpty(fileName)) return "*/*";
        int dotIndex = fileName.lastIndexOf(".");
        if (dotIndex < 0) {
            return "*/*";
        }
        /* 获取文件的后缀名*/
        String end = fileName.substring(dotIndex).toLowerCase();
        if (TextUtils.isEmpty(end)) return "*/*";
        String type = "*/*";
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        String[][] fileMiMeType = U_fileOpen.getFileMiMeType();
        for (String[] strings : fileMiMeType) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(strings[0])) type = strings[1];
        }
        return type;
    }

    /**
     * 读取文件的修改时间
     *
     * @param file 文件
     * @return
     */
    public static String getModifiedTime(File file) {
        Calendar cal = Calendar.getInstance();
        long time = file.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat(U_time.yyyy_MM_ddHH_mm_ss, Locale.CHINA);
        cal.setTimeInMillis(time);
        return formatter.format(cal.getTime());
    }
}