package com.xing.gfox.base.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.xing.gfox.base.activity.HLStatusView;
import com.xing.gfox.base.activity.HLTitleView;
import com.xing.gfox.base.life.MyLifeObserver8;
import com.xing.gfox.R;
import com.xing.gfox.util.U_screen;
import com.xing.gfox.util.U_view;

/**
 * 新的base类
 */
public abstract class HLBaseFragment extends Fragment {
    public FragmentActivity mActivity;
    public View mView;
    public HLStatusView mStatus;
    public HLTitleView mTitle;
    private boolean isNeedInit = isInitDataAfterVisibleToUser();

    public boolean isInitDataAfterVisibleToUser() {
        return false;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLifecycle().addObserver(new MyLifeObserver8(getClass().getSimpleName()));
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mActivity = getActivity();
            if (isTitleContentOverLap()) {
                mView = inflater.inflate(R.layout.activity_base_overlap, container, false);
            } else {
                mView = inflater.inflate(R.layout.activity_base, container, false);
            }
            initTitleView();
            initContentView();
            initStatusView();
        }
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUI(savedInstanceState);
        initData();
        if (!isNeedInit) {
            lazyLoadData();
        }
    }

    private void initContentView() {
        ViewGroup baseContentLayout = mView.findViewById(R.id.baseContentLayout);
        LinearLayout.LayoutParams lpContent = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        lpContent.weight = 1;
        View view = getLayoutView();
        if (getLayoutId() != 0) {
            View contentView = LayoutInflater.from(mActivity).inflate(getLayoutId(), null);
            baseContentLayout.addView(contentView, lpContent);
        } else if (view != null) {
            baseContentLayout.addView(view, lpContent);
        }
        int backgroundColor = getBackgroundColor();
        if (backgroundColor != 0) {
            baseContentLayout.setBackgroundColor(backgroundColor);
        } else if (getBackgroundColorResource() != 0) {
            baseContentLayout.setBackgroundResource(getBackgroundColorResource());
        }
    }

    private void initStatusView() {
        LinearLayout baseStatusLayout = mView.findViewById(R.id.baseStatusLayout);
        mStatus = new HLStatusView(mActivity, baseStatusLayout);
    }

    private void initTitleView() {
        ViewGroup baseTitleLayout = mView.findViewById(R.id.baseTitleLayout);
        if (isShowTitle()) {
            if (mTitle == null)
                mTitle = new HLTitleView(mActivity, baseTitleLayout, isAddStatusBar());
        } else {
            if (isAddStatusBar()) {
                U_view.setViewHeight(baseTitleLayout, U_screen.getStatusBarHeight(mActivity));
            }
        }
    }

    public boolean isShowTitle() {
        return false;
    }

    public abstract void initUI(Bundle savedInstanceState);

    public boolean isAddStatusBar() {
        return false;
    }

    public void lazyLoadData() {
    }

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

    /**
     * 基础布局，标题栏和内容是否重叠--标题栏在内容之上
     */
    public boolean isTitleContentOverLap() {
        return false;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isNeedInit) {
            isNeedInit = false;
            lazyLoadData();
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        //完成Fragment和Activity的绑定，参数中的Activity即为要绑定的Activity，可以进行赋值等操作。
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
