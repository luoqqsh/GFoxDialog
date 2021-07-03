package com.xing.gfox.base.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.xing.gfox.R;
import com.xing.gfox.base.interfaces.OnSimpleListener;

/**
 * activity直接加载fragment
 */
public class HLActivityLoadFragment extends HLBaseActivity {
    private static Fragment fragment;
    private Fragment privateFragment;
    private static OnSimpleListener onBtnRightClickListener;
    private OnSimpleListener privateOnBtnRightClickListener;

    public static void launch(FragmentActivity mActivity, @NonNull String titleStr, Fragment fragment) {
        launch(mActivity, titleStr, null, null, fragment);
    }

    public static void launch(FragmentActivity mActivity, @NonNull String titleStr, String btnRightStr, OnSimpleListener onBtnRightClickListener, Fragment fragment) {
        if (fragment == null) {
            return;
        }
        HLActivityLoadFragment.fragment = fragment;
        Intent intent = new Intent(mActivity, HLActivityLoadFragment.class);
        intent.putExtra("titleStr", titleStr);
        if (!TextUtils.isEmpty(btnRightStr) && onBtnRightClickListener != null) {
            HLActivityLoadFragment.onBtnRightClickListener = onBtnRightClickListener;
            intent.putExtra("btnRightStr", btnRightStr);
        }
        mActivity.startActivity(intent);
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        Intent intent = getIntent();
        String titleStr = intent.getStringExtra("titleStr");
        String btnRightStr = intent.getStringExtra("btnRightStr");

        mTitle.setLeftButtonImage(R.mipmap.icon_back, view -> finish());
        mTitle.setTitleText(titleStr);

        if (btnRightStr != null && onBtnRightClickListener != null) {
            privateOnBtnRightClickListener = onBtnRightClickListener;
            onBtnRightClickListener = null;
            mTitle.setRightButtonText(btnRightStr, view -> {
                if (privateOnBtnRightClickListener != null) {
                    privateOnBtnRightClickListener.onListen();
                }
            });
        }

        if (fragment != null) {
            privateFragment = fragment;
            fragment = null;
        }
        if (privateFragment != null) {
            getSupportFragmentManager().beginTransaction().add(R.id.loadFragment, privateFragment).commit();
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.base_activity_fragment;
    }
}