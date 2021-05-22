package com.xing.gfox.base.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.lang.ref.WeakReference;

//https://www.jianshu.com/p/742279658ee0
public abstract class FixNDialogFragment extends DialogFragment {
    private Handler mHandler;
    private static final int DISMISS = 0x43;
    private static final int CANCEL = 0x44;
    private static final int SHOW = 0x45;

    public FixNDialogFragment() {
        mHandler = new ListenersHandler(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (getDialog() != null && mHandler != null) {
            //先让super.onActivityCreated不执行
            if (getShowsDialog()) {
                setShowsDialog(false);
            }
            super.onActivityCreated(savedInstanceState);
            setShowsDialog(true);

            //执行自己的onActivityCreated
            View view = getView();
            if (view != null) {
                //1.3.0版本开始，这里view.getParent()取到的值不为空
                if (view.getParent() != null) {
                    throw new IllegalStateException(
                            "DialogFragment can not be attached to a container view");
                }
                getDialog().setContentView(view);
            }
            final Activity activity = getActivity();
            if (activity != null) {
                getDialog().setOwnerActivity(activity);
            }

            //this.mDialog.setCancelable(this.mCancelable);
            //使用自定义message处理dialog回调信息
            getDialog().setCancelMessage(mHandler.obtainMessage(CANCEL));
            getDialog().setDismissMessage(mHandler.obtainMessage(DISMISS));

            if (savedInstanceState != null) {
                Bundle dialogState = savedInstanceState.getBundle("android:savedDialogState");
                if (dialogState != null) {
                    getDialog().onRestoreInstanceState(dialogState);
                }
            }
        } else {
            super.onActivityCreated(savedInstanceState);
        }
    }

    private static final class ListenersHandler extends Handler {
        private final WeakReference<DialogFragment> mDialog;

        public ListenersHandler(DialogFragment fragmentDialog) {
            mDialog = new WeakReference<>(fragmentDialog);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == DISMISS) {
                DialogFragment fragmentDialog = mDialog.get();
                if (fragmentDialog != null) {
                    //回调处理
                    if (msg.obj != null && msg.obj instanceof DialogInterface.OnDismissListener) {
                        ((DialogInterface.OnDismissListener) msg.obj).onDismiss(fragmentDialog.getDialog());
                    }
                    //自身处理
                    fragmentDialog.onDismiss(fragmentDialog.getDialog());
                }
                msg.setTarget(null);
            } else if (msg.what == CANCEL) {
                DialogFragment fragmentDialog = mDialog.get();
                if (fragmentDialog != null) {
                    //回调处理
                    if (msg.obj != null && msg.obj instanceof DialogInterface.OnCancelListener) {
                        ((DialogInterface.OnCancelListener) msg.obj).onCancel(mDialog.get().getDialog());
                    }
                    //自身处理
                    fragmentDialog.onCancel(fragmentDialog.getDialog());
                }
                msg.setTarget(null);
            }
        }
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }
}