package com.xing.gfox.immbar.components;

import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

/**
 * Fragment快速实现沉浸式的代理类
 *
 * @author geyifeng
 * @date 2018/11/15 12:53 PM
 */
public class SimpleImmersionProxy {
    /**
     * 要操作的Fragment对象
     */
    private Fragment mFragment;
    /**
     * 沉浸式实现接口
     */
    private SimpleImmersionOwner mSimpleImmersionOwner;
    /**
     * Fragment的view是否已经初始化完成
     */
    private boolean mIsActivityCreated;

    public SimpleImmersionProxy(Fragment fragment) {
        this.mFragment = fragment;
        if (fragment instanceof SimpleImmersionOwner) {
            this.mSimpleImmersionOwner = (SimpleImmersionOwner) fragment;
        } else {
            throw new IllegalArgumentException("Fragment请实现SimpleImmersionOwner接口");
        }
    }

    public void setUserVisibleHint(boolean isVisibleToUser) {
        setImmBar();
    }

    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mIsActivityCreated = true;
        setImmBar();
    }

    public void onDestroy() {
        mFragment = null;
        mSimpleImmersionOwner = null;
    }

    public void onConfigurationChanged(Configuration newConfig) {
        setImmBar();
    }

    public void onHiddenChanged(boolean hidden) {
        if (mFragment != null) {
            mFragment.setUserVisibleHint(!hidden);
        }
    }

    /**
     * 是否已经对用户可见
     * Is user visible hint boolean.
     *
     * @return the boolean
     */
    public boolean isUserVisibleHint() {
        if (mFragment != null) {
            return mFragment.getUserVisibleHint();
        } else {
            return false;
        }
    }

    private void setImmBar() {
        if (mFragment != null && mIsActivityCreated && mFragment.getUserVisibleHint()
                && mSimpleImmersionOwner.ImmBarEnabled()) {
            mSimpleImmersionOwner.initImmBar();
        }
    }
}
