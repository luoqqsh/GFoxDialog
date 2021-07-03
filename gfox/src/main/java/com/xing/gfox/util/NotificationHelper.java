package com.xing.gfox.util;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;

import android.widget.RemoteViews;

import androidx.core.app.NotificationCompat;

import com.xing.gfox.R;
import com.xing.gfox.log.ViseLog;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 状态栏通知消息
 */
public class NotificationHelper {
    private static final int NOTIFICATION_REQUEST_CODE = 0x001;
    public static final int NOTIFICATION_TYPE_NORMAL = 0x0011;
    public static final int NOTIFICATION_TYPE_DOWNLOAD = 0x012;
    public static final int NOTIFICATION_TYPE_OTHER = 0x013;
    public static final int NOTIFICATION_TYPE_DIALOG = 0x014;
    private static final String NOTIFICATION_CHANNEL_ID_OTHER = "other";
    private static final String NOTIFICATION_CHANNEL_ID_NORMAL = "normal";
    private static final String NOTIFICATION_CHANNEL_ID_DOWNLOAD = "download";
    private static final String NOTIFICATION_CHANNEL_ID_DIALOG = "dialog";
    private static final String NOTIFICATION_CHANNEL_NAME_OTHER = "其他";//优先级1 min
    private static final String NOTIFICATION_CHANNEL_NAME_NORMAL = "应用通知";//优先级3 default
    private static final String NOTIFICATION_CHANNEL_NAME_DOWNLOAD = "下载通知";//优先级2 low
    private static final String NOTIFICATION_CHANNEL_NAME_DIALOG = "应用内通知";//优先级4 high

    private Context mContext;
    private NotificationManager mNotifyManager;
    private NotificationCompat.Builder mBuilder;
    private NotificationChannel mNotificationChannel;
    private int mNotificationId;
    private String mPushChannelName;
    private String mPushChannelId;

    /**
     * @param context 上下文
     */
    public NotificationHelper(Context context) {
        mContext = context;
        initNotification();
    }

    /**
     * 设置内容
     *
     * @param contentText 内容
     */
    public NotificationHelper setContent(String contentText) {
        mBuilder.setContentText(contentText);
        mBuilder.setTicker(contentText);
        return this;
    }

    /**
     * 设置标题
     *
     * @param title 标题
     */
    public NotificationHelper setTitle(String title) {
        mBuilder.setContentTitle(title);
        return this;
    }

    /**
     * 设置小图标
     *
     * @param smallIconId 小图标
     */
    public NotificationHelper setSmallIcon(int smallIconId) {
        mBuilder.setSmallIcon(smallIconId);
        return this;
    }

    /**
     * 设置大图标
     *
     * @param largeIcon 大图标
     */
    public NotificationHelper setLargeIcon(Bitmap largeIcon) {
        mBuilder.setLargeIcon(largeIcon);
        return this;
    }

    /**
     * 设置自定义样式
     *
     * @param remoteViews 自定义view
     * @return this
     */
    public NotificationHelper setCustomContentView(RemoteViews remoteViews) {
        mBuilder.setCustomContentView(remoteViews);
        return this;
    }

    public NotificationHelper setBigTextStyle(String title, String content) {
        NotificationCompat.BigTextStyle style = new NotificationCompat.BigTextStyle();
        style.bigText(content);
        style.setBigContentTitle(title);
        mBuilder.setStyle(style);
        return this;
    }

    /**
     * 设置内容跳转Intent
     *
     * @param intent 跳转
     */
    public NotificationHelper setContextIntent(Intent intent) {
        if (intent != null) {
            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, NOTIFICATION_REQUEST_CODE,
                    Intent.createChooser(intent, ""), PendingIntent.FLAG_UPDATE_CURRENT);
            mBuilder.setContentIntent(pendingIntent);
        }
        return this;
    }

    /**
     * 通知类型
     * NOTIFICATION_TYPE_NORMAL
     * NOTIFICATION_TYPE_DOWNLOAD
     * NOTIFICATION_TYPE_OTHER
     * NOTIFICATION_TYPE_DIALOG
     *
     * @param notificationType 通知类型
     */
    public NotificationHelper setType(int notificationType) {
        switch (notificationType) {
            case NOTIFICATION_TYPE_DOWNLOAD://下载通知
                downloadSetting();
                break;
            case NOTIFICATION_TYPE_OTHER://其他通知
                otherSetting();
                break;
            case NOTIFICATION_TYPE_DIALOG://应用内通知
                dialogSetting();
                break;
            default:
                //默认设置
                break;
        }
        return this;
    }

    /**
     * 设置通知id，不能为0
     *
     * @param notificationId 通知id
     */
    public NotificationHelper setNotificationId(int notificationId) {
        mNotificationId = notificationId;
        return this;
    }

    /**
     * 设置正在进行的通知，禁止滑动删除
     *
     * @param isOngoing 是否滑动删除
     */
    public NotificationHelper setOngoing(boolean isOngoing) {
        mBuilder.setOngoing(isOngoing);
        return this;
    }

    /**
     * 点击该条通知会自动删除，false时只能通过滑动来删除
     *
     * @param isAutoCancel 是否自动删除
     */
    public NotificationHelper setAutoCancel(boolean isAutoCancel) {
        mBuilder.setAutoCancel(isAutoCancel);
        return this;
    }

    /**
     * 取消通知
     */
    public void cancel() {
        mNotifyManager.cancel(mNotificationId);
    }

    /**
     * 静态方法 根据id清除通知
     *
     * @param context 上下文
     */
    public static void cancelById(Context context, int id) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) notificationManager.cancel(id);
    }

    /**
     * 静态方法 清除所以通知
     *
     * @param context context
     */
    public static void cancelAll(Context context) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        if (notificationManager != null) notificationManager.cancelAll();
    }

    /*
     * Notification.PRIORITY_DEFAULT(优先级为0)
     * Notification.PRIORITY_HIGH
     * Notification.PRIORITY_LOW
     * Notification.PRIORITY_MAX(优先级为2)
     * Notification.PRIORITY_MIN(优先级为-2)
     *
     * Oreo不用Priority了，用importance
     * IMPORTANCE_NONE 关闭通知
     * IMPORTANCE_MIN 开启通知，不会弹出，但没有提示音，状态栏中无显示
     * IMPORTANCE_LOW 开启通知，不会弹出，不发出提示音，状态栏中显示
     * IMPORTANCE_DEFAULT 开启通知，不会弹出，发出提示音，状态栏中显示
     * IMPORTANCE_HIGH 开启通知，会弹出，发出提示音，状态栏中显示
     */

    /**
     * @param defaults 提醒方式
     * @return Notification.DEFAULT_VIBRATE //添加默认震动提醒 需要 VIBRATE permission
     * <p>
     * Notification.DEFAULT_SOUND // 添加默认声音提醒
     * <p>
     * Notification.DEFAULT_LIGHTS// 添加默认三色灯提醒
     * <p>
     * Notification.DEFAULT_ALL// 添加默认以上3种全部提醒
     */
    public NotificationHelper setDefaults(int defaults) {
        mBuilder.setDefaults(defaults);
        return this;
    }

    /**
     * 刷新显示通知
     */
    public void notifyShow() {
        //区分开下载通知，根据当前时间可以直接show出多个应用通知
        if (mNotificationId == 0) {
            mNotificationId = (int) System.currentTimeMillis();
        }
        mNotifyManager.notify(mNotificationId, mBuilder.build());
    }

    /**
     * 初始化通知
     */
    private void initNotification() {
        //默认设置
        mPushChannelId = NOTIFICATION_CHANNEL_ID_NORMAL;
        mPushChannelName = NOTIFICATION_CHANNEL_NAME_NORMAL;
        if (mNotifyManager == null) {
            mNotifyManager = (NotificationManager) mContext.getSystemService(NOTIFICATION_SERVICE);
        }
        mBuilder = new NotificationCompat.Builder(mContext, mPushChannelId);
        mBuilder.setContentTitle("默认标题")//设置通知栏标题
                .setSmallIcon(R.mipmap.fx_qq);//设置通知小ICON// ;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //清除掉上一次推送建立的消息通道，否则新通道设置无效
            mNotificationChannel = new NotificationChannel(mPushChannelId, mPushChannelName, NotificationManager.IMPORTANCE_DEFAULT);
            mNotifyManager.createNotificationChannel(mNotificationChannel);
            mBuilder.setGroup(NOTIFICATION_CHANNEL_ID_NORMAL);
        } else {
            mBuilder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
            mBuilder.setDefaults(NotificationCompat.DEFAULT_ALL);
        }
    }

    /**
     * 下载通知 配置
     */
    private void downloadSetting() {
        mPushChannelName = NOTIFICATION_CHANNEL_NAME_DOWNLOAD;
        mPushChannelId = NOTIFICATION_CHANNEL_ID_DOWNLOAD;
        mNotificationId = (int) System.currentTimeMillis();//控制更新在同一条推送上
        //设置本次推送走那个通道
        mBuilder.setChannelId(mPushChannelId);
        mBuilder.setOngoing(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationChannel = new NotificationChannel(mPushChannelId, mPushChannelName, NotificationManager.IMPORTANCE_LOW);
            mNotificationChannel.enableVibration(false);//取消震动
            mNotificationChannel.setSound(null, null);
            mNotifyManager.createNotificationChannel(mNotificationChannel);
        } else {
            mBuilder.setPriority(NotificationCompat.PRIORITY_LOW);
            mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        }
    }

    /**
     * 其他通知 配置
     */
    private void otherSetting() {
        mPushChannelName = NOTIFICATION_CHANNEL_NAME_OTHER;
        mPushChannelId = NOTIFICATION_CHANNEL_ID_OTHER;
        //设置本次推送走哪个通道
        mBuilder.setChannelId(mPushChannelId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationChannel = new NotificationChannel(mPushChannelId, mPushChannelName, NotificationManager.IMPORTANCE_HIGH);
            mNotificationChannel.enableVibration(false);//取消震动
            mNotificationChannel.setSound(null, null);
            mNotifyManager.createNotificationChannel(mNotificationChannel);
        } else {
            mBuilder.setPriority(NotificationCompat.PRIORITY_MIN);
            mBuilder.setDefaults(NotificationCompat.DEFAULT_LIGHTS);
        }
    }

    /**
     * 应用内通知 配置
     */
    private void dialogSetting() {
        mPushChannelName = NOTIFICATION_CHANNEL_NAME_DIALOG;
        mPushChannelId = NOTIFICATION_CHANNEL_ID_DIALOG;
        //设置本次推送走哪个通道
        mBuilder.setChannelId(mPushChannelId);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotificationChannel = new NotificationChannel(mPushChannelId, mPushChannelName, NotificationManager.IMPORTANCE_HIGH);
            mNotifyManager.createNotificationChannel(mNotificationChannel);
        } else {
            mBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }
    }

    /**
     * 分组
     *
     * @param groupName 分组名
     */
    public void setNotificationGroup(String groupName) {
        mBuilder.setGroup(groupName);
        mBuilder.setGroupSummary(true);
        mNotifyManager.notify(mNotificationId, mBuilder.build());
    }

    /**
     * 更新通知栏的进度(下载中)
     *
     * @param progress 进度
     */
    public void setProgress(int progress, Intent intent) {
        ViseLog.d("TAG", "setProgress: " + progress);
        if (progress == 100) {
            mNotifyManager.cancel(mNotificationId);//不管当前显示进度多少，直接清空，并设置下载完成
            mBuilder.setContentText("下载完成").setProgress(100, 100, false);
            //设置点击启动安装
            if (intent != null) {
                PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, Intent.createChooser(intent, "标题"),
                        PendingIntent.FLAG_UPDATE_CURRENT);
                mBuilder.setContentIntent(pendingIntent);
            }
            mBuilder.setOngoing(false);
            mBuilder.setAutoCancel(true);
        } else {
            mBuilder.setContentText("正在下载:" + String.valueOf(progress) + "%").setProgress(100, progress, false);
        }
        mNotifyManager.notify(mNotificationId, mBuilder.build());

    }

}
