package com.xing.gfox.base.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import com.xing.gfox.base.life.MyLifeObserver8
import com.xing.gfox.base.mvvm.BaseViewModel
import com.xing.gfox.base.mvvm.ViewModelFactory
import java.lang.reflect.ParameterizedType

abstract class HLBaseVMFragment<VM : BaseViewModel, DB : ViewDataBinding> : Fragment() {
    protected var mActivity: FragmentActivity? = null
    protected lateinit var dataBinding: DB
    protected lateinit var viewModel: VM

    //是否第一次加载
    private var isFirst: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = activity
        if (savedInstanceState != null) {
            val isSupportHidden = savedInstanceState.getBoolean(STATE_SAVE_IS_HIDDEN)
            val ft = childFragmentManager.beginTransaction()
            if (isSupportHidden) {
                ft.hide(this)
            } else {
                ft.show(this)
            }
            ft.commit()
        }
        lifecycle.addObserver(MyLifeObserver8(javaClass.simpleName))
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putBoolean(STATE_SAVE_IS_HIDDEN, isHidden)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val cls = (javaClass.genericSuperclass as ParameterizedType).actualTypeArguments[1] as Class<*>
        if (ViewDataBinding::class.java != cls && ViewDataBinding::class.java.isAssignableFrom(cls)) {
            dataBinding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
            return dataBinding.root
        }
        return inflater.inflate(getLayoutId(), container, false)
    }

    @Suppress("UNCHECKED_CAST")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onVisible()
        val type = javaClass.genericSuperclass
        if (type is ParameterizedType) {
            val tp = type.actualTypeArguments[0]
            val tClass = tp as? Class<VM> ?: BaseViewModel::class.java
            viewModel = ViewModelProvider(this, ViewModelFactory()).get(tClass) as VM
        }
        lifecycle.addObserver(viewModel)
        initUI(savedInstanceState)
    }


    companion object {

        private const val STATE_SAVE_IS_HIDDEN = "STATE_SAVE_IS_HIDDEN"
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun initUI(savedInstanceState: Bundle?)

    /**
     * 是否需要懒加载
     */
    private fun onVisible() {
        if (lifecycle.currentState == Lifecycle.State.STARTED && isFirst) {
            lazyLoadData()
            isFirst = false
        }
    }

    /**
     * 懒加载
     */
    open fun lazyLoadData() {}
    override fun onResume() {
        super.onResume()
        onVisible()
    }
}