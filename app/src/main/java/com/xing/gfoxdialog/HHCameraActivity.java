package com.xing.gfoxdialog;


import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;

import com.xing.gfox.camera.Camera1;
import com.xing.gfox.camera.HHCamera;
import com.xing.gfoxdialog.BaseApp.BaseActivity;

public class HHCameraActivity extends BaseActivity implements View.OnClickListener {
    private HHCamera camera1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        RelativeLayout rl_camera = findViewById(R.id.rl_camera);
        findViewById(R.id.get).setOnClickListener(this);
        findViewById(R.id.video).setOnClickListener(this);
        findViewById(R.id.mainCamera).setOnClickListener(this);
        findViewById(R.id.stop).setOnClickListener(this);
        findViewById(R.id.switchcamera).setOnClickListener(this);
        camera1 = new HHCamera(this, new Camera1(), rl_camera);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get:
                camera1.startPreView();
                break;
            case R.id.mainCamera:
                break;
            case R.id.video:
                break;
            case R.id.switchcamera:
                camera1.switchCamera();
                break;
            case R.id.stop:
                camera1.stopPreView();
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (camera1 != null) {
            camera1.stopPreView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (camera1 != null) {
            camera1.startPreView();
        }
    }
}
