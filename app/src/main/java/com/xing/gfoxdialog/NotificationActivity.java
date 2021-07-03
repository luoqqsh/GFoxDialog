package com.xing.gfoxdialog;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.RemoteViews;

import com.xing.gfox.util.NotificationHelper;
import com.xing.gfoxdialog.BaseApp.BaseActivity;


public class NotificationActivity extends BaseActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_notification;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        //通知使用
        new NotificationHelper(this)
                //通知标题
                .setTitle("默认通知")
                //通知内容
                .setContent("测试1")
                //设置提醒方式
                .setDefaults(Notification.DEFAULT_ALL)
                //通知类型
                //NOTIFICATION_TYPE_OTHER 折叠
                //NOTIFICATION_TYPE_DOWNLOAD 下载
                //NOTIFICATION_TYPE_NORMAL 普通
                //NOTIFICATION_TYPE_DIALOG 弹框
                .setType(NotificationHelper.NOTIFICATION_TYPE_OTHER)
                //设置左上角小图标
                .setSmallIcon(R.mipmap.ic_launcher)
                //设置右侧大图标
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_error))
                //设置跳转
                .setContextIntent(null)
                //传入通知id，可以不传，id相同会覆盖
                .setNotificationId(111)
                //true点击自动删除，false滑动才能删除
                .setAutoCancel(true)
                //设置自定义布局
                .setCustomContentView(new RemoteViews(getPackageName(), R.layout.notification_custom_layout))
                //设置样式——多文字
                .setBigTextStyle("多文字样式标题", "多文字样式内容")
                //设置是否为正在进行中，true则禁止删除
                .setOngoing(true)
                //设置是否点击后清除
                .setAutoCancel(false)
                //设置下载进度，当type为DOWNLOAD时，传入进度和跳转Intent，不传则点击无效果
                //.setProgress(100, null);
                //显示
                .notifyShow();
        //取消通知
        //.cancel()
    }

    public void sendNormal(View view) {
        //优先级default（3）默认通知，不可折叠
        new NotificationHelper(this)
                .setTitle("普通通知")
                .setContent("测试1")//通知内容
                .setDefaults(Notification.DEFAULT_ALL)//设置提醒方式
                .setType(NotificationHelper.NOTIFICATION_TYPE_NORMAL)//通知类型
                .setSmallIcon(R.mipmap.ic_launcher)//
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.icon_error))
                .setAutoCancel(true)//true点击自动删除，false滑动才能删除
                .setOngoing(true)//正在进行的通知，禁止滑动删除
                .setBigTextStyle("大标题", "展开的内展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容展开的内容")
                .notifyShow();
    }

    public void sendOther(View view) {
        //优先级min（1）没有提醒，可以折叠
        new NotificationHelper(this)
                .setTitle("折叠通知")
                .setContent("测试1")//通知内容
                .setType(NotificationHelper.NOTIFICATION_TYPE_OTHER)//通知类型
                .setSmallIcon(R.mipmap.ic_launcher)
                .notifyShow();
    }

    public void sendDownload(View view) {
        //优先级low（2）下载通知，带进度条
        NotificationHelper notificationHelper = new NotificationHelper(this)
                .setTitle("下载通知")
                .setContent("测试1")//通知内容
                .setType(NotificationHelper.NOTIFICATION_TYPE_DOWNLOAD)
                .setSmallIcon(R.mipmap.ic_launcher);
        notificationHelper.notifyShow();
        notificationHelper.setProgress(100, new Intent(Intent.ACTION_SEND));
    }

    public void sendDialog(View view) {
        //优先级heigh（4）应用内通知，弹出框
        new NotificationHelper(this)
                .setTitle("QQ")
                .setContent("收到一条未读消息")//通知内容
                .setType(NotificationHelper.NOTIFICATION_TYPE_DIALOG)//通知类型
                .setNotificationId(123)
                .notifyShow();
    }

    public void sendCustom(View view) {
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.notification_custom_layout);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, Intent.createChooser(new Intent(NotificationActivity.this, DialogActivity.class), "标题"),
                PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn, pendingIntent);
        new NotificationHelper(this)
                .setDefaults(Notification.DEFAULT_ALL)//设置提醒方式
                .setType(NotificationHelper.NOTIFICATION_TYPE_OTHER)//通知类型
                .setAutoCancel(false)//true点击自动删除，false滑动才能删除
                .setCustomContentView(remoteViews)
                .notifyShow();
    }

    public void cancelAll(View view) {
        NotificationHelper.cancelAll(this);
    }

    public void intent(View view) {
        startActivity(new Intent(this, DialogActivity.class));
    }
}
