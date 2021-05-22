package com.xing.gfox.base.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.xing.gfox.R;
import com.xing.gfox.log.ViseLog;

public class NDialog {

    /**
     * 对话框处于屏幕顶部位置
     */
    public static final int DIALOG_LOCATION_TOP = 12;
    /**
     * 对话框处于屏幕中间位置
     */
    public static final int DIALOG_LOCATION_CENTER = 10;
    /**
     * 对话框处于屏幕底部位置
     */
    public static final int DIALOG_LOCATION_BOTTOM = 11;
    /**
     * 消息位于对话框的位置 居左
     */
    public static final int MSG_LAYOUT_LEFT = 1;
    /**
     * 消息位于对话框的位置 居中
     */
    public static final int MSG_LAYOUT_CENTER = 0;
    /**
     * 动画
     */
    public static final int DIALOG_ANIM_NORMAL = R.style.dialogNoAnim;
//    //从下往上滑动动画
//    public static final int DIALOG_ANIM_SLID_BOTTOM = R.style.bottomMenuAnim;
//    //从上往下滑动动画
//    public static final int DIALOG_ANIM_SLID_TOP = R.style.topMenuAnim;

    /**
     * Dialog对象
     */
    private Dialog dialog;
    private Context context;
    private float alpha = 0.8f;//点击透明度
    private CountDownTimer timer;

    /**
     * 构造器
     *
     * @param context          上下文
     * @param layoutStyle      对话框布局样式
     * @param widthcoefficient 对话框宽度时占屏幕宽度的比重（0-1）
     */
    public NDialog(Context context, int layoutStyle, float widthcoefficient) {
        this(context, layoutStyle, widthcoefficient, true);
    }

    /**
     * 构造器
     *
     * @param context          上下文
     * @param widthcoefficient 对话框宽度时占屏幕宽度的比重（0-1）
     */
    public NDialog(Context context, float widthcoefficient) {
        this(context, R.layout.ndialog_default, widthcoefficient, true);
    }

    /**
     * 构造器
     *
     * @param context          context
     * @param layoutStyle      布局样式
     * @param widthcoefficient 对话框宽度所占屏幕宽度的比重（0-1）
     * @param dimEnable        是否显示阴影
     */
    public NDialog(Context context, int layoutStyle, float widthcoefficient, boolean dimEnable) {
        Dialog dialog;
        if (dimEnable) {
            dialog = new Dialog(context, R.style.TDialog);
        } else {
            dialog = new Dialog(context, R.style.TDialogNoShadow);
        }
        dialog.setContentView(layoutStyle);

        Window window = dialog.getWindow();
        // 获取屏幕宽度
        DisplayMetrics metrics = new DisplayMetrics();
        if (window != null) {
            window.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        } else {
            ViseLog.e("window为空");
        }
        int screenwidth = metrics.widthPixels;
        int width;
        if (widthcoefficient >= 0) {
            width = (int) (screenwidth * widthcoefficient);
        } else {
            width = (int) (screenwidth * 0.8f);
        }
        // 设置对话框宽度
        if (window != null) {
            window.getAttributes().width = width;
        } else {
            ViseLog.e("window为空");
        }
        this.dialog = dialog;
        this.context = context;
    }


    //属性设置
    public NDialog setTouchOutSideCancelable(boolean outside, boolean cancelable) {
        dialog.setCanceledOnTouchOutside(outside);
        dialog.setCancelable(cancelable);
        return this;
    }

    /**
     * 设置显示关闭按钮
     * isShowClose 是否显示关闭按钮
     *
     * @return this
     */
    public NDialog isShowClose(boolean isShowClose) {
        if (isShowClose) {
            ImageView imgv_close = dialog.findViewById(R.id.nDialogClose);
            if (imgv_close == null) {
                ViseLog.e("请检查id是否正确，将关闭按钮id设置为nDialogClose");
                return this;
            }
            imgv_close.setVisibility(View.VISIBLE);
            imgv_close.setOnClickListener(v -> dialog.dismiss());
        }
        return this;
    }

    /**
     * 设置是否系统弹窗
     * 需要悬浮窗权限
     *
     * @return this
     */
    public NDialog setSystemAlert(boolean isSystemAlert) {
        if (isSystemAlert) {
            if (dialog.getWindow() != null) {
                dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            } else {
                ViseLog.e("setSystemAlert时，getWindow为空");
            }
        }
        return this;
    }

    /**
     * 设置对话框标题
     *
     * @param title 标题
     * @return this
     */
    public NDialog setTitle(String title, int layout) {
        TextView dialogTitle = dialog.findViewById(R.id.nDialogTitle);
        if (dialogTitle == null) {
            ViseLog.e("请检查id是否正确，将标题id设置为nDialogTitle");
            return null;
        }
        if (title != null) {
            dialogTitle.setText(title);
            dialogTitle.setVisibility(View.VISIBLE);
            dialogTitle.setGravity(layout);
        } else {
            dialogTitle.setVisibility(View.GONE);
        }
        return this;
    }

    /**
     * 设置对话框的消息内容
     *
     * @param message 消息内容
     * @param layout  文字对齐方式
     * @return this
     */
    public NDialog setMessage(String message, int layout) {
        TextView dialogMsg = dialog.findViewById(R.id.nDialogMessage);
        if (dialogMsg == null) {
            ViseLog.e("请检查id是否正确，将消息内容id设置为nDialogMessage");
            return null;
        }
        if (message != null) {
            dialogMsg.setText(message);
            dialogMsg.setVisibility(View.VISIBLE);
            dialogMsg.setGravity(layout);
        } else {
            dialogMsg.setVisibility(View.GONE);
        }

        return this;
    }


    /**
     * 给对话框设置动画
     */
    public NDialog setDialogAnimation(int resId) {
        dialog.getWindow().setWindowAnimations(resId);
        return this;
    }

    /**
     * 设置对话框的位置
     *
     * @param location 位置
     * @return this
     */
    public NDialog setDialogLocation(int location) {
        Window window = this.dialog.getWindow();
        if (window != null) {
            switch (location) {
                case DIALOG_LOCATION_CENTER:
                    window.setGravity(Gravity.CENTER);
                    break;
                case DIALOG_LOCATION_BOTTOM:
                    window.setGravity(Gravity.BOTTOM);
                    break;
                case DIALOG_LOCATION_TOP:
                    window.setGravity(Gravity.TOP);
                    break;
            }
        }
        return this;
    }

    /**
     * 监听器监听对话框按钮点击
     *
     * @author zhl
     */
    public interface onDialogBtnClickListener {
        void onDialogBtnClick(Context context, Dialog dialog, int whichBtn);
    }

    //一个按钮
    public NDialog setBtnClickListener(final boolean isDismiss, String btn1text, final onDialogBtnClickListener btnClickListener) {
        return this.setClickListener(isDismiss, btn1text, null, null, null, null, btnClickListener);
    }

    //两个按钮
    public NDialog setBtnClickListener(final boolean isDismiss, String btn1text, String btn2text, final onDialogBtnClickListener btnClickListener) {
        return this.setClickListener(isDismiss, btn1text, btn2text, null, null, null, btnClickListener);
    }

    //三个按钮
    public NDialog setBtnClickListener(final boolean isDismiss, String btn1text, String btn2text, String btn3text, final onDialogBtnClickListener btnClickListener) {
        return this.setClickListener(isDismiss, btn1text, btn2text, btn3text, null, null, btnClickListener);
    }

    //四个按钮
    public NDialog setBtnClickListener(final boolean isDismiss, String btn1text, String btn2text, String btn3text, String btn4text, final onDialogBtnClickListener btnClickListener) {
        return this.setClickListener(isDismiss, btn1text, btn2text, btn3text, btn4text, null, btnClickListener);
    }

    //五个按钮
    public NDialog setBtnClickListener(final boolean isDismiss, String btn1text, String btn2text, String btn3text, String btn4text, String btn5text, final onDialogBtnClickListener btnClickListener) {
        return this.setClickListener(isDismiss, btn1text, btn2text, btn3text, btn4text, btn5text, btnClickListener);
    }

    /**
     * 给按钮设置回调监听
     *
     * @param btnClickListener 按钮的回调监听
     * @param isDismiss        点击按钮后是否取消对话框
     * @return
     */
    @SuppressLint("ClickableViewAccessibility")
    private NDialog setClickListener(final boolean isDismiss, String btn1text, String btn2text, String btn3text,
                                     String btn4text, String btn5text, final onDialogBtnClickListener btnClickListener) {
        setBtnListener(btn1text, isDismiss, btnClickListener, 1, R.id.nDialogBtn1);
        setBtnListener(btn2text, isDismiss, btnClickListener, 2, R.id.nDialogBtn2);
        setBtnListener(btn3text, isDismiss, btnClickListener, 3, R.id.nDialogBtn3);
        setBtnListener(btn4text, isDismiss, btnClickListener, 4, R.id.nDialogBtn4);
        setBtnListener(btn5text, isDismiss, btnClickListener, 5, R.id.nDialogBtn5);
        return this;
    }

    @SuppressLint("ClickableViewAccessibility")
    private NDialog setBtnListener(String btnText, final boolean isDismiss,
                                   final onDialogBtnClickListener btnClickListener, final int i, int id) {
        if (btnText != null) {
            // 设置确认按钮
            final TextView mBtn = dialog.findViewById(id);
            if (mBtn == null) {
                ViseLog.e("请检查id是否正确，将第" + i + "个按钮id设置为nDialogBtn" + i);
                return null;
            }
            mBtn.setText(btnText);
            mBtn.setVisibility(View.VISIBLE);
            // 给按钮绑定监听器
            mBtn.setOnClickListener(v -> {
                if (isDismiss) {
                    dialog.dismiss();
                }
                if (btnClickListener != null) {
                    btnClickListener.onDialogBtnClick(context, dialog, i);
                }
            });
            mBtn.setOnTouchListener((view, motionEvent) -> {
                switch (motionEvent.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        mBtn.setAlpha(alpha);
                        break;
                    case MotionEvent.ACTION_UP:
                        mBtn.setAlpha(1f);
                        break;
                }
                return false;
            });
        }
        return this;
    }

    /**
     * 创建对话框
     *
     * @return dialog
     */
    public Dialog create() {
        return dialog;
    }

    /**
     * 一段时间后关闭对话框
     *
     * @param time 时间，单位毫秒
     */
    public void createTrans(long time) {
        if (context instanceof Activity) {
            dialog.setOwnerActivity((Activity) context);
        }
        dialog.show();
        timer = new CountDownTimer(time, 1) {
            public void onTick(long millisUntilFinished) {
            }

            public void onFinish() {
                //显示错误对话框
                dialog.dismiss();
                if (timer != null) {
                    timer.cancel();
                    timer = null;
                }
            }
        }.start();
    }
}
