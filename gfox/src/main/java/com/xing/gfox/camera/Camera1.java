package com.xing.gfox.camera;

import android.content.Context;
import android.hardware.Camera;
import android.os.Build;
import android.view.Surface;
import android.view.SurfaceHolder;

import java.io.IOException;

import com.xing.gfox.log.ViseLog;
import com.xing.gfox.util.U_device;

public class Camera1 extends CameraCore {
    private Camera mCamera;

    private Camera.PreviewCallback mPreviewCallback;//预览实时回调监听
    private Camera.ShutterCallback mShutterCallback;//拍照监听
    private Camera.AutoFocusCallback mAutoFocusCallback;//自动聚焦监听
    public SurfaceHolder surfaceHolder;

    public Camera1() {
        this(CameraConfig.CAMERA_FRONT);
    }

    public Camera1(int CameraId) {
        ViseLog.d("相机数：" + Camera.getNumberOfCameras());
        mCameraId = CameraId;
        mCamera = Camera.open(mCameraId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            mCamera.enableShutterSound(CameraConfig.isOpenVoice);
        }
        mCamera.setPreviewCallbackWithBuffer(mPreviewCallback);
        mCamera.addCallbackBuffer(CameraConfig.getBuffer());
        setCameraRotation(CameraConfig.cameraRotation);
    }

    @Override
    public void initCameraParam() {
        ViseLog.d(surfaceHolder);
        Camera.Parameters parameters = mCamera.getParameters();//得到摄像头的参数
        parameters.setJpegQuality(CameraConfig.JpegQuality);//设置照片的质量
        parameters.setPreviewSize(CameraConfig.PreviewWidth, CameraConfig.PreviewHeight);//设置预览尺寸
        parameters.setPictureSize(CameraConfig.PictureWidth, CameraConfig.PictureHeight);//设置照片尺寸
        parameters.setFocusMode(CameraConfig.FOCUS_CONTINUOUS_PICTURE);
        parameters.setZoom(CameraConfig.zoom);
        //设置预览数据格式为nv21
        parameters.setPreviewFormat(CameraConfig.pixelFormat);
        //数据缓存区
        mCamera.setParameters(parameters);//必须先设置preview才能设置预览尺寸
    }

    public static boolean isSupportCamera(Context context) {
        return U_device.INSTANCE.isSupportCamera(context);
    }

    @Override
    public void setPreviewDisplay(SurfaceHolder surfaceHolder) {
        try {
            if (surfaceHolder != null) {
                this.surfaceHolder = surfaceHolder;
                mCamera.setPreviewDisplay(surfaceHolder);
                initCameraParam();
                startPreView();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setCameraId(int mCameraId) {
        mCamera = Camera.open(mCameraId);
    }

    @Override
    public void takePhoto() {
        if (mCamera != null) {
            mCamera.takePicture(new Camera.ShutterCallback() {//shutter 拍照瞬间回调
                                    @Override
                                    public void onShutter() {

                                    }
                                }, new Camera.PictureCallback() {//raw 回调未压缩的原始数据
                                    @Override
                                    public void onPictureTaken(byte[] data, Camera camera) {

                                    }
                                }, null//postview 回调与postview图像数据
                    , new Camera.PictureCallback() {//jpeg 回调JPEG图片数据
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {

                        }
                    });
        }
    }

    private void setCameraRotation(int rotation) {
        Camera.CameraInfo info = new Camera.CameraInfo();
        //获取摄像头信息
        Camera.getCameraInfo(CameraConfig.CAMERA_FRONT, info);
        //获取摄像头当前的角度
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        switch (info.facing) {
            case Camera.CameraInfo.CAMERA_FACING_FRONT:
                int result = (info.orientation + degrees) % 360;
                result = (360 - result) % 360;
                mCamera.setDisplayOrientation(result);
                break;
            case Camera.CameraInfo.CAMERA_FACING_BACK:
                result = (info.orientation - degrees + 360) % 360;
                mCamera.setDisplayOrientation(result);
                break;
        }
    }

    public void stopPreview() {
        if (mCamera != null) {
            //预览数据回调接口
            mCamera.setPreviewCallback(null);
            //停止预览
            mCamera.stopPreview();
        }
    }

    @Override
    public void releaseCamera() {
        if (mCamera != null) {
            stopPreview();
            //释放摄像头
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void startPreView() {
        if (mCamera != null ) {
            mCamera.startPreview();
        } else {
            ViseLog.e("surfaceHolder为空，无法预览");
        }
    }

    @Override
    public void stopPreView() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }


    //可以获得连续的帧
    public void setPreviewCallback(Camera.PreviewCallback previewCallback) {
        if (previewCallback != null) {
            mPreviewCallback = previewCallback;
        } else {
            mPreviewCallback = callback;
        }
    }


    private Camera.PreviewCallback callback = new Camera.PreviewCallback() {
        @Override
        public void onPreviewFrame(byte[] bytes, Camera camera) {
            //SurfaceView 直接预览的  会影响到你的预览吗？
            //Thread.sleep(10_000);
            // data数据依然是倒的
            if (null != mPreviewCallback) {
                mPreviewCallback.onPreviewFrame(bytes, camera);
            }
            camera.addCallbackBuffer(CameraConfig.getBuffer());
        }
    };

    private Camera.AutoFocusCallback autoFocusCallback = new Camera.AutoFocusCallback() {
        @Override
        public void onAutoFocus(boolean success, Camera camera) {

        }
    };

    //拍照后回调--data便是图片数据
    public void setmShutterCallback(Camera.ShutterCallback mShutterCallback) {
        this.mShutterCallback = mShutterCallback;
    }

    public void setmAutoFocusCallback(Camera.AutoFocusCallback mAutoFocusCallback) {
        this.mAutoFocusCallback = mAutoFocusCallback;
    }
}
