package com.xing.gfox.immbar

import android.app.Activity
import android.app.Dialog
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment

/**
 * @author geyifeng
 * @date 2019/3/27 5:45 PM
 */

// 初始化ImmBar
inline fun Activity.ImmBar(block: ImmBar.() -> Unit) =
    ImmBar.with(this).apply { block(this) }.init()

inline fun Fragment.ImmBar(block: ImmBar.() -> Unit) =
    ImmBar.with(this).apply { block(this) }.init()

inline fun android.app.Fragment.ImmBar(block: ImmBar.() -> Unit) =
    ImmBar.with(this).apply { block(this) }.init()

inline fun DialogFragment.ImmBar(block: ImmBar.() -> Unit) =
    ImmBar.with(this).apply { block(this) }.init()

inline fun android.app.DialogFragment.ImmBar(block: ImmBar.() -> Unit) =
    ImmBar.with(this).apply { block(this) }.init()

inline fun Dialog.ImmBar(activity: Activity, block: ImmBar.() -> Unit) =
    ImmBar.with(activity, this).apply { block(this) }.init()

inline fun Activity.ImmBar(dialog: Dialog, block: ImmBar.() -> Unit) =
    ImmBar.with(this, dialog).apply { block(this) }.init()

inline fun Fragment.ImmBar(dialog: Dialog, block: ImmBar.() -> Unit) =
    activity?.run { ImmBar.with(this, dialog).apply { block(this) }.init() }
        ?: Unit

inline fun android.app.Fragment.ImmBar(dialog: Dialog, block: ImmBar.() -> Unit) =
    activity?.run { ImmBar.with(this, dialog).apply { block(this) }.init() }
        ?: Unit

fun Activity.ImmBar() = ImmBar { }

fun Fragment.ImmBar() = ImmBar { }

fun android.app.Fragment.ImmBar() = ImmBar { }

fun DialogFragment.ImmBar() = ImmBar { }

fun android.app.DialogFragment.ImmBar() = ImmBar { }

fun Dialog.ImmBar(activity: Activity) = ImmBar(activity) {}

fun Activity.ImmBar(dialog: Dialog) = ImmBar(dialog) {}

fun Fragment.ImmBar(dialog: Dialog) = ImmBar(dialog) {}

fun android.app.Fragment.ImmBar(dialog: Dialog) = ImmBar(dialog) {}

// dialog销毁
fun Activity.destroyImmBar(dialog: Dialog) = ImmBar.destroy(this, dialog)

fun Fragment.destroyImmBar(dialog: Dialog) = activity?.run { ImmBar.destroy(this, dialog) }
    ?: Unit

fun android.app.Fragment.destroyImmBar(dialog: Dialog) =
    activity?.run { ImmBar.destroy(this, dialog) }
        ?: Unit

// 状态栏扩展
val Activity.statusBarHeight get() = ImmBar.getStatusBarHeight(this)

val Fragment.statusBarHeight get() = ImmBar.getStatusBarHeight(this)

val android.app.Fragment.statusBarHeight get() = ImmBar.getStatusBarHeight(this)

// 导航栏扩展
val Activity.navigationBarHeight get() = ImmBar.getNavigationBarHeight(this)

val Fragment.navigationBarHeight get() = ImmBar.getNavigationBarHeight(this)

val android.app.Fragment.navigationBarHeight get() = ImmBar.getNavigationBarHeight(this)

val Activity.navigationBarWidth get() = ImmBar.getNavigationBarWidth(this)

val Fragment.navigationBarWidth get() = ImmBar.getNavigationBarWidth(this)

val android.app.Fragment.navigationBarWidth get() = ImmBar.getNavigationBarWidth(this)

// ActionBar扩展
val Activity.actionBarHeight get() = ImmBar.getActionBarHeight(this)

val Fragment.actionBarHeight get() = ImmBar.getActionBarHeight(this)

val android.app.Fragment.actionBarHeight get() = ImmBar.getActionBarHeight(this)

// 是否有导航栏
val Activity.hasNavigationBar get() = ImmBar.hasNavigationBar(this)

val Fragment.hasNavigationBar get() = ImmBar.hasNavigationBar(this)

val android.app.Fragment.hasNavigationBar get() = ImmBar.hasNavigationBar(this)

// 是否有刘海屏
val Activity.hasNotchScreen get() = ImmBar.hasNotchScreen(this)

val Fragment.hasNotchScreen get() = ImmBar.hasNotchScreen(this)

val android.app.Fragment.hasNotchScreen get() = ImmBar.hasNotchScreen(this)

val View.hasNotchScreen get() = ImmBar.hasNotchScreen(this)

// 获得刘海屏高度
val Activity.notchHeight get() = ImmBar.getNotchHeight(this)

val Fragment.notchHeight get() = ImmBar.getNotchHeight(this)

val android.app.Fragment.notchHeight get() = ImmBar.getNotchHeight(this)

// 是否支持状态栏字体变色
val isSupportStatusBarDarkFont get() = ImmBar.isSupportStatusBarDarkFont()

// 师傅支持导航栏图标
val isSupportNavigationIconDark get() = ImmBar.isSupportNavigationIconDark()

// 检查view是否使用了fitsSystemWindows
val View.checkFitsSystemWindows get() = ImmBar.checkFitsSystemWindows(this)

// 导航栏是否在底部
val Activity.isNavigationAtBottom get() = ImmBar.isNavigationAtBottom(this)
val Fragment.isNavigationAtBottom get() = ImmBar.isNavigationAtBottom(this)

val android.app.Fragment.isNavigationAtBottom get() = ImmBar.isNavigationAtBottom(this)

// statusBarView扩展
fun Activity.fitsStatusBarView(view: View) = ImmBar.setStatusBarView(this, view)

fun Fragment.fitsStatusBarView(view: View) = ImmBar.setStatusBarView(this, view)

fun android.app.Fragment.fitsStatusBarView(view: View) = ImmBar.setStatusBarView(this, view)

// titleBar扩展
fun Activity.fitsTitleBar(vararg view: View) = ImmBar.setTitleBar(this, *view)

fun Fragment.fitsTitleBar(vararg view: View) = ImmBar.setTitleBar(this, *view)

fun android.app.Fragment.fitsTitleBar(vararg view: View) = ImmBar.setTitleBar(this, *view)

fun Activity.fitsTitleBarMarginTop(vararg view: View) = ImmBar.setTitleBarMarginTop(this, *view)

fun Fragment.fitsTitleBarMarginTop(vararg view: View) = ImmBar.setTitleBarMarginTop(this, *view)

fun android.app.Fragment.fitsTitleBarMarginTop(vararg view: View) =
    ImmBar.setTitleBarMarginTop(this, *view)

// 隐藏状态栏
fun Activity.hideStatusBar() = ImmBar.hideStatusBar(window)

fun Fragment.hideStatusBar() = activity?.run { ImmBar.hideStatusBar(window) } ?: Unit

fun android.app.Fragment.hideStatusBar() = activity?.run { ImmBar.hideStatusBar(window) }
    ?: Unit

// 显示状态栏
fun Activity.showStatusBar() = ImmBar.showStatusBar(window)

fun Fragment.showStatusBar() = activity?.run { ImmBar.showStatusBar(window) } ?: Unit

fun android.app.Fragment.showStatusBar() = activity?.run { ImmBar.showStatusBar(window) }
    ?: Unit

// 解决顶部与布局重叠问题，不可逆
fun Activity.setFitsSystemWindows() = ImmBar.setFitsSystemWindows(this)

fun Fragment.setFitsSystemWindows() = ImmBar.setFitsSystemWindows(this)

fun android.app.Fragment.setFitsSystemWindows() = ImmBar.setFitsSystemWindows(this)



