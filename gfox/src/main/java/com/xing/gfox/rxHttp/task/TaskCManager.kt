package com.xing.gfox.rxHttp.task

import kotlinx.coroutines.*
import com.xing.gfox.base.interfaces.HOnListener
import com.xing.gfox.log.ViseLog

class TaskCManager {
    constructor()

    fun getaa(hOnListener: HOnListener<Any>, onListener: HOnListener<Any>) {
        val launch = GlobalScope.launch {
            //异步线程
            val async = async {
                delay(0)
                hOnListener.onListen("Object")
            }
            //切换主线程
            withContext(Dispatchers.Main) {
                onListener.onListen("a")
            }
        }
        ViseLog.d(launch.isCancelled)
        launch.cancel("error",null)
        ViseLog.d(launch.isCancelled)
    }
    
    fun get3aa(hOnListener: HOnListener<Any>) {
        GlobalScope.launch (Dispatchers.IO){
            hOnListener.onListen("Object" )
        }
    }
}