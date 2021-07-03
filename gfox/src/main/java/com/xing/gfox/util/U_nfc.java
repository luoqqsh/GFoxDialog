package com.xing.gfox.util;

import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class U_nfc {
    //跳转系统的NFC设置界面
    public static void gotoNFCSet(Context context) {
        Intent intent = new Intent(Settings.ACTION_NFC_SETTINGS);
        context.startActivity(intent);
    }
}
