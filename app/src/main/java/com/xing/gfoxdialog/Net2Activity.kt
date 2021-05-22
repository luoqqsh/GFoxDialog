package com.xing.gfoxdialog

import android.content.ComponentName
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.view.View
import com.xing.gfox.base.activity.HLBaseActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.xing.gfox.hardware.netWork.U_net
import com.xing.gfox.log.ViseLog
import com.xing.gfox.rxHttp.download.DownloadListener
import com.xing.gfox.rxHttp.download.FileDownloadManager
import com.xing.gfox.rxHttp.download.FileDownloadService
import com.xing.gfox.util.U_file
import com.xing.gfox.util.U_string
import java.io.File

class Net2Activity : HLBaseActivity() {
    override fun getLayoutId(): Int {
        return R.layout.activity_net
    }

    override fun initUI(savedInstanceState: Bundle) {
        mTitle.setTitleText("网络测试")
    }

    fun download(view: View?) {
        val instance: FileDownloadManager = FileDownloadManager.getInstance(mActivity)
        instance.setDownloadListener(object : DownloadListener {
            override fun onDownLoadStart(filePath: String?, startProgress: Int) {
            }

            override fun onDownLoadProgress(filePath: String?, progress: Int) {
            }

            override fun onDownLoadError(filePath: String?, error: String?) {
            }

            override fun onDownloadFinish(filePath: String?) {
            }

        })
        instance.startDownload(
            "下载地址",
            U_file.DOWNLOADS + File.separator + U_string.getFileNameFromUrl("下载地址")
        )
    }

    fun downloadService(view: View?) {
        //后台下载
        FileDownloadService.bindService(mActivity.applicationContext, object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName, service: IBinder) {
                (service as FileDownloadService.DownloadBinder).start(
                    "下载地址",
                    U_file.DOWNLOADS,
                    "xx.apk", true,
                    object : DownloadListener {
                        override fun onDownLoadStart(filePath: String?, startProgress: Int) {
                        }

                        override fun onDownLoadProgress(filePath: String?, progress: Int) {
                            ViseLog.d(progress)
                        }

                        override fun onDownLoadError(filePath: String?, error: String?) {
                        }

                        override fun onDownloadFinish(filePath: String?) {
                            ViseLog.d(filePath)
                        }

                    })
            }

            override fun onServiceDisconnected(name: ComponentName) {}
        })
    }

    private fun net() {
        val launch1 = CoroutineScope(Dispatchers.IO).launch {
            ViseLog.d(U_net.canConnGoogle())
            ViseLog.d(U_net.getNetIp())
            U_net.getNetworkType(mActivity)
        }
    }
}