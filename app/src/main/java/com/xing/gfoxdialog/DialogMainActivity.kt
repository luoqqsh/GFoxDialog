package com.xing.gfoxdialog

import android.content.ContentUris
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.View
import com.xing.gfox.log.ViseLog
import com.xing.gfox.util.U_file
import com.xing.gfox.util.U_permissions
import com.xing.gfoxdialog.BaseApp.BaseActivity
import com.xing.gfoxdialog.FragmentTest.switchFragment
import com.xing.gfoxdialog.Media.HHMedia3Activity
import com.xing.gfoxdialog.Media.MediaCodecActivity
import com.xing.gfoxdialog.databinding.ActivityMainBinding
import com.xing.gfoxdialog.zuoye.StudyTestActivity


import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

class DialogMainActivity : BaseActivity() {
    var videoPath = "/storage/emulated/0/DCIM/videoWallpaper/video/e746eea4ce4d8af5cff561f4dd768698.mp4"//三星
    var videoPath2 = "/storage/emulated/0/DCIM/wallpaperfairy/videoWallpaper/48f51b346e353f1d1ac2c1a4c906eaa2.mp4"//小米m1
    var onLineVideoPath3 = "http://s.bizhijingling.com/uploadfile/coustom/2021/6162/20210127134728841.mp4"
    var onLineMp4Path3 = "http://111.12.102.68:6610/PLTV/77777777/224/3221225674/index.m3u8"
    lateinit var binding: ActivityMainBinding
    override fun getLayoutView(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun isShowTitle(): Boolean {
        return false
    }

    private fun init() {
        ViseLog.d(U_file.getQdir(mActivity,Environment.DIRECTORY_PICTURES))
        binding.mainJava.setOnClickListener { startActivity(Intent(mActivity,DialogMainJavaActivity::class.java)) }
        binding.mainNotice.setOnClickListener {startActivity(Intent(mActivity,NotificationActivity::class.java))}
        binding.mainJava.setOnClickListener { startActivity(Intent(mActivity, DialogMainJavaActivity::class.java)) }
        binding.mainNotice.setOnClickListener { startActivity(Intent(mActivity, NotificationActivity::class.java)) }
        binding.mainPlayer.setOnClickListener { startActivity(Intent(mActivity, HHMedia3Activity::class.java)) }
        binding.mainWeb.setOnClickListener { startActivity(Intent(mActivity, H5Activity::class.java)) }
        binding.mainLog.setOnClickListener { startActivity(Intent(mActivity, LogActivity::class.java)) }
        binding.mainOpenGL.setOnClickListener { startActivity(Intent(mActivity, GLActivity::class.java)) }
        binding.mainConfusion.setOnClickListener { startActivity(Intent(mActivity, MappingActivity::class.java)) }
        binding.mainView.setOnClickListener { startActivity(Intent(mActivity, DiyViewActivity::class.java)) }
        binding.mainCamera.setOnClickListener { startActivity(Intent(mActivity, HKCameraActivity::class.java)) }
        binding.mainStudy.setOnClickListener { startActivity(Intent(mActivity, StudyTestActivity::class.java)) }
        binding.mainMediaCodec.setOnClickListener { startActivity(Intent(mActivity, MediaCodecActivity::class.java)) }
        binding.mainToast.setOnClickListener { startActivity(Intent(mActivity, ToastActivity::class.java)) }
//        binding.mainMap.setOnClickListener { startActivity(Intent(mActivity, TaxiMainActivity::class.java)) }
        binding.mainSMS.setOnClickListener { startActivity(Intent(mActivity, NetActivity::class.java)) }
        binding.mainInformation.setOnClickListener { startActivity(Intent(mActivity, InformationActivity::class.java)) }
        binding.mainLocal.setOnClickListener {
            startActivity(Intent(mActivity, switchFragment::class.java))
        }
        binding.mainBBZ.setOnClickListener {
//            binding.mainImg.setImageBitmap(U_media.getNetVideoBitmap(onLineVideoPath3))
//            CoroutineScope(Dispatchers.IO).launch {
//                ViseLog.d(JniUtil.stringFromJNI())
//                ViseLog.d(JniUtil.getFFmpegVersion())
//                ViseLog.d(JniUtil.testSocket())
//            }
            //打开某个app 参考 https://blog.csdn.net/mlj1668956679/article/details/51983238/
            val packageName = "com.google.android.documentsui";
            val activityName = "com.android.documentsui.files.FilesActivity";

            val intent = Intent()
            intent.setClassName(packageName, activityName)
            startActivity(intent)
        }
        binding.mainDialog.setOnClickListener {
            startActivity(Intent(mActivity, DialogActivity::class.java))
        }
    }

    override fun initUI(savedInstanceState: Bundle?) {
        super.initUI(savedInstanceState)
        init()

        U_permissions.applyStoragePermission11(mActivity, object :
            U_permissions.RequestPermissionCallBack {
            override fun requestPermissionSuccess() {
            }

            override fun requestPermissionFail(failPermission: MutableList<String>?) {
            }
        })
    }

    private fun test() {
        //分区存储空间
        val file = File(filesDir, "filename")
        //应用专属外部存储空间
        val appSpecificExternalDir = File(getExternalFilesDir(Environment.DIRECTORY_DCIM), "filename")

        //访问公共媒体目录文件
        val cursor = contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, null, null, null, "${MediaStore.MediaColumns.DATE_ADDED} desc")
        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id = cursor.getLong(cursor.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                println("image uri is $uri")
            }
            cursor.close()
        }

        // SAF(存储访问框架--Storage Access Framework)
//        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//        intent.addCategory(Intent.CATEGORY_OPENABLE)
//        intent.type = "image/*"
//        startActivityForResult(intent, 100)
    }

//    //SAF回调
//    @RequiresApi(Build.VERSION_CODES.KITKAT)
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if (data == null || resultCode != Activity.RESULT_OK) return
//        if (requestCode == 100) {
//            val uri = data.data
//            println("image uri is $uri")
//        }
//    }

    @Throws(Exception::class)
    fun getFileStream(url: String?): ByteArray? {
        try {
            val httpUrl = URL(url)
            val conn: HttpURLConnection = httpUrl.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.connectTimeout = 5 * 1000
            val inStream: InputStream = conn.inputStream //通过输入流获取图片数据
            return readInputStream(inStream)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    @Throws(java.lang.Exception::class)
    fun readInputStream(inStream: InputStream): ByteArray? {
        val outStream = ByteArrayOutputStream()
        val buffer = ByteArray(1024)
        var len = 0
        while (inStream.read(buffer).also { len = it } != -1) {
            outStream.write(buffer, 0, len)
        }
        inStream.close()
        return outStream.toByteArray()
    }
}

