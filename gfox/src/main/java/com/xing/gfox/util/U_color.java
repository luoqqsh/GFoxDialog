package com.xing.gfox.util;

import android.graphics.Color;

public class U_color {

    /**
     * 判断颜色是否偏黑色
     *
     * @param color 颜色
     * @return
     */
    public static boolean isBlackColor(int color) {
        int grey = toGrey(color);
        return grey < 50;
    }

    /**
     * 颜色转换成灰度值
     *
     * @param rgb 颜色值
     * @return　灰度值
     */
    public static int toGrey(int rgb) {
        int blue = rgb & 0x000000FF;
        int green = (rgb & 0x0000FF00) >> 8;
        int red = (rgb & 0x00FF0000) >> 16;
        return (red * 38 + green * 75 + blue * 15) >> 7;
    }

    /**
     * 是否是白色
     *
     * @param color 颜色值
     * @return
     */
    public static boolean isWhiteColor(int color) {
        int grey = toGrey(color);
        return grey > 200;
    }

    /**
     * 是否亮色
     *
     * @param color 颜色值
     * @return
     */
    public static boolean isLightColor(int color) {
        double darkness = 1 - (0.299 * Color.red(color) + 0.587 * Color.green(color) + 0.114 * Color.blue(color)) / 255;
        // It's a dark color
        return darkness < 0.5; // It's a light color
    }

    /**
     * #5bc0de值转为颜色值
     */
    public static int getColorFromString(String color) {
        return Color.parseColor(color);
    }
}
