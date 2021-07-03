package com.xing.gfox.util.model

import java.io.Serializable

class SMSBean : Serializable {
    var phoneNo: String? = null//對方短信號碼

    var content: String? = null //短信內容

    var type = 0//類型 0.所有短信 1.接收 2.發送 3.草稿 4.發件箱 5.發送失敗 6.待發送

    var longDate: Long = 0
    val typeStr: String
        get() = when (type) {
            0 -> "所有短信"
            1 -> "接收"
            2 -> "发送"
            3 -> "草稿"
            4 -> "发件箱"
            5 -> "发送失败"
            6 -> "待发送列表"
            else -> ""
        }
}