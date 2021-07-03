package com.xing.gfox.rxHttp.download;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.IBinder;
import android.text.TextUtils;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import java.io.File;

import com.xing.gfox.R;
import com.xing.gfox.util.U_app;
import com.xing.gfox.util.U_bitmap;
import com.xing.gfox.util.U_intent;

public class FileDownloadService extends Service {
    private static final int NOTIFY_ID = 0;
    private static final String TAG = FileDownloadService.class.getSimpleName();
    private static final String CHANNEL_ID = "app_download_id";
    private static final CharSequence CHANNEL_NAME = "app_update_channel";

    public static boolean isRunning = false;
    private NotificationManager mNotificationManager;
    private DownloadBinder binder = new DownloadBinder();
    private NotificationCompat.Builder mBuilder;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    public static void bindService(Context context, ServiceConnection connection) {
        if (isRunning) {
            return;
        }
        isRunning = true;
        Intent intent = new Intent(context, FileDownloadService.class);
        context.startService(intent);
        context.bindService(intent, connection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        isRunning = false;
        return super.onUnbind(intent);
    }

    private void stopDownloadService(String contentText) {
        if (mBuilder == null) {
            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        }
        close();
    }

    private void close() {
        stopSelf();
        isRunning = false;
    }

    private void setUpNotification() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            //设置绕过免打扰模式
//            channel.setBypassDnd(false);
//            //检测是否绕过免打扰模式
//            channel.canBypassDnd();
//            //设置在锁屏界面上显示这条通知
//            channel.setLockscreenVisibility(Notification.VISIBILITY_SECRET);
//            channel.setLightColor(Color.GREEN);
//            channel.setShowBadge(true);
//            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            channel.enableVibration(false);
            channel.enableLights(false);

            mNotificationManager.createNotificationChannel(channel);
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            mBuilder = new NotificationCompat.Builder(this);
        }
        mBuilder.setContentTitle("开始下载")
                .setContentText("正在连接服务器")
                .setSmallIcon(R.mipmap.fx_qq)
//                .setLargeIcon(AppUpdateUtils.drawableToBitmap(AppUpdateUtils.getAppIcon(DownloadService.this)))
                .setLargeIcon(U_bitmap.drawable2Bitmap(U_app.getAppIcon(this, getPackageName())))
                .setOngoing(true)
                .setAutoCancel(true)
                .setWhen(System.currentTimeMillis());
        mNotificationManager.notify(NOTIFY_ID, mBuilder.build());
    }

    /**
     * DownloadBinder中定义了一些实用的方法
     *
     * @author user
     */
    public class DownloadBinder extends Binder {
        /**
         * 开始下载
         *
         * @param callback 下载回调
         */
        public void startAndOpen(String downloadUrl, String target, String fileName, boolean isShowNotification, DownloadListener callback) {
            start(downloadUrl, target, fileName, true, isShowNotification, callback);
        }

        public void start(String downloadUrl, String target, String fileName, boolean isShowNotification, DownloadListener callback) {
            start(downloadUrl, target, fileName, false, isShowNotification, callback);
        }

        private void start(String downloadUrl, String target, String fileName, boolean isOpen, boolean isShowNotification, DownloadListener callback) {
            if (TextUtils.isEmpty(downloadUrl)) {
                String contentText = "下载路径错误";
                stopDownloadService(contentText);
                return;
            }
            //下载目录不存在，新建！
            File appDir = new File(target);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }

            String filePath = appDir.getAbsolutePath() + File.separator + fileName;

            //开始下载

            FileDownloadManager fileDownloadManager = FileDownloadManager.getInstance(FileDownloadService.this);
            fileDownloadManager.setDownloadListener(new DownloadListener() {
                @Override
                public void onDownLoadStart(String resultFilePath, int progress) {
                    if (isShowNotification) setUpNotification();
                    if (callback != null) {
                        callback.onDownLoadStart(resultFilePath, progress);
                    }
                }

                @Override
                public void onDownLoadProgress(String resultFilePath, int progress) {
                    if (callback != null) {
                        callback.onDownLoadProgress(resultFilePath, progress);
                    }
                    if (isShowNotification) {
                        int rate = Math.round(progress * 100);
                        if (mBuilder != null) {
                            mBuilder.setContentTitle("正在下载：" + fileName)
                                    .setContentText(progress + "%")
                                    .setProgress(100, progress, false)
                                    .setWhen(System.currentTimeMillis());
                            Notification notification = mBuilder.build();
                            notification.flags = Notification.FLAG_AUTO_CANCEL | Notification.FLAG_ONLY_ALERT_ONCE;
                            mNotificationManager.notify(NOTIFY_ID, notification);
                        }
                    }
                }

                @Override
                public void onDownLoadError(String resultFilePath, String error) {
                    if (isShowNotification) {
                        if (mNotificationManager != null) {
                            mNotificationManager.cancel(NOTIFY_ID);
                        }
                    }
                    if (callback != null) {
                        callback.onDownLoadError(resultFilePath, error);
                    }
                    close();
                }

                @Override
                public void onDownloadFinish(String filePath) {
                    if (callback != null) {
                        callback.onDownloadFinish(filePath);
                    }
                    if (isShowNotification) {
                        PendingIntent intent = U_intent.getInstallAppIntent(FileDownloadService.this, new File(filePath));
                        mBuilder = new NotificationCompat.Builder(FileDownloadService.this, CHANNEL_ID);
                        mBuilder.setContentIntent(intent)
                                .setSmallIcon(R.mipmap.fx_qq)
                                .setContentTitle("xxx")
                                .setContentText("下载完成")
                                .setProgress(0, 0, false)
//                            .setAutoCancel(true)
                                .setDefaults((Notification.DEFAULT_ALL));
                        Notification notification = mBuilder.build();
                        notification.flags = Notification.FLAG_AUTO_CANCEL;
                        mNotificationManager.notify(NOTIFY_ID, notification);
                    }
                    close();
                }
            });
            if (isOpen) {
                fileDownloadManager.startDownloadAndOpen(downloadUrl, filePath);
            } else {
                fileDownloadManager.startDownload(downloadUrl, filePath);
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    @Override
    public void onDestroy() {
        mNotificationManager = null;
        super.onDestroy();
    }
}
