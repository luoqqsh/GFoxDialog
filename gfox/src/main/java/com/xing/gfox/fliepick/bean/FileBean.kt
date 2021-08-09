package com.xing.gfox.fliepick.bean

import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import com.xing.gfox.util.U_uri
import java.io.File
import java.io.Serializable
import java.util.*

class FileBean : Serializable, Comparable<FileBean?> {
    constructor()
    constructor(filePath: String) {
        this.filePath = filePath
    }

    constructor(filePathUri: Uri) {
        this.filePathUri = filePathUri
    }

    constructor(file: File) {
        file.absolutePath.also { this.filePath = it }
        this.fileName = file.name
    }

    //文件
    var filePath: String? = ""//路径
    var filePathUri: Uri? = U_uri.str2Uri(filePath) //路径uri
    var mediaId: String? = ""//文件资源id
    var fileSize = 0L//文件大小
    var fileModifiedDate = 0L //文件修改日期
    var fileCreateDate = 0L //文件创建日期
    var fileName: String? = ""//文件名
    var fileMimeType: String? = ""//文件类型
    var title: String? = ""//标题

    var imgFileBean: ImgFileBean? = null
        get() {
            if (field == null) {
                return ImgFileBean()
            }
            return field
        }
    var musicFileBean: MusicFileBean? = null
        get() {
            if (field == null) {
                return MusicFileBean()
            }
            return field
        }
    var videoFileBean: VideoFileBean? = null
        get() {
            if (field == null) {
                return VideoFileBean()
            }
            return field
        }

    fun setImgInfo(width: Int, height: Int) {
        imgFileBean = ImgFileBean()
        imgFileBean?.imgWidth = width
        imgFileBean?.imgHeight = height
    }

    fun setMusicInfo(
        artist: String? = "",
        mediaDuration: Long,
        musicAlbum: String? = "",
        musicAlbumId: Long,
        year: Int,
        musicName: String? = "",
    ) {
        musicFileBean = MusicFileBean()
        musicFileBean?.artist = artist
        musicFileBean?.mediaDuration = mediaDuration
        musicFileBean?.musicAlbum = musicAlbum
        musicFileBean?.musicAlbumId = musicAlbumId
        musicFileBean?.year = year
        musicFileBean?.musicName = musicName
    }

    fun setVideoInfo(width: Int, height: Int, mediaDuration: Long) {
        videoFileBean = VideoFileBean()
        videoFileBean?.vWidth = width
        videoFileBean?.vHeight = height
        videoFileBean?.mediaDuration = mediaDuration
    }

    override fun compareTo(o: FileBean?): Int {
        return 0
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o == null || javaClass != o.javaClass) return false
        val that = o as FileBean
        return filePath == that.filePath &&
                filePathUri == that.filePathUri &&
                fileName == that.fileName
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun hashCode(): Int {
        return Objects.hash(filePath, filePathUri, fileName)
    }

    class ImgFileBean {
        var imgWidth = 0
        var imgHeight = 0
    }

    class VideoFileBean {
        var mediaDuration = 0L//視頻長度
        var vWidth = 0
        var vHeight = 0
        var thumbPath: String? = ""//封面
    }

    class MusicFileBean {
        var mediaDuration = 0L
        var musicName: String? = "" //歌曲名
        var musicAlbum: String? = ""//所属专辑
        var musicAlbumId = 0L//所属专辑id
        var artist: String? = ""//艺术家(作者)
        var year = 0//发行日期
    }

    //adapter使用
    var tag: Any? = null//tag
    var select = false//是否选择
}