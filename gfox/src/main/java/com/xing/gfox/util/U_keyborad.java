package com.xing.gfox.util;

import android.content.Context;
import android.text.Editable;
import android.text.Selection;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

public class U_keyborad {
    /**
     * 隐藏输入框
     *
     * @param view view
     */
    public static void hideSoftInput(View view) {
        if (view != null && view.getWindowToken() != null) {
            InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static boolean isInputShow(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        return imm.isActive();
    }

    /**
     * 显示输入框
     *
     * @param view view
     */
    public static void showSoftInput(final View view) {
        showSoftInput(view, 200);
    }

    /**
     * 显示输入框
     *
     * @param view  view
     * @param delay 延时 毫秒
     */
    public static void showSoftInput(final View view, int delay) {
        view.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                InputMethodManager m = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                m.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }, delay);
    }

    /**
     * 移动光标到文本框最后
     *
     * @param editText EditText
     */
    public static void moveSelectionToEnd(EditText editText) {
        Editable etext = editText.getText();
        Selection.setSelection(etext, etext.length());
    }
}
