package com.xing.gfox.view.EditText;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import java.util.regex.Pattern;

public class DecimalInputTextWatcher implements TextWatcher {
    private Pattern mPattern;

    /**
     * 不限制整数位数和小数位数
     */
    public DecimalInputTextWatcher() {
    }

    /**
     * 限制整数位数或着限制小数位数
     *
     * @param type   限制类型
     * @param number 限制位数
     */
    public DecimalInputTextWatcher(Type type, int number) {
        if (type == Type.decimal) {
            mPattern = Pattern.compile("^[0-9]+(\\.[0-9]{0," + number + "})?$");
        } else if (type == Type.integer) {
            mPattern = Pattern.compile("^[0-9]{0," + number + "}+(\\.[0-9]{0,})?$");
        }
    }

    /**
     * 既限制整数位数又限制小数位数
     *
     * @param integers 整数位数
     * @param decimals 小数位数
     */

    public DecimalInputTextWatcher(int integers, int decimals) {
        mPattern = Pattern.compile("^[0-9]{0," + integers + "}+(\\.[0-9]{0," + decimals + "})?$");
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        String text = editable.toString();
        if (TextUtils.isEmpty(text)) return;
        if ((editable.length() > 1) && (editable.charAt(0) == '0') && editable.charAt(1) != '.') {   //删除整数首位的“0”
            editable.delete(0, 1);
            return;
        }
        if (text.equals(".")) {//首位是“.”自动补“0”
            editable.insert(0, "0");
            return;
        }
        if (mPattern != null && !mPattern.matcher(text).matches() && editable.length() > 0) {
            editable.delete(editable.length() - 1, editable.length());
            return;
        }
        //TODO：可在此处额外添加代码
    }

    public enum Type {
        integer, decimal
    }

    //不限制整数位数和小数位数
    //editText.addTextChangedListener(new DecimalInputTextWatcher());
    //不限制整数位数，限制小数位数为2位
    //editText.addTextChangedListener(new DecimalInputTextWatcher(DecimalInputTextWatcher.Type.decimal, 2));
    //限制整数位数为4位，不限制小数位数
    //editText.addTextChangedListener(new DecimalInputTextWatcher(DecimalInputTextWatcher.Type.integer, 4));
    //限制整数位数为4位，小数位数为2位
    //editText.addTextChangedListener(new DecimalInputTextWatcher( 4, 2));
}
