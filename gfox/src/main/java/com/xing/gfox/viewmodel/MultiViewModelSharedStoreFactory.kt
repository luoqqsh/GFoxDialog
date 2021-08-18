package com.xing.gfox.viewmodel

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import com.xing.gfox.viewmodel.MultiViewModelSharedStore

/**
 * 这个类只是一个包装器和一个映射，负责创建MultiViewModelSharedStore和管理它们，以便我们可以在LifeCycleOwners之间共享视图模型
 *
 */
internal class MultiViewModelSharedStoreFactory : MultiViewModelSharedStore.Callback {

    //Key -> ViewModel class name
    //Value -> ViewModelStore
    private val storeMap = HashMap<String, MultiViewModelSharedStore>()

    private fun <T : ViewModel> get(
        clazz: Class<T>,
        owner: LifecycleOwner
    ): MultiViewModelSharedStore? = storeMap[clazz.name]?.apply {
        addOwner(owner)
    }

    private fun <T : ViewModel> create(
        clazz: Class<T>,
        owner: LifecycleOwner
    ): MultiViewModelSharedStore {
        return MultiViewModelSharedStore.create(clazz, this).apply {
            addOwner(owner)
            storeMap[clazz.name] = this
        }
    }

    fun <T : ViewModel> getOrCreate(
        clazz: Class<T>,
        owner: LifecycleOwner
    ): MultiViewModelSharedStore = get(clazz, owner) ?: create(clazz, owner)


    override fun onStoreClear(clazz: Class<*>) {
        storeMap.remove(clazz.name)
    }
}