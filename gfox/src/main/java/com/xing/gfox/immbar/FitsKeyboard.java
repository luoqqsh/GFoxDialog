package com.xing.gfox.immbar;

import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.FrameLayout;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;



/**
 * 适配软键盘弹出问题
 *
 * @author geyifeng
 * @date 2018/11/9 10:24 PM
 */
class FitsKeyboard implements ViewTreeObserver.OnGlobalLayoutListener {

    private ImmBar mImmBar;
    private Window mWindow;
    private View mDecorView;
    private View mContentView;
    private View mChildView;
    private int mPaddingLeft = 0, mPaddingTop = 0, mPaddingRight = 0, mPaddingBottom = 0;
    private int mTempKeyboardHeight;
    private boolean mIsAddListener;

    FitsKeyboard(ImmBar immBar) {
        mImmBar = immBar;
        mWindow = immBar.getWindow();
        mDecorView = mWindow.getDecorView();
        FrameLayout frameLayout = mDecorView.findViewById(android.R.id.content);
        if (immBar.isDialogFragment()) {
            Fragment supportFragment = immBar.getSupportFragment();
            if (supportFragment != null) {
                mChildView = supportFragment.getView();
            } else {
                android.app.Fragment fragment = immBar.getFragment();
                if (fragment != null) {
                    mChildView = fragment.getView();
                }
            }
        } else {
            mChildView = frameLayout.getChildAt(0);
            if (mChildView != null) {
                if (mChildView instanceof DrawerLayout) {
                    mChildView = ((DrawerLayout) mChildView).getChildAt(0);
                }
            }
        }
        if (mChildView != null) {
            mPaddingLeft = mChildView.getPaddingLeft();
            mPaddingTop = mChildView.getPaddingTop();
            mPaddingRight = mChildView.getPaddingRight();
            mPaddingBottom = mChildView.getPaddingBottom();
        }
        mContentView = mChildView != null ? mChildView : frameLayout;

    }

    void enable(int mode) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mWindow.setSoftInputMode(mode);
            if (!mIsAddListener) {
                mDecorView.getViewTreeObserver().addOnGlobalLayoutListener(this);
                mIsAddListener = true;
            }
        }
    }

    void disable() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mIsAddListener) {
            if (mChildView != null) {
                mContentView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, mPaddingBottom);
            } else {
                mContentView.setPadding(mImmBar.getPaddingLeft(),
                        mImmBar.getPaddingTop(),
                        mImmBar.getPaddingRight(),
                        mImmBar.getPaddingBottom());
            }
        }
    }

    void cancel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && mIsAddListener) {
            mDecorView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            mIsAddListener = false;
        }
    }

    @Override
    public void onGlobalLayout() {
        if (mImmBar != null && mImmBar.getBarParams() != null && mImmBar.getBarParams().keyboardEnable) {
            BarConfig barConfig = mImmBar.getBarConfig();
            int bottom = 0, keyboardHeight, navigationBarHeight = barConfig.isNavigationAtBottom() ? barConfig.getNavigationBarHeight() : barConfig.getNavigationBarWidth();
            boolean isPopup = false;
            Rect rect = new Rect();
            //获取当前窗口可视区域大小
            mDecorView.getWindowVisibleDisplayFrame(rect);
            keyboardHeight = mContentView.getHeight() - rect.bottom;
            if (keyboardHeight != mTempKeyboardHeight) {
                mTempKeyboardHeight = keyboardHeight;
                if (!ImmBar.checkFitsSystemWindows(mWindow.getDecorView().findViewById(android.R.id.content))) {
                    if (mChildView != null) {
                        if (mImmBar.getBarParams().isSupportActionBar) {
                            keyboardHeight += mImmBar.getActionBarHeight() + barConfig.getStatusBarHeight();
                        }
                        if (mImmBar.getBarParams().fits) {
                            keyboardHeight += barConfig.getStatusBarHeight();
                        }
                        if (keyboardHeight > navigationBarHeight) {
                            bottom = keyboardHeight + mPaddingBottom;
                            isPopup = true;
                        }
                        mContentView.setPadding(mPaddingLeft, mPaddingTop, mPaddingRight, bottom);
                    } else {
                        bottom = mImmBar.getPaddingBottom();
                        keyboardHeight -= navigationBarHeight;
                        if (keyboardHeight > navigationBarHeight) {
                            bottom = keyboardHeight + navigationBarHeight;
                            isPopup = true;
                        }
                        mContentView.setPadding(mImmBar.getPaddingLeft(),
                                mImmBar.getPaddingTop(),
                                mImmBar.getPaddingRight(),
                                bottom);
                    }
                } else {
                    keyboardHeight -= navigationBarHeight;
                    if (keyboardHeight > navigationBarHeight) {
                        isPopup = true;
                    }
                }
                if (keyboardHeight < 0) {
                    keyboardHeight = 0;
                }
                if (mImmBar.getBarParams().onKeyboardListener != null) {
                    mImmBar.getBarParams().onKeyboardListener.onKeyboardChange(isPopup, keyboardHeight);
                }
                if (!isPopup && mImmBar.getBarParams().barHide != BarHide.FLAG_SHOW_BAR) {
                    mImmBar.setBar();
                }
            }
        }
    }
}
