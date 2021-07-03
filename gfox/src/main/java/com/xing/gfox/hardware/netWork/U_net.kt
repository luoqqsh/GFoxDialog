package com.xing.gfox.hardware.netWork

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkInfo
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
import com.xing.gfox.base.activity.HLBaseActivity
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit

/**
 * 网络连接状态的监听器。通过注册broadcast实现的
 */
class U_net {
    companion object {
        private val TAG = U_net::class.java.simpleName// 从反馈的结果中提取出IP地址

        //获取外网ip地址，需要在子线程调用
        @JvmStatic
        fun getNetIp(): String {
            var infoUrl: URL? = null
            var inStream: InputStream? = null
            var line = ""
            try {
                infoUrl = URL("http://pv.sohu.com/cityjson?ie=utf-8")
                val connection = infoUrl.openConnection()
                val httpConnection = connection as HttpURLConnection
                val responseCode = httpConnection.responseCode
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    inStream = httpConnection.inputStream
                    val reader =
                        BufferedReader(InputStreamReader(inStream, StandardCharsets.UTF_8))
                    val strber = StringBuilder()
                    while (reader.readLine().also { line = it } != null) strber.append(line)
                        .append("\n")
                    inStream.close()
                    // 从反馈的结果中提取出IP地址
                    val start = strber.indexOf("{")
                    val end = strber.indexOf("}")
                    val json = strber.substring(start, end + 1)
                    try {
                        val jsonObject = JSONObject(json)
                        line = jsonObject.optString("cip")
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    return line
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return line
        }

        @JvmStatic
        fun canConnGoogle(): Boolean {
            val okHttpClient =
                OkHttpClient().newBuilder().connectTimeout(2, TimeUnit.SECONDS).build()
            val request: Request = Request.Builder().url("https://www.google.com").build()
            try {
                okHttpClient.newCall(request).execute()
                    .use { response -> return response.isSuccessful }
            } catch (e: Exception) {
                return false
            }
        }

        //获取网络类型
        @JvmStatic
        fun getNetworkType(mContext: Context): String {
            return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                getNetType(mContext)
            } else {
                getNetTypeOld(mContext)
            }
        }

        private fun getNetType(mContext: Context): String {
            val cm = mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val capabilities: NetworkCapabilities = cm.getNetworkCapabilities(cm.getActiveNetwork())
                ?: return NetWorkType.NO
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_VPN) -> {
                    return NetWorkType.VPN
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    return NetWorkType.WIFI
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI_AWARE) -> {
                    return NetWorkType.WIFI
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> {
                    return NetWorkType.BLUETOOTH
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_LOWPAN) -> {
                    return NetWorkType.LOWPAN
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    return NetWorkType.ETHERNET
                }
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    val tm = mContext
                        .getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                    return getMobileType(tm.dataNetworkType)
                }
            }
            return NetWorkType.UNKNOWN
        }

        private fun getNetTypeOld(mContext: Context): String {
            val cm = mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo: NetworkInfo = cm.activeNetworkInfo!!
            return if (networkInfo.isConnectedOrConnecting) {
                when (networkInfo.type) {
                    ConnectivityManager.TYPE_VPN -> NetWorkType.VPN
                    ConnectivityManager.TYPE_ETHERNET -> NetWorkType.ETHERNET
                    ConnectivityManager.TYPE_BLUETOOTH -> NetWorkType.BLUETOOTH
                    ConnectivityManager.TYPE_WIFI -> NetWorkType.WIFI
                    ConnectivityManager.TYPE_MOBILE -> getMobileType(networkInfo.subtype)
                    else -> NetWorkType.UNKNOWN
                }
            } else {
                NetWorkType.NO
            }
        }

        private fun getMobileType(type: Int): String {
            return when (type) {
                TelephonyManager.NETWORK_TYPE_GPRS,
                TelephonyManager.NETWORK_TYPE_CDMA,
                TelephonyManager.NETWORK_TYPE_GSM,
                TelephonyManager.NETWORK_TYPE_EDGE,
                TelephonyManager.NETWORK_TYPE_1xRTT,
                TelephonyManager.NETWORK_TYPE_IDEN -> NetWorkType.Mobile2G
                TelephonyManager.NETWORK_TYPE_EVDO_A,
                TelephonyManager.NETWORK_TYPE_UMTS,
                TelephonyManager.NETWORK_TYPE_EVDO_0,
                TelephonyManager.NETWORK_TYPE_HSDPA,
                TelephonyManager.NETWORK_TYPE_HSUPA,
                TelephonyManager.NETWORK_TYPE_TD_SCDMA,
                TelephonyManager.NETWORK_TYPE_HSPA,
                TelephonyManager.NETWORK_TYPE_EVDO_B,
                TelephonyManager.NETWORK_TYPE_EHRPD,
                TelephonyManager.NETWORK_TYPE_HSPAP -> NetWorkType.Mobile3G
                TelephonyManager.NETWORK_TYPE_LTE -> NetWorkType.Mobile4G
                TelephonyManager.NETWORK_TYPE_NR -> NetWorkType.Mobile5G
                else -> NetWorkType.UNKNOWN
            }
        }

        //是不是按流量计费
        @JvmStatic
        fun isNotFlowPay(mContext: Context) {
            val manager =
                mContext.getSystemService(HLBaseActivity.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                manager.registerDefaultNetworkCallback(object :
                    ConnectivityManager.NetworkCallback() {
                    override fun onCapabilitiesChanged(
                        network: Network,
                        networkCapabilities: NetworkCapabilities
                    ) {
                        super.onCapabilitiesChanged(network, networkCapabilities)
                        //true 代表连接不按流量计费
                        val isNotFlowPay =
                            networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_NOT_METERED) ||
                                    networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_TEMPORARILY_NOT_METERED)
                    }
                })
            }
        }

        @JvmStatic
        fun isNetConnectedByPingBaidu(): Boolean {
            return isNetConnectedByPing("www.baidu.com")
        }

        @JvmStatic
        fun isNetConnectedByPing(address: String): Boolean {
            return try {
                // 通过ping百度检测网络是否可用
                val p = Runtime.getRuntime().exec("/system/bin/ping -c 1 $address")
                val status = p.waitFor() // 只有0时表示正常返回
                status == 0
            } catch (e: IOException) {
                e.printStackTrace()
                false
            } catch (e: InterruptedException) {
                e.printStackTrace()
                false
            }
        }

        /**
         * 判断网络是否连接
         */
        @JvmStatic
        fun isNetworkConnected(context: Context): Boolean {
            return getNetworkType(context) != NetWorkType.NO
        }

        /**
         * 判断是否是WiFi连接
         */
        @JvmStatic
        fun isWifiConnected(context: Context): Boolean {
            return getNetworkType(context) == NetWorkType.WIFI
        }

        /**
         * 判断是否是数据网络连接
         */
        @JvmStatic
        fun isMobileConnected(context: Context): Boolean {
            val networkType = getNetworkType(context)
            return networkType == NetWorkType.Mobile2G ||
                    networkType == NetWorkType.Mobile3G ||
                    networkType == NetWorkType.Mobile4G ||
                    networkType == NetWorkType.Mobile5G
        }

        @JvmStatic
        fun gotoWIFISet(context: Context) {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            context.startActivity(intent)
        }

        @JvmStatic
        fun longToIp(ip: Long): String {
            return (ip and 0xFF).toString() + "." +
                    (ip shr 8 and 0xFF) + "." +
                    (ip shr 16 and 0xFF) + "." +
                    (ip shr 24 and 0xFF)
        }

        @JvmStatic
        fun ipToLong(ipAddress: String): Long {
            var result: Long = 0
            val ipAddressInArray = ipAddress.split("\\.".toRegex()).toTypedArray()
            for (i in 3 downTo 0) {
                val ip = ipAddressInArray[3 - i].toLong()
                //left shifting 24,16,8,0 and bitwise OR
                //1. 192 << 24
                //1. 168 << 16
                //1. 1   << 8
                //1. 2   << 0
                result = result or (ip shl i * 8)
            }
            return result
        }
    }
}