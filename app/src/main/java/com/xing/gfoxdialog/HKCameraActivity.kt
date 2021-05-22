package com.xing.gfoxdialog

import android.content.IntentFilter
import android.graphics.SurfaceTexture
import android.os.Bundle
import android.view.TextureView
import android.view.View
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.xing.gfox.camera.VolumeDownReceiver
import com.xing.gfox.util.U_view
import com.xing.gfoxdialog.BaseApp.BaseActivity
import com.xing.gfoxdialog.databinding.ActivityCameraBinding


class HKCameraActivity : BaseActivity() {
    var preview: TextureView? = null
    val KEY_EVENT_ACTION = "KEY_EVENT_ACTION"

    lateinit var binding: ActivityCameraBinding
    override fun getLayoutView(): View {
        binding = ActivityCameraBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initUI(savedInstanceState: Bundle?) {
        super.initUI(savedInstanceState)
        preview = TextureView(mActivity)
        preview!!.surfaceTextureListener = object : TextureView.SurfaceTextureListener {
            override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture, width: Int, height: Int) {
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
                surface.release()
                return true
            }

            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture, width: Int, height: Int) {}
            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {}
        }
        U_view.addToParentCenter(binding.rlCamera, preview)

        // 设置音量-拍照
        val filter = IntentFilter().apply { addAction(KEY_EVENT_ACTION) }
        val volumeDownReceiver = VolumeDownReceiver()
        LocalBroadcastManager.getInstance(mActivity!!).registerReceiver(volumeDownReceiver, filter)
    }
}