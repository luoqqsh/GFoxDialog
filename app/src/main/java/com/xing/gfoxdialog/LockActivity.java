package com.xing.gfoxdialog;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.xing.gfox.base.activity.HLBaseActivity;


public class LockActivity extends HLBaseActivity {
    @Override
    public void initUI(Bundle savedInstanceState) {
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        findViewById(R.id.lockTest).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent(mActivity,HHMedia3Activity.class);
//                startActivity(intent);
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_lock;
    }
}
