package com.xing.gfox.rxHttp.await

import kotlinx.coroutines.withTimeout

/**
 * 请求超时处理
 */
internal class AwaitTimeout<T>(
    private val iAwait: IAwait<T>,
    private var timeoutMillis: Long = 0L
) : IAwait<T> {

    override suspend fun await() = withTimeout(timeoutMillis) { iAwait.await() }
}