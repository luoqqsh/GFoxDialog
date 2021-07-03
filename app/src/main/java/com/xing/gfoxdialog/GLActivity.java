package com.xing.gfoxdialog;

import android.os.Bundle;
import android.view.View;

import com.xing.gfox.opengl.GLView;
import com.xing.gfoxdialog.BaseApp.BaseActivity;

public class GLActivity extends BaseActivity {

    @Override
    public void initUI(Bundle savedInstanceState) {

    }

    @Override
    public View getLayoutView() {
        return new GLView(this);
    }

}
