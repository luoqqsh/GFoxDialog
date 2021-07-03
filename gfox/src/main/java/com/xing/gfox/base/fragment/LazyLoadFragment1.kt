package com.xing.gfox.base.fragment

import androidx.fragment.app.Fragment

/**
 * add+show+hide 模式下的老方案
 */
internal abstract class LazyLoadFragment1 : Fragment(){
    private var isLoaded = false //控制是否执行懒加载

    override fun onResume() {
        super.onResume()
        judgeLazyInit()

    }
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
//        isVisibleToUser = !hidden
        judgeLazyInit()
    }

    private fun judgeLazyInit() {
        if (!isLoaded && !isHidden) {
            lazyInit()
            isLoaded = true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        isLoaded = false
    }

    //懒加载方法
    abstract fun lazyInit()

}