package com.xing.gfoxdialog.Media

import android.os.Bundle
import android.view.View
import com.xing.gfox.log.ViseLog
import com.xing.gfox.media.mediaCodec.kotlin.MediaCInfo
import com.xing.gfox.media.mediaCodec.kotlin.decoder.AudioDecoder
import com.xing.gfox.media.mediaCodec.kotlin.decoder.VideoDecoder
import com.xing.gfox.media.mediaCodec.kotlin.muxer.MP4Repack
import com.xing.gfoxdialog.BaseApp.BaseActivity
import com.xing.gfoxdialog.databinding.ActivityMediacodecBinding
import java.util.concurrent.Executors

class MediaCodecActivity : BaseActivity() {
    private lateinit var audioDecoder: AudioDecoder
    private lateinit var videoDecoder: VideoDecoder
    private val musicPath = "/storage/emulated/0/Music/不可说 - 霍建华、赵丽颖.mp3"
    private val videoPath = "/storage/emulated/0/mvtest.mp4"
//    private val videoPath = "http://s.bizhijingling.com/uploadfile/coustom/2019//309/20190613170125729.mp4"
//    private val videoPath = "http://cstvpull.live.wscdns.com/live/xiamen1.flv"

    lateinit var binding: ActivityMediacodecBinding
    override fun getLayoutView(): View {
        binding = ActivityMediacodecBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initUI(savedInstanceState: Bundle?) {
        binding.path.text = videoPath
        binding.start.setOnClickListener { initPlayer() }
    }

    private fun initPlayer() {
        ViseLog.d(MediaCInfo(videoPath))
        //创建线程池
        val threadPool = Executors.newFixedThreadPool(2)
//        //创建视频解码器
        videoDecoder = VideoDecoder(videoPath, binding.sfv, null)
        threadPool.execute(videoDecoder)
//        //创建音频解码器
        audioDecoder = AudioDecoder(videoPath)
        threadPool.execute(audioDecoder)
//        //开启播放
        videoDecoder.goOn()
        audioDecoder.goOn()

    }

    private fun dabao(path: String) {
        MP4Repack(path).start()
    }

    override fun onDestroy() {
        super.onDestroy()
        videoDecoder.doneDecode()
        audioDecoder.doneDecode()
    }
}