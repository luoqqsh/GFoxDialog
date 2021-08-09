package com.xing.gfox.view.scrollview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewParent;

import androidx.core.widget.NestedScrollView;

public class ContentNestedScrollView extends NestedScrollView {

    public interface OnScrollChangedListener {
        void onScrollChanged(int l, int t, int oldl, int oldt);
    }

    private OnScrollChangedListener listener;

    public ContentNestedScrollView(Context context) {
        super(context);
    }

    public ContentNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentNestedScrollView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnScrollChangeListener(OnScrollChangedListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        listener.onScrollChanged(l, t, oldl, oldt);
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        ViewParent parent = this.getParent();
        while (parent != null) {
            if (parent instanceof ScrollLayout) {
                ((ScrollLayout) parent).setAssociatedScrollView(this);
                break;
            }
            parent = parent.getParent();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        ViewParent parent = this.getParent();
        if (parent instanceof ScrollLayout) {
            if (((ScrollLayout) parent).getCurrentStatus() == ScrollLayout.Status.OPENED)
                return false;
        }
        return super.onTouchEvent(ev);
    }
}
