package com.xing.gfoxdialog;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;

import androidx.core.app.NotificationManagerCompat;

import com.xing.gfox.base.toast.U_Toast;
import com.xing.gfox.base.toast.style.BlackToastStyle;
import com.xing.gfox.base.toast.style.WhiteToastStyle;
import com.xing.gfoxdialog.BaseApp.BaseActivity;


public class ToastActivity extends BaseActivity {
    @Override
    protected int getBackgroundColorResource() {
        return R.color.white;
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_toast;
    }

    @Override
    public boolean isShowTitle() {
        return true;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        mTitle.setLeftButtonImage(R.mipmap.hl_back_black, v -> finish());
        mTitle.setTitleText("Toast");
    }

    public void show1(View v) {
        U_Toast.show("我是普通的 Toast");
    }

    @SuppressWarnings("AlibabaAvoidManuallyCreateThread")
    public void show2(View v) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                U_Toast.show("我是子线程中弹出的吐司");
            }
        }).start();
    }

    public void show3(View v) {
        U_Toast.setStyle(new WhiteToastStyle());
        U_Toast.show("动态切换白色吐司样式成功");
    }

    public void show4(View v) {
        U_Toast.setStyle(new BlackToastStyle());
        U_Toast.show("动态切换黑色吐司样式成功");
    }

    public void show5(View v) {
        U_Toast.setView(R.layout.toast_custom_view);
        U_Toast.setGravity(Gravity.CENTER);
        U_Toast.show("自定义 Toast 布局");
    }

    public void show6(View v) {
        // 推荐使用 XXPermissions 来申请通知栏权限：https://github.com/getActivity/XXPermissions
        if (NotificationManagerCompat.from(this).areNotificationsEnabled()) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
            v.postDelayed(new BackgroundRunnable(), 500);
        } else {
            U_Toast.show("在后台显示 Toast 需要先获取通知栏权限");
        }
    }

    public void show7(View v) {
//        new XToast<>(ToastActivity.this)
//                .setDuration(1000)
//                .setView(U_Toast.getStyle().createView(getApplication()))
//                .setAnimStyle(android.R.style.Animation_Translucent)
//                .setText(android.R.id.message, "就问你溜不溜")
//                .setGravity(Gravity.BOTTOM)
//                .setYOffset((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 50, getResources().getDisplayMetrics()))
//                .show();
    }

    private static class BackgroundRunnable implements Runnable {

        @Override
        public void run() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                U_Toast.show("我是在后台显示的 Toast（在 Android 11 上只能跟随系统 Toast 样式）");
            } else {
                U_Toast.show("我是在后台显示的 Toast");
            }
        }
    }

    ;
}
