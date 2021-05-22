package com.xing.gfoxdialog;


import android.Manifest;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.xing.gfox.util.U_permissions;
import com.xing.gfox.view.webView.AndroidWebView;
import com.xing.gfoxdialog.BaseApp.BaseActivity;

public class H5Activity extends BaseActivity {
    private AndroidWebView webView;

    @Override
    public View getLayoutView() {
        webView = new AndroidWebView(this);
        return webView;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.ACCESS_FINE_LOCATION};
        U_permissions.applyPermission(this, permission, null);
        webView.loadUrl("https://www.iqiyi.com");
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroyWebView();
            webView = null;
        }
    }

    private void goBackPage() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            finish();
        }
    }

    private void goForwardPage() {
        if (webView.canGoForward()) {
            webView.goForward();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        webView.pauseTimers();
        webView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        webView.resumeTimers();
        webView.onResume();
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            goBackPage();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}