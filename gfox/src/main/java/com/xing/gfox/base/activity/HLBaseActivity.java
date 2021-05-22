package com.xing.gfox.base.activity;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.xing.gfox.R;
import com.xing.gfox.base.life.MyLifeObserver8;
import com.xing.gfox.immbar.BarHide;
import com.xing.gfox.immbar.BarParams;
import com.xing.gfox.immbar.ImmBar;
import com.xing.gfox.liveEventBus.LiveEventBus;
import com.xing.gfox.util.U_orientation2;
import com.xing.gfox.util.U_screen;
import com.xing.gfox.util.U_view;


public abstract class HLBaseActivity extends HLSlideActivity {
    public FragmentActivity mActivity;
    protected ViewGroup baseContentLayout;
    protected LinearLayout baseStatusLayout;
    public HLTitleView mTitle;
    public HLStatusView mStatus;
    private U_orientation2 ori;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        doBeforeCreate();
        super.onCreate(savedInstanceState);
        mActivity = this;
        getLifecycle().addObserver(new MyLifeObserver8(getClass().getSimpleName()));
        //在dialog对话框添加一个监听，如果Activity关掉，则调用对话框dismiss方法，是否有必要？
        if (isOverlap()) {
            setContentView(R.layout.activity_base_overlap);
        } else {
            setContentView(R.layout.activity_base);
        }
        baseContentLayout = findViewById(R.id.baseContentLayout);
        baseStatusLayout = findViewById(R.id.baseStatusLayout);
        int backgroundColor = getBackgroundColor();
        if (backgroundColor != 0) {
            baseContentLayout.setBackgroundColor(backgroundColor);
        } else if (getBackgroundColorResource() != 0) {
            baseContentLayout.setBackgroundResource(getBackgroundColorResource());
        }
        initTitleView();//加载标题
        initContentView();//加载内容
        initStatusView(null);//加载状态
        //沉浸式
        if (isInitImmBar()) {
            ImmBar immBar = ImmBar.with(mActivity);
            onStatusBarSetting(immBar);
            immBar.init();
        }
        initUI(savedInstanceState);
        initData();
    }

    private void initTitleView() {
        ViewGroup baseTitleLayout = findViewById(R.id.baseTitleLayout);
        if (isShowTitle()) {
            if (mTitle == null)
                mTitle = new HLTitleView(mActivity, baseTitleLayout, isAddStatusBar());
        } else {
            if (isAddStatusBar()) {
                U_view.setViewHeight(baseTitleLayout, U_screen.getStatusBarHeight(mActivity));
            }
        }
    }

    private void initContentView() {
        baseContentLayout = findViewById(R.id.baseContentLayout);
        LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lpContent.weight = 1;
        if (getLayoutId() != 0) {
            View contentView = LayoutInflater.from(this).inflate(getLayoutId(), null);
            baseContentLayout.addView(contentView, lpContent);
        } else if (getLayoutView() != null) {
            baseContentLayout.addView(getLayoutView(), lpContent);
        }
        if (!isShowTitle() && isAddStatusBar()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                baseContentLayout.setPadding(0, U_screen.getStatusBarHeight(mActivity), 0, 0);
            }
        }
    }

    public void initStatusView(HLStatusView statusView) {
        if (statusView == null) {
            mStatus = new HLStatusView(mActivity, baseStatusLayout);
        } else {
            mStatus = statusView;
        }
    }

    protected void onStatusBarSetting(ImmBar ImmBar) {
        ImmBar.fitsSystemWindows(false);
        ImmBar.statusBarDarkFont(isTitleBarTextDark());
        ImmBar.keyboardEnable(keyboardEnable());
//        ImmBar.transparentBar();//加了这个，导航栏会和布局内容重叠

//        ImmBar.with(this)//
//                .transparentStatusBar()//透明状态栏，不写默认透明色
//                .statusBarColor(statusBarColor())//状态栏颜色，不写默认透明色
//                .statusBarDarkFont(statusBarDarkFont(), 0.2f)  //\状态栏字体是深色，不写默认为亮色
//                //原理：如果当前设备支持状态栏字体变色，会设置状态栏字体为黑色，如果当前设备不支持状态栏字体变色，会使当前状态栏加上透明度，否则不执行透明度
//                .fitsSystemWindows(fitsSystemWindows())
//                //显示导航和状态栏
//                .hideBar(BarHide.FLAG_SHOW_BAR)
//                //解决状态栏和布局重叠问题，任选其一，默认为false，当为true时一定要指定statusBarColor()，不然状态栏为透明色，还有一些重载方法
//                .keyboardEnable(keyboardEnable())//键盘弹起时，强制改变布局高度
//                .setOnKeyboardListener(this)//软键盘监听回调
//                .init();
    }

    public void hideStatusBar() {
        ImmBar.with(this).hideBar(BarHide.FLAG_HIDE_STATUS_BAR).init();
    }

    public void hideBar() {
        ImmBar.with(this).hideBar(BarHide.FLAG_HIDE_BAR).init();
        if (isAddStatusBar()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                baseContentLayout.setPadding(0, 0, 0, 0);
            }
        }
    }

    public void showBar() {
        ImmBar.with(this).hideBar(BarHide.FLAG_SHOW_BAR).init();
        if (isAddStatusBar()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                baseContentLayout.setPadding(0, U_screen.getStatusBarHeight(mActivity), 0, 0);
            }
        }
    }

    public void hideNavigationBar() {
        if (ImmBar.hasNavigationBar(this)) {
            ImmBar.with(this).hideBar(BarHide.FLAG_HIDE_NAVIGATION_BAR).init();
        } else {
//            Toast.makeText(this, "当前设备没有导航栏或者导航栏已经被隐藏或者低于4.4系统", Toast.LENGTH_SHORT).show();
        }
    }

    public void showNavigationBar() {
        if (ImmBar.hasNavigationBar(this)) {
            BarParams barParams = ImmBar.with(this).getBarParams();
            if (barParams.fullScreen) {
                ImmBar.with(this).fullScreen(false).navigationBarColor(R.color.colorPrimary).init();
            } else {
                ImmBar.with(this).fullScreen(true).transparentNavigationBar().init();
            }
        } else {
//            Toast.makeText(this, "当前设备没有导航栏或者导航栏已经被隐藏或者低于4.4系统", Toast.LENGTH_SHORT).show();
        }
    }

    protected HLStatusView getStatusView() {
        return new HLStatusView(mActivity, baseStatusLayout);
    }

    public boolean isInitImmBar() {
        return true;
    }

    public boolean isShowTitle() {
        return true;
    }

    public boolean isTitleBarTextDark() {
        return false;
    }

    public boolean isAddStatusBar() {
        return false;
    }

    public boolean isOverlap() {
        return false;
    }

    public abstract void initUI(Bundle savedInstanceState);

    public void initData() {
    }

    public int getLayoutId() {
        return 0;
    }

    public View getLayoutView() {
        return null;
    }

    protected int getBackgroundColorResource() {
        return 0;
    }

    protected int getBackgroundColor() {
        return 0;
    }

    public void doBeforeCreate() {
    }

    private boolean canClickFinish = true;

    public void finish(long time) {
        if (canClickFinish) {
            canClickFinish = false;
            new Handler().postDelayed(this::finish, time);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (ori != null) {
            ori.stopWatch();
        }
    }

    /**
     * 启动旋转监听
     */
    public void startOriListener() {
        ori = new U_orientation2(mActivity);
        ori.setOnOrientationListener(new U_orientation2.OnOrientationListener() {
            @Override
            public void changedToLandScape(boolean fromPort) {
                if (fromPort) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                    changeToLand();
                    LiveEventBus.get("aaa").post("SCREEN_ORIENTATION_SENSOR_LANDSCAPE");
                }
            }

            @Override
            public void changedToPortrait(boolean fromLand) {
                if (fromLand) {
                    setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_PORTRAIT);
                    changeToPort();
                    LiveEventBus.get("aaa").post("SCREEN_ORIENTATION_SENSOR_PORTRAIT");
                }
            }
        });
        ori.startWatch();
    }

    public void changeToLand() {

    }

    public void changeToPort() {

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (touchHideSoft()) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                View v = getCurrentFocus();
                if (isShouldHideKeyboard(v, ev)) {
                    hideKeyboard(v.getWindowToken());
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    protected boolean keyboardEnable() {
        return false;
    }

    /**
     * 是否触摸edittext以外的隐藏软键盘
     *
     * @return
     */
    protected boolean touchHideSoft() {
        return true;
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            // 点击EditText的事件，忽略它。
            return !(event.getX() > left) || !(event.getX() < right)
                    || !(event.getY() > top) || !(event.getY() < bottom);
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }
}