package com.xing.gfox.immbar;

import android.app.Activity;
import android.app.Dialog;
import android.content.res.Configuration;
import android.os.Build;
import android.view.Surface;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

/**
 * @author geyifeng
 * @date 2019/4/12 4:01 PM
 */
class ImmersionDelegate implements Runnable {

    private ImmBar mImmBar;
    private BarProperties mBarProperties;
    private OnBarListener mOnBarListener;
    private int mNotchHeight;

    ImmersionDelegate(Object o) {
        if (o instanceof Activity) {
            if (mImmBar == null) {
                mImmBar = new ImmBar((Activity) o);
            }
        } else if (o instanceof Fragment) {
            if (mImmBar == null) {
                if (o instanceof DialogFragment) {
                    mImmBar = new ImmBar((DialogFragment) o);
                } else {
                    mImmBar = new ImmBar((Fragment) o);
                }
            }
        } else if (o instanceof android.app.Fragment) {
            if (mImmBar == null) {
                if (o instanceof android.app.DialogFragment) {
                    mImmBar = new ImmBar((android.app.DialogFragment) o);
                } else {
                    mImmBar = new ImmBar((android.app.Fragment) o);
                }
            }
        }
    }

    ImmersionDelegate(Activity activity, Dialog dialog) {
        if (mImmBar == null) {
            mImmBar = new ImmBar(activity, dialog);
        }
    }

    public ImmBar get() {
        return mImmBar;
    }

    void onActivityCreated(Configuration configuration) {
        barChanged(configuration);
    }

    void onResume() {
        if (mImmBar != null) {
            mImmBar.onResume();
        }
    }

    void onDestroy() {
        mBarProperties = null;
        if (mImmBar != null) {
            mImmBar.onDestroy();
            mImmBar = null;
        }
    }

    void onConfigurationChanged(Configuration newConfig) {
        if (mImmBar != null) {
            mImmBar.onConfigurationChanged(newConfig);
            barChanged(newConfig);
        }
    }

    /**
     * 横竖屏切换监听
     * Orientation change.
     *
     * @param configuration the configuration
     */
    private void barChanged(Configuration configuration) {
        if (mImmBar != null && mImmBar.initialized() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mOnBarListener = mImmBar.getBarParams().onBarListener;
            if (mOnBarListener != null) {
                final Activity activity = mImmBar.getActivity();
                if (mBarProperties == null) {
                    mBarProperties = new BarProperties();
                }
                mBarProperties.setPortrait(configuration.orientation == Configuration.ORIENTATION_PORTRAIT);
                int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
                if (rotation == Surface.ROTATION_90) {
                    mBarProperties.setLandscapeLeft(true);
                    mBarProperties.setLandscapeRight(false);
                } else if (rotation == Surface.ROTATION_270) {
                    mBarProperties.setLandscapeLeft(false);
                    mBarProperties.setLandscapeRight(true);
                } else {
                    mBarProperties.setLandscapeLeft(false);
                    mBarProperties.setLandscapeRight(false);
                }
                activity.getWindow().getDecorView().post(this);
            }
        }
    }

    @Override
    public void run() {
        if (mImmBar != null && mImmBar.getActivity() != null) {
            Activity activity = mImmBar.getActivity();
            BarConfig barConfig = new BarConfig(activity);
            mBarProperties.setStatusBarHeight(barConfig.getStatusBarHeight());
            mBarProperties.setNavigationBar(barConfig.hasNavigationBar());
            mBarProperties.setNavigationBarHeight(barConfig.getNavigationBarHeight());
            mBarProperties.setNavigationBarWidth(barConfig.getNavigationBarWidth());
            mBarProperties.setActionBarHeight(barConfig.getActionBarHeight());
            boolean notchScreen = NotchUtils.hasNotchScreen(activity);
            mBarProperties.setNotchScreen(notchScreen);
            if (notchScreen && mNotchHeight == 0) {
                mNotchHeight = NotchUtils.getNotchHeight(activity);
                mBarProperties.setNotchHeight(mNotchHeight);
            }
            mOnBarListener.onBarChange(mBarProperties);
        }
    }
}
