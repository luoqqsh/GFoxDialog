package com.xing.gfox.media.mediaCodec.kotlin.decoder

import android.media.MediaCodec
import java.nio.ByteBuffer

/**
 * 一帧数据
 *
 */
class Frame {
    var buffer: ByteBuffer? = null

    var bufferInfo = MediaCodec.BufferInfo()
    private set

    fun setBufferInfo(info: MediaCodec.BufferInfo) {
        bufferInfo.set(info.offset, info.size, info.presentationTimeUs, info.flags)
    }
}