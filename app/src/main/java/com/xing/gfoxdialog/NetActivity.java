package com.xing.gfoxdialog;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;

import com.xing.gfox.log.ViseLog;
import com.xing.gfox.rxHttp.download.DownloadListener;
import com.xing.gfox.rxHttp.download.FileDownloadManager;
import com.xing.gfox.rxHttp.download.FileDownloadService;
import com.xing.gfox.util.U_file;
import com.xing.gfox.util.U_string;
import com.xing.gfoxdialog.BaseApp.BaseActivity;

import java.io.File;

public class NetActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_net;
    }

    @Override
    public boolean isShowTitle() {
        return true;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        mTitle.setTitleText("网络测试");
    }

    public void download(View view) {
        FileDownloadManager manager = FileDownloadManager.getInstance(mActivity);
        manager.setDownloadListener(new DownloadListener() {

            @Override
            public void onDownLoadStart(String filePath, int startProgress) {

            }

            @Override
            public void onDownLoadProgress(String filePath, int progress) {
                ViseLog.d(filePath + " : " + progress);
            }

            @Override
            public void onDownLoadError(String filePath, String error) {

            }

            @Override
            public void onDownloadFinish(String filePath) {
                ViseLog.d(filePath);
            }
        });
//        manager.startDownload("https://hequn-1258324382.cos.ap-guangzhou.myqcloud.com/pic/95dc5c2bc4a342629fbcd445199af05d.mp4", U_file.DCIM + File.separator + U_string.getFileNameFromUrl("https://hequn-1258324382.cos.ap-guangzhou.myqcloud.com/pic/95dc5c2bc4a342629fbcd445199af05d.mp4"));
        manager.startDownload("https://hequn-1258324382.cos.ap-guangzhou.myqcloud.com/pic/cc9cf6a28fd54489a20930d058a550c3.mp4", U_file.DCIM + File.separator + U_file.getFileNameFromUrl("https://hequn-1258324382.cos.ap-guangzhou.myqcloud.com/pic/cc9cf6a28fd54489a20930d058a550c3.mp4"));
        manager.startDownloadAndOpen("https://hequn-1258324382.cos.ap-guangzhou.myqcloud.com/pic/ea6c1a3f302a4655ac4af41bcf308c58.mp4", U_file.DCIM + File.separator + U_file.getFileNameFromUrl("https://hequn-1258324382.cos.ap-guangzhou.myqcloud.com/pic/ea6c1a3f302a4655ac4af41bcf308c58.mp4"));
        manager.startDownloadAndOpen("https://d89e5f3d31f228f7455771c43bd08ddc.dlied1.cdntips.net/godlied4.myapp.com/myapp/1104466820/cos.release-40109/10040714_com.tencent.tmgp.sgame_a1338022_3.63.1.5_pCSk3N.apk?mkey=60a455b66e576053&f=9634&cip=110.87.70.166&proto=https&access_type=$header_X-Forwarded-Access-Type", U_file.DCIM + File.separator + "wzry.apk");
    }

    public void downloadService(View view) {
        FileDownloadService.bindService(mActivity, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                ((FileDownloadService.DownloadBinder) service).start("https://d89e5f3d31f228f7455771c43bd08ddc.dlied1.cdntips.net/godlied4.myapp.com/myapp/1104466820/cos.release-40109/10040714_com.tencent.tmgp.sgame_a1338022_3.63.1.5_pCSk3N.apk?mkey=60a455b66e576053&f=9634&cip=110.87.70.166&proto=https&access_type=$header_X-Forwarded-Access-Type",
                        U_file.DCIM, "wzry1.apk", true, new DownloadListener() {

                            @Override
                            public void onDownLoadStart(String filePath, int startProgress) {

                            }

                            @Override
                            public void onDownLoadProgress(String filePath, int progress) {
                                ViseLog.d(progress);
                            }

                            @Override
                            public void onDownLoadError(String filePath, String error) {

                            }

                            @Override
                            public void onDownloadFinish(String filePath) {
                                ViseLog.d(filePath);
                            }
                        });
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        });
    }

    public void okHttpSocket() {

    }
}
