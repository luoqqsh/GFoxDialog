package com.xing.gfoxdialog;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.view.WindowManager;


/**
 * 视频播放悬浮窗
 */
public class FloatingWindowService extends Service implements View.OnClickListener {

    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private View display;
    private TextureView surfaceView;
//    private CVPlayer client;
    private boolean show=true;
    private int qiehuan=0;

    private long startTime=0;
    private long endTime=0;

    private boolean isclick=false;


    public FloatingWindowService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        layoutParams = new WindowManager.LayoutParams();

        // 设置图片格式，效果为背景透明
        layoutParams.format = PixelFormat.RGB_565;

        Log.i("悬浮窗", "Build.VERSION.SDK_INT" + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            // android 8.0及以后使用
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            // android 8.0以前使用
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        //该flags描述的是窗口的模式，是否可以触摸，可以聚焦等
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        // 设置视频的播放窗口大小
        layoutParams.width = 400;
        layoutParams.height = 550;
        layoutParams.x = 700;
        layoutParams.y = 0;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (show) {
            showFloatingWindow();
        }
        return super.onStartCommand(intent, flags, startId);

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @SuppressLint("NewApi")
    private void showFloatingWindow(){
        if (Settings.canDrawOverlays(this)) {
            LayoutInflater layoutInflater = LayoutInflater.from(this);
//            display = layoutInflater.inflate(R.layout.little, null);
//            surfaceView = display.findViewById(R.id.texture_view);
//            client = new EasyPlayerClient(this, BuildConfig.KEY, surfaceView, null, null);
//            final EditText et = new EditText(this);
//            et.setHint("请输入RTMP地址");
//            final SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
//            et.setText(sp.getString("url", "rtmp://111.198.38.150:10085/live/869496039536917_0001"));
//            client.play("rtmp://111.198.38.150:10085/live/869496039536917_0001");
            windowManager.addView(display, layoutParams);
            display.setOnTouchListener(new FloatingOnTouchListener());
            display.setOnClickListener(new FloatingOnClickListener());
            show=false;
        }
    }

    @Override
    public void onClick(View v) {

    }


    //点击事件
    private class FloatingOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (qiehuan % 2 == 0) {
//                client.stop();
//                client.play("rtmp://111.198.38.150:10085/live/863065035752780_0001");
            } else {
//                client.stop();
//                client.play("rtmp://111.198.38.150:10085/live/869496039536917_0001");
            }
            qiehuan++;
        }
    }


    // touch移动视频窗口 | 事件拦截
    private class FloatingOnTouchListener implements View.OnTouchListener {
        private int x;
        private int y;
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();

                    isclick = false;//当按下的时候设置isclick为false

                    startTime = System.currentTimeMillis();
                    break;

                case MotionEvent.ACTION_MOVE:
                    isclick = true;//当按钮被移动的时候设置isclick为true
                    int nowX = (int) event.getRawX();
                    int nowY = (int) event.getRawY();
                    int movedX = nowX - x;
                    int movedY = nowY - y;
                    Log.d("悬浮窗", "movedX = " + movedX + ", movedY =" + movedY);
                    x = nowX;
                    y = nowY;
                    layoutParams.x = layoutParams.x + movedX;
                    layoutParams.y = layoutParams.y + movedY;
                    windowManager.updateViewLayout(view, layoutParams);
                    break;
                case MotionEvent.ACTION_UP:
                    endTime = System.currentTimeMillis();
                    //当从点击到弹起小于半秒的时候,则判断为点击,如果超过则不响应点击事件
                    if ((endTime - startTime) > 0.1 * 1000L) {
                        isclick = true;
                        Log.e("tag","拖动");
                    } else {
                        isclick = false;
                        Log.e("tag","点击");
                    }
                    System.out.println("执行顺序up");
                    break;
            }
            return isclick;
        }
    }

    @Override
    public void onDestroy() {
        // 移除浮动框
        if (windowManager != null) {
            windowManager.removeView(display);
        }
        super.onDestroy();
    }
}
//    //判断是否授权及开启服务
//    @RequiresApi(api = Build.VERSION_CODES.M)
//    public void startFloatingService() {
//        if (ActivityUtil.isServiceWork(this, "com.demon.suspensionbox.FloatingService")) {//防止重复启动
//            Toast.makeText(this, "已启动！", Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if (!Settings.canDrawOverlays(this)) {
//            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
//            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 0);
//        } else {
//            if (istui)
//                startService(new Intent(MainActivity.this, FloatingWindowService.class));
//        }
//    }
//
//    @SuppressLint("NewApi")
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // 判断获取权限是否成功
//        if (requestCode == 0) {
//            if (!Settings.canDrawOverlays(this)) {
//                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
//                startService(new Intent(MainActivity.this, FloatingWindowService.class));
//            }
//        }
//    }
//
//
//    //关闭服务
//    @Override
//    public void onDestroy() {
//        stopService(new Intent(MainActivity.this, FloatingWindowService.class));
//        super.onDestroy();
//    }