package com.xing.gfox.base.app;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.xing.gfox.base.interfaces.HOnListener;
import com.xing.gfox.base.toast.ToastInterceptor;
import com.xing.gfox.base.toast.U_Toast;
import com.xing.gfox.hardware.lockScreen.ScreenBroadcastReceiver;
import com.xing.gfox.hardware.netWork.NetWorkCallBack;
import com.xing.gfox.hardware.netWork.NetworkStateReceive;
import com.xing.gfox.hardware.netWork.ToolsNetState;
import com.xing.gfox.log.ViseLog;
import com.xing.gfox.log.inner.LogcatTree;
import com.xing.gfox.util.U_screen;
import com.xing.gfox.util.U_thread;

import com.xing.gfox.hardware.battery.BatteryChangedReceiver;
import com.xing.gfox.util.U_device;

public class AppInit {
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;
    public static boolean isDebug = false;
    private static volatile Application sApplication;

    public static void setContext(Context mContext) {
        AppInit.mContext = mContext.getApplicationContext();
    }

    public static Context getAppContext() {
        if (mContext == null) {
            ViseLog.e("context初始化异常");
        }
        return mContext.getApplicationContext();
    }

    public static void initToast(Application application) {
        if (U_thread.isRunInUIThread()) {
            U_Toast.init(application);
            U_Toast.setGravity(Gravity.BOTTOM, 0, 250);
            // 设置 Toast 拦截器
            if (isDebug) {
                U_Toast.setInterceptor(new ToastInterceptor());
            }
        }
    }

    public static Application getApplicationByInvoke() {
        if (sApplication == null) {
            synchronized (AppInit.class) {
                if (sApplication == null) {
                    try {
                        sApplication = (Application) Class.forName("android.app.ActivityThread")
                                .getMethod("currentApplication")
                                .invoke(null, (Object[]) null);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new NullPointerException("u should init first");
                    }
                }
            }
        }
        return sApplication;
    }

    /**
     * 输出设备信息
     */
    public static void printInformation() {
        printInformation(mContext);
    }

    public static void printInformation(Context context) {
        if (U_thread.isRunInUIThread()) {
            ViseLog.d(U_screen.getScreenInfo(context));
            ViseLog.d(U_device.INSTANCE.getDeviceInfo(context));
//            ViseLog.d(U_device.INSTANCE.getDeviceInfoDetail(context));
            BatteryChangedReceiver.getInstance().register(context);
            ScreenBroadcastReceiver.getInstance().register(context);
            NetworkStateReceive.getInstance().register(context);
            NetWorkCallBack.getInstance().registerNetworkCallback(context);
            ToolsNetState.printWifiInfo(context);
        }
    }

    /**
     * 日志配置
     */
    public static void initLog() {
        initLog(3);
    }

    public static void initLog(int tagCount) {
        if (U_thread.isRunInUIThread()) {
            ViseLog.getLogConfig()
                    .configAllowLog(isDebug)//是否输出日志
                    .configShowBorders(true)//是否排版显示
                    .configTagPrefix("ViseLog")//设置标签前缀
                    .configFormatTag("%d{HH:mm:ss:SSS} %t %c{-" + tagCount + "} ")//个性化设置标签，默认显示包名
                    .configLevel(Log.VERBOSE);//设置日志最小输出级别，默认Log.VERBOSE
            ViseLog.plant(new LogcatTree());//添加打印日志信息到Logcat的树
        }
    }

    public static void setNeverCrash() {
        setNeverCrash(null);
    }

    public static void setNeverCrash(HOnListener<Throwable> onListener) {
        if (U_thread.isRunInUIThread()) {
            NeverCrashHelper.init((t, e) -> {
//                ViseLog.e(e.getMessage());
                e.printStackTrace();
                new Handler(Looper.getMainLooper()).post(() -> {
                    if (onListener != null) {
                        onListener.onListen(e);
                    } else {
                        Toast.makeText(mContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            });
        }
    }
}
