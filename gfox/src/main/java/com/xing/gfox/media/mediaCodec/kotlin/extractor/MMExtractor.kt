package com.xing.gfox.media.mediaCodec.kotlin.extractor

import android.media.MediaExtractor
import android.media.MediaFormat
import java.nio.ByteBuffer

/**
 * 音视频分离器
 */

class MMExtractor(path: String) {

    /**音视频分离器*/
    private var mExtractor: MediaExtractor? = null

    /**音频通道索引*/
    private var mAudioTrack = -1

    /**视频通道索引*/
    private var mVideoTrack = -1

    /**当前帧时间戳*/
    private var mCurSampleTime: Long = 0

    /**当前帧标志*/
    private var mCurSampleFlag: Int = 0

    /**开始解码时间点*/
    private var mStartPos: Long = 0

    /**
     * 初始化
     */
    init {
        //新建，然后设置音视频文件路径
        mExtractor = MediaExtractor()
        mExtractor?.setDataSource(path)
    }

    /**
     * 获取音视频多媒体格式
     * 1）遍历视频文件中所有的通道，一般是音频和视频两个通道；
     * 2）然后获取对应通道的编码格式，判断是否包含"video/"或者"audio/"开头的编码格式；
     * 3）最后通过获取的索引，返回对应的音视频多媒体格式信息。
     */
    fun getVideoFormat(): MediaFormat? {
        for (i in 0 until mExtractor!!.trackCount) {
            val mediaFormat = mExtractor!!.getTrackFormat(i)
            val mime = mediaFormat.getString(MediaFormat.KEY_MIME)
            if (mime!!.startsWith("video/")) {
                mVideoTrack = i
                break
            }
        }
        return if (mVideoTrack >= 0)
            mExtractor!!.getTrackFormat(mVideoTrack)
        else null
    }

    /**
     * 获取音频格式参数
     */
    fun getAudioFormat(): MediaFormat? {
        for (i in 0 until mExtractor!!.trackCount) {
            val mediaFormat = mExtractor!!.getTrackFormat(i)
            val mime = mediaFormat.getString(MediaFormat.KEY_MIME)
            if (mime!!.startsWith("audio/")) {
                mAudioTrack = i
                break
            }
        }
        return if (mAudioTrack >= 0) {
            mExtractor!!.getTrackFormat(mAudioTrack)
        } else null
    }

    /**
     * 读取视频数据
     * 1）readBuffer(byteBuffer: ByteBuffer)中的参数就是解码器传进来的，用于存放待解码数据的缓冲区。
     * 2）selectSourceTrack()方法中，根据当前选择的通道（同时只选择一个音/视频通道），调用mExtractor!!.selectTrack(mAudioTrack)将通道切换正确。
     * 3）然后读取数据
     */
    fun readBuffer(byteBuffer: ByteBuffer): Int {
        byteBuffer.clear()
        selectSourceTrack()
        val readSampleCount = mExtractor!!.readSampleData(byteBuffer, 0)
        if (readSampleCount < 0) {//小于0表示数据已经读完
            return -1
        }
        //记录当前帧的时间戳
        mCurSampleTime = mExtractor!!.sampleTime
        mCurSampleFlag = mExtractor!!.sampleFlags
        //进入下一帧
        mExtractor!!.advance()
        return readSampleCount
    }

    /**
     * 选择通道
     */
    private fun selectSourceTrack() {
        if (mVideoTrack >= 0) {
            mExtractor!!.selectTrack(mVideoTrack)
        } else if (mAudioTrack >= 0) {
            mExtractor!!.selectTrack(mAudioTrack)
        }
    }

    /**
     * Seek到指定位置，并返回实际帧的时间戳
     * SEEK_TO_PREVIOUS_SYNC：跳播位置的上一个关键帧
     * SEEK_TO_NEXT_SYNC：跳播位置的下一个关键帧
     * SEEK_TO_CLOSEST_SYNC：距离跳播位置的最近的关键帧
     */
    fun seek(pos: Long): Long {
        mExtractor!!.seekTo(pos, MediaExtractor.SEEK_TO_PREVIOUS_SYNC)
        return mExtractor!!.sampleTime
    }

    /**
     * 停止读取数据
     * 释放提取器
     */
    fun stop() {
        mExtractor?.release()
        mExtractor = null
    }

    fun getVideoTrack(): Int {
        return mVideoTrack
    }

    fun getAudioTrack(): Int {
        return mAudioTrack
    }

    fun setStartPos(pos: Long) {
        mStartPos = pos
    }

    /**
     * 获取当前帧时间
     */
    fun getCurrentTimestamp(): Long {
        return mCurSampleTime
    }

    fun getSampleFlag(): Int {
        return mCurSampleFlag
    }
}