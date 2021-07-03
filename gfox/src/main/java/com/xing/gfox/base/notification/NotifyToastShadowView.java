package com.xing.gfox.base.notification;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.xing.gfox.base.interfaces.OnSimpleListener;

public class NotifyToastShadowView extends RelativeLayout {

    private FragmentActivity activity;
    private int notifyHeight;
    private OnSimpleListener onNotificationClickListener;
    private boolean dispatchTouchEvent = true;

    public NotifyToastShadowView(Context context) {
        super(context);
    }

    public NotifyToastShadowView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public NotifyToastShadowView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (dispatchTouchEvent) {
            if (ev.getAction() == MotionEvent.ACTION_CANCEL || ev.getAction() == MotionEvent.ACTION_DOWN || ev.getAction() == MotionEvent.ACTION_UP) {
                if (ev.getY() < notifyHeight) {
                    if (onNotificationClickListener != null) onNotificationClickListener.onListen();
                    onNotificationClickListener = null;
                    return super.dispatchTouchEvent(ev);
                } else {
                    if (activity != null) activity.dispatchTouchEvent(ev);
                    return false;
                }
            } else {
                return super.dispatchTouchEvent(ev);
            }
        } else {
            return super.dispatchTouchEvent(ev);
        }
    }

    public void setParent(FragmentActivity c) {
        this.activity = c;
    }

    public void setNotifyHeight(int notifyHeight) {
        this.notifyHeight = notifyHeight;
    }

    public void setOnNotificationClickListener(OnSimpleListener onNotificationClickListener) {
        this.onNotificationClickListener = onNotificationClickListener;
    }

    public void setDispatchTouchEvent(boolean dispatchTouchEvent) {
        this.dispatchTouchEvent = dispatchTouchEvent;
    }
}
