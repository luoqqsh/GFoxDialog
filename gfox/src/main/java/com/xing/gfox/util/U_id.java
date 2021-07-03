package com.xing.gfox.util;

import android.Manifest;
import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;

public class U_id {
    //非手机设备，如平板、机顶盒等无此id
    public static String getDeviceId(Context context) {
        if (!com.xing.gfox.util.U_permissions.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return "获取失败，检查READ_PHONE_STATE权限";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return "安卓10以上系统无法获取";
        }
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        return tm.getDeviceId();
    }

    public static String getSSAID(Context context) {
        return android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
    }

    public static String getIMEI(Context context) {
        if (!com.xing.gfox.util.U_permissions.checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
            return "获取失败，检查READ_PHONE_STATE权限";
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return "安卓10以上系统无法获取";
        }
        TelephonyManager tm = (TelephonyManager) context.getApplicationContext().getSystemService("phone");
        return tm.getImei();
    }
}
