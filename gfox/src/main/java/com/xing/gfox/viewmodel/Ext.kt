package com.xing.gfox.viewmodel

import android.app.Application
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import com.xing.gfox.BuildConfig

internal fun Application.registerAppExitListener(listener: AppExitListener) {
    registerComponentCallbacks(listener)
    registerActivityLifecycleCallbacks(listener)
}

internal fun Application.unregisterAppExitListener(listener: AppExitListener) {
    unregisterComponentCallbacks(listener)
    unregisterActivityLifecycleCallbacks(listener)
}

/**
 * Starts MultiViewModel and make it ready to use
 */
fun Application.startMultiVM() {
    MultiViewModel.createInstance(this)
}

/**
 * 确定生命周期所有者是否要更改配置
 */
internal fun LifecycleOwner.isChangingConfigurations(): Boolean {
    return when (this) {
        is Fragment -> activity != null && activity?.isChangingConfigurations ?: false
        is FragmentActivity -> isChangingConfigurations
        else -> false
    }
}

/**
 * 获取要用作键的类名
 */
inline fun <reified T : Any> Any.className(): String {
    return T::class.java.name
}

/**
 * Log for debug purpose
 */
internal fun Any.logD(msg: String) {
    if (BuildConfig.DEBUG)
        Log.d("mViewModel", msg)
}

