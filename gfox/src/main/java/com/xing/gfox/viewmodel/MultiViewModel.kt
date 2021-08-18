package com.xing.gfox.viewmodel

import android.app.Application
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStore

class MultiViewModel internal constructor(@PublishedApi internal val app: Application) {

    private val globalOwner = ViewModelStore()
    private val viewModelStoreFactory = MultiViewModelSharedStoreFactory()

    companion object {
        @Volatile
        private var INSTANCE: MultiViewModel? = null

        fun getInstance(): MultiViewModel {
            return INSTANCE ?: throw IllegalStateException("请先初始化！！！")
        }

        internal fun createInstance(app: Application) {
            synchronized(this) {
                INSTANCE = MultiViewModel(app)
            }
        }
    }

    init {
        app.registerAppExitListener(object : AppExitListener() {
            override fun onAppExit() {
                this@MultiViewModel.onAppExit()
            }
        })
    }

    private fun onAppExit() {
        globalOwner.clear()
    }

    /**
     * 创建MultiViewModelBuilder以获取所需的视图模型
     * @param owner MultiViewModelOwner that is responsible for ViewModel life
     */
    fun with(owner: MultiViewModelOwner) = MultiViewModelBuilder(owner)


    /**
     * Responsible for creating provider for global view models in application scope
     * 全局viewmodel创建
     */
    @PublishedApi
    internal fun createGlobalProvider(factory: ViewModelProvider.Factory? = null): ViewModelProvider {
        return ViewModelProvider(
            globalOwner,
            factory ?: ViewModelProvider.AndroidViewModelFactory.getInstance(app)
        )
    }

    /**
     * Responsible for creating provider for view models with single owner (android default)
     * 安卓默认的创建viewModel
     */
    @PublishedApi
    internal fun createSingleProvider(
        lifecycleOwner: LifecycleOwner,
        factory: ViewModelProvider.Factory
    ): ViewModelProvider {
        return when (lifecycleOwner) {
            is Fragment -> ViewModelProvider(lifecycleOwner, factory)
            is FragmentActivity -> ViewModelProvider(lifecycleOwner, factory)
            else -> throw IllegalArgumentException("Unsupported owner passed")
        }
    }

    /**
     * Responsible for creating provider for view models with multiple owners
     */
    @PublishedApi
    internal fun <T : ViewModel> createMultipleProvider(
        clazz: Class<T>,
        lifecycleOwner: LifecycleOwner,
        factory: ViewModelProvider.Factory? = null
    ): ViewModelProvider {
        val store = viewModelStoreFactory.getOrCreate<T>(clazz, lifecycleOwner)
        return ViewModelProvider(
            store,
            factory ?: ViewModelProvider.AndroidViewModelFactory.getInstance(app)
        )
    }
}