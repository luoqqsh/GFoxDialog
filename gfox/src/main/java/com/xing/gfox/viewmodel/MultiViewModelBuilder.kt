package com.xing.gfox.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

typealias FactoryFun<T> = () -> T

class MultiViewModelBuilder internal constructor(@PublishedApi internal val owner: MultiViewModelOwner) {
    /**
     * Use this method to get ViewModel
     * @param factoryFun factory function to pass data to view model by its constructor
     * @return ViewModel object
     */
    @Suppress("UNCHECKED_CAST")
    inline fun <reified T : ViewModel> getViewModel(
        noinline factoryFun: FactoryFun<T>? = null
    ): T {
        return when (owner) {
            is MultiViewModelOwner.Single -> owner.getViewModel(factoryFun!!)
            is MultiViewModelOwner.Multiple -> owner.getViewModel(factoryFun)
            is MultiViewModelOwner.None -> owner.getViewModel(factoryFun)
        }
    }

    @Suppress("UNCHECKED_CAST")
    @PublishedApi
    internal inline fun <reified T : ViewModel> MultiViewModelOwner.Single.getViewModel(noinline factoryFun: FactoryFun<T>): T {
        val factory = factoryFun.let {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>) = it() as T
            }
        }
        return MultiViewModel.getInstance().createSingleProvider(lifecycleOwner, factory)[T::class.java]
    }

    @Suppress("UNCHECKED_CAST")
    @PublishedApi
    internal inline fun <reified T : ViewModel> MultiViewModelOwner.Multiple.getViewModel(noinline factoryFun: FactoryFun<T>? = null): T {
        val factory = factoryFun?.let {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>) = it() as T
            }
        }
        return MultiViewModel.getInstance().createMultipleProvider(
            T::class.java,
            lifecycleOwner,
            factory
        )[T::class.java]
    }

    @Suppress("UNCHECKED_CAST")
    @PublishedApi
    internal inline fun <reified T : ViewModel> MultiViewModelOwner.None.getViewModel(noinline factoryFun: FactoryFun<T>? = null): T {
        val factory = factoryFun?.let {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel?> create(modelClass: Class<T>) = it() as T
            }
        }
        return MultiViewModel.getInstance().createGlobalProvider(factory)[T::class.java]
    }
}