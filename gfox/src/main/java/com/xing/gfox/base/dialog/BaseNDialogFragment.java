package com.xing.gfox.base.dialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import org.jetbrains.annotations.NotNull;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

public class BaseNDialogFragment extends DialogFragment {
    private int layoutId, dialogStyle;
    private String parentId;
    private WeakReference<BaseNDialog> parent;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        if (getDialog() == null) {
            setShowsDialog(false);
        }
        super.onActivityCreated(savedInstanceState);
    }

    //parentId：因不可抗力，诸如横竖屏切换、分屏、折叠屏切换等可能造成DialogFragment重启，为保证重启后功能正常运行，因此需要保存自己爹（BaseDialog）的id，方便重新绑定到BaseDialog
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (getDialog() == null) {
            setShowsDialog(false);
        }
        if (savedInstanceState != null) {
            layoutId = savedInstanceState.getInt("layoutId");
            parentId = savedInstanceState.getString("parentId");
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public int getTheme() {
        return dialogStyle;
    }

    public BaseNDialogFragment setLayoutId(BaseNDialog baseDialog, int layoutId, int dialogStyle) {
        this.layoutId = layoutId;
        this.dialogStyle = dialogStyle;
        this.parentId = baseDialog.toString();
        this.parent = new WeakReference<>(baseDialog);
        return this;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (layoutId == 0) {
            findMyParentAndBindView(null);
            return super.onCreateView(inflater, container, savedInstanceState);
        }
        View rootView = inflater.inflate(layoutId, null);
        findMyParentAndBindView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (parent != null && parent.get().showEvent != null) {
            parent.get().showEvent.onListen();
        }
    }

    //找爸爸行动
    private void findMyParentAndBindView(View rootView) {
        List<BaseNDialog> cache = new ArrayList<>(BaseNDialog.dialogList);
        BaseNDialog.newContext = new WeakReference<>((AppCompatActivity) getContext());
        for (BaseNDialog baseDialog : cache) {
            baseDialog.mActivitys = new WeakReference<>((AppCompatActivity) getContext());
            if (baseDialog.toString().equals(parentId)) {
                parent = new WeakReference<>(baseDialog);
                parent.get().dialog = new WeakReference<>(this);
                parent.get().initWindow(getDialog());
                parent.get().initUI(rootView, getArguments());
            }
        }
    }

    private boolean findMyParent() {
        boolean flag = false;
        List<BaseNDialog> cache = new ArrayList<>(BaseNDialog.dialogList);
        BaseNDialog.newContext = new WeakReference<>((AppCompatActivity) getContext());
        for (BaseNDialog baseDialog : cache) {
            baseDialog.mActivitys = new WeakReference<>((AppCompatActivity) getContext());
            if (baseDialog.toString().equals(parentId)) {
                flag = true;
                parent = new WeakReference<>(baseDialog);
                parent.get().dialog = new WeakReference<>(this);
                parent.get().initWindow(getDialog());
            }
        }
        return flag;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt("layoutId", layoutId);
        outState.putString("parentId", parentId);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDismiss(@NotNull DialogInterface dialog) {
        if (parent == null || parent.get() == null) {
            if (!findMyParent()) {
                return;
            }
        }
        if (parent != null && parent.get().dismissEvent != null) {
            parent.get().dismissEvent.onListen();
        }


        super.onDismiss(dialog);
        parent.clear();
        parent = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (parent == null || parent.get() == null) {
            if (!findMyParent()) {
                return;
            }
        }
        if (parent != null) {
            if (parent.get().dismissedFlag) {
                dismiss();
                return;
            }
        }
        if (parent != null && parent.get().resumeEvent != null) {
            parent.get().resumeEvent.onListen();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (parent != null && parent.get().pauseEvent != null) {
            parent.get().pauseEvent.onListen();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (parent != null && parent.get().pauseEvent != null) {
            parent.get().activityResultEvent.onListen(requestCode, resultCode, intent);
        }
    }
}