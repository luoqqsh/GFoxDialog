package com.xing.gfox.base.toast;

import android.util.Log;

import com.xing.gfox.base.toast.config.IToastInterceptor;

/**
 * desc   : 自定义 Toast 拦截器（用于追踪 Toast 调用的位置）
 */
public final class ToastInterceptor implements IToastInterceptor {

    @Override
    public boolean intercept(CharSequence text) {
        // 获取调用的堆栈信息
        StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        // 跳过最前面两个堆栈
        for (int i = 2; stackTrace.length > 2 && i < stackTrace.length; i++) {
            // 获取代码行数
            int lineNumber = stackTrace[i].getLineNumber();
            // 获取类的全路径
            String className = stackTrace[i].getClassName();
            if (lineNumber <= 0 || className.startsWith(U_Toast.class.getName())) {
                continue;
            }

            Log.d(U_Toast.class.getName(), "(" + stackTrace[i].getFileName() + ":" + lineNumber + ") " + text.toString());
            break;
        }
        return false;
    }
}