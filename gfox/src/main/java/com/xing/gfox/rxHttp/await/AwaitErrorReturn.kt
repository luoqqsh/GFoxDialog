package com.xing.gfox.rxHttp.await

/**
 * 出现错误，返回默认值
 */
internal class AwaitErrorReturn<T>(
    private val iAwait: IAwait<T>,
    private val map: (Throwable) -> T
) : IAwait<T> {

    override suspend fun await(): T = try {
        iAwait.await()
    } catch (e: Throwable) {
        map(e)
    }
}

