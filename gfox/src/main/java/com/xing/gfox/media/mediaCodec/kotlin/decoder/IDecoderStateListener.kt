package com.xing.gfox.media.mediaCodec.kotlin.decoder

/**
 * 解码状态回调接口
 *
 */
interface IDecoderStateListener {
    fun decoderPrepare(decodeJob: Decoder?)
    fun decoderReady(decodeJob: Decoder?)
    fun decoderRunning(decodeJob: Decoder?)
    fun decoderPause(decodeJob: Decoder?)
    fun decodeOneFrame(decodeJob: Decoder?, frame: Frame)
    fun decoderFinish(decodeJob: Decoder?)
    fun decoderDestroy(decodeJob: Decoder?)
    fun decoderError(decodeJob: Decoder?, msg: String)
}