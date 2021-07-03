package com.xing.gfox.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.security.auth.x500.X500Principal;

import com.xing.gfox.log.ViseLog;

public class U_app {
    /**
     * 获得应用申明的所有权限列表
     *
     * @param context 上下文
     * @return 获得应用申明的所有权限列表
     */
    public static List<String> getPermissions(Context context) {
        List<String> permissions = new ArrayList<>();
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_PERMISSIONS);
            permissions.addAll(Arrays.asList(packageInfo.requestedPermissions));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return permissions;
    }

    /**
     * 是否是系统应用
     *
     * @param context     context
     * @param packageName 包名
     * @return 结果
     */
    public static boolean isSystemApp(Context context, String packageName) {
        boolean isSys = false;
        PackageManager pm = context.getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(packageName, 0);
            if (applicationInfo != null && (applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) > 0) {
                isSys = true;
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            isSys = false;
        }
        return isSys;
    }

    /**
     * 通过APK地址获取此APP的包名和版本等信息
     */
    public static String getCurrentPackageName(Context context) {
        return getPackageName(context, context.getPackageName());
    }

    public static String getPackageName(Context context, String filePath) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = pm.getPackageArchiveInfo(filePath, PackageManager.GET_ACTIVITIES);
        if (info != null) {
            ApplicationInfo appInfo = info.applicationInfo;
//            String appName = pm.getApplicationLabel(appInfo).toString();
            String packageName = appInfo.packageName;  //获取安装包名称
            ViseLog.i("包名：" + packageName);
            String version = info.versionName; //获取版本信息
            ViseLog.i("版本：" + version);
            return packageName;
        }
        return null;
    }

    //是否安装该应用
    public static boolean isApkInstalled(Context context, String packageName) {
        if (TextUtils.isEmpty(packageName)) {
            return false;
        }
        try {
            ApplicationInfo info = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static PackageInfo getPackageInfo(Context context, String packageName) throws PackageManager.NameNotFoundException {
        return context.getPackageManager().getPackageInfo(packageName, 0);
    }

    public static int getVersionCode(Context context, String packageName) {
        try {
            return getPackageInfo(context, packageName).versionCode;
        } catch (Exception e) {
            return 0;
        }
    }

    public static String getVersionName(Context context, String packageName) {
        try {
            return getPackageInfo(context, packageName).versionName;
        } catch (Exception e) {
            return "0";
        }
    }

    /**
     * 获取应用第一次安装日期
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 日期
     */
    public static long getAppFirstInstallTime(Context context, String packageName) {
        try {
            return getPackageInfo(context, packageName).firstInstallTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取应用更新日期
     *
     * @param context
     * @param packageName
     * @return
     */
    public static long getAppLastUpdateTime(Context context, String packageName) {
        try {
            return getPackageInfo(context, packageName).lastUpdateTime;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 获取应用大小
     *
     * @param context
     * @param packageName
     * @return
     */
    public static long getAppSize(Context context, String packageName) {
        long appSize = 0;
        try {
            ApplicationInfo applicationInfo = context.getPackageManager().getApplicationInfo(packageName, 0);
            appSize = new File(applicationInfo.sourceDir).length();
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appSize;
    }

    /**
     * 获取应用图标
     *
     * @param context
     * @param packageName
     * @return
     */
    public static Drawable getAppIcon(Context context, String packageName) {
        try {
            return context.getPackageManager().getApplicationIcon(packageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取应用的安装市场
     *
     * @param context
     * @param packageName
     * @return
     */
    public static String getAppInstaller(Context context, String packageName) {
        return context.getPackageManager().getInstallerPackageName(packageName);
    }

    //根据包名获取签名ID
    @SuppressLint("PackageManagerGetSignatures")
    public static int getSignature(Context context, String packageName) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo info;
        try {
            info = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES);
            Signature[] signatures = info.signatures;
            return signatures[0].hashCode();
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static String getAppName(Context context) {
        try {
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
            return appInfo.loadLabel(context.getPackageManager()) + "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 检测当前应用是否是Debug版本
     *
     * @param ctx 上下文
     * @return 是否是Debug版本
     */
    public static boolean isDebuggable(Context ctx) {
        boolean debuggable = false;
        try {
            PackageInfo pinfo = ctx.getPackageManager()
                    .getPackageInfo(ctx.getPackageName(),
                            PackageManager.GET_SIGNATURES);
            Signature[] signatures = pinfo.signatures;
            for (Signature signature : signatures) {
                CertificateFactory cf = CertificateFactory.getInstance("X.509");
                ByteArrayInputStream stream = new ByteArrayInputStream(
                        signature.toByteArray());
                X509Certificate cert = (X509Certificate) cf.generateCertificate(
                        stream);
                debuggable = cert.getSubjectX500Principal().equals(DEBUG_DN);
                if (debuggable) break;
            }
        } catch (PackageManager.NameNotFoundException | CertificateException e) {
            e.printStackTrace();
        }
        return debuggable;
    }

    private final static X500Principal DEBUG_DN = new X500Principal(
            "CN=Android Debug,O=Android,C=US");

    /**
     * 比较版本号的大小,  支持4.1.2,4.1.23.4.1.rc111这种形式
     *
     * @param version1 版本号1
     * @param version2 版本号2
     * @return 前者大则返回一个正数, 后者大返回一个负数, 相等则返回0
     */
    public static int compareVersion(String version1, String version2) throws Exception {
        if (version1 == null || version2 == null) {
            throw new Exception("compareVersion error:illegal params.");
        }
        String[] versionArray1 = version1.split("\\.");//注意此处为正则匹配，不能用"."；
        String[] versionArray2 = version2.split("\\.");
        int idx = 0;
        int minLength = Math.min(versionArray1.length, versionArray2.length);//取最小长度值
        int diff = 0;
        while (idx < minLength
                && (diff = versionArray1[idx].length() - versionArray2[idx].length()) == 0//先比较长度
                && (diff = versionArray1[idx].compareTo(versionArray2[idx])) == 0) {//再比较字符
            ++idx;
        }
        //如果已经分出大小，则直接返回，如果未分出大小，则再比较位数，有子版本的为大；
        diff = (diff != 0) ? diff : versionArray1.length - versionArray2.length;
        return diff;
    }

    /**
     * 获取没有点号的版本名
     *
     * @return
     */
    public static String getVersionNameWithoutDot(Context context) {
        try {
            String versionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
            String[] versionArr = versionName.split("\\.");
            if (versionArr.length == 2) {//1.2-->0120 || 2.0-->0200
                if (versionArr[0].length() == 2) {
                    versionName = versionArr[0] + versionArr[1] + "0";
                } else {
                    versionName = "0" + versionArr[0] + versionArr[1] + "0";
                }
            } else if (versionArr.length == 3 || versionArr.length == 4) {//1.2.1-->0121 || 1.4.5.5-->0145
                if (versionArr[0].length() == 2) {
                    versionName = versionArr[0] + versionArr[1] + versionArr[2];
                } else {
                    versionName = "0" + versionArr[0] + versionArr[1] + versionArr[2];
                }
            }
            return versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return "";
    }
}
