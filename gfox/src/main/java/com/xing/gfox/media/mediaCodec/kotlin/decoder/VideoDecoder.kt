package com.xing.gfox.media.mediaCodec.kotlin.decoder

import android.media.MediaCodec
import android.media.MediaFormat
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.xing.gfox.media.mediaCodec.kotlin.extractor.IExtractor
import com.xing.gfox.media.mediaCodec.kotlin.extractor.VideoExtractor
import java.nio.ByteBuffer

/**
 * 视频解码器
 * 视频播放
 */
class VideoDecoder(path: String, sfv: SurfaceView?, surface: Surface?) : Decoder(path) {
    private val TAG = "VideoDecoder"

    private val mSurfaceView = sfv
    private var mSurface = surface

    override fun checkSurface(): Boolean {
        if (mSurfaceView == null && mSurface == null) {
            Log.e(TAG, "SurfaceView和Surface都为空，至少需要一个不为空")
            mStateListener?.decoderError(this, "Surface为空")
            return false
        }
        return true
    }

    //生成数据提取器
    override fun initExtractor(path: String): IExtractor {
        return VideoExtractor(path)
    }

    override fun initSpecParams(format: MediaFormat) {
    }

    //配置解码器
    override fun configCodec(codec: MediaCodec, format: MediaFormat): Boolean {
        when {
            mSurface != null -> {
                codec.configure(format, mSurface, null, 0)
                notifyDecode()
            }
            mSurfaceView?.holder?.surface != null -> {
                mSurface = mSurfaceView.holder?.surface
                configCodec(codec, format)
            }
            else -> {
                mSurfaceView?.holder?.addCallback(object : SurfaceHolder.Callback2 {
                    override fun surfaceRedrawNeeded(holder: SurfaceHolder) {
                    }

                    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {
                    }

                    override fun surfaceDestroyed(holder: SurfaceHolder) {
                    }

                    override fun surfaceCreated(holder: SurfaceHolder) {
                        mSurface = holder.surface
                        configCodec(codec, format)
                    }
                })

                return false
            }
        }
        return true
    }

    override fun initRender(): Boolean {
        return true
    }

    override fun render(outputBuffer: ByteBuffer, bufferInfo: MediaCodec.BufferInfo) {
    }

    override fun doneDecode() {
    }
}