package com.xing.gfox.util;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.content.FileProvider;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.xing.gfox.base.app.AppInit;
import com.xing.gfox.log.ViseLog;

public class U_intent {
    public static boolean isIntentAvailable(final Intent intent) {
        return AppInit.getAppContext()
                .getPackageManager()
                .queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .size() > 0;
    }

    /**
     * 启动指定app
     *
     * @param pkgName 包名
     * @return the intent of launch app
     */
    public static void launchApp(Context context, final String pkgName) {
        String launcherActivity = U_activity.getLauncherActivity(pkgName);
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setClassName(pkgName, launcherActivity);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳到拨号页面并输入指定号码
     *
     * @param phoneNumber The phone number.
     */
    public static void callPhoneDialog(Context context, final String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 直接拨打电话
     *
     * @param phoneNumber The phone number.
     */
    @RequiresPermission(Manifest.permission.CALL_PHONE)
    public static void callPhoneNum(Context context, final String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 发送短信
     *
     * @param phoneNumber The phone number.
     * @param content     The content of SMS.
     */
    public static void sendSms(Context context, final String phoneNumber, final String content) {
        Uri uri = Uri.parse("smsto:" + (TextUtils.isEmpty(phoneNumber) ? "" : phoneNumber));
        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
        intent.putExtra("sms_body", TextUtils.isEmpty(content) ? "" : content);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 跳转到浏览器
     *
     * @param context 上下文
     * @param url     网址
     */
    public static void toWebBrowser(Context context, String url) {
        Uri uri = Uri.parse(url);
        Intent it = new Intent(Intent.ACTION_VIEW, uri);
        it.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(it);
    }

    /**
     * 通过其他App打开该文件
     *
     * @param context context
     * @param uri     uri
     * @param type    文件类型
     */
    public static void toUseOtherApp(Context context, Uri uri, String type) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }


    /**
     * 系统分享
     */
    public static void shareText(Context context, final String content) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent = Intent.createChooser(intent, "");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    public static void shareImage(Context context, final String imagePath) {
        shareTextImage(context, "", imagePath);
    }

    public static void shareImage(Context context, final File imageFile) {
        shareTextImage(context, "", imageFile);
    }

    public static void shareImage(Context context, final Uri imageUri) {
        shareTextImage(context, "", imageUri);
    }

    public static void shareTextImage(Context context, @Nullable final String content, final String imagePath) {
        shareTextImage(context, content, new File(imagePath));
    }

    public static void shareTextImage(Context context, @Nullable final String content, final File imageFile) {
        shareTextImage(context, content, U_uri.file2Uri(imageFile));
    }

    public static void shareTextImage(Context context, @Nullable final String content, final Uri imageUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        intent.setType("image/*");
        intent = Intent.createChooser(intent, "");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }


    public static void shareImgList(Context context, @Nullable final String content,
                                    final LinkedList<String> imagePaths) {
        List<File> files = new ArrayList<>();
        if (imagePaths != null) {
            for (String imagePath : imagePaths) {
                File file = new File(imagePath);
                files.add(file);
            }
        }
        shareImgList(context, content, files);
    }

    public static void shareImgList(Context context, @Nullable final String content, final List<File> images) {
        ArrayList<Uri> uris = new ArrayList<>();
        if (images != null) {
            for (File image : images) {
                Uri uri = U_uri.file2Uri(image);
                if (uri != null) {
                    uris.add(uri);
                }
            }
        }
        shareImgList(context, content, uris);
    }

    public static void shareImgList(Context context, @Nullable final String content, final ArrayList<Uri> uris) {
        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uris);
        intent.setType("image/*");
        intent = Intent.createChooser(intent, "");
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 调用系统的分享面板
     *
     * @param context 上下文
     * @param path    要分享文件的路径
     */
    public static void shareFile(Context context, String path) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri contentUri = FileProvider.getUriForFile(context.getApplicationContext(), context.getPackageName() + ".dialog.fileProvider", new File(path));
            intent.putExtra(Intent.EXTRA_STREAM, contentUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + new File(path)));
        }
        intent.setType("*/*");
        context.startActivity(Intent.createChooser(intent, "分享"));
    }

    //安装
    public static void installAPK(Context context, File apkFile) {
        if (!apkFile.exists() || !apkFile.isFile() || apkFile.length() <= 0) {
            ViseLog.e("安装包文件错误");
            return;
        }
        Intent intent = new Intent(Intent.ACTION_VIEW);
        Uri uri = U_uri.getUri(context, intent, apkFile);
        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        if (isIntentAvailable(intent)) {
            context.startActivity(intent);
        } else {
            ViseLog.e("安装失败");
        }
    }

    /**
     * 安装应用
     *
     * @param context  上下文
     * @param filePath 文件路径
     */
    public static void installAPK(Context context, String filePath) {
        installAPK(context, new File(filePath));
    }

    /**
     * 获取安装的intent，通知栏点击调起安装需要
     *
     * @param context 上下文
     * @param appFile 文件
     * @return intent
     */
    public static PendingIntent getInstallAppIntent(Context context, File appFile) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //区别于 FLAG_GRANT_READ_URI_PERMISSION 跟 FLAG_GRANT_WRITE_URI_PERMISSION， URI权限会持久存在即使重启，直到明确的用 revokeUriPermission(Uri, int) 撤销。 这个flag只提供可能持久授权。但是接收的应用必须调用ContentResolver的takePersistableUriPermission(Uri, int)方法实现
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
            Uri fileUri = FileProvider.getUriForFile(context, context.getApplicationContext().getPackageName() + ".dialog.fileProvider", appFile);
            intent.setDataAndType(fileUri, "application/vnd.android.package-archive");
        } else {
            intent.setDataAndType(Uri.fromFile(appFile), "application/vnd.android.package-archive");
        }
        return PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    /**
     * 卸载应用
     *
     * @param context     上下文
     * @param packageName 包名
     * @return
     */
    public static void uninstallApk(Context context, String packageName) {
        Intent i = new Intent(Intent.ACTION_DELETE, Uri.parse("package:" + packageName));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(i);
    }

    /**
     * 返回到桌面
     *
     * @param context 上下文
     */
    public static void backToHomeLauncher(Context context) {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        home.addCategory(Intent.CATEGORY_HOME);
        context.startActivity(home);
    }

    // 跳转账户同步界面
    public static void gotoSyncSet(Context context) {
        Intent intent = new Intent(Settings.ACTION_SYNC_SETTINGS);
        context.startActivity(intent);
    }

    // 跳转到声音设置界面
    public static void gotoSoundSet(Context context) {
        Intent intent = new Intent(Settings.ACTION_SOUND_SETTINGS);
        context.startActivity(intent);
    }

    // 跳转应用程序列表界面
    public static void gotoAppListSet(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_SETTINGS);
        context.startActivity(intent);
    }

    // 跳转所有应用程序列表界面
    public static void gotoAllAppSet(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_APPLICATIONS_SETTINGS);
        context.startActivity(intent);
    }

    // 跳转已安装应用程序列表界面
    public static void gotoAppListSet2(Context context) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
        context.startActivity(intent);
    }

    // 跳转日期时间设置界面
    public static void gotoDATASet(Context context) {
        Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
        context.startActivity(intent);
    }
}