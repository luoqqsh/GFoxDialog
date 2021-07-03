package com.xing.gfox.base.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import com.xing.gfox.R;
import com.xing.gfox.view.SlideLayout;

public abstract class HLSlideActivity extends AppCompatActivity {
    private SlideLayout swipeBackLayout;

    protected boolean isOpenSlideBack() {
        return false;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (isOpenSlideBack()) {
            swipeBackLayout = new SlideLayout(this);
            swipeBackLayout.attachToActivity(this);
        }
    }

    public void setDrawerScrollEnable(boolean isEnable) {
        if (swipeBackLayout != null) {
            swipeBackLayout.setIsCanScroll(isEnable);
        }
    }

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (swipeBackLayout != null) {
            overridePendingTransition(R.anim.slide_right_in, R.anim.no_anim);
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (swipeBackLayout != null) {
            overridePendingTransition(0, R.anim.slide_right_out);
        }
    }

    /**
     * 安卓8.0的系统在targetSdkVersion不是26的情况下，可以使用这个
     *
     * @return
     */
    private boolean fixOrientation() {
        try {
            Field field = Activity.class.getDeclaredField("mActivityInfo");
            field.setAccessible(true);
            ActivityInfo o = (ActivityInfo) field.get(this);
            if (o != null) {
                o.screenOrientation = ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED;
            }
            field.setAccessible(false);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    protected boolean isTranslucentOrFloating() {
        boolean isTranslucentOrFloating = false;
        try {
            int[] styleableRes = (int[]) Class.forName("com.android.internal.R$styleable").getField("Window").get(null);
            final TypedArray ta = obtainStyledAttributes(styleableRes);
            Method m = ActivityInfo.class.getMethod("isTranslucentOrFloating", TypedArray.class);
            m.setAccessible(true);
            isTranslucentOrFloating = (boolean) m.invoke(null, ta);
            m.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return isTranslucentOrFloating;
    }

    //该方法是将activity设置为不透明，需求冲突的话，需另寻方案
    public void fixRotateBug26() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O && isTranslucentOrFloating()) {
            try {
                Method method = Activity.class.getDeclaredMethod("convertFromTranslucent");
                method.setAccessible(true);
                method.invoke(this);
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }
}
