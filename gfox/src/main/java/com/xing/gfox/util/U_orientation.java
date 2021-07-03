package com.xing.gfox.util;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.provider.Settings;
import android.view.OrientationEventListener;

import androidx.fragment.app.FragmentActivity;

/**
 * Created by Mai on 2018/11/14
 * *
 * Description: 重力感应方向管理器
 * *
 */
public class U_orientation {

    private int currentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
    private long orientationListenerDelayTime = 0;//时间间隔处理，防止频繁切换横竖屏
    private static int Rotate = -1;//0 代表方向锁定，1 代表没方向锁定

    //加速度传感器监听
    private OrientationEventListener orientationEventListener;
    private OnOrientationChangeListener mOrientationChangeListener;

    public static int getCurrentOrientation(Context context) {
        Configuration mConfiguration = context.getResources().getConfiguration(); //获取设置的配置信息
        return mConfiguration.orientation;
    }

    public static void setLandscape(FragmentActivity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//强制为横屏
    }

    public static void setPortrait(FragmentActivity activity) {
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//强制为竖屏
    }

    /**
     * 系统是否开启方向锁定
     *
     * @param context 上下文
     * @return 是否锁定
     */
    public static boolean isAutoRotate(Context context) {
        try {
            Rotate = Settings.System.getInt(context.getContentResolver(), Settings.System.ACCELEROMETER_ROTATION);
            return Rotate == 0;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public void orientationEnable(final Context context, OnOrientationChangeListener orientationChangeListener) {
        isAutoRotate(context);
        if (orientationEventListener == null) {
            this.mOrientationChangeListener = orientationChangeListener;
            orientationEventListener = new OrientationEventListener(context, 5) { // 加速度传感器监听，用于自动旋转屏幕
                @Override
                public void onOrientationChanged(int orientation) {
                    if (Rotate == 0) return;//方向被锁定，直接返回
                    if ((orientation >= 300 || orientation <= 30) && System.currentTimeMillis() - orientationListenerDelayTime > 1000) {
                        //屏幕顶部朝上
                        currentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                        if (mOrientationChangeListener != null) {
                            mOrientationChangeListener.onOrientationPortrait();
                        }
                    } else if (orientation >= 260 && orientation <= 280
                            && System.currentTimeMillis() - orientationListenerDelayTime > 1000) {
                        //屏幕左边朝上
                        currentOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
                        if (mOrientationChangeListener != null) {
                            mOrientationChangeListener.onOrientationLandscape();
                        }
                    } else if (orientation >= 70 && orientation <= 90
                            && System.currentTimeMillis() - orientationListenerDelayTime > 1000) {
                        //屏幕右边朝上
                        currentOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;
                        if (mOrientationChangeListener != null) {
                            mOrientationChangeListener.onOrientationReverseLandscape();
                        }
                    } else if (orientation >= 150 && orientation <= 210
                            && System.currentTimeMillis() - orientationListenerDelayTime > 1000) {
                        //屏幕底部朝上
                        currentOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
                        if (mOrientationChangeListener != null) {
                            mOrientationChangeListener.onOrientationBottomPortrait();
                        }
                    }
                    orientationListenerDelayTime = System.currentTimeMillis();
                }
            };
            currentOrientation = U_context.getRequestedOrientation(context);
            orientationEventListener.enable();
        }
    }

    public interface OnOrientationChangeListener {
        //ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;竖屏
        //ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE;横屏
        void onOrientationLandscape();

        void onOrientationReverseLandscape();

        void onOrientationPortrait();

        void onOrientationBottomPortrait();
    }

    public void orientationDestroy() {
        if (orientationEventListener != null) {
            orientationEventListener.disable();
            orientationEventListener = null;
        }
        if (mOrientationChangeListener != null) {
            mOrientationChangeListener = null;
        }
    }
}
