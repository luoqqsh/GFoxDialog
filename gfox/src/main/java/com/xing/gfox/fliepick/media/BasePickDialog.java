package com.xing.gfox.fliepick.media;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.core.content.FileProvider;

import com.xing.gfox.R;
import com.xing.gfox.base.dialog.BaseNDialog;
import com.xing.gfox.base.toast.U_Toast;
import com.xing.gfox.fliepick.bean.CPickType;
import com.xing.gfox.media.U_media;
import com.xing.gfox.util.U_file;
import com.xing.gfox.util.U_permissions;
import com.xing.gfox.util.U_time;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.xing.gfox.fliepick.bean.FileBean;

public abstract class BasePickDialog extends BaseNDialog {
    protected List<FileBean> mFileList = new ArrayList<>();//数据
    protected @CPickType
    int chooseType, chooseTypeTemp;
    protected MediaConfig mediaConfig;

    public void setChooseType(int chooseType) {
        this.chooseType = chooseType;
    }

    public void setMediaConfig(MediaConfig mediaConfig) {
        this.mediaConfig = mediaConfig;
    }

    public void startRecordVideo() {
        U_permissions.applyCameraPermission(mActivitys.get(), new U_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getMediaUri());// 指定路径，uri格式
                intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, mediaConfig.getRecordSizeLimit() * 1024 * 1024L);// 1 限制录制大小，Long类型，最小5MB
                intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);// 1 质量最高，默认的话是1.
                intent.putExtra(MediaStore.EXTRA_DURATION_LIMIT, mediaConfig.getRecordDuration());//最大长度，单位秒
                dialog.get().startActivityForResult(intent, chooseType);
            }

            @Override
            public void requestPermissionFail(Map<String, Boolean> failPermission) {
                dismiss();
            }
        });
    }

    private Uri getMediaUri() {
        if (mediaConfig.getMediaOutputUri() == null) {
            Uri mediaUri;
            String type;
            String imgName;
            if (chooseType == CPickType.record) {
                type = Environment.DIRECTORY_MOVIES;
                imgName = "video" + U_time.getNowTimeLong() + ".mp4";
            } else {
                imgName = "pic" + U_time.getNowTimeLong() + ".jpg";
                type = Environment.DIRECTORY_PICTURES;
            }
            File photoFile = new File(U_file.getQdir(mActivitys.get(), type).getAbsolutePath() + "/" + imgName);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                mediaUri = FileProvider.getUriForFile(mActivitys.get(), mActivitys.get().getPackageName() + ".dialog.fileProvider", photoFile);
            } else {
                mediaUri = Uri.fromFile(photoFile);
            }
            mediaConfig.setMediaOutputUri(mediaUri);
            return mediaUri;
        } else {
            return mediaConfig.getMediaOutputUri();
        }
    }

    public void startCrop() {
        if (mediaConfig.getSelectMedia().getFilePathUri() == null) {
            dismiss();
            return;
        }
        chooseType = CPickType.crop;
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        intent.setDataAndType(mediaConfig.getSelectMedia().getFilePathUri(), "image/*");
        // 下面这个crop=true是设置在开启的Intent中设置显示的VIEW可裁剪
        intent.putExtra("crop", true);
        intent.putExtra("scale", true);
        intent.putExtra("scaleUpIfNeeded", true);// 去黑边
        if (mediaConfig.getCropOutputX() != 0 && mediaConfig.getCropOutputY() != 0) {
            intent.putExtra("outputX", mediaConfig.getCropOutputX());
            intent.putExtra("outputY", mediaConfig.getCropOutputY());
        }
        intent.putExtra("aspectX", mediaConfig.getCropAspectX());
        intent.putExtra("aspectY", mediaConfig.getCropAspectY());
        intent.putExtra("return-data", false);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true); // no face detection
        dialog.get().startActivityForResult(intent, chooseType);
    }

    public void startCamera() {
        U_permissions.applyCameraPermission(mActivitys.get(), new U_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用手机的相机
                intent.putExtra(MediaStore.EXTRA_OUTPUT, getMediaUri());
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                dialog.get().startActivityForResult(intent, chooseType);
            }

            @Override
            public void requestPermissionFail(Map<String, Boolean> failPermission) {
                U_Toast.show(mActivitys.get().getString(R.string.no_camera_permission));
                dismiss();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (resultCode == Activity.RESULT_OK) {
            if (chooseType == CPickType.camera || chooseType == CPickType.record) {
                FileBean fileBean = U_media.getMediaInfoFromDB(mActivitys.get(), new FileBean(mediaConfig.getMediaOutputUri()));
                mediaConfig.setSelectList(fileBean);
                if (mediaConfig.isNeedSystemCrop() && chooseType == CPickType.camera) {
                    startCrop();
                } else {
                    mediaConfig.callback(mActivitys.get());
                    dismiss();
                }
            }
        } else dismiss();
    }
}
