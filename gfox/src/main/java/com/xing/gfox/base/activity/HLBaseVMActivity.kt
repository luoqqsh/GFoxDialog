package com.xing.gfox.base.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import com.xing.gfox.base.mvvm.BaseViewModel
import com.xing.gfox.base.mvvm.ViewModelFactory
import java.lang.reflect.ParameterizedType

abstract class HLBaseVMActivity<VM : BaseViewModel, DB : ViewDataBinding> : AppCompatActivity() {
    //不用viewModel，传NoViewModel
    protected lateinit var viewModel: VM

    //不用DataBinding，传ViewDataBinding
    protected lateinit var dataBinding: DB
    protected lateinit var mActivity: FragmentActivity

    @Suppress("UNCHECKED_CAST")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        val cls =
            (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            dataBinding = DataBindingUtil.setContentView(this, getLayoutId())
            dataBinding.lifecycleOwner = this
        } else {
            setContentView(getLayoutId())
        }
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(this, ViewModelFactory()).get(tClass) as VM
        }
        //////////////////////////////////////////////////////////
        lifecycle.addObserver(viewModel)
        //注册 UI事件
//        registorDefUIChange()
        initView(savedInstanceState)
        initData()
    }

    abstract fun getLayoutId(): Int
    abstract fun initView(savedInstanceState: Bundle?)

    open fun initData() {
    }
}