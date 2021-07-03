package com.xing.gfox.fliepick.media

import android.content.Context
import android.net.Uri
import android.view.View
import com.xing.gfox.R
import com.xing.gfox.base.toast.U_Toast
import com.xing.gfox.fliepick.bean.FileBean
import com.xing.gfox.fliepick.bean.MediaSelectListener
import com.xing.gfox.util.U_screen
import java.io.Serializable

class MediaConfig : Serializable {
    /**
     * 自定义多选页面
     */
    var minSelectCount = 1 //限制最小数量
    var maxSelectCount = 1 //限制最大数量
    var adTipView: View? = null //提示view
    var isShowCamera = true //是否顯示拍照
    var isNeedPreview = true //是否顯示預覽

    //每行显示个数,为0根据横竖屏默认显示
    var spanCount = 0
        get() {
            if (field == 0) {
                field = if (U_screen.isPortrait()) {
                    3
                } else {
                    5
                }
            }
            return field
        }
    var itemDecoration = 3 //item间距
    var mediaType: Array<String>? = null //指定文件类型
    private var mSelectList: ArrayList<FileBean>? = null//已选择的媒体或者需要传入的媒体
    private var onSingleListener: MediaSelectListener<FileBean>? = null//单选回调
    private var onMoreSelectListener: MediaSelectListener<ArrayList<FileBean>>? = null //多选回调
    private var onSelectChangeListener: MediaSelectListener<ArrayList<FileBean>>? = null //选择变化回调

    /**
     * 系统-获取所有本地图片
     */
    var contentMediaType: String? = null//媒体类型

    /**
     * 系统录像参数
     */
    var recordSizeLimit: Long = 0//录制大小限制，Long类型，最小5MB
    var recordDuration = 0//录制时长限制，单位秒
    var mediaOutputUri: Uri? = null//拍照、录制视频指定输出路径，默认保存在沙盒中

    /**
     * 系统裁剪参数
     */
    var isNeedSystemCrop = false
    var cropAspectX = 0
        private set
    var cropAspectY = 0
        private set
    var cropOutputX = 0
        private set
    var cropOutputY = 0
        private set
    val selectList: ArrayList<FileBean>
        get() {
            if (mSelectList == null) {
                mSelectList = ArrayList()
            }
            return mSelectList as ArrayList<FileBean>
        }

    //获取选中的媒体，这里只取第一条，仅适用于传入单个媒体文件的情况
    val selectMedia: FileBean?
        get() = if (mSelectList != null && mSelectList!!.isNotEmpty()) {
            mSelectList!![0]
        } else null

    fun setSelectList(vararg mSelectList: FileBean) {
        this.mSelectList = java.util.ArrayList(listOf(*mSelectList))
    }

    fun setSelectList(mSelectList: ArrayList<FileBean>?) {
        this.mSelectList = mSelectList
    }

    fun setOnSingleListener(onSingleListener: MediaSelectListener<FileBean>?) {
        this.onSingleListener = onSingleListener
    }

    fun setOnMoreSelectListener(onMoreSelectListener: MediaSelectListener<ArrayList<FileBean>>?) {
        this.onMoreSelectListener = onMoreSelectListener
    }

    fun setOnSelectChangeListener(onSelectChangeListener: MediaSelectListener<ArrayList<FileBean>>?) {
        this.onSelectChangeListener = onSelectChangeListener
    }

    fun listChange() {
        onSelectChangeListener?.onListen(mSelectList)
    }


    val isSingleSelect: Boolean
        get() = minSelectCount == 1 && maxSelectCount == 1

    fun callback(context: Context): Boolean {
        if (selectList.size == 0) {
            U_Toast.show(context.getString(R.string.pick_no_select))
            return false
        }
        if (selectList.size < minSelectCount) {
            U_Toast.show(
                String.format(context.getString(R.string.pick_least_select), minSelectCount)
            )
            return false
        }
        if (selectList.size > maxSelectCount) {
            U_Toast.show(
                String.format(context.getString(R.string.pick_most_select), maxSelectCount)
            )
            return false
        }
        if (isSingleSelect) {
            if (onSingleListener != null) {
                onSingleListener!!.onListen(mSelectList!![0])
            }
        }
        if (onMoreSelectListener != null) {
            onMoreSelectListener!!.onListen(mSelectList)
        }
        return true
    }

    fun setCropParams(cropAspectX: Int, cropAspectY: Int, cropOutputX: Int, cropOutputY: Int) {
        this.cropAspectX = cropAspectX
        this.cropAspectY = cropAspectY
        this.cropOutputX = cropOutputX
        this.cropOutputY = cropOutputY
    }

    companion object {
        private const val serialVersionUID = 6357206378404061331L

        @JvmField
        var imgType = arrayOf("image/jpeg", "image/png", "image/gif", "image/heic")

        @JvmField
        var videoType = arrayOf("video/mp4")
    }
}