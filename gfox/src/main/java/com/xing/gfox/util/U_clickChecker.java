package com.xing.gfox.util;

/**
 * 使用 if (!hl_clickChecker.checkClickEnable()) return;
 */
public class U_clickChecker {
    private static long lastClickTime;
    private static long lastLongClickTime;

    public static boolean checkClickEnable() {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastClickTime > 800) {
            lastClickTime = clickTime;
            return true;
        } else if (clickTime - lastClickTime < 100) {
            lastClickTime = clickTime;
            return true;
        }
        return false;
    }

    public static boolean checkClickLongEnable() {
        long clickTime = System.currentTimeMillis();
        if (clickTime - lastLongClickTime > 2000) {
            lastLongClickTime = clickTime;
            return true;
        } else if (clickTime - lastLongClickTime < 100) {
            lastLongClickTime = clickTime;
            return true;
        }
        return false;
    }
}
