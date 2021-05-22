package com.xing.gfox.util;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class U_string {
    /**
     * 判断邮箱格式
     *
     * @param str 验证字符串
     * @return 是否邮箱
     */
    public static boolean isEmail(String str) {
        String check = "\\w+([-.]\\w+)*@\\w+([-]\\w+)*\\.(\\w+([-]\\w+)*\\.)*[a-z]{2,3}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(str);
        return matcher.matches();
    }

    /**
     * 去除字符串数字前面多余的0
     *
     * @param str
     * @return
     */
    public static String removeZeroFromString(String str) {
        String newStr = str.replaceFirst("^0*", "");
        if (newStr.subSequence(0, 1).equals(".")) {
            newStr = "0" + newStr;
        }
        return newStr;
    }

    /**
     * 隐藏手机号码中间4位
     */
    public static String getMobile(String str) {
        String top = str.substring(0, 3);
        String bottom = str.substring(7);
        return top + "****" + bottom;
    }

    /**
     * 判断是否是一个IP
     *
     * @param IP ip
     * @return boolean
     */
    public static boolean isIp(String IP) {
        boolean b = false;
        IP = trimSpaces(IP);
        if (IP.matches("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}")) {
            String[] s = IP.split("\\.");
            if (Integer.parseInt(s[0]) < 255)
                if (Integer.parseInt(s[1]) < 255)
                    if (Integer.parseInt(s[2]) < 255)
                        if (Integer.parseInt(s[3]) < 255)
                            b = true;
        }
        return b;
    }

    public static String trimSpaces(String IP) {// 去掉IP字符串前后所有的空格
        while (IP.startsWith(" ")) {
            IP = IP.substring(1).trim();
        }
        while (IP.endsWith(" ")) {
            IP = IP.substring(0, IP.length() - 1).trim();
        }
        return IP;
    }

    /**
     * 点击输入法中“下一个”将焦点与光标跳转到下一输入框中
     *
     * @param currentEt 当前的输入框
     * @param nextEt    下一个输入框
     */
    public static void setInputType(final EditText currentEt, final EditText nextEt) {
        currentEt.setSingleLine(true); // android:singleLine=”true”
        currentEt.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        currentEt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    currentEt.clearFocus();
                    nextEt.requestFocus();
                    return true;
                }
                return false;
            }
        });
    }

    public static boolean isHanZi(char c) {
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        Matcher matcher = pattern.matcher(String.valueOf(c));
        return matcher.matches();
    }

    public static boolean isEnglish(char c) {
        return String.valueOf(c).matches("^[a-zA-Z]*");
    }

    /**
     * 返回带参数的get请求url地址
     *
     * @param url    url
     * @param params 参数
     * @return 带参数的get请求url地址
     */
    public static String getURLWithParams(String url, Map<String, String> params) {
        return url + "?" + joinParam(params);
    }

    /**
     * 连接参数
     *
     * @param params 参数
     * @return 连接结果
     */
    public static StringBuffer joinParam(Map<String, String> params) {
        StringBuffer result = new StringBuffer();
        Iterator<Map.Entry<String, String>> iterator = params.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> param = iterator.next();
            String key = param.getKey();
            String value = param.getValue();
            result.append(key).append('=').append(value);
            if (iterator.hasNext()) {
                result.append('&');
            }
        }
        return result;
    }

    //获取url里的/后面的文件名
    public static String getFileNameFromUrl(String url) {
        int index = url.lastIndexOf("/");
        return url.substring(index + 1);
    }

    //判断是否是json数据
    public static boolean isJson(String str) {
        if (TextUtils.isEmpty(str)) return false;
        if (str.startsWith("{") && str.endsWith("}")) {
            try {
                U_gson.instance().fromJson(str, Object.class);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 判断是否是json结构
     */
    public static boolean isJson2(String value) {
        if (TextUtils.isEmpty(value)) return false;
        if (value.startsWith("{") || value.endsWith("}")) {
            try {
                new JSONObject(value);
            } catch (JSONException e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }
}
