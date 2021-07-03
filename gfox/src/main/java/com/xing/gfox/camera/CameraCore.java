package com.xing.gfox.camera;

import android.content.Context;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Build;
import android.view.SurfaceHolder;

public abstract class CameraCore {
    public int mCameraId = CameraConfig.CAMERA_BACK;

    public abstract void takePhoto();

    public abstract void startPreView();

    public abstract void stopPreView();

    public abstract void releaseCamera();

    //设置预览
    public abstract void setPreviewDisplay(SurfaceHolder surfaceHolder);

    //设置摄像头id
    public abstract void setCameraId(int mCameraId);

    //设置参数
    public abstract void initCameraParam();

    public synchronized void switchCamera() {
        if (mCameraId == CameraConfig.CAMERA_BACK) {
            mCameraId = CameraConfig.CAMERA_FRONT;
        } else {
            mCameraId = CameraConfig.CAMERA_BACK;
        }
        setCameraId(mCameraId);
        startPreView();
    }

    /**
     * 用于检查是否支持Camera 2
     * <p>
     * 事实上，在各个厂商的的Android设备上，Camera2的各种特性并不都是可用的，
     * 需要通过characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL)方法来根据返回值来获取支持的级别：
     * <p>
     * 1. INFO_SUPPORTED_HARDWARE_LEVEL_FULL：全方位的硬件支持，允许手动控制全高清的摄像、支持连拍模式以及其他新特性。
     * 2. INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED：有限支持，这个需要单独查询。
     * 3. INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY：所有设备都会支持，也就是和过时的Camera API支持的特性是一致的。
     *
     * @param mContext mContext
     * @return 结果
     */
    public static boolean hasCamera2(Context mContext) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) return false;
        if (mContext == null) return false;
        try {
            CameraManager manager = (CameraManager) mContext.getSystemService(Context.CAMERA_SERVICE);
            String[] idList = manager.getCameraIdList();
            boolean notFull = true;
            if (idList.length == 0) {
                notFull = false;
            } else {
                for (final String str : idList) {
                    if (str == null || str.trim().isEmpty()) {
                        notFull = false;
                        break;
                    }
                    final CameraCharacteristics characteristics = manager.getCameraCharacteristics(str);
                    final int supportLevel = characteristics.get(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL);
                    if (supportLevel == CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY) {
                        notFull = false;
                        break;
                    }
                }
            }
            return notFull;
        } catch (Throwable ignore) {
            return false;
        }
    }
}
