package com.xing.gfox.view;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Scroller;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.xing.gfox.R;
import com.xing.gfox.base.interfaces.NoInterceptTouchView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;


/**
 * 需求透明主题，只能在清单里设置，Activity里设置无效！
 * <p>
 * 改自blog http://blog.csdn.net/xiaanming
 */
public class SlideLayout extends FrameLayout {
    private View mContentView;
    private int mTouchSlop;
    private int downX;
    private int downY;
    private int tempX;
    private Scroller mScroller;
    private int viewWidth;
    private boolean isSliding;//是否滑动
    private boolean isFinish;
    private Drawable mShadowDrawable;
    private Activity mActivity;
    private List<View> mChildViewList = new LinkedList<>();
    private boolean isEnable = true;

    public SlideLayout(Context context) {
        super(context);
        init(context);
    }

    public SlideLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public SlideLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        //获得的是触发移动事件的最短距离，如果小于这个距离就不触发移动控件，如viewpager就是用这个距离来判断用户是否翻页
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mScroller = new Scroller(context);
        mShadowDrawable = getResources().getDrawable(R.mipmap.slide_left);
    }

    public void attachToActivity(Activity activity) {
        mActivity = activity;
        TypedArray a = activity.getTheme().obtainStyledAttributes(
                new int[]{android.R.attr.windowBackground});
        int background = a.getResourceId(0, 0);
        a.recycle();

        ViewGroup decor = (ViewGroup) activity.getWindow().getDecorView();
        ViewGroup decorChild = (ViewGroup) decor.getChildAt(0);
        decorChild.setBackgroundResource(background);
        decor.removeView(decorChild);
        addView(decorChild);
        setContentView(decorChild);
        decor.addView(this);
    }

    private void setContentView(View decorChild) {
        mContentView = (View) decorChild.getParent();
    }

    /**
     * 事件拦截操作
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (!isEnable) {
            return super.onInterceptTouchEvent(ev);
        }

        //处理滑动冲突问题
        List<View> touchViewList = getTouchViewList(mChildViewList, ev);
//        ViseLog.showLog(TAG, "mChildViewList:" + mChildViewList);
//        ViseLog.showLog(TAG, "touchViewList:" + touchViewList);
        if (touchViewList != null && touchViewList.size() > 0) {
            for (View touchView : touchViewList) {
//                ViseLog.showLog(TAG, "touchView = " + touchView.getClass().getName());
                //设置不拦截情况
                if (touchView instanceof NoInterceptTouchView) {
                    return false;
                } else if (touchView instanceof ViewPager) {
                    ViewPager viewPager = (ViewPager) touchView;
                    if (viewPager.getCurrentItem() != 0) {
                        return super.onInterceptTouchEvent(ev);
                    }
                } else if (touchView instanceof HorizontalScrollView) {
                    HorizontalScrollView horizontalScrollView = (HorizontalScrollView) touchView;
                    if (horizontalScrollView.getScrollX() > 0) {
                        return super.onInterceptTouchEvent(ev);
                    }
                } else if (touchView instanceof RecyclerView) {

                } else {
                    if (touchView.getClass().getName().contains("noslide")) {
                        //noslide 控件在包含这个的包名下的，触摸不拦截
                        return false;
                    } else if (touchView instanceof SeekBar) {
                        return false;
                    } else if (touchView instanceof TextView
                            || touchView instanceof ImageView
                    ) {

                    } else {
//                        if (touchView.onTouchEvent(ev)) {
//                            return super.onInterceptTouchEvent(ev);
//                        }
                    }
                }
            }
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                downX = tempX = (int) ev.getRawX();
                downY = (int) ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) ev.getRawX();
                // 满足此条件屏蔽SildingFinishLayout里面子类的touch事件
                if (moveX - downX > mTouchSlop
                        && Math.abs((int) ev.getRawY() - downY) < mTouchSlop) {
                    return true;
                }
                break;
        }

        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isEnable) {
            return super.onTouchEvent(event);
        }
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                int moveX = (int) event.getRawX();
                int deltaX = tempX - moveX;
                tempX = moveX;
                if (moveX - downX > mTouchSlop
                        && Math.abs((int) event.getRawY() - downY) < mTouchSlop) {
                    isSliding = true;
                }

                if (moveX - downX >= 0 && isSliding) {
                    mContentView.scrollBy(deltaX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                isSliding = false;
                if (mContentView.getScrollX() <= -viewWidth / 3) {
                    isFinish = true;
                    scrollRight();
                } else {
                    scrollOrigin();
                    isFinish = false;
                }
                break;
        }
        return true;
    }


    /**
     * 获取需求不拦截的ChildView列表
     */
    private void getChildShouldInterceptViewList(List<View> mChildViewList, ViewGroup parent) {
        int childCount = parent.getChildCount();
        mChildViewList.add(parent);
        for (int i = 0; i < childCount; i++) {
            View child = parent.getChildAt(i);
            if (child instanceof ViewPager) {
                mChildViewList.add(child);
            } else if (child instanceof ViewGroup) {
                if (child instanceof HorizontalScrollView) {
                    mChildViewList.add(child);
                    getChildShouldInterceptViewList(mChildViewList, (ViewGroup) child);
                } else if (child instanceof ScrollView) {
                    getChildShouldInterceptViewList(mChildViewList, (ViewGroup) child);
                } else {
                    getChildShouldInterceptViewList(mChildViewList, (ViewGroup) child);
                }
            } else {
                mChildViewList.add(child);
            }
        }
    }

    /**
     * 返回我们触摸到的View 列表
     */
    private List<View> getTouchViewList(List<View> mChildViewList, MotionEvent ev) {
        if (mChildViewList == null || mChildViewList.size() == 0) {
            return null;
        }
        List<View> touchViewList = new ArrayList<>();
        Rect mRect = new Rect();
        for (View v : mChildViewList) {
            v.getGlobalVisibleRect(mRect);
            if (mRect.contains((int) ev.getRawX(), (int) ev.getRawY())) {
                touchViewList.add(v);
            }
        }
        return touchViewList;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            viewWidth = this.getWidth();

            mChildViewList.clear();
            getChildShouldInterceptViewList(mChildViewList, this);
        }
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        try {
            super.dispatchDraw(canvas);
            if (mShadowDrawable != null && mContentView != null) {

                int left = mContentView.getLeft()
                        - mShadowDrawable.getIntrinsicWidth();
                int right = left + mShadowDrawable.getIntrinsicWidth();
                int top = mContentView.getTop();
                int bottom = mContentView.getBottom();

                mShadowDrawable.setBounds(left, top, right, bottom);
                mShadowDrawable.draw(canvas);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 滚动出界面
     */
    private void scrollRight() {
        final int delta = (viewWidth + mContentView.getScrollX());
        // 调用startScroll方法来设置一些滚动的参数，我们在computeScroll()方法中调用scrollTo来滚动item
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta + 1, 0,
                Math.abs(delta));
        postInvalidate();
    }

    /**
     * 滚动到起始位置
     */
    private void scrollOrigin() {
        int delta = mContentView.getScrollX();
        mScroller.startScroll(mContentView.getScrollX(), 0, -delta, 0,
                Math.abs(delta));
        postInvalidate();
    }

    @Override
    public void computeScroll() {
        // 调用startScroll的时候scroller.computeScrollOffset()返回true，
        if (mScroller.computeScrollOffset()) {
            mContentView.scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

            if (mScroller.isFinished() && isFinish) {
                mActivity.finish();
            }
        }
    }

    public void setIsCanScroll(boolean isEnable) {
        this.isEnable = isEnable;
    }
}
