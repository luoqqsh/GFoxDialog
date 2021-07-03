package com.xing.gfox.rxHttp.kotlin

import retrofit2.Call
import retrofit2.Response
import kotlin.coroutines.*

suspend fun <T> Call<T>.await(
        netFail: Continuation<T>.(e: Throwable) -> Boolean = { true }, //网络异常
        responseFail: Continuation<T>.(response: Response<T>?) -> Boolean = { true }, //请求错误
        success: ((response: Response<T>) -> Unit)? = null //请求成功
): T = suspendCoroutine { continuation ->
    //开始请求
    enqueue(object : RequestCallBack<T>() {
        override fun onSuccess(call: Call<T>, response: Response<T>) {
            success?.invoke(response)
            continuation.resume(response.body()!!)
        }

        override fun onFailed(call: Call<T>?, response: Response<T>?) {
            super.onFailed(call, response)
            if (continuation.responseFail(response)) continuation.resumeWithException(RuntimeException("response body is null"))
        }

        override fun onFailure(call: Call<T>, t: Throwable) {
            super.onFailure(call, t)
            if (continuation.netFail(t)) continuation.resumeWithException(t)
        }
    })
}
