package com.xing.gfox.base.dialog;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xing.gfox.log.ViseLog;

import java.lang.ref.WeakReference;


public abstract class CommonDialog extends DialogFragment {
    private Handler mHandler;
    private static final int DISMISS = 0x43;
    private static final int CANCEL = 0x44;
    private static final int SHOW = 0x45;
    private DialogInterface.OnCancelListener cancelListener;
    private DialogInterface.OnDismissListener dismissListener;

    public CommonDialog() {
        mHandler = new ListenersHandler(this);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        //先让super.onActivityCreated不执行
        if (getDialog() != null && mHandler != null) {
            if (getShowsDialog()) {
                setShowsDialog(false);
            }
            super.onActivityCreated(savedInstanceState);
            setShowsDialog(true);

            //执行自己的onActivityCreated
            View view = getView();
            if (view != null) {
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
            getDialog().setCancelMessage(mHandler.obtainMessage(CANCEL, cancelListener));
            getDialog().setDismissMessage(mHandler.obtainMessage(DISMISS, dismissListener));

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

    @SuppressLint("CommitTransaction")
    @Override
    public void show(FragmentManager manager, String tag) {
        this.show(manager.beginTransaction(), tag);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
        if (dismissListener != null) {
            dismissListener.onDismiss(dialog);
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        if (cancelListener != null) {
            cancelListener.onCancel(dialog);
        }
    }

    public void setOnCancelListener(@Nullable DialogInterface.OnCancelListener listener) {
        cancelListener = listener;
    }

    public void setOnDismissListener(@Nullable DialogInterface.OnDismissListener listener) {
        dismissListener = listener;
    }

    public boolean isShowing() {
        return isAdded() && getDialog() != null && getDialog().isShowing();
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        if (isResumed() || isAdded() || isVisible() || isRemoving() || isDetached()) {
            boolean isResumed = isResumed();
            boolean isAdded = isAdded();
            boolean isVisible = isVisible();
            boolean isRemoving = isRemoving();
            boolean isDetached = isDetached();
            ViseLog.showLog(getClass().getSimpleName(), "showDialog Failed !isResumed=" + isResumed
                    + ",isAdded=" + isAdded + ",isVisible=" + isVisible + ",isRemoving=" + isRemoving + ",isDetached=" + isDetached);
            return -1;
        }
        try {
            return transaction.add(this, tag).commitAllowingStateLoss();
        } catch (Exception e) {
            Log.e(getClass().getSimpleName(), "showDialog Failed", e);
            e.printStackTrace();
            return -1;
        }
    }

    /**
     * 设置是否能取消
     *
     * @param isEnable
     */
    public void setDialogCancelEnable(boolean isEnable) {
        if (!isEnable) {
            getDialog().setOnKeyListener((dialog, keyCode, event) -> {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    return true;
                }
                return false;
            });
        }
        getDialog().setCanceledOnTouchOutside(isEnable);
        getDialog().setCancelable(isEnable);

    }

}