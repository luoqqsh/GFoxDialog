package com.xing.gfox.rxHttp.bean

/**
 * Created by luyao
 * on 2018/3/13 14:38
 */
data class HttpResponse<out T>(val errorCode: Int, val errorMsg: String, val data: T)