package com.xing.gfoxdialog

import android.content.Intent
import android.os.Bundle
import android.os.Environment
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

class DialogMainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    override fun getLayoutView(): View {
        binding = ActivityMainBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun isShowTitle(): Boolean {
        return false
    }

    override fun initUI(savedInstanceState: Bundle?) {
        super.initUI(savedInstanceState)
        U_permissions.applyStoragePermission11(mActivity, object :
            U_permissions.RequestPermissionCallBack {
            override fun requestPermissionSuccess() {
            }

            override fun requestPermissionFail(failPermission: MutableList<String>?) {
            }
        })
        ViseLog.d(U_file.getQdir(mActivity,Environment.DIRECTORY_PICTURES))
        binding.mainJava.setOnClickListener { startActivity(Intent(mActivity,DialogMainJavaActivity::class.java)) }
        binding.mainNotice.setOnClickListener {startActivity(Intent(mActivity,NotificationActivity::class.java))}
        binding.mainJava.setOnClickListener { startActivity(Intent(mActivity, DialogMainJavaActivity::class.java)) }
        binding.mainNotice.setOnClickListener { startActivity(Intent(mActivity, NotificationActivity::class.java)) }
        binding.mainPlayer.setOnClickListener { startActivity(Intent(mActivity, HHMedia3Activity::class.java)) }
        binding.mainWeb.setOnClickListener { startActivity(Intent(mActivity, H5Activity::class.java)) }
        binding.mainLog.setOnClickListener { startActivity(Intent(mActivity, LogActivity::class.java)) }
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
        }
        binding.mainDialog.setOnClickListener {
            startActivity(Intent(mActivity, DialogActivity::class.java))
        }
    }
}

