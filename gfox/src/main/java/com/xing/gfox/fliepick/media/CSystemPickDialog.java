package com.xing.gfox.fliepick.media;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import java.util.List;

import com.xing.gfox.base.toast.U_Toast;
import com.xing.gfox.fliepick.bean.CPickType;
import com.xing.gfox.fliepick.bean.FileBean;
import com.xing.gfox.fliepick.bean.MediaSelectListener;
import com.xing.gfox.util.U_device;
import com.xing.gfox.util.U_permissions;

public class CSystemPickDialog extends BasePickDialog {

    private CSystemPickDialog() {
    }

    public static MediaSelectBuilder builder() {
        return new MediaSelectBuilder();
    }

    public static class MediaSelectBuilder {
        private final MediaConfig mediaConfig;

        private MediaSelectBuilder() {
            mediaConfig = new MediaConfig();
        }

        //设置单选回调
        public MediaSelectBuilder setOnSingleListener(MediaSelectListener<FileBean> onSingleListener) {
            mediaConfig.setOnSingleListener(onSingleListener);
            return this;
        }

        //设置多选回调
        public MediaSelectBuilder setMediaUri(Uri uri) {
            mediaConfig.setMediaOutputUri(uri);
            return this;
        }

        //设置裁剪参数
        public MediaSelectBuilder setCropParam(int cropAspectX, int cropAspectY, int cropOutputX, int cropOutputY) {
            mediaConfig.setCropParams(cropAspectX, cropAspectY, cropOutputX, cropOutputY);
            return this;
        }

        //设置是否调用系统裁剪
        public MediaSelectBuilder setNeedCrop(boolean isNeedCrop) {
            mediaConfig.setNeedSystemCrop(isNeedCrop);
            return this;
        }

        //开始录像（大小限制或时长限制）
        public void startTakeVideo(FragmentActivity mActivity, long sizeLimit, int duration) {
            CSystemPickDialog cSystemPickDialog = new CSystemPickDialog();
            mediaConfig.setRecordSizeLimit(sizeLimit);
            mediaConfig.setRecordDuration(duration);
            cSystemPickDialog.setMediaConfig(mediaConfig);
            cSystemPickDialog.setChooseType(CPickType.record);
            cSystemPickDialog.showNow(mActivity);
        }

        //开始录像
        public void startTakeVideo(FragmentActivity mActivity) {
            startTakeVideo(mActivity, 0, 0);
        }

        //开始裁剪图片
        public void startCropPic(FragmentActivity mActivity, Uri uri) {
            CSystemPickDialog cSystemPickDialog = new CSystemPickDialog();
            mediaConfig.setSelectList(new FileBean(uri));
            cSystemPickDialog.setMediaConfig(mediaConfig);
            cSystemPickDialog.setChooseType(CPickType.crop);
            cSystemPickDialog.showNow(mActivity);
        }

        //开始拍照
        public void startTakePhoto(FragmentActivity mActivity) {
            CSystemPickDialog cSystemPickDialog = new CSystemPickDialog();
            cSystemPickDialog.setMediaConfig(mediaConfig);
            cSystemPickDialog.setChooseType(CPickType.camera);
            cSystemPickDialog.showNow(mActivity);
        }

        //开始调用选择器（指定类型）
        public void startPick(FragmentActivity mActivity, int pickType) {
            CSystemPickDialog cSystemPickDialog = new CSystemPickDialog();
            cSystemPickDialog.setMediaConfig(mediaConfig);
            cSystemPickDialog.setChooseType(pickType);
            cSystemPickDialog.showNow(mActivity);
        }

        //开始调用选择器（指定类型）
        public void startPickByType(FragmentActivity mActivity, String pickType) {
            CSystemPickDialog cSystemPickDialog = new CSystemPickDialog();
            mediaConfig.setContentMediaType(pickType);
            cSystemPickDialog.setMediaConfig(mediaConfig);
            cSystemPickDialog.setChooseType(CPickType.byType);
            cSystemPickDialog.showNow(mActivity);
        }
    }

    @Override
    protected int getLayoutId() {
        return 0;
    }

    @Override
    protected void initUI(View mView, Bundle bundle) {
        initData();
    }

    private void initData() {
        //直接调起拍照
        if (chooseType == CPickType.camera) {
            startCamera();
            return;
        }
        //直接调起录像
        if (chooseType == CPickType.record) {
            startRecordVideo();
            return;
        }
        U_permissions.applyWriteStoragePermission(mActivitys.get(), new U_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {
                Intent intent;
                switch (chooseType) {
                    case CPickType.images://获取相册中的图片
                        intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        dialog.get().startActivityForResult(intent, chooseType);
                        break;
                    case CPickType.videos://获取相册中的视频
                        intent = new Intent();
                        if (U_device.INSTANCE.isMeiZuPhone()) {
                            intent.setAction(Intent.ACTION_OPEN_DOCUMENT);
                            intent.addCategory(Intent.CATEGORY_OPENABLE);
                            intent.setType(CPickType.mediaVideo);
                        } else {
                            intent.setAction(Intent.ACTION_PICK);
                            intent.setData(android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI);
                        }
                        dialog.get().startActivityForResult(intent, chooseType);
                        break;
                    case CPickType.music://获取相册中的音乐
                        intent = new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI);
                        dialog.get().startActivityForResult(intent, chooseType);
                        break;
                    case CPickType.crop://裁剪
                        startCrop();
                        break;
                    case CPickType.byType://根据类型获取所有本地资源，必须要有资源类型
                        if (!TextUtils.isEmpty(mediaConfig.getContentMediaType())) {
                            intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType(mediaConfig.getContentMediaType());
                            dialog.get().startActivityForResult(intent, chooseType);
                        } else {
                            U_Toast.show("文件类型设置异常");
                        }
                        break;
                }
            }

            @Override
            public void requestPermissionFail(List<String> failPermission) {
                U_Toast.show("缺少相关权限，请到设置里授权！");
                dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (chooseType == CPickType.crop) {
                mediaConfig.setSelectList(new FileBean(intent.getData()));
                mediaConfig.callback(mActivitys.get());
                dismiss();
                return;
            }
            if (chooseType == CPickType.camera || chooseType == CPickType.record) {
                super.onActivityResult(requestCode, resultCode, intent);
            } else {
                //拍照完进行裁剪的跳转
                if (intent != null) {
                    mediaConfig.setSelectList(new FileBean(intent.getData()));
                    if (mediaConfig.isNeedSystemCrop() && chooseType != CPickType.crop) {
                        startCrop();
                    } else {
                        mediaConfig.callback(mActivitys.get());
                        dismiss();
                    }
                }
            }
        } else dismiss();
    }
}
