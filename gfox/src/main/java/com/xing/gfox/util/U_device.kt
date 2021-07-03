package com.xing.gfox.util

import android.app.ActivityManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Environment
import android.os.StatFs
import android.telephony.TelephonyManager
import android.text.TextUtils
import android.text.format.Formatter
import android.util.Log
import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.io.IOException
import java.util.*
import java.util.regex.Pattern

object U_device {
    //判断当前设备是否为手机
    fun isPhone(context: Context): Boolean {
        val telephony = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return telephony.phoneType != TelephonyManager.PHONE_TYPE_NONE
    }

    /**
     * 是否是三星手机
     */
    val isSamSungPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("samsung")
            } else false
        }

    /**
     * 是否是乐视手机
     */
    val isLePhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("leeco") || brand.toLowerCase().contains("letv")
            } else false
        }

    /**
     * 是否是360手机
     */
    val is360Phone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("360") || brand.toLowerCase().contains("qiku")
            } else false
        }

    /**
     * 是否是魅族手机
     */
    val isMeiZuPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("meizu")
            } else false
        }

    /**
     * 是否是VIVO手机
     */
    val isVivoPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("vivo")
            } else false
        }

    /**
     * 是否是小米手机
     */
    val isXiaoMiPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("xiaomi")
            } else false
        }

    val isRedMiPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("redmi")
            } else false
        }

    val isMiPhone: Boolean
        get() {
            return isXiaoMiPhone || isRedMiPhone
        }

    /**
     * 是否是中兴手机
     */
    val isZTEPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("zte")
            } else false
        }

    /**
     * 是否是一加手机
     */
    val isOnePlusPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("oneplus")
            } else false
        }

    /**
     * 是否是努比亚手机
     */
    val isNubiaPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("nubia")
            } else false
        }

    /**
     * 是否是索尼手机
     */
    val isSonyPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("sony")
            } else false
        }

    /**
     * 是否是锤子手机
     */
    val isSmartisanPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("smartisan")
            } else false
        }

    /**
     * 是否是谷歌手机
     */
    val isGooglePhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("google")
            } else false
        }

    val isOppoPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                brand.lowercase(Locale.getDefault()).contains("oppo") || isOppoR15Phone
            } else false
        }

    val isHuaWeiPhone: Boolean
        get() {
            val brand = Build.BRAND
            return if (!TextUtils.isEmpty(brand)) {
                if (brand.uppercase(Locale.getDefault()) == "HUAWEI") {
                    true
                } else
                    brand.uppercase(Locale.getDefault()) == "HONOR"
            } else false
        }

    val isOppoR15Phone: Boolean
        get() = Build.MODEL == "PACM00"


    //设备是否支持相机设备,需要系统大于4.2版本
    fun isSupportCamera(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY)
        } else {
            false
        }
    }

    fun getSystemLanguage(): String {
        return Locale.getDefault().language
    }


    fun getSystemVersion(): String {
        return Build.VERSION.RELEASE
    }


    fun getSupportedAbis(): String {
        var abis = ""
        for (i in if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Build.SUPPORTED_ABIS
        } else {
            return ""
        }) {
            abis += "$i "
        }
        return abis
    }

    /**
     * 获取Cpu内核数
     * @return
     */
    val numCores: Int
        get() = try {
            val dir = File("/sys/devices/system/cpu/")
            val files = dir.listFiles { pathname -> Pattern.matches("cpu[0-9]", pathname.name) }
            files.size
        } catch (e: Exception) {
            e.printStackTrace()
            1
        }

    /**
     * 获得SD卡总大小
     *
     * @return
     */
    fun getSDTotalSize(context: Context): String? {
        val path = Environment.getExternalStorageDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return Formatter.formatFileSize(context, blockSize * totalBlocks)
    }

    /**
     * 获得sd卡剩余容量，即可用大小
     *
     * @return
     */
    fun getSDAvailableSize(context: Context): String? {
        val path = Environment.getExternalStorageDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return Formatter.formatFileSize(context, blockSize * availableBlocks)
    }

    /**
     * 获得机身内存总大小
     *
     * @return
     */
    fun getRomTotalSize(context: Context): String? {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val totalBlocks = stat.blockCountLong
        return Formatter.formatFileSize(context, blockSize * totalBlocks)
    }

    /**
     * 获得机身可用内存
     *
     * @return
     */
    fun getRomAvailableSize(context: Context): String? {
        val path = Environment.getDataDirectory()
        val stat = StatFs(path.path)
        val blockSize = stat.blockSizeLong
        val availableBlocks = stat.availableBlocksLong
        return Formatter.formatFileSize(context, blockSize * availableBlocks)
    }

    /**
     * 获取应用运行的最大内存
     *
     * @return 最大内存
     */
    fun getAppRamSize(): Long {
        return Runtime.getRuntime().maxMemory() shr 10
    }

    fun getRamAvailMemory(context: Context): String? {
        val am = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val mi = ActivityManager.MemoryInfo()
        am.getMemoryInfo(mi)
        // mi.availMem; 当前系统的可用内存
        return Formatter.formatFileSize(context, mi.availMem) // 将获取的内存大小规格化
    }

    fun getRamTotalMemory(context: Context): String {
        val str1 = "/proc/meminfo" // 系统内存信息文件

        val str2: String
        val arrayOfString: Array<String>
        var initialMemory: Long = 0
        try {
            val localFileReader = FileReader(str1)
            val localBufferedReader = BufferedReader(localFileReader, 8192)
            str2 = localBufferedReader.readLine() // 读取meminfo第一行，系统总内存大小
            arrayOfString = str2.split("\\s+".toRegex()).toTypedArray()
            for (num in arrayOfString) {
                Log.i(str2, num + "\t")
            }
            // 获得系统总内存，单位是KB
            val i = Integer.valueOf(arrayOfString[1]).toInt()
            //int值乘以1024转换为long类型
            initialMemory = i.toLong() * 1024
            localBufferedReader.close()
        } catch (e: IOException) {
        }
        return Formatter.formatFileSize(context, initialMemory) // Byte转换为KB或者MB，内存大小规格化
    }

    fun getDeviceInfo(context: Context): String {
        return "BRAND-品牌:" + Build.BRAND + "\n" +
                "MANUFACTURER-制造商:" + Build.MANUFACTURER + "\n" +
                "MODEL-型号:" + Build.MODEL + "\n" +
                "设备版本号:" + Build.ID + "\n" +
                "当前语言:" + getSystemLanguage() + "\n" +
                "Android系统版本:" + getSystemVersion() + "\n" +
                "Android系统SDK:" + Build.VERSION.SDK_INT + "\n" +
                "系统ROM:" + U_rom.getName() + "\n" +
                "ROM版本:" + U_rom.getVersion() + "\n" +
                "支持的CPU架构:" + getSupportedAbis() + "\n" +
                "CPU核数:" + numCores + "\n" +
                "SSAID:" + U_id.getSSAID(context) + "\n" +
                "IMEI:" + U_id.getIMEI(context) + "\n" +
                "DeviceId:" + U_id.getDeviceId(context) + "\n" +
                "SD卡空间:" + getSDAvailableSize(context) + " / " + getSDTotalSize(context) + "\n" +
                "系统剩余空间:" + getRomAvailableSize(context) + " / " + getRomTotalSize(context) + "\n" +
                "RAM空间:" + getRamAvailMemory(context) + " / " + getRamTotalMemory(context) + "\n" +
                "adb启用:" + (if (U_root.isAdbEnabled()) "已启用adb" else "未启用adb") + "\n" +
                "Root权限:" + (if (U_root.isRoot()) "已获得Root权限" else "未获得Root权限")
    }

    fun getDeviceInfoDetail(context: Context): String {
        return "BRAND-厂商:" + Build.BRAND + "\n" +
                "BOOTLOADER:" + Build.BOOTLOADER + "\n" +
                "BOARD:" + Build.BOARD + "\n" +
                "DEVICE-设备参数:" + Build.DEVICE + "\n" +
                "DISPLAY-版本显示:" + Build.DISPLAY + "\n" +
                "FINGERPRINT:" + Build.FINGERPRINT + "\n" +
                "HARDWARE-硬件类型:" + Build.HARDWARE + "\n" +
                "HOST-主机:" + Build.HOST + "\n" +
                "ID-生产ID:" + Build.ID + "\n" +
                "MANUFACTURER-制造商:" + Build.MANUFACTURER + "\n" +
                "MODEL-型号:" + Build.MODEL + "\n" +
                "PRODUCT:" + Build.PRODUCT + "\n" +
                "TAGS:" + Build.TAGS + "\n" +
                "USER:" + Build.USER + "\n" +
                "TIME-手机时间:" + Build.TIME + "\n" +
                "UNKNOWN:" + Build.UNKNOWN + "\n" +
                "TYPE:" + Build.TYPE + "\n"
    }
}
