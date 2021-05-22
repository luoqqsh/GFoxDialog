package com.xing.gfox.util.DialogTest.utils;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.xing.gfox.base.app.AppInit;

import java.io.File;
import java.util.Locale;


public class OpenFileUtil {

    public static Intent openFile(String filePath) {

        File file = new File(filePath);
        if (!file.exists())
            return null;
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase(Locale.getDefault());
        /* 依扩展名的类型决定MimeType */
        switch (end) {
            case "m4a":
            case "mp3":
            case "mp2":
            case "mid":
            case "xmf":
            case "ogg":
            case "m3u":
            case "wma":
            case "wav":
            case "mpga":
                return getAudioFileIntent(filePath);
            case "3gp":
            case "mp4":
            case "mkv":
            case "vob":
            case "wmv":
            case "rmvb":
            case "rm":
            case "m4v":
            case "m4u":
            case "avi":
            case "asf":
            case "mpeg":
            case "mpg":
            case "mov":
                return getVideoFileIntent(filePath);
            case "jpg":
            case "gif":
            case "png":
            case "jpeg":
            case "bmp":
                return getImageFileIntent(filePath);
            case "apk":
                return getApkFileIntent(filePath);
            case "ppt":
                return getPptFileIntent(filePath);
            case "xls":
                return getExcelFileIntent(filePath);
            case "doc":
                return getWordFileIntent(filePath);
            case "pdf":
                return getPdfFileIntent(filePath);
            case "zip":
                return getZipFileIntent(filePath);
            case "rar":
                return getFileIntent(filePath, "application/x-rar-compressed");
            case "jar":
                return getFileIntent(filePath, "application/java-archive");
            case "gz":
                return getFileIntent(filePath, "application/gzip");
            case "gtar":
                return getFileIntent(filePath, "application/x-gtar");
            case "tar":
                return getFileIntent(filePath, "application/x-tar");
            case "tgz":
                return getFileIntent(filePath, "application/x-compressed");
            case "chm":
                return getChmFileIntent(filePath);
            case "txt":
                return getTextFileIntent(filePath, false);
            default:
                return getAllIntent(filePath);
        }
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getAllIntent(String param) {
        return getFileIntent(param, "*/*");
    }

    // Android获取一个用于打开APK文件的intent
    public static Intent getApkFileIntent(String param) {

        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(AppInit.getAppContext(), AppInit.getAppContext().getPackageName() + ".dialog.fileProvider", new File(param));
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
        } else {
            Uri uri = Uri.fromFile(new File(param));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + new File(param)));
            intent.setDataAndType(uri, "application/vnd.android.package-archive");
        }
        return intent;
    }

    // Android获取一个用于打开VIDEO文件的intent
    public static Intent getVideoFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(AppInit.getAppContext(), AppInit.getAppContext().getPackageName() + ".dialog.fileProvider", new File(param));
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setDataAndType(contentUri, "video/*");
        } else {
            Uri uri = Uri.fromFile(new File(param));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + new File(param)));
            intent.setDataAndType(uri, "video/*");
        }
        return intent;
    }

    // Android获取一个用于打开AUDIO文件的intent
    public static Intent getAudioFileIntent(String param) {

        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("oneshot", 0);
        intent.putExtra("configchange", 0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(AppInit.getAppContext(), AppInit.getAppContext().getPackageName() + ".dialog.fileProvider", new File(param));
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(contentUri, "audio/*");
        } else {
            Uri uri = Uri.fromFile(new File(param));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + new File(param)));
            intent.setDataAndType(uri, "audio/*");
        }
        return intent;
    }

    // Android获取一个用于打开Html文件的intent
    public static Intent getHtmlFileIntent(String param) {
        Uri uri = Uri.parse(param).buildUpon().encodedAuthority("com.android.htmlfileprovider").scheme("content").encodedPath(param).build();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(uri, "text/html");
        return intent;
    }

    // Android获取一个用于打开图片文件的intent
    public static Intent getImageFileIntent(String param) {
        return getFileIntent(param, "image/*");
    }

    // Android获取一个用于打开PPT文件的intent
    public static Intent getPptFileIntent(String param) {
        return getFileIntent(param, "vnd.ms-powerpoint");
    }

    // Android获取一个用于打开Excel文件的intent
    public static Intent getExcelFileIntent(String param) {
        return getFileIntent(param, "application/vnd.ms-excel");
    }

    // Android获取一个用于打开Word文件的intent
    public static Intent getWordFileIntent(String param) {
        return getFileIntent(param, "application/msword");
    }

    public static Intent getFileIntent(String param, String type) {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(AppInit.getAppContext(), AppInit.getAppContext().getPackageName() + ".dialog.fileProvider", new File(param));
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setDataAndType(contentUri, type);
        } else {
            Uri uri = Uri.fromFile(new File(param));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + new File(param)));
            intent.setDataAndType(uri, type);
        }
        return intent;
    }

    // Android获取一个用于打开CHM文件的intent
    public static Intent getChmFileIntent(String param) {
        return getFileIntent(param, "application/x-chm");
    }

    // Android获取一个用于打开文本文件的intent
    public static Intent getTextFileIntent(String param, boolean paramBoolean) {
        return getFileIntent(param, "text/plain");
    }

    // Android获取一个用于打开PDF文件的intent
    public static Intent getPdfFileIntent(String param) {
        return getFileIntent(param, "application/pdf");
    }

    // Android获取一个用于打开压缩文件的intent
    public static Intent getZipFileIntent(String param) {
        return getFileIntent(param, "application/zip");
    }
}
