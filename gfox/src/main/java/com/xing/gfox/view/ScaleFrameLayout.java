package com.xing.gfox.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.xing.gfox.R;

public class ScaleFrameLayout extends FrameLayout {

    private float scaleHeight, scaleWidth;

    public ScaleFrameLayout(Context context) {
        super(context);
    }

    public ScaleFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ScaleFrameLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.ScaleFrameLayout);

            scaleHeight = a.getFloat(R.styleable.ScaleFrameLayout_scaleHeight, 0);
            scaleWidth = a.getFloat(R.styleable.ScaleFrameLayout_scaleWidth, 0);

            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            ViewGroup.LayoutParams lp = child.getLayoutParams();
            if (lp.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                if (scaleHeight != 0) lp.height = (int) (sizeWidth * scaleHeight);
            }
            if (lp.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                if (scaleWidth != 0) lp.width = (int) (sizeWidth * scaleWidth);
            }
            child.setLayoutParams(lp);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
