package com.xing.gfox.rxHttp.await


/**
 * 数据转换 T->R
 * User: ljx
 * Date: 2020/4/15
 * Time: 17:19
 */
internal class AwaitMap<T, R>(
    private val iAwait: IAwait<T>,
    private val map: (T) -> R
) : IAwait<R> {

    override suspend fun await(): R {
        return map(iAwait.await())
    }
}