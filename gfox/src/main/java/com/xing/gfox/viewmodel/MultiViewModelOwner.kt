package com.xing.gfox.viewmodel

import androidx.lifecycle.LifecycleOwner
//sealed 密封类
sealed class MultiViewModelOwner {
    /**
     * Use this when you need a ViewModel with only one owner (default behavior),
     * provided ViewModels are alive while the owner is alive
     * 默认生命周期
     * @param lifecycleOwner owner of the ViewModel
     */
    class Single(@PublishedApi internal val lifecycleOwner: LifecycleOwner) : MultiViewModelOwner()

    /**
     * Use this when you want to share a ViewModel between multiple lifecycle owners,
     * it means you can share it between activities or fragments with different host activity and so on...
     * provided view models will be cleared when the last owner is destroyed
     * 多个activity共用生命周期
     * @param lifecycleOwner owner of the ViewModel
     */
    class Multiple(@PublishedApi internal val lifecycleOwner: LifecycleOwner) : MultiViewModelOwner()

    /**
     * In case you need a global ViewModel that is available in application scope,
     * provided ViewModel will be cleared when app is closed (last activity is finished)
     * 整个生命周期viewmodel
     */
    object None : MultiViewModelOwner()
}