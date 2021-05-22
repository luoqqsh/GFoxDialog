package com.xing.gfox.dialog;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import com.xing.gfox.R;
import com.xing.gfox.base.dialog.BaseNDialog;
import com.xing.gfox.view.LoadingBaseView;


public class CWaitingDialog extends BaseNDialog {

    private LoadingBaseView loadingView;

    @Override
    protected void onDialogResume() {
        super.onDialogResume();
        if (loadingView != null) {
            loadingView.start();
        }
    }

    @Override
    protected void onDialogPause() {
        super.onDialogPause();
        if (loadingView != null) {
            loadingView.stop();
        }
    }

    @Override
    protected void initUI(View layout, Bundle bundle) {
        loadingView = layout.findViewById(R.id.loadingView);
        if (loadingView != null) {
            loadingView.start();
        }
    }

    @Override
    public float getWidthScale() {
        return ViewGroup.LayoutParams.WRAP_CONTENT;
    }

    @Override
    public int getDialogStyle() {
        return R.style.TDialog;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ndialog_waitting;
    }

    @Override
    public boolean isCanceledOnTouchOutside() {
        return false;
    }

    @Override
    protected void onDialogDismiss() {
        super.onDialogDismiss();
        if (loadingView != null) {
            loadingView.stop();
            loadingView = null;
        }
    }
}
