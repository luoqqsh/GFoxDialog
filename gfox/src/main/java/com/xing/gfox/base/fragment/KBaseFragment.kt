package com.xing.gfox.base.fragment

import kotlinx.coroutines.*
import com.xing.gfox.base.interfaces.OnSimpleListener
import com.xing.gfox.base.interfaces.OnTSimpleListener

abstract class KBaseFragment : HLBaseFragment(), CoroutineScope by MainScope() {
    fun KTaskManager(hOnListener1: OnTSimpleListener<Any>) {
        KTaskManager(0, hOnListener1)
    }

    fun KTaskManager(delay: Long, hOnListener1: OnTSimpleListener<Any>) {
        launch {
            val deferred = async(Dispatchers.IO) {
                delay(delay)
                hOnListener1.onBackGround()
            }
            hOnListener1.onUI(deferred.await())
        }
    }

    fun KTaskManagerB(hOnListener1: OnSimpleListener) {
        KTaskManagerB(0, hOnListener1)
    }

    fun KTaskManagerB(delay: Long, hOnListener1: OnSimpleListener) {
        launch {
            async(Dispatchers.IO) {
                delay(delay)
                hOnListener1.onListen()
            }
        }
    }

    fun cancelTask(){
        cancel()
    }
    
    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }
} 