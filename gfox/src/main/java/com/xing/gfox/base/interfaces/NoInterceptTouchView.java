package com.xing.gfox.base.interfaces;


/**
 * 用于自定义View，带有这个标记的，触摸特殊，不被 SlideLayout 及 自己的ViewPager滑动干涉
 * 所有实现这个的，触摸时，SlideLayout 不滑动 ViewPager(自己的)不滑动
 */
public interface NoInterceptTouchView {
}
