package com.xing.gfox.util;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.Random;
import java.util.regex.Pattern;

public class U_math {
    //保留n位小数
    public static String RoundingDouble(String str, String doubleType) {
        DecimalFormat df = new DecimalFormat(doubleType);
        return df.format(Double.parseDouble(str));
    }

    /**
     * Double类型保留指定位数的小数，返回double类型（四舍五入）
     * newScale 为指定的位数
     */
    public static double formatDouble(double d, int newScale) {
        BigDecimal bd = new BigDecimal(d);
        return bd.setScale(newScale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    //获取随机数
    public static int getRandom(int first, int last) {
        int number;
        while (true) {
            number = (int) (Math.random() * 1000);
            if (number >= first && number < last) {
                break;
            }
        }
        return number;
    }

    public static float getRandom() {
        return new Random().nextFloat();
    }

    /**
     * 生成随机的num位的字符串(数字+字母)
     */
    public static String getRandomStr(int num) {
        StringBuilder builder = new StringBuilder();
        char[] chars = {
                '1', '2', '3', '4', '5', '6', '7', '8', '9', '0',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j',
                'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
                'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D',
                'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N',
                'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X',
                'Y', 'Z'};
//        int[] cha = new int[num];
        for (int i = 0; i < num; i++) {
            int j = (int) (Math.random() * 62);
            builder.append(chars[j]);
        }
        return builder.toString();
    }

    /**
     * 限制2位小数
     */
    public static void setEditText2D(final EditText etChangeAmt) {
        etChangeAmt.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable edt) {
                String temp = edt.toString();
                int posDot = temp.indexOf(".");
                if (posDot <= 0) return;
                if (temp.length() - posDot - 1 > 2) {
                    edt.delete(posDot + 3, posDot + 4);
                }

            }

            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void onTextChanged(CharSequence s, int arg1, int arg2, int arg3) {
                if (s.toString().trim().equals(".")) {
                    s = "0" + s;
                    etChangeAmt.setText(s);
                    etChangeAmt.setSelection(2);
                }
            }
        });
    }

    /**
     * 判断string是否是数字
     *
     * @param str 文本
     * @return 结果
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9]*");
        return pattern.matcher(str).matches();
    }

    /**
     * 十进制转二进制
     *
     * @param n 二进制数
     * @return 十进制
     */
    public static String Ten2Two(int n) {
        return Integer.toBinaryString(n);
    }

    /**
     * 二进制转十进制
     *
     * @param n 十进制数
     * @return 二进制
     */
    public static int Two2Ten(String n) {
        return Any2Ten(n, 2);//数字2代表进制，可以是各种进制，转换成10进制
    }

    public static int Any2Ten(String n, int radix) {
        return Integer.parseInt(n, radix);//数字2代表进制，可以是各种进制，转换成10进制
    }

    /**
     * 十进制转十六进制
     */
    private static String intToHex(int n) {
        StringBuffer s = new StringBuffer();
        String a;
        char[] b = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        while (n != 0) {
            s = s.append(b[n % 16]);
            n = n / 16;
        }
        a = s.reverse().toString();
        return a;
    }
}
