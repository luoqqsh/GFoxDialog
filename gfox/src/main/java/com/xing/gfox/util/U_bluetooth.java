package com.xing.gfox.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class U_bluetooth {
    //跳转系统的蓝牙设置界面
    public static void gotoBTSet(Context context) {
        Intent intent = new Intent(Settings.ACTION_BLUETOOTH_SETTINGS);
        context.startActivity(intent);
    }
}
