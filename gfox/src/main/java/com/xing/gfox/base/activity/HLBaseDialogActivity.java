package com.xing.gfox.base.activity;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.xing.gfox.base.life.MyLifeObserver8;
import com.xing.gfox.R;
import com.xing.gfox.util.U_screen;
import com.xing.gfox.util.U_view;

public abstract class HLBaseDialogActivity extends HLSlideActivity {
    public FragmentActivity mActivity;
    protected ViewGroup baseContentLayout;
    protected LinearLayout baseStatusLayout;
    public HLTitleView mTitle;
    public HLStatusView mStatus;

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
        initUI(savedInstanceState);
        initData();
    }

    private void initTitleView() {
        ViewGroup baseTitleLayout = findViewById(R.id.baseTitleLayout);
        if (isShowTitle()) {
            if (mTitle == null)
                mTitle = new HLTitleView(mActivity, baseTitleLayout, isAddStatusBar());
        } else if (isAddStatusBar()) {
            U_view.setViewHeight(baseTitleLayout, U_screen.getStatusBarHeight(mActivity));
        }
    }

    private void initContentView() {
        baseContentLayout = findViewById(R.id.baseContentLayout);
        if (getLayoutId() != 0) {
            View contentView = LayoutInflater.from(this).inflate(getLayoutId(), null);
            LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);
            lpContent.weight = 1;
            contentView.setLayoutParams(lpContent);
            baseContentLayout.addView(contentView);
        } else {
            View view = getLayoutView();
            if (view != null) baseContentLayout.addView(view);
        }
    }

    public void initStatusView(HLStatusView statusView) {
        if (statusView == null) {
            mStatus = new HLStatusView(mActivity, baseStatusLayout);
        } else {
            mStatus = statusView;
        }
    }

    protected HLStatusView getStatusView() {
        return new HLStatusView(mActivity, baseStatusLayout);
    }

    public boolean isShowTitle() {
        return true;
    }

    public boolean isAddStatusBar() {
        return isShowTitle();
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

}