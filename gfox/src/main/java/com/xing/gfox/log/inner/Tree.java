package com.xing.gfox.log.inner;

import android.text.TextUtils;
import android.util.Log;

import com.xing.gfox.log.ViseLog;
import com.xing.gfox.log.common.LogConstant;
import com.xing.gfox.log.common.LogConvert;
import com.xing.gfox.log.config.LogDefaultConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 * @Description: 日志树功能实现
 */
public abstract class Tree implements ITree {

    private LogDefaultConfig mLogConfig;
    private final ThreadLocal<String> localTags = new ThreadLocal<>();
    private boolean isDefaultBolder = true;//是否默认边框设置，否则强制不带边框
    private boolean isShowEasy = false;//是否简单显示，仅showInfo才简单显示


    protected Tree() {
        mLogConfig = LogDefaultConfig.getInstance();
        mLogConfig.addParserClass(LogConstant.DEFAULT_PARSE_CLASS);
    }

    public ITree setTag(String tag) {
        if (!TextUtils.isEmpty(tag) && mLogConfig.isEnable()) {
            localTags.set(tag);
        }
        return this;
    }

    @Override
    public void wtf(String message, Object... args) {
        isDefaultBolder = true;
        isShowEasy = false;
        logString(Log.ASSERT, message, args);
    }

    @Override
    public void wtf(Object object) {
        isDefaultBolder = true;
        isShowEasy = false;
        logObject(Log.ASSERT, object);
    }

    @Override
    public void e(String message, Object... args) {
        isDefaultBolder = true;
        isShowEasy = false;
        logString(Log.ERROR, message, args);
    }

    @Override
    public void e(Object object) {
        isDefaultBolder = true;
        isShowEasy = false;
        logObject(Log.ERROR, object);
    }

    @Override
    public void showLog(Object object) {
        isDefaultBolder = false;
        isShowEasy = false;
        logObject(Log.ERROR, object);
    }

    @Override
    public void showInfo(Object object) {
        isDefaultBolder = false;
        isShowEasy = true;
        logObject(Log.ERROR, object);
    }

    @Override
    public void w(String message, Object... args) {
        isDefaultBolder = true;
        isShowEasy = false;
        logString(Log.WARN, message, args);
    }

    @Override
    public void w(Object object) {
        isDefaultBolder = true;
        isShowEasy = false;
        logObject(Log.WARN, object);
    }

    @Override
    public void d(String message, Object... args) {
        isDefaultBolder = true;
        isShowEasy = false;
        logString(Log.DEBUG, message, args);
    }

    @Override
    public void d(Object object) {
        isDefaultBolder = true;
        isShowEasy = false;
        logObject(Log.DEBUG, object);
    }

    @Override
    public void i(String message, Object... args) {
        isDefaultBolder = true;
        isShowEasy = false;
        logString(Log.INFO, message, args);
    }

    @Override
    public void i(Object object) {
        isDefaultBolder = true;
        isShowEasy = false;
        logObject(Log.INFO, object);
    }

    @Override
    public void v(String message, Object... args) {
        isDefaultBolder = true;
        isShowEasy = false;
        logString(Log.VERBOSE, message, args);
    }

    @Override
    public void v(Object object) {
        isDefaultBolder = true;
        isShowEasy = false;
        logObject(Log.VERBOSE, object);
    }

    @Override
    public void json(String json) {
        int indent = 4;
        if (TextUtils.isEmpty(json)) {
            d("JSON{json is empty}");
            return;
        }
        try {
            if (json.startsWith("{")) {
                JSONObject jsonObject = new JSONObject(json);
                String msg = jsonObject.toString(indent);
                d(msg);
            } else if (json.startsWith("[")) {
                JSONArray jsonArray = new JSONArray(json);
                String msg = jsonArray.toString(indent);
                d(msg);
            }
        } catch (JSONException e) {
            e(e.toString() + "\n\njson = " + json);
        }
    }

    @Override
    public void xml(String xml) {
        if (TextUtils.isEmpty(xml)) {
            d("XML{xml is empty}");
            return;
        }
        try {
            Source xmlInput = new StreamSource(new StringReader(xml));
            StreamResult xmlOutput = new StreamResult(new StringWriter());
            Transformer transformer = TransformerFactory.newInstance().newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            transformer.transform(xmlInput, xmlOutput);
            d(xmlOutput.getWriter().toString().replaceFirst(">", ">\n"));
        } catch (TransformerException e) {
            e(e.toString() + "\n\nxml = " + xml);
        }
    }

    private void logObject(int type, Object object) {
        logString(type, LogConvert.objectToString(object));
    }

    private synchronized void logString(int type, String msg, Object... args) {
        logString(type, msg, false, args);
    }

    private void logString(int type, String msg, boolean isPart, Object... args) {
        if (!mLogConfig.isEnable()) {//判定是否显示日志
            return;
        }
        if (type < mLogConfig.getLogLevel()) {//判断日志显示最小级别
            return;
        }
        String tag = generateTag();
        //判断信息是否超过一行最大显示
        if (msg.length() > LogConstant.LINE_MAX) {//超过一行
            if (mLogConfig.isShowBorder()) {
                printLog(type, tag, LogConvert.printDividingLine(LogConstant.DIVIDER_TOP));
                printLog(type, tag, LogConvert.printDividingLine(LogConstant.DIVIDER_NORMAL) + getTopStackInfo());
                printLog(type, tag, LogConvert.printDividingLine(LogConstant.DIVIDER_CENTER));
            }
            for (String subMsg : LogConvert.largeStringToList(msg)) {
                logString(type, subMsg, true, args);
            }
            if (mLogConfig.isShowBorder()) {
                printLog(type, tag, LogConvert.printDividingLine(LogConstant.DIVIDER_BOTTOM));
            }
            return;
        }
        if (args.length > 0) {//有格式化参数
            //类似C语言的写法，msg带百分号类型，对应args里的参数
//            try {
//                msg = String.format(msg, args);
//            } catch (MissingFormatArgumentException e) {
//                e.printStackTrace();
//            }
            //安卓上还是用纯组合的做吧
            StringBuilder msgBuilder = new StringBuilder(msg);
            for (Object arg : args) {
                msgBuilder.append(",").append(arg);
            }
            msg = msgBuilder.toString();
        }
        //判断是否显示排版线条
        if (mLogConfig.isShowBorder() && isDefaultBolder) {//显示排版线条
            //判定是否需要分段显示
            if (isPart) {//需要分段显示
                for (String sub : msg.split(LogConstant.BR)) {
                    printLog(type, tag, LogConvert.printDividingLine(LogConstant.DIVIDER_NORMAL) + sub);
                }
            } else {//不需要分段显示
                printLog(type, tag, LogConvert.printDividingLine(LogConstant.DIVIDER_TOP));
                printLog(type, tag, LogConvert.printDividingLine(LogConstant.DIVIDER_NORMAL) + getTopStackInfo());
                printLog(type, tag, LogConvert.printDividingLine(LogConstant.DIVIDER_CENTER));
                for (String sub : msg.split(LogConstant.BR)) {
                    printLog(type, tag, LogConvert.printDividingLine(LogConstant.DIVIDER_NORMAL) + sub);
                }
                printLog(type, tag, LogConvert.printDividingLine(LogConstant.DIVIDER_BOTTOM));
            }
        } else {//直接显示
            printLog(type, tag, msg);
        }
    }

    /**
     * 生成标签
     *
     * @return
     */
    private String generateTag() {
        String tempTag = localTags.get();
        if (!TextUtils.isEmpty(tempTag)) {
            localTags.remove();
            return tempTag;
        }
        return mLogConfig.getTagPrefix();
    }

    /**
     * 获取当前堆栈信息
     *
     * @return
     */
    private StackTraceElement getCurrentStackTrace() {
        StackTraceElement[] trace = Thread.currentThread().getStackTrace();
        int stackOffset = getStackOffset(trace, ViseLog.class);
        if (stackOffset == -1) {
            return null;
        }
        return trace[stackOffset];
    }

    /**
     * 获取顶部堆栈信息
     *
     * @return
     */
    private String getTopStackInfo() {
        String customTag = mLogConfig.getFormatTag(getCurrentStackTrace());
        if (customTag != null) {
            return customTag;
        }
        StackTraceElement caller = getCurrentStackTrace();
        if (caller == null) {
            return "";
        }
        String stackTrace = caller.toString();
        stackTrace = stackTrace.substring(stackTrace.lastIndexOf('('), stackTrace.length());
        String tag = "%s.%s%s";
        String callerClazzName = caller.getClassName();
        callerClazzName = callerClazzName.substring(callerClazzName.lastIndexOf(".") + 1);
        tag = String.format(tag, callerClazzName, caller.getMethodName(), stackTrace);
        return tag;
    }

    /**
     * 获取堆栈信息下标
     *
     * @param trace
     * @param cla
     * @return
     */
    private int getStackOffset(StackTraceElement[] trace, Class cla) {
        for (int i = LogConstant.MIN_STACK_OFFSET; i < trace.length; i++) {
            StackTraceElement e = trace[i];
            String name = e.getClassName();
            if (cla.equals(ViseLog.class) && i < trace.length - 1 && trace[i + 1].getClassName()
                    .equals(ViseLog.class.getName())) {
                continue;
            }
            if (name.equals(cla.getName())) {
                return ++i;
            }
        }
        return -1;
    }

    /**
     * 输出日志
     *
     * @param type
     * @param tag
     * @param msg
     */
    private void printLog(int type, String tag, String msg) {
        if (isShowEasy) {
            log(type, tag, msg);
            return;
        }
        if (!mLogConfig.isShowBorder() || !isDefaultBolder) {
            msg = getTopStackInfo() + ": " + msg;
        }
        log(type, tag, msg);
    }

    /**
     * 日志具体实现方式，可以是打印、文件存储等
     *
     * @param type
     * @param tag
     * @param message
     */
    protected abstract void log(int type, String tag, String message);
}
