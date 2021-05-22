package com.xing.gfox.dialog;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.xing.gfox.R;
import com.xing.gfox.base.dialog.BaseNDialog;
import com.xing.gfox.base.interfaces.OnClickItemListener;
import com.xing.gfox.log.ViseLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class CTipDialog extends BaseNDialog {
    /**
     * 对话框模版样式
     */
    public static final int nDialog_Iphone1 = R.layout.cdialog_iphone_tip_1;
    public static final int nDialog_Iphone2 = R.layout.cdialog_iphone_tip_2;
    public static final int nDialog_Iphone_Scroll = R.layout.ndialog_iphone_scroll;
    public static final int nDialog_ListFile = R.layout.ndialog_listfile;
    public static final int ndialog_Trans = R.layout.ndialog_trans;
    public static final int ndialog_Input = R.layout.ndialog_input;

    private int layoutId, dialogAnim = 0;
    private float clickAlpha = 0.8f;//点击透明度
    private boolean outside, cancelable;
    private int location;//显示位置
    private float widthScale = 0.8f;
    public boolean isShowClose;
    private String title, message;
    private CharSequence titleChar;
    private int titlePosition, messagePosition;
    private List<String> btnText = new ArrayList<>();
    private OnClickItemListener btnClickListener;
    private TextView dialogMsg;
    private EditText nDialogInput;

    public CTipDialog(int layoutId) {
        this.layoutId = layoutId;
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void initUI(View mView, Bundle bundle) {
        if (isShowClose) {
            ImageView nDialogClose = mView.findViewById(R.id.nDialogClose);
            if (nDialogClose != null) {
                nDialogClose.setVisibility(View.VISIBLE);
                nDialogClose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dismiss();
                    }
                });
            } else {
                ViseLog.e("找不到Id：nDialogClose");
            }
        }
        TextView dialogTitle = mView.findViewById(R.id.nDialogTitle);
        if (dialogTitle != null) {
            if (!TextUtils.isEmpty(title)) {
                dialogTitle.setText(title);
                dialogTitle.setVisibility(View.VISIBLE);
                dialogTitle.setGravity(titlePosition);
            } else if (titleChar != null) {
                dialogTitle.setText(titleChar);
                dialogTitle.setVisibility(View.VISIBLE);
                dialogTitle.setGravity(titlePosition);
            } else {
                dialogTitle.setVisibility(View.GONE);
            }
        } else {
            ViseLog.e("找不到Id：nDialogTitle");
        }
        dialogMsg = mView.findViewById(R.id.nDialogMessage);
        if (dialogMsg != null) {
            if (!TextUtils.isEmpty(message)) {
                dialogMsg.setText(message);
                dialogMsg.setVisibility(View.VISIBLE);
                dialogMsg.setGravity(messagePosition);
            } else {
                dialogMsg.setVisibility(View.GONE);
            }
        } else {
            ViseLog.e("找不到Id：nDialogMessage");
        }
        nDialogInput = mView.findViewById(R.id.nDialogInput);
        // 设置确认按钮
        for (int i = 0; i < btnText.size(); i++) {
            final TextView nDialogBtn = mView.findViewWithTag("nDialogBtn" + (i + 1));

            if (nDialogBtn == null) {
                ViseLog.showLog("找不到TAG：nDialogBtn" + (i + 1));
            } else {
                nDialogBtn.setText(btnText.get(i));
                nDialogBtn.setVisibility(View.VISIBLE);
                // 给按钮绑定监听器
                int finalI = i;
                nDialogBtn.setOnClickListener(v -> {
                    if (btnClickListener != null) {
                        if (btnClickListener.onItemClick(nDialogBtn.getText().toString(), finalI + 1)) {
                            dismiss();
                        }
                    } else {
                        dismiss();
                    }
                });
                nDialogBtn.setOnTouchListener((view, motionEvent) -> {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            nDialogBtn.setAlpha(clickAlpha);
                            break;
                        case MotionEvent.ACTION_UP:
                            nDialogBtn.setAlpha(1f);
                            break;
                    }
                    return false;
                });
            }
        }
    }

    @Override
    protected int getLayoutId() {
        return layoutId;
    }

    @Override
    public float getWidthScale() {
        return widthScale;
    }

    @Override
    public boolean isCancelable() {
        return cancelable;
    }

    @Override
    public boolean isCanceledOnTouchOutside() {
        return outside;
    }

    //属性设置
    public CTipDialog setTouchOutSideCancelable(boolean outside, boolean cancelable) {
        this.outside = outside;
        this.cancelable = cancelable;
        return this;
    }

    public CTipDialog setWidthScale(float widthScale) {
        this.widthScale = widthScale;
        return this;
    }

    /**
     * 设置显示关闭按钮
     * isShowClose 是否显示关闭按钮
     *
     * @return this
     */
    public CTipDialog isShowClose(boolean isShowClose) {
        this.isShowClose = isShowClose;
        return this;
    }

    /**
     * 设置对话框标题
     *
     * @param title 标题
     * @return this
     */
    public CTipDialog setTitle(String title, int position) {
        this.title = title;
        this.titlePosition = position;
        return this;
    }

    public CTipDialog setTitle(CharSequence titleChar, int position) {
        this.titleChar = titleChar;
        this.titlePosition = position;
        return this;
    }

    /**
     * 设置对话框的消息内容
     *
     * @param message  消息内容
     * @param position 文字对齐方式
     * @return this
     */
    public CTipDialog setMessage(String message, int position) {
        this.message = message;
        this.messagePosition = position;
        return this;
    }

    public void refresh(String text, int position) {
        if (dialogMsg != null) {
            dialogMsg.setText(text);
            dialogMsg.setGravity(position);
        }
    }

    /**
     * 设置对话框的位置
     *
     * @param location 位置。例如Gravity.Center
     * @return this
     */
    public CTipDialog setDialogLocation(int location) {
        this.location = location;
        return this;
    }

    /**
     * 设置对话框的位置
     *
     * @param dialogAnim 动画
     * @return this
     */
    public CTipDialog setDialogAnim(int dialogAnim) {
        this.dialogAnim = dialogAnim;
        return this;
    }

    public String getInputText() {
        if (nDialogInput != null) {
            return nDialogInput.getText().toString();
        } else {
            ViseLog.e("找不到输入框控件，检查控件id是否是nDialogInput");
            return "";
        }
    }

    @Override
    public int getGravity() {
        return location;
    }

    @Override
    public int getDialogAnim() {
        return dialogAnim;
    }

    //一个按钮
    public CTipDialog setBtnClickListener(String btn1text,
                                          final OnClickItemListener btnClickListener) {
        return this.setClickListener(btn1text, null, null, null, null, btnClickListener);
    }

    //两个按钮
    public CTipDialog setBtnClickListener(String btn1text, String btn2text,
                                          final OnClickItemListener btnClickListener) {
        return this.setClickListener(btn1text, btn2text, null, null, null, btnClickListener);
    }

    //三个按钮
    public CTipDialog setBtnClickListener(String btn1text, String btn2text, String btn3text,
                                          final OnClickItemListener btnClickListener) {
        return this.setClickListener(btn1text, btn2text, btn3text, null, null, btnClickListener);
    }

    //四个按钮
    public CTipDialog setBtnClickListener(String btn1text, String btn2text, String
            btn3text, String btn4text, final OnClickItemListener btnClickListener) {
        return this.setClickListener(btn1text, btn2text, btn3text, btn4text, null, btnClickListener);
    }

    //五个按钮
    public CTipDialog setBtnClickListener(String btn1text, String btn2text, String
            btn3text, String btn4text, String btn5text, final OnClickItemListener btnClickListener) {
        return this.setClickListener(btn1text, btn2text, btn3text, btn4text, btn5text, btnClickListener);
    }

    public CTipDialog setBtnClickListener(final OnClickItemListener btnClickListener, String... btn1text) {
        btnText.clear();
        btnText = Arrays.asList(btn1text);
        this.btnClickListener = btnClickListener;
        return this;
    }

    /**
     * 给按钮设置回调监听
     *
     * @param btnClickListener 按钮的回调监听
     * @return
     */
    private CTipDialog setClickListener(String btn1text, String btn2text, String btn3text,
                                        String btn4text, String btn5text, final OnClickItemListener btnClickListener) {
        btnText.clear();
        if (btn1text != null) setBtnListener(btn1text);
        if (btn2text != null) setBtnListener(btn2text);
        if (btn3text != null) setBtnListener(btn3text);
        if (btn4text != null) setBtnListener(btn4text);
        if (btn5text != null) setBtnListener(btn5text);
        this.btnClickListener = btnClickListener;
        return this;
    }

    private void setBtnListener(String btnText) {
        this.btnText.add(btnText);
    }
}
