package com.xing.gfox.rxHttp.await


/**
 * 失败重试
 */
internal class AwaitRetry<T>(
    private val iAwait: IAwait<T>,
    private var times: Int = 0,
    private val period: Long = 0L,
    private val test: ((Throwable) -> Boolean)? = null
) : IAwait<T> {

    override suspend fun await(): T {
        return try {
            iAwait.await()
        } catch (e: Throwable) {
            val remaining = times  //剩余次数
            if (remaining != Int.MAX_VALUE) {
                times = remaining - 1
            }
            val pass = test?.invoke(e) ?: true
            if (remaining > 0 && pass) {
                kotlinx.coroutines.delay(period)
                await() //递归，直到剩余次数为0
            } else throw e
        }
    }
}

