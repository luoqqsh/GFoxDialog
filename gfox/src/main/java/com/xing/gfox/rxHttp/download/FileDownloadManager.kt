package com.xing.gfox.rxHttp.download

import android.content.Context
import com.xing.gfox.log.ViseLog
import com.xing.gfox.util.DialogTest.utils.OpenFileUtil
import com.xing.gfox.util.U_file
import com.xing.gfox.util.U_permissions
import kotlinx.coroutines.*
import okhttp3.*
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream
import java.util.*

class FileDownloadManager(context: Context) {
    private var downloadListener: DownloadListener? = null
    private val downloadUrls: MutableList<String>
    private val resultFilePaths: MutableList<String>
    private var okHttpClient: OkHttpClient? = null
    private val context: Context = context.applicationContext
    private var downloadCall: Call? = null

    private var isCancel = false
    fun setDownloadListener(downloadListener: DownloadListener?): FileDownloadManager {
        this.downloadListener = downloadListener
        return this
    }

    @Synchronized
    fun startDownload(url: String, folderName: String, fileName: String, isOpen: Boolean) {
        startDownload(url, "$folderName/$fileName", isOpen)
    }

    @Synchronized
    fun startDownloadAndOpen(url: String, resultFilePath: String) {
        startDownload(url, resultFilePath, true)
    }

    @Synchronized
    fun startDownload(url: String, resultFilePath: String) {
        startDownload(url, resultFilePath, false)
    }

    @Synchronized
    private fun startDownload(url: String, resultFilePath: String, isOpen: Boolean) {
        ViseLog.showInfo("""$url$resultFilePath""".trimIndent())
        U_permissions.applyWriteStoragePermission(context, object :
            U_permissions.RequestPermissionCallBack {
            override fun requestPermissionSuccess() {
                if (checkRepeat(url, resultFilePath)) {
                    downloadUrls.add(0, url)
                    resultFilePaths.add(0, resultFilePath)
                    download(url, resultFilePath, isOpen)
                } else {
                    downloadFinish(resultFilePath, isOpen)
                }
            }

            override fun requestPermissionFail(failPermission: MutableList<String>?) {
                ViseLog.e("????????????????????????")
                if (downloadListener != null) {
                    downloadListener!!.onDownLoadError(resultFilePath, "????????????????????????")
                }
            }
        })
    }

    @Synchronized
    fun cancelDownload() {
        isCancel = true
        downloadCall!!.cancel()
    }

    @Synchronized
    private fun checkRepeat(url: String, resultFilePath: String): Boolean {
        for (i in downloadUrls.indices) {
            if (downloadUrls[i] == url && resultFilePaths[i] == resultFilePath) {
                return false
            }
        }
        return true
    }

    @Synchronized
    private fun download(url: String, resultFilePath: String, isOpen: Boolean) {
        CoroutineScope(Dispatchers.IO).launch {
            if (okHttpClient == null) {
                okHttpClient = OkHttpClient()
            }
            var downloadedLength: Long = 0//????????????????????????
            var downloadFileLength: Long = 0//????????????????????????
            //?????????????????????????????????
            if (U_file.isFileExist(resultFilePath)) {
                //??????????????????????????????????????????????????????????????????????????????????????????
                downloadedLength = File(resultFilePath).length()
                val getSizeRequest = Request.Builder().url(url).build()
                try {
                    val execute = okHttpClient!!.newCall(getSizeRequest).execute()
                    val body = execute.body
                    if (body != null) {
                        downloadFileLength = body.contentLength()
                        body.close()
                        //????????????????????????????????????????????????????????????????????????
                        if (downloadedLength == downloadFileLength) {
                            withContext(Dispatchers.Main) {
                                downloadFinish(resultFilePath, isOpen)
                            }
                            return@launch
                        }
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                val request = if (downloadFileLength == 0L) {
                    Request.Builder().url(url).build()
                } else {
                    Request.Builder()
                        .addHeader("RANGE", "bytes=$downloadedLength-$downloadFileLength")
                        .url(url).build()
                }
                downloadCall = okHttpClient!!.newCall(request)
            } else {
                U_file.createFileFolder(resultFilePath)
                val request = Request.Builder().url(url).build()
                downloadCall = okHttpClient!!.newCall(request)
            }
            val response = downloadCall!!.execute()
            isCancel = false
            saveFile(response, resultFilePath, downloadFileLength, downloadedLength, isOpen)
        }
    }

    private suspend fun saveFile(
        response: Response,
        resultFilePath: String,
        downloadFileLength: Long,
        downloadedLength: Long,
        isOpen: Boolean
    ) {
        var fileLength = downloadFileLength
        var downLength = downloadedLength
        var downloadStream: InputStream? = null
        var fos: FileOutputStream? = null
        var body: ResponseBody? = null
        try {
            val buf = ByteArray(2048)
            var len = 0
            body = response.body
            if (body == null) {
                if (downloadListener != null) downloadListener!!.onDownLoadError(
                    resultFilePath,
                    "????????????????????????"
                )
                return
            }
            if (fileLength == 0L) {
                fileLength = body.contentLength()
            }
            downloadStream = body.byteStream()
            withContext(Dispatchers.Main) {
                if (downloadListener != null) downloadListener!!.onDownLoadStart(
                    resultFilePath,
                    downloadedLength.toInt()
                )
            }
            fos = FileOutputStream(File(resultFilePath), true)
            var progressLast = 0
            //??????????????????????????????????????????????????????????????????
            if (isCancel) {
                return
            }
            while (!isCancel && downloadStream.read(buf).also { len = it } != -1) {
                fos.write(buf, 0, len)
                downLength += len.toLong()
                val progress = (downLength * 1.0f / fileLength * 100).toInt()
//                ViseLog.showInfo("$downloadedLength $downloadFileLength $progress")
                //???????????????????????????????????????
                if (progress > progressLast) {
                    progressLast = progress
                    // ????????????????????????
                    withContext(Dispatchers.Main) {
                        if (downloadListener != null) downloadListener!!.onDownLoadProgress(
                            resultFilePath,
                            progress
                        )
                    }
                }
            }
            fos.flush()
            //????????????????????????while???????????????????????????
            if (isCancel) {
                return
            }
            // ????????????
            withContext(Dispatchers.Main) {
                downloadFinish(resultFilePath, isOpen)
            }
        } catch (e: Exception) {
            if (isCancel) {
                return
            }
            e.printStackTrace()
            withContext(Dispatchers.Main) {
                if (downloadListener != null) downloadListener!!.onDownLoadError(
                    resultFilePath,
                    e.message
                )
            }
        } finally {
            try {
                body?.close()
                downloadStream?.close()
                fos?.close()
            } catch (e: IOException) {
                e.printStackTrace()
                if (downloadListener != null) downloadListener!!.onDownLoadError(
                    resultFilePath,
                    e.message
                )
            }
        }
    }

    private fun downloadFinish(resultFilePath: String, isOpen: Boolean) {
        if (isOpen) {
            context.startActivity(OpenFileUtil.openFile(resultFilePath))
        }
        if (downloadListener != null) downloadListener!!.onDownloadFinish(resultFilePath)
    }

    companion object {
        private var fileDownloadManager: FileDownloadManager? = null

        @JvmStatic
        fun getInstance(context: Context): FileDownloadManager {
            if (fileDownloadManager == null) {
                fileDownloadManager = FileDownloadManager(context)
            }
            return fileDownloadManager!!
        }
    }

    init {
        downloadUrls = ArrayList()
        resultFilePaths = ArrayList()
    }
}
