package com.xing.gfoxdialog

import com.xing.gfox.base.activity.HKBaseActivity
import android.os.Bundle
import android.view.View
import com.xing.gfox.liveEventBus.LiveEventBus
import com.xing.gfox.log.ViseLog
import kotlinx.coroutines.*

class ThreadKActivity : HKBaseActivity() {
    override fun getBackgroundColorResource(): Int {
        return R.color.white
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_thread
    }

    override fun isShowTitle(): Boolean {
        return true
    }

    override fun initUI(savedInstanceState: Bundle) {
        mTitle.setLeftButtonImage(R.mipmap.hl_back_black) { v: View? -> finish() }
        mTitle.setTitleText("线程协程")
    }

    fun xiecheng(view: View?) {
    }

    suspend fun xiecheng() {
        //launch方法返回Job
        //job.cancel(),取消协程
        //job.join(),等待相应的协程完成
        //job.cancelAndJoin()，取消并确保父协程取消成功
        //一般情况下不适用
        val launch = GlobalScope.launch {
            // 在后台启动一个新的协程并继续
            delay(10000L)
            println("World!")
            ViseLog.d("good")
            withContext(NonCancellable) {
                //不可取消的代码块，一般用于切换线程
            }
        }

        //在所有子协程完成其任务并不阻塞当前线程.launch无返回值
        val launch1 = CoroutineScope(Dispatchers.IO).launch {
            delay(3000L)
            ViseLog.d("aaa")
            withContext(Dispatchers.Main) {
                ViseLog.d("goodhappy")
            }
        }
        //async带返回值的函数
        val async = CoroutineScope(Dispatchers.IO).async {
            "njn"//返回结果。。。
        }
        val result = async.await()//取得返回结果，需要在协程环境或挂起函数调用
//        if (async.isCompleted) {
//            val completed = async.getCompleted()
//        }

        //会阻塞主线程，相当于同步做法，执行完成才会继续后面的代码
        val runBlocking = runBlocking(Dispatchers.IO) {
            launch {
                delay(5000L)//延迟
                yield()//挂起当前协程，让别人先行
                ViseLog.d("adsf")
            }
        }
    }

    fun taskManager(view: View?) {

    }

    fun liveEventBus(view: View?) {
        //接收
        LiveEventBus.get("KEY_TEST_OBSERVE", Int::class.java).observe(mActivity, { })

        //发送
        LiveEventBus.get("KEY_TEST_OBSERVE").post("fff")
    }
}