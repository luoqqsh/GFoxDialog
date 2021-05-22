package com.xing.gfox.base.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.xing.gfox.log.ViseLog;

import io.reactivex.Completable;
import io.reactivex.schedulers.Schedulers;

public class StepService extends Service implements SensorEventListener {
    //与activity通信
    private StepBinder stepBinder = new StepBinder();
    private SensorManager sensorManager;
    private int stepSensorType;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            //数字是随便写的“40”，
            nm.createNotificationChannel(new NotificationChannel("40", "App Service", NotificationManager.IMPORTANCE_DEFAULT));
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "40");
            //其中的2，是也随便写的，正式项目也是随便写
            startForeground(2, builder.build());
        }
        //只有完成和错误事件的rxjava
        Completable.fromAction(this::startStepDetector).subscribeOn(Schedulers.io()).subscribe();
    }

    //TYPE_STEP_COUNTER计步
    //TYPE_STEP_DETECTOR走路检测
    private void startStepDetector() {
        if (sensorManager != null) {
            sensorManager = null;
        }
        // 获取传感器管理器的实例
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        //android4.4以后可以使用计步传感器
        if (Build.VERSION.SDK_INT >= 19) {
            Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            Sensor detectorSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_DETECTOR);
            if (countSensor != null) {
                stepSensorType = Sensor.TYPE_STEP_COUNTER;
                ViseLog.d("Sensor.TYPE_STEP_COUNTER");
                sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else if (detectorSensor != null) {
                stepSensorType = Sensor.TYPE_STEP_DETECTOR;
                ViseLog.d("Sensor.TYPE_STEP_DETECTOR");
                sensorManager.registerListener(this, detectorSensor, SensorManager.SENSOR_DELAY_NORMAL);
            } else {
                ViseLog.d("Count sensor not available!");
                //不通过加速传感器计步
//            addBasePedometerListener();
            }
        }
    }

    public class StepBinder extends Binder {
        public StepService getService() {
            return StepService.this;
        }
    }

    public static void startStepService(Context context) {
        Intent todayStepIntent = new Intent(context, StepService.class);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(todayStepIntent);
        } else {
            context.startService(todayStepIntent);
        }
    }

    public static void stopStepService() {

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return stepBinder;
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if (stepSensorType == Sensor.TYPE_STEP_COUNTER) {
            //获取系统自开机后的步数
            int tempStep = (int) event.values[0];
            ViseLog.d(tempStep);
        } else if (stepSensorType == Sensor.TYPE_STEP_DETECTOR) {
            ViseLog.d((int) event.values[0]);
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
