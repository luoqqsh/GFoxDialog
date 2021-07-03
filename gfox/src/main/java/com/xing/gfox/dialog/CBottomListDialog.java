package com.xing.gfox.dialog;

import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.xing.gfox.R;
import com.xing.gfox.base.dialog.BaseNDialog;
import com.xing.gfox.base.interfaces.OnClickItemListener;
import com.xing.gfox.util.U_screen;
import com.xing.gfox.util.U_view;

public class CBottomListDialog extends BaseNDialog {

    private List<String> dataList;
    private LinearLayout nDialogBottomList, nDialogBottomCancel;
    private View nDialogBottomScroll;
    private OnClickItemListener onClickItemListener;
    private boolean isShowCancelBtn = true;

    private int itemPadding = 0;
    private int roundRadius = 60;
    private int itemBackGround = 0xffffffff;

    private String title = "";

    private int lineHeight = 1;
    private int lineColor = Color.parseColor("#c1c1c1");
    private int offset = 0;
    private int maxHeight = 0;
    private Map<Integer, Integer> positionColorMap;

    private String cancelText = "取消";
    private int cancelTextColor = 0xFF000000;
    private int cancelBackGround = 0xFFFFFFFF;
    private int cancelSize = 17;

    @Override
    protected void initUI(View mView, Bundle bundle) {
        nDialogBottomList = mView.findViewById(R.id.nDialogBottomList);
        nDialogBottomCancel = mView.findViewById(R.id.nDialogBottomCancel);
        nDialogBottomScroll = mView.findViewById(R.id.nDialogBottomScroll);
        View bottomList = mView.findViewById(R.id.nDialogBottomListLayout);
        bottomList.setPadding(itemPadding, itemPadding, itemPadding, itemPadding);
        setOrRefreshDataList(dataList);
    }

    public void setOrRefreshDataList(List<String> dataList) {
        nDialogBottomList.removeAllViews();
        if (!TextUtils.isEmpty(title)) {
            nDialogBottomList.addView(addTitleView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        //设定nDialogBottomScroll高度
        int height = (U_screen.dp2px(mActivitys.get().getApplicationContext(), 50) * dataList.size());
        if (maxHeight == 0)
            maxHeight = U_screen.getScreenHeight(mActivitys.get().getApplicationContext()) * 3 / 5;
        if (height > maxHeight) {//超过最大高度
            ViewGroup.LayoutParams lp = nDialogBottomScroll.getLayoutParams();
            lp.height = maxHeight;
            nDialogBottomScroll.setLayoutParams(lp);
        }
        //设定nDialogBottomList圆角和背景
        if (itemPadding == 0) {
            U_view.setRoundBackground(nDialogBottomScroll, roundRadius, roundRadius, 0, 0, itemBackGround);
        } else {
            U_view.setRoundBackground(nDialogBottomScroll, roundRadius, itemBackGround);
        }
        for (int i = 0; i < dataList.size(); i++) {
            View itemView;
            if (i == 0) {//第一个
                itemView = getListItemView(dataList.get(i), i);
                nDialogBottomList.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (!(!isShowCancelBtn && dataList.size() == 1)) {
                    nDialogBottomList.addView(getLine(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            } else if (i == dataList.size() - 1) {//最后一个
                itemView = getListItemView(dataList.get(i), i);
                nDialogBottomList.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                if (isShowCancelBtn && itemPadding == 0) {
                    nDialogBottomList.addView(getLineView(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                }
            } else {//中间的
                itemView = getListItemView(dataList.get(i), i);
                nDialogBottomList.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                nDialogBottomList.addView(getLine(), ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            }
        }
        if (isShowCancelBtn) {
            View itemView = getCancelView();
            if (itemPadding != 0) {
                U_view.setRoundBackground(itemView, roundRadius, itemBackGround);
            } else {
                U_view.setRoundBackground(itemView, 0, itemBackGround);
            }
            nDialogBottomCancel.addView(itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    protected View addTitleView() {
        TextView textView = new TextView(mActivitys.get());
        textView.setText(title);
        textView.setGravity(Gravity.CENTER);
        textView.setSingleLine();
        textView.setMaxLines(1);
        textView.setTextColor(mActivitys.get().getResources().getColor(R.color.black));
        textView.setTextSize(18);
        textView.setHeight(U_screen.dp2px(mActivitys.get(), 50));
        return textView;
    }

    @Override
    public float getWidthScale() {
        return 1;
    }

    public void setOnClickItemListener(OnClickItemListener onClickItemListener) {
        this.onClickItemListener = onClickItemListener;
    }

    protected View getLine() {
        TextView textView = new TextView(mActivitys.get());
        textView.setHeight(lineHeight);
        textView.setBackgroundColor(lineColor);
        textView.setPadding(offset, 0, offset, 0);
        return textView;
    }

    protected View getLineView() {
        return getLine();
    }

    protected View getListItemView(String text, int position) {
        TextView textView = new TextView(mActivitys.get());
        textView.setText(text);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(17);
        textView.setSingleLine();
        textView.setMaxLines(1);
        if (positionColorMap != null && positionColorMap.containsKey(position)) {
            textView.setTextColor(positionColorMap.get(position));
        } else {
            textView.setTextColor(Color.parseColor("#000000"));
        }
        textView.setHeight(U_screen.dp2px(mActivitys.get(), 50));
        textView.setOnClickListener(v -> {
            if (position == -1) {
                dismiss();
                return;
            }
            if (onClickItemListener != null) {
                if (onClickItemListener.onItemClick(text, position)) {
                    dismiss();
                }
            }
        });
        return textView;
    }

    protected View getCancelView() {
        TextView textView = new TextView(mActivitys.get());
        textView.setText(cancelText);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(cancelSize);
        textView.setSingleLine();
        textView.setMaxLines(1);
        textView.setTextColor(cancelTextColor);
        textView.setBackgroundColor(cancelBackGround);
        textView.setHeight(U_screen.dp2px(mActivitys.get(), 50));
        textView.setOnClickListener(v -> {
            dismiss();
            if (onClickItemListener != null) {
                onClickItemListener.onItemClick(cancelText, -1);
            }
        });
        return textView;
    }

    @Override
    public int getGravity() {
        return Gravity.BOTTOM;
    }

    @Override
    public int getDialogAnim() {
        return R.style.bottomMenuAnim;
    }

    @Override
    public int getLayoutId() {
        return R.layout.ndialog_bottom_list;
    }

    public void setMaxHeight(int maxHeight) {
        this.maxHeight = maxHeight;
    }

    public void setDataList(List<String> dataList) {
        if (dataList == null || dataList.size() == 0) {
            throw new NullPointerException("BottomListDialog create imageList should not be null or size==0");
        }
        this.dataList = dataList;
    }

    public void setDataList(String... dataList) {
        setDataList(Arrays.asList(dataList));
    }

    public void setItemLineStyle(int lineHeight, int lineColor, int offset) {
        this.lineHeight = lineHeight;
        this.offset = offset;
        this.lineColor = lineColor;
    }

    public void setItemTextColor(int position, int positionColor) {
        if (positionColorMap == null) {
            positionColorMap = new HashMap<>();
        }
        positionColorMap.put(position, positionColor);
    }

    public void setShowCancelBtn(boolean showCancelBtn) {
        isShowCancelBtn = showCancelBtn;
    }

    public void setItemStyle(int itemPadding, int roundRadius, int itemBackGround) {
        this.itemPadding = itemPadding;
        this.roundRadius = roundRadius;
        this.itemBackGround = itemBackGround;
    }

    public void setCancelStyle(String cancelText, int cancelTextColor, int cancelBackGround, int cancelSize) {
        this.cancelText = cancelText;
        this.cancelTextColor = cancelTextColor;
        this.cancelBackGround = cancelBackGround;
        this.cancelSize = cancelSize;
    }

    public void setTitle(String text) {
        title = text;
    }
}
