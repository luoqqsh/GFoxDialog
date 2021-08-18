package com.xing.gfoxdialog;

import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.xing.gfox.base.dialog.NDialog;
import com.xing.gfox.base.interfaces.OnSimpleListener;
import com.xing.gfox.base.interfaces.ProgressListener;
import com.xing.gfox.base.toast.U_Toast;
import com.xing.gfox.dialog.CBottomListDialog;
import com.xing.gfox.dialog.CFileListDialog;
import com.xing.gfox.dialog.CProgressDialog;
import com.xing.gfox.dialog.CTipDialog;
import com.xing.gfox.dialog.CWaitingDialog;
import com.xing.gfox.fliepick.bean.CPickType;
import com.xing.gfox.fliepick.media.CMediaPickDialog;
import com.xing.gfox.fliepick.media.CSystemPickDialog;
import com.xing.gfox.log.ViseLog;
import com.xing.gfox.media.U_media;
import com.xing.gfox.rxHttp.U_http;
import com.xing.gfox.rxHttp.li.livedata.BaseObserverCallBack;
import com.xing.gfox.rxHttp.task.TaskDelayBManager;
import com.xing.gfox.util.U_file;
import com.xing.gfoxdialog.BaseApp.BaseActivity;
import com.xing.gfoxdialog.api.BaseObserver;
import com.xing.gfoxdialog.api.TestEnv;
import com.xing.gfoxdialog.databinding.ActivityDialogBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DialogActivity extends BaseActivity {
    ActivityDialogBinding binding;

    @Override
    public View getLayoutView() {
        binding = ActivityDialogBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected int getBackgroundColorResource() {
        return R.color.white;
    }

    @Override
    public boolean isShowTitle() {
        return true;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        mTitle.setLeftButtonImage(R.mipmap.back_black, v -> finish());
        mTitle.setTitleText("对话框");
    }

    //加载弹窗
    public void loadingDialog(View view) {
        CWaitingDialog cWaitingDialog = new CWaitingDialog();
        cWaitingDialog.showDialog(mActivity);
    }

    public void transDialog(View view) {
        //透明弹窗
        new NDialog(mActivity, R.layout.ndialog_trans, 0, false).createTrans(150);
    }

    //普通消息弹窗
    public void tipDialog(View view) {
        //显示iPhone样式弹窗
        CTipDialog c = new CTipDialog(CTipDialog.nDialog_Iphone2)
                .setTouchOutSideCancelable(true, true)
                .setMessage("我是鬼，我来抓你了！", Gravity.CENTER)
                .setBtnClickListener("好", "不好", (dialog, whichBtn) -> {
                    switch (whichBtn) {
                        case 1:
                            Toast.makeText(mActivity, "点击了好", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(mActivity, "点击了不好", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    return false;
                });
        c.setOnCancelListener(new OnSimpleListener() {
            @Override
            public void onListen() {
                ViseLog.d("cancel");
            }
        });
        c.setOnDismissListener(new OnSimpleListener() {
            @Override
            public void onListen() {
                ViseLog.d("dismiss");
            }
        });
        c.showDialog(mActivity);
    }

    //系统自带选择图片、视频弹窗
    public void systemDialog(View view) {
        CSystemPickDialog.builder().setNeedCrop(false).setOnSingleListener(it -> {
            ViseLog.d(it);
        }).startPick(mActivity, CPickType.videos);
    }

    //进度弹窗
    public void progressDialog(View view) {
        CProgressDialog p = new CProgressDialog();
        p.setStartProgress(60);
        p.showDialog(mActivity);
        new TaskDelayBManager() {
            @Override
            public void onListen(Long index) {
                ViseLog.d(index);
                p.showProgress(Integer.parseInt(index.toString()));
            }
        }.loop(1, 1000);
    }

    //自定义多选弹窗
    public void diyDialog(View view) {
        CMediaPickDialog.builder().addTip(mActivity, "123")
                .setNeedPreview(true)
                .setShowCamera(true)
                .setOnMoreSelectListener(info -> {
                    ViseLog.d(info);
//                    upLoadImg(info.get(0).getFilePathUri());
//                    GlideUtil.instance().setDefaultImage(mActivity, info.get(0).getFilePathUri(), binding.imgimg);
                })
                .startSelectImg(mActivity);
    }

    //底部列表弹窗
    public void bottomDialog(View view) {
        CBottomListDialog cBottomListDialog = new CBottomListDialog();
        cBottomListDialog.setDataList("选取照片", "选取视频");
        cBottomListDialog.setShowCancelBtn(true);
        cBottomListDialog.setOnClickItemListener((text, position) -> {
            ViseLog.d(text + " " + position);
            return true;
        });
        cBottomListDialog.showDialog(mActivity);
    }

    //文件选择对话框，来自第三方
    public void fileDialog(View view) {
        CFileListDialog c = new CFileListDialog();
        c.setPath(U_file.SDROOT);
        c.showDialog(mActivity);
    }

    public void upLoadImg(Uri uri) {
        TestEnv.getUploadApi().uploadHead("user/uploadHead", U_http.uriToMultipartRequestBody("file", uri, new ProgressListener() {
            @Override
            public void onProgress(long progress) {
                U_Toast.show(progress);
            }
        })).observe(mActivity, new BaseObserver<>(new BaseObserverCallBack<String>() {
            @Override
            public void onSuccess(String data) {
                ViseLog.d(data);
            }

            @Override
            public void onFail(String msg) {
                super.onFail(msg);
                ViseLog.d(msg);
            }
        }));
    }

    public void musicDialog(View view) {
        File file = new File(U_file.DOWNLOADS);
        if (!file.exists()) {
            return;
        }
        File[] a = file.listFiles();
        if (a == null) return;
        List<File> fileListSort = new ArrayList<>(Arrays.asList(a));
        for (int i = 0; i < fileListSort.size(); i++) {
            if (U_file.isFileExist(fileListSort.get(i))) {
                U_media.updateMedia(mActivity, fileListSort.get(i).getAbsolutePath());
            }
        }
        ViseLog.d("刷新音乐完成");
    }
}
