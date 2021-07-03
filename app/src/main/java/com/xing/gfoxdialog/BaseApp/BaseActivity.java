package com.xing.gfoxdialog.BaseApp;

import android.os.Bundle;

import com.xing.gfox.base.activity.HLBaseActivity;
import com.xing.gfoxdialog.R;

public abstract class BaseActivity extends HLBaseActivity {

    @Override
    public void initUI(Bundle savedInstanceState) {
        if (isShowTitle()) {
            mTitle.setLeftButtonImage(R.mipmap.icon_back, view -> finish());
        }
    }

    @Override
    protected int getBackgroundColorResource() {
        return R.color.yellow;
    }

    @Override
    protected boolean isOpenSlideBack() {
        return false;
    }

    @Override
    public boolean isShowTitle() {
        return false;
    }

    @Override
    public boolean isAddStatusBar() {
        return true;
    }

    @Override
    public boolean isTitleBarTextDark() {
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if (hl_device.INSTANCE.isHuaWeiPhone()) {
//            hl_leak.fixHuaWeiLeak(this);
//        }
    }
}
