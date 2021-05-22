package com.xing.gfox.hardware.netWork

import android.Manifest
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.telephony.PhoneStateListener
import android.telephony.TelephonyDisplayInfo
import android.telephony.TelephonyManager
import com.xing.gfox.base.activity.HLBaseActivity
import com.xing.gfox.log.ViseLog
import com.xing.gfox.util.U_permissions
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONException
import org.json.JSONObject
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

        //判断是否是5G网络
        fun getNetworkType(mContext: Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                val tManager =
                    mContext.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
                tManager.listen(object : PhoneStateListener() {
                    override fun onDisplayInfoChanged(telephonyDisplayInfo: TelephonyDisplayInfo) {
                        if (U_permissions.checkPermission(
                                mContext,
                                Manifest.permission.READ_PHONE_STATE
                            )
                        ) {
                            super.onDisplayInfoChanged(telephonyDisplayInfo)
                            when (telephonyDisplayInfo.networkType) {
                                TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_LTE_ADVANCED_PRO -> ViseLog.d(
                                    "高级专业版 LTE (5Ge)"
                                )
                                TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA -> ViseLog.d("NR (5G) - 5G Sub-6 网络")
                                TelephonyDisplayInfo.OVERRIDE_NETWORK_TYPE_NR_NSA_MMWAVE -> ViseLog.d(
                                    "5G+/5G UW - 5G mmWave 网络"
                                )
                                else -> ViseLog.d("other")
                            }
                        }
                    }
                }, PhoneStateListener.LISTEN_DISPLAY_INFO_CHANGED)
            }
        }

        //是不是按流量计费
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

        /**
         * 判断网络是否连接
         */
        fun isNetworkConnected(context: Context?): Boolean {
            if (context != null) {
                val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    val mNetworkInfo = mConnectivityManager.activeNetworkInfo
                    if (mNetworkInfo != null) {
                        return mNetworkInfo.isAvailable
                    }
                } else {
                    val network = mConnectivityManager.activeNetwork ?: return false
                    val status = mConnectivityManager.getNetworkCapabilities(network)
                        ?: return false
                    if (status.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)) {
                        return true
                    }
                }
            }
            return false
        }

        /**
         * 判断是否是WiFi连接
         */
        fun isWifiConnected(context: Context?): Boolean {
            if (context != null) {
                val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    val mWiFiNetworkInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                    if (mWiFiNetworkInfo != null) {
                        return mWiFiNetworkInfo.isAvailable
                    }
                } else {
                    val network = mConnectivityManager.activeNetwork ?: return false
                    val status = mConnectivityManager.getNetworkCapabilities(network)
                        ?: return false
                    if (status.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true
                    }
                }
            }
            return false
        }

        /**
         * 判断是否是数据网络连接
         */
        fun isMobileConnected(context: Context?): Boolean {
            if (context != null) {
                val mConnectivityManager = context
                    .getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    val mMobileNetworkInfo = mConnectivityManager
                        .getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                    if (mMobileNetworkInfo != null) {
                        return mMobileNetworkInfo.isAvailable
                    }
                } else {
                    val network = mConnectivityManager.activeNetwork ?: return false
                    val status = mConnectivityManager.getNetworkCapabilities(network)
                        ?: return false
                    status.transportInfo
                    if (status.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    }
                }
            }
            return false
        }

        fun gotoWIFISet(context: Context) {
            val intent = Intent(Settings.ACTION_WIFI_SETTINGS)
            context.startActivity(intent)
        }

        fun downloadImg(downloadUrl: String): Bitmap {
            val url = URL(downloadUrl)
            val conn = url.openConnection() as HttpURLConnection
            val bitmap = BitmapFactory.decodeStream(conn.inputStream)
            conn.disconnect()
            return bitmap
        }
    }
}