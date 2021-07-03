package com.xing.gfox.util;

import android.content.Context;
import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Formatter;
import java.util.Locale;

import com.xing.gfox.R;
import com.xing.gfox.util.model.TimeConstants;

public class U_time {
    public static final String yyyy_MM_ddHH_mm_ssSSS = "yyyy-MM-dd HH:mm:ss SSS";
    public static final String yyyy_MM_ddHH_mm_ss = "yyyy-MM-dd HH:mm:ss";
    public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
    public static final String yyyyMMdd_HHmmss = "yyyyMMdd_HHmmss";
    public static final String yyyy_MM_dd = "yyyy-MM-dd";
    public static final String yyyyMMdd = "yyyyMMdd";
    public static final String HHmmss = "HHmmss";
    public static final String HH_mm_ss = "HH:mm:ss";

    /**
     * 时间戳格式化成指定格式
     *
     * @param time     时间戳
     * @param timeType 要得到的时间格式
     * @return 转换后的时间
     */
    public static String convertLongToTime(long time, String timeType) {
        SimpleDateFormat df = new SimpleDateFormat(timeType, Locale.CHINA);
        Date date = new Date(time);
        return df.format(date);
    }

    /**
     * 输入时间00:00:00，获取秒数
     *
     * @param time 00:00:00
     * @return 秒数
     */
    public static long getSecond(String time) {
        long s = 0;
        if (time.length() == 8) { //时分秒格式00:00:00
            int index1 = time.indexOf(":");
            int index2 = time.indexOf(":", index1 + 1);
            s = Integer.parseInt(time.substring(0, index1)) * 3600L;//小时
            s += Integer.parseInt(time.substring(index1 + 1, index2)) * 60L;//分钟
            s += Integer.parseInt(time.substring(index2 + 1));//秒
        }
        if (time.length() == 5) {//分秒格式00:00
            s = Integer.parseInt(time.substring(time.length() - 2)); //秒  后两位肯定是秒
            s += Integer.parseInt(time.substring(0, 2)) * 60L;    //分钟
        }
        return s;
    }

    /**
     * 时间格式转换，如20190101 转成 2019-01-01
     *
     * @param time      时间
     * @param timeType1 需要转换的时间格式
     * @param timeType2 要得到的时间格式
     * @return 转换后的时间
     * @throws ParseException 异常
     */
    public static String convertToTime(String time, String timeType1, String timeType2) throws ParseException {
        SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat(timeType1, Locale.CHINA);
        SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(timeType2, Locale.CHINA);
        Date dt = simpleDateFormat1.parse(time);
        return simpleDateFormat2.format(dt != null ? dt : "");
    }

    /**
     * 将秒数转为xx小时xx分钟xx秒
     *
     * @param second 秒数
     * @return 结果
     */
    public static String getFriendlyTime(int second) {
        return getFriendlyTime(null, second);
    }

    public static String getFriendlyTime(Context context, int second) {
        if (second > 3600) {
            int hour = second / 3600;
            int minute = (second % 3600) / 60;
            if (context != null)
                return hour + context.getString(R.string.time_hour) + minute + context.getString(R.string.time_minute);
            return hour + "小时" + minute + "分钟";
        }
        if (second >= 60) {
            int minute = second / 60;
            if (context != null)
                return minute + context.getString(R.string.time_minute);
            return minute + "分钟";
        }
        if (context != null)
            return second + context.getString(R.string.time_second);
        return second + "秒";
    }

    /**
     * 获取当前时间戳
     *
     * @return long时间戳
     */
    public static long getNowTimeLong() {
        return new Date().getTime();
    }

    /**
     * 获取现在时间
     *
     * @param timeType 日期格式，例如yyyy-MM-dd HH:mm:ss SSS
     * @return 今天时间
     */
    public static String getNowTimeStr(String timeType) {
        Calendar calendar = Calendar.getInstance();
        Date tempDate = new Date();
        calendar.setTime(tempDate);
        SimpleDateFormat sdf = new SimpleDateFormat(timeType, Locale.CHINA);
        return sdf.format(calendar.getTime());
    }

    /**
     * 指定日期转时间戳
     *
     * @param timeStr  指定日期
     * @param timeType 日期格式
     * @return 时间戳
     */
    public static long getLongFromTimeStr(String timeType, String timeStr) {
        if (TextUtils.isEmpty(timeStr)) return 0;
        try {
            SimpleDateFormat df = new SimpleDateFormat(timeType, Locale.CHINA);
            Date parse = df.parse(timeStr);
            if (parse == null) return 0;
            return parse.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 是否是同一天
     *
     * @param aMills 时间1
     * @param bMills 时间2
     * @return 结果
     */
    public static boolean isSameDay(long aMills, long bMills) {
        String aDay = convertLongToTime(aMills, yyyyMMdd);
        String bDay = convertLongToTime(bMills, yyyyMMdd);
        return aDay.equals(bDay);
    }

    /**
     * 获取几天后日期
     *
     * @param timeType 日期格式，例如yyyy-MM-dd HH:mm:ss SSS
     * @param amount   明天为1，后天为2，以此类推
     * @return 日期
     */
    public static String getTomoData(String timeType, int amount) {
        Calendar calendar = Calendar.getInstance();
        Date tempDate = new Date();
        calendar.setTime(tempDate);
        calendar.add(Calendar.DAY_OF_MONTH, amount);
        SimpleDateFormat sdf = new SimpleDateFormat(timeType, Locale.CHINA);
        return sdf.format(calendar.getTime());
    }

    /**
     * 将毫秒数格式化为"##:##"的时间
     *
     * @param milliseconds 毫秒数
     * @return ##:##
     */
    public static String formatTime(long milliseconds) {
        if (milliseconds <= 0 || milliseconds >= 24 * 60 * 60 * 1000) {
            return "00:00";
        }
        long totalSeconds = milliseconds / 1000;
        long seconds = totalSeconds % 60;
        long minutes = (totalSeconds / 60) % 60;
        long hours = totalSeconds / 3600;
        StringBuilder stringBuilder = new StringBuilder();
        Formatter mFormatter = new Formatter(stringBuilder, Locale.getDefault());
        if (hours > 0) {
            return mFormatter.format("%d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return mFormatter.format("%02d:%02d", minutes, seconds).toString();
        }
    }

    /*获取星期几*/
    public static String getWeek() {
        Calendar cal = Calendar.getInstance();
        int i = cal.get(Calendar.DAY_OF_WEEK);
        switch (i) {
            case 1:
                return "星期日";
            case 2:
                return "星期一";
            case 3:
                return "星期二";
            case 4:
                return "星期三";
            case 5:
                return "星期四";
            case 6:
                return "星期五";
            case 7:
                return "星期六";
            default:
                return "";
        }
    }

    /**
     * 是否今天第一次
     *
     * @param context 上下文
     * @param key     关键字
     * @param isSave  是否保存
     * @return 结果
     */
    private static boolean isTodayFirst(Context context, String key, boolean isSave) {
        String currentActivityDate = getNowTimeStr(yyyyMMdd);
        String activityDate = U_sharedP.getParam(context, key + "activity_date", "");
        if (!activityDate.equals(currentActivityDate)) {
            if (isSave) {
                U_sharedP.setParam(context, key + "activity_date", currentActivityDate);
            }
            return true;
        }
        return false;
    }

    /**
     * Time span in unit to milliseconds.
     *
     * @param timeSpan The time span.
     * @param unit     The unit of time span.
     *                 <ul>
     *                 <li>{@link TimeConstants#MSEC}</li>
     *                 <li>{@link TimeConstants#SEC }</li>
     *                 <li>{@link TimeConstants#MIN }</li>
     *                 <li>{@link TimeConstants#HOUR}</li>
     *                 <li>{@link TimeConstants#DAY }</li>
     *                 </ul>
     * @return milliseconds
     */
    public static long timeSpan2Millis(long timeSpan, @TimeConstants.Unit int unit) {
        return timeSpan * unit;
    }

    /**
     * Milliseconds to time span in unit.
     *
     * @param millis The milliseconds.
     * @param unit   The unit of time span.
     *               <ul>
     *               <li>{@link TimeConstants#MSEC}</li>
     *               <li>{@link TimeConstants#SEC }</li>
     *               <li>{@link TimeConstants#MIN }</li>
     *               <li>{@link TimeConstants#HOUR}</li>
     *               <li>{@link TimeConstants#DAY }</li>
     *               </ul>
     * @return time span in unit
     */
    public static long millis2TimeSpan(long millis, @TimeConstants.Unit int unit) {
        return millis / unit;
    }

    /**
     * 将秒数转换为日时分秒，
     *
     * @param second 秒数
     * @return
     */
    public static String secondToTime(long second) {
        long days = second / 86400;         //转换天数
        second = second % 86400;            //剩余秒数
        long hours = second / 3600;         //转换小时
        second = second % 3600;             //剩余秒数
        long minutes = second / 60;         //转换分钟
        second = second % 60;               //剩余秒数
        if (days > 0) {
            return days + "天" + hours + "小时" + minutes + "分" + second + "秒";
        } else if (hours > 0) {
            return hours + "小时" + minutes + "分" + second + "秒";
        } else if (minutes > 0) {
            return minutes + "分" + second + "秒";
        } else {
            return second + "秒";
        }
    }

    /**
     * 按照分钟 格式化为 小时 ：分钟
     */
    public static String[] formatHomeTime(long walkTime) {
        String hour = "00";
        String min = "00";
        if (walkTime > 60) {
            long h = walkTime / 60;
            if (h >= 10) {
                hour = h + "";
            } else {
                hour = "0" + h;
            }
            //剩余分钟
            long m = walkTime - 60 * h;

            if (m >= 10) {
                min = m + "";

            } else {
                min = "0" + m;
            }
        } else if (walkTime < 10) {
            min = "0" + walkTime;
        } else {
            min = "" + walkTime;

        }
        return new String[]{hour, min};
    }

    public static long getBetweenDays(long date1, long date2) {
        return (Math.abs(date2 - date1)) / (24 * 60 * 60 * 1000);
    }
}
