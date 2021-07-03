package com.xing.gfox.media.mediaCodec.kotlin.decoder
/**
 * 解码状态
 */
enum class DecodeState {
    /**开始状态*/
    START,

    /**解码中*/
    DECODING,

    /**解码暂停*/
    PAUSE,

    /**正在快进*/
    SEEKING,

    /**解码完成*/
    FINISH,

    /**解码器释放*/
    STOP
}
