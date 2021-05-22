package com.xing.gfox.base.activity;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;

import com.xing.gfox.R;
import com.xing.gfox.databinding.ActivityViewTitleBinding;
import com.xing.gfox.util.U_screen;


public class HLTitleView {
    ActivityViewTitleBinding binding;
    private View titleView;
    private int titleHeight;

    private HLTitleView() {
    }

    /**
     * 初始化标题栏
     *
     * @param mActivity  context
     * @param parentView 父类
     */
    public HLTitleView(FragmentActivity mActivity, ViewGroup parentView) {
        this(mActivity, parentView, false);
    }

    public HLTitleView(FragmentActivity mActivity, ViewGroup parentView, boolean isAddStatusBar) {
        titleView = View.inflate(mActivity, R.layout.activity_view_title, null);
        binding = ActivityViewTitleBinding.bind(titleView);
        int height = (int) mActivity.getResources().getDimension(R.dimen.dp44);
        if (isAddStatusBar) {
            parentView.addView(titleView, ViewGroup.LayoutParams.MATCH_PARENT, height + U_screen.getStatusBarHeight(mActivity));
        } else {
            parentView.addView(titleView, ViewGroup.LayoutParams.MATCH_PARENT, height);
        }
        parentView.setVisibility(View.VISIBLE);
    }

    public View getTitleView() {
        return titleView;
    }

    //返回
    public void setLeftButtonImage(int drawId, View.OnClickListener clickListener) {
        if (drawId > 0) {
            binding.titleLeftImg.setImageResource(drawId);
            binding.titleLeftBtn.setVisibility(View.VISIBLE);
            binding.titleLeftBtn.setOnClickListener(clickListener);
        } else {
            binding.titleLeftBtn.setVisibility(View.GONE);
        }
    }

    public void setLeftButtonText(String text, View.OnClickListener clickListener) {
        binding.titleLeftText.setText(text);
        binding.titleLeftText.setVisibility(View.VISIBLE);
        binding.titleLeftBtn.setVisibility(View.VISIBLE);
        binding.titleLeftBtn.setOnClickListener(clickListener);
    }


    //右边
    public void setRightButtonText(String text) {
        binding.titleRightText.setText(text);
    }

    public void setRightButtonText(String text, View.OnClickListener clickListener) {
        setRightButtonText(text);
        binding.titleRightTextBtn.setVisibility(View.VISIBLE);
        binding.titleRightTextBtn.setOnClickListener(clickListener);
    }

    public void setRightButtonTextColor(int textColor) {
        binding.titleRightText.setTextColor(textColor);
    }

    public void setRightButtonTextSize(int size) {
        binding.titleRightText.setTextSize(size);
    }

    public void setRightButtonImg(int imageResource, View.OnClickListener clickListener) {
        binding.titleRightImg.setImageResource(imageResource);
        binding.titleRightImgBtn.setVisibility(View.VISIBLE);
        binding.titleRightImgBtn.setOnClickListener(clickListener);
    }

    //标题
    public void setTitleText(String titleText1) {
        binding.titleText1.setText(titleText1);
        binding.titleText1.setVisibility(View.VISIBLE);
    }

    public void setTitleClickListener(View.OnClickListener clickListener) {
        binding.titleText1.setOnClickListener(clickListener);
    }

    public void setTitleBg(int imageResource) {
        binding.titleText1.setBackgroundResource(imageResource);
    }

    public void setTitleTextSize(int size) {
        binding.titleText1.setTextSize(size);
    }

    public void setTitleTextColor(int titleTextColor) {
        binding.titleText1.setTextColor(titleTextColor);
    }

    //标题2
    public void setTitle2Text(String titleText) {
        binding.titleText2.setText(titleText);
        binding.titleText2.setVisibility(View.VISIBLE);
    }

    public void setTitle2TextSize(int size) {
        binding.titleText2.setTextSize(size);
    }

    public void setTitleText2Color(int titleTextColor) {
        binding.titleText2.setTextColor(titleTextColor);
    }

    //线
    public void setTitleLineColor(int lineColor) {
        binding.titleCustomLine.setBackgroundColor(lineColor);
        binding.titleCustomLine.setVisibility(View.VISIBLE);
    }

    //背景
    public void setTitleBackGroundColor(int titleBackGroundColor) {
        binding.baseTitleLayout.setBackgroundColor(titleBackGroundColor);
    }

    //设置左右间距
    public void setTitlePadding(int left, int right) {
        binding.baseDefaultTitle.setPadding(left, 0, right, 0);
    }

    public void setHideTitle(boolean isHideTitle) {
        titleView.setVisibility(isHideTitle ? View.GONE : View.VISIBLE);
    }

    public int getTitleHeight(Context context) {
        //getDimension 获取绝对尺寸（"像素"单位，只是float）,
        //getDimensionPixelSize是将getDimension获取的小数部分四舍五入，
        //getDimensionPixelOffset是强制转换成int，即舍去小数部分
        if (titleHeight != 0) {
            return titleHeight;
        }
        titleHeight = context.getResources().getDimensionPixelOffset(R.dimen.height_of_title);
        return titleHeight;
    }

    public void setShowTextRedDot(boolean isShow) {
        binding.titleRightTextRed.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setShowImgRedDot(boolean isShow) {
        binding.titleRightImgRed.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    public void setShowDotTextDrawable(Drawable dotDrawable) {
        binding.titleRightTextRed.setBackground(dotDrawable);
        binding.titleRightTextRed.setVisibility(View.VISIBLE);
    }

    public void setShowDotImgDrawable(Drawable dotDrawable) {
        binding.titleRightImgRed.setBackground(dotDrawable);
        binding.titleRightImgRed.setVisibility(View.VISIBLE);
    }
}
