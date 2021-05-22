package com.xing.gfox.camera;

import android.graphics.ImageFormat;
import android.hardware.Camera;

public class CameraConfig {
    //相机摄像头
    public static int CAMERA_FRONT = Camera.CameraInfo.CAMERA_FACING_FRONT;
    public static int CAMERA_BACK = Camera.CameraInfo.CAMERA_FACING_BACK;
    public static int currentCamera = CAMERA_BACK;
    //照片质量
    public static int JpegQuality = 100;
    //预览宽高
    public static int PreviewWidth = 1080;
    public static int PreviewHeight = 1920;
    //拍照图片宽高
    public static int PictureWidth = 1080;
    public static int PictureHeight = 1920;
    //对焦模式
    public static final String FOCUS_CONTINUOUS_PICTURE = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE;//连续对焦模式
    public static final String FOCUS_AUTO = Camera.Parameters.FOCUS_MODE_AUTO;//自动聚焦模式
    public static final String FOCUS_INFINITY = Camera.Parameters.FOCUS_MODE_INFINITY;//无穷远
    public static final String FOCUS_MACRO = Camera.Parameters.FOCUS_MODE_MACRO;//微距
    public static final String FOCUS_FIXED = Camera.Parameters.FOCUS_MODE_FIXED;//固定焦距
    public static String currentFocusMode = FOCUS_CONTINUOUS_PICTURE;
    //缩放
    public static int zoom = 1;
    //预览数据格式
    public static int pixelFormat = ImageFormat.NV21;
    //方向
    public static int cameraRotation = 0;
    //拍照音
    public static boolean isOpenVoice = false;

    public static byte[] getBuffer() {
        return new byte[PreviewWidth * PreviewHeight * 3 / 2];
    }

}
