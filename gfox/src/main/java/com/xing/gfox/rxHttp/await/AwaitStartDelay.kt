package com.xing.gfox.rxHttp.await

/**
 * 发请求前延迟
 */
internal class AwaitStartDelay<T>(
    private val iAwait: IAwait<T>,
    private val delay: Long
) : IAwait<T> {

    override suspend fun await(): T {
        kotlinx.coroutines.delay(delay)
        return iAwait.await()
    }
}