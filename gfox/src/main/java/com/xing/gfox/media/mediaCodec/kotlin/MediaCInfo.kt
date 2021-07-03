package com.xing.gfox.media.mediaCodec.kotlin

import android.media.MediaFormat
import android.os.Build
import com.xing.gfox.media.mediaCodec.kotlin.extractor.AudioExtractor
import com.xing.gfox.media.mediaCodec.kotlin.extractor.VideoExtractor

/**
 * 通过mediacodec获取视频信息，支持格式mp4、mpg、3gp
 */
class MediaCInfo(val path: String) {
    var mediaPath: String = ""
    var vDurationUs: Long = 0
    var vType: String = ""
    var vFrameRate: Int = 0//帧率
    var vWidth: Int = 0//宽
    var vHeight: Int = 0//高
    var vRotateAngle: Float = 0.toFloat()//旋转角度
    var vTrackId: Int = 0//轨道：1.视频轨道，2.音频轨道
    var vMaxInputSize = 0//数据缓冲区最大大小(字节)

    var aDurationUs: Long = 0
    var aType: String = ""
    var aSampleRate: Int = 0//音频采样率
    var aChannels: Int = 0//音频通道数量
    var aBitRate: Int = 0//比特率（比特/秒）
    var aTrackId: Int = 0//轨道：1.视频轨道，2.音频轨道
    var aMaxInputSize = 0//数据缓冲区最大大小(字节)

    init {
        setPath(path)
    }

    fun setPath(path: String) {
        clearInfo()
        mediaPath = path
        val videoExtractor = VideoExtractor(path)
        val videoFormat = videoExtractor.getFormat()
        vDurationUs = videoFormat?.getLong(MediaFormat.KEY_DURATION) ?: 0
        vMaxInputSize = videoFormat?.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE) ?: 0
        vType = videoFormat?.getString(MediaFormat.KEY_MIME) ?: ""
        vFrameRate = videoFormat?.getInteger(MediaFormat.KEY_FRAME_RATE) ?: 0
        vWidth = videoFormat?.getInteger(MediaFormat.KEY_WIDTH) ?: 0
        vHeight = videoFormat?.getInteger(MediaFormat.KEY_HEIGHT) ?: 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            vTrackId = videoFormat?.getInteger(MediaFormat.KEY_TRACK_ID) ?: 0
        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            vRotateAngle = videoFormat?.getFloat(MediaFormat.KEY_ROTATION) ?: 0F
//        }
        videoExtractor.stop()

        val audioExtractor = AudioExtractor(path)
        val audioFormat = audioExtractor.getFormat()
        aDurationUs = audioFormat?.getLong(MediaFormat.KEY_DURATION) ?: 0
        aType = audioFormat?.getString(MediaFormat.KEY_MIME) ?: ""
        aChannels = audioFormat?.getInteger(MediaFormat.KEY_CHANNEL_COUNT) ?: 0
        aBitRate = audioFormat?.getInteger(MediaFormat.KEY_BIT_RATE) ?: 0
        aSampleRate = audioFormat?.getInteger(MediaFormat.KEY_SAMPLE_RATE) ?: 0
        aMaxInputSize = videoFormat?.getInteger(MediaFormat.KEY_MAX_INPUT_SIZE) ?: 0
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            aTrackId = audioFormat?.getInteger(MediaFormat.KEY_TRACK_ID) ?: 0
        }
        audioExtractor.stop()
    }

    private fun clearInfo() {
        mediaPath = ""
        vDurationUs = 0
        vType = ""
        vFrameRate = 0
        vWidth = 0
        vHeight = 0
        vRotateAngle = 0.toFloat()
        vTrackId = 0
        vMaxInputSize = 0

        aDurationUs = 0
        aType = ""
        aSampleRate = 0
        aChannels = 0
        aBitRate = 0
        aTrackId = 0
        aMaxInputSize = 0
    }
}