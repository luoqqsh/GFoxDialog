package com.xing.gfox.base.activity;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;

import com.xing.gfox.base.interfaces.OnSimpleListener;
import com.xing.gfox.R;
import com.xing.gfox.log.ViseLog;

public class HLStatusView {
    protected ViewGroup parentView;
    protected View statusView;
    protected FragmentActivity mContext;
    protected OnSimpleListener onRetryListener;
    protected String noDataTip = "没有数据";
    protected String noNetTip = "没有网络连接";
    protected String errorTip = "点击屏幕，重新加载";
    protected LinearLayout.LayoutParams layoutParams;
    private int noDataImg, noNetImg, errorImg;
    private int noDataUp, noNetUp, errorUp;
    private int noDataDown, noNetDown, errorDown;
    private int textSize = 16;
    private TextView loadingRetry;
    private boolean isSHowLoading = false;
    private ImageView loadingImg;

    private HLStatusView() {
    }

    /**
     * 初始化状态页
     *
     * @param mActivity  context
     * @param parentView 父类
     */
    public HLStatusView(FragmentActivity mActivity, LinearLayout parentView) {
        this.mContext = mActivity;
        this.parentView = parentView;
        this.layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        this.layoutParams.gravity = Gravity.CENTER;
        parentView.setVisibility(View.VISIBLE);
    }

    public void setLoading() {
        removeView();
        statusView = View.inflate(mContext, getLoadingLayoutId(), null);
        statusView.findViewById(R.id.loadingLayout).setOnClickListener(view -> {
        });
        if (parentView != null) {
            parentView.addView(statusView, 0, layoutParams);
            isSHowLoading = true;
        } else {
            ViseLog.showLog("parentView为空");
        }
    }

    public void setError() {
        setError(Gravity.CENTER);
    }

    public void setError(int location) {
        removeView();
        statusView = View.inflate(mContext, getStatusLayoutId(), null);
        loadingImg = statusView.findViewById(R.id.noDataImg);
        loadingImg.setPadding(0, errorUp, 0, errorDown);
        loadingImg.setImageResource(errorImg);
        LinearLayout noDataLayout = statusView.findViewById(R.id.noDataLayout);
        noDataLayout.setOnClickListener(view -> {
        });
        noDataLayout.setGravity(location);
        loadingRetry = statusView.findViewById(R.id.noDataTip);
        loadingRetry.setText(errorTip);
        loadingRetry.setTextSize(textSize);
        loadingRetry.setOnClickListener(view -> {
            retry();
        });
        parentView.addView(statusView, 0, layoutParams);
    }

    public void setNoData() {
        setNoData(Gravity.CENTER);
    }

    public void setNoData(int location) {
        removeView();
        statusView = View.inflate(mContext, getStatusLayoutId(), null);
        LinearLayout noDataLayout = statusView.findViewById(R.id.noDataLayout);
        noDataLayout.setOnClickListener(view -> {
        });
        noDataLayout.setGravity(location);
        ImageView img = statusView.findViewById(R.id.noDataImg);
        img.setPadding(0, noDataUp, 0, noDataDown);
        img.setImageResource(noDataImg);
        loadingRetry = statusView.findViewById(R.id.noDataTip);
        loadingRetry.setText(noDataTip);
        loadingRetry.setTextSize(textSize);
        loadingRetry.setOnClickListener(view -> {
            retry();
        });
        parentView.addView(statusView, 0, layoutParams);
    }

    public void setNoNet() {
        setNoNet(Gravity.CENTER);
    }

    public void setNoNet(int location) {
        removeView();
        LinearLayout noDataLayout = statusView.findViewById(R.id.noDataLayout);
        noDataLayout.setOnClickListener(view -> {
        });
        noDataLayout.setGravity(location);
        ImageView img = statusView.findViewById(R.id.noDataImg);
        img.setPadding(0, noNetUp, 0, noNetDown);
        img.setImageResource(noNetImg);
        loadingRetry = statusView.findViewById(R.id.noDataTip);
        loadingRetry.setText(noNetTip);
        loadingRetry.setTextSize(textSize);
        loadingRetry.setOnClickListener(view -> retry());
        parentView.addView(statusView, 0, layoutParams);
    }

    public void setFinish() {
        isSHowLoading = false;
        removeView();
    }

    protected void removeView() {
        if (statusView != null && parentView != null) {
            parentView.removeView(statusView);
            statusView = null;
        }
    }

    protected void retry() {
        if (onRetryListener != null) {
            onRetryListener.onListen();
        }
    }

    public void setOnRetryListener(OnSimpleListener onRetryListener) {
        this.onRetryListener = onRetryListener;
    }

    public boolean isSHowLoading() {
        return isSHowLoading;
    }

    protected int getLoadingLayoutId() {
        return R.layout.base_loading;
    }

    protected int getStatusLayoutId() {
        return R.layout.base_status;
    }

    public void setTipTextSize(int textSize) {
        this.textSize = textSize;
    }

    public void setNoDataTip(String noDataTip) {
        this.noDataTip = noDataTip;
    }

    public void setNoNetTip(String noNetTip) {
        this.noNetTip = noNetTip;
    }

    public void setErrorTip(String errorTip) {
        this.errorTip = errorTip;
    }

    public void setNoDataImg(int img, int top, int bottom) {
        this.noDataImg = img;
        this.noDataUp = top;
        this.noDataDown = bottom;
    }

    public void setNoNetImg(int img, int top, int bottom) {
        this.noNetImg = img;
        this.noNetUp = top;
        this.noNetDown = bottom;
    }

    public void setErrorImg(int img, int top, int bottom) {
        this.errorImg = img;
        this.errorUp = top;
        this.errorDown = bottom;
    }

    public TextView getLoadingRetry() {
        return loadingRetry;
    }

    public ImageView getLoadingImg() {
        return loadingImg;
    }
}
