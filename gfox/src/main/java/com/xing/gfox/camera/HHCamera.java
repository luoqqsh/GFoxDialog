package com.xing.gfox.camera;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

public class HHCamera {
    private CameraCore mCamera;
    private ViewGroup viewGroup;
    private Context mContext;

    public HHCamera(Context mContext, CameraCore mCamera) {
        this.mCamera = mCamera;
        this.mContext = mContext;

    }

    public HHCamera(Context mContext, CameraCore mCamera, ViewGroup viewGroup) {
        this.viewGroup = viewGroup;
        this.mCamera = mCamera;
        this.mContext = mContext;

        initSurfaceView();
    }

    public void takePhoto() {
        if (mCamera != null) {
            mCamera.takePhoto();
        }
    }

    public void releaseCamera() {
        if (mCamera != null) {
            mCamera.releaseCamera();
        }
    }

    public void startPreView() {
        if (mCamera != null) {
            mCamera.startPreView();
        }
    }

    public void stopPreView() {
        if (mCamera != null) {
            mCamera.stopPreView();
        }
    }

    public void switchCamera() {
        if (mCamera != null) {
            mCamera.switchCamera();
        }
    }

    public void initSurfaceView() {
        if (viewGroup == null) return;
        SurfaceView mSurfaceView = new SurfaceView(viewGroup.getContext());
        //获取SurfaceHolder对象
        SurfaceHolder mSurfaceHolder = mSurfaceView.getHolder();
        // 设置 Surface 格式
        // 参数： PixelFormat中定义的 int 值 ,详细参见 PixelFormat.java
        mSurfaceHolder.setFormat(PixelFormat.TRANSPARENT);
        //保持屏幕常亮（可选）
        mSurfaceHolder.setKeepScreenOn(true);
        // 添加 Surface 的 callback 接口
        mSurfaceHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                //在 Surface 首次创建时被立即调用：获得焦点时。一般在这里开启相机预览
                mCamera.setPreviewDisplay(surfaceHolder);
            }

            @Override
            public void surfaceChanged(SurfaceHolder surfaceHolder, int format, int width, int height) {
                //在 Surface 格式 和 大小发生变化时会立即调用，可以在这个方法中更新 Surface
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                //在 Surface 被销毁时立即调用：失去焦点时。一般在这里将相机预览停止销毁
                releaseCamera();
            }
        });
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        viewGroup.addView(mSurfaceView, params);
    }
}
