package com.xing.gfox.util.model

import android.content.pm.ApplicationInfo
import android.graphics.drawable.Drawable
import java.io.File

/**
 * 获取手机已安装app列表信息
 */
class AppInfo {
    var versionCode = 0
    var versionName = "0"
    var minSdkVersion = 0
    var targetSdkVersion = 0
    var uid = 0
    var dataDir: String? = null
    var sourceDir: String? = null
    var flags = 0
    var name: String? = null
    var processName: String? = null
    var isEnabled = false
    var icon: Drawable? = null//图标
    var apkName: String? = null

    //应用名
    var firstInstallTime: Long = 0 //首次安装时间
    var lastUpdateTime: Long = 0 //最后更新时间

    /**
     * 表示到底是用户app还是系统app
     * 如果表示为true 就是用户app
     * 如果是false表示系统app
     */
    var apkPackageName: String? = null//包名

    val apkSize: Long
        get() {
            val file = File(sourceDir)
            return file.length()
        }
    val isSystemApp: Boolean
        get() = flags and ApplicationInfo.FLAG_SYSTEM != 0
    val isSetupToExternalStorage: Boolean
        get() = flags and ApplicationInfo.FLAG_EXTERNAL_STORAGE != 0
}