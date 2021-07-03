package com.xing.gfox.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.appcompat.app.ActionBar;

import com.xing.gfox.base.interfaces.OnSimpleListener;

public class U_view {

    /**
     * 适合将view生成图片的测量
     *
     * @param v      view
     * @param width  宽
     * @param height 高
     * @return
     */
    public static void measureView(View v, int width, int height) {
        // 整个View的大小 参数是左上角 和右下角的坐标
        v.layout(0, 0, width, height);
        int measuredWidth = View.MeasureSpec.makeMeasureSpec(width, View.MeasureSpec.EXACTLY);
        int measuredHeight = View.MeasureSpec.makeMeasureSpec(10000, View.MeasureSpec.AT_MOST);
        /** 当然，measure完后，并不会实际改变View的尺寸，需要调用View.layout方法去进行布局。
         * 按示例调用layout函数后，View的大小将会变成你想要设置成的大小。
         */
        v.measure(measuredWidth, measuredHeight);
        v.layout(0, 0, v.getMeasuredWidth(), v.getMeasuredHeight());
    }

    /**
     * 测量view的宽高
     *
     * @param view
     */
    public static void measureView(View view) {
        ViewGroup.LayoutParams params = view.getLayoutParams();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
        }
        // 获得精确的宽度、高度
        int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(params.width,
                View.MeasureSpec.EXACTLY);
        int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(params.height,
                View.MeasureSpec.EXACTLY);
        view.measure(widthMeasureSpec, heightMeasureSpec);
    }

    /**
     * 计算ListView的高度，父控件为LinearLayout
     *
     * @param lv
     */
    public static void measureLinearLayoutLVHeight(ListView lv) {
        ListAdapter adapter = lv.getAdapter();
        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                view.measure(0, 0);
                totalHeight += view.getMeasuredHeight();
            }
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) lv.getLayoutParams();
            params.height = totalHeight + (lv.getDividerHeight() * (adapter.getCount() - 1));
            lv.setLayoutParams(params);
        }
    }

    /**
     * 计算ListView的高度，父控件为FrameLayout
     *
     * @param lv
     */
    public static void measureFrameLayoutLVHeight(ListView lv) {
        ListAdapter adapter = lv.getAdapter();
        if (adapter != null) {
            int totalHeight = 0;
            for (int i = 0; i < adapter.getCount(); i++) {
                View view = adapter.getView(i, null, null);
                view.measure(0, 0);
                totalHeight += view.getMeasuredHeight();
            }
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) lv.getLayoutParams();
            params.height = totalHeight + (lv.getDividerHeight() * (adapter.getCount() - 1));
            lv.setLayoutParams(params);
        }
    }

    /**
     * scrollview嵌套listview 计算高度
     * listView嵌套listView,设置子listView的高度,子listView的item必须是LinearLayout,因为其他的没重写onMeasure()方法
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 20;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }


    /**
     * scrollview嵌套expandablelistview 计算高度
     */
    public static void setExpandableListviewHeightBasedOnChildren(ExpandableListView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 20;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        // listView.getDividerHeight()获取子项间分隔符占用的高度
        // params.height最后得到整个ListView完整显示需要的高度
        listView.setLayoutParams(params);
    }

    /**
     * scrollview嵌套GridView 计算高度
     */
    public static void setGridViewHeightBasedOnChildren(GridView listView) {
        // 获取ListView对应的Adapter
        ListAdapter listAdapter = listView.getAdapter();
        int numColumns = listView.getNumColumns();
        if (listAdapter == null) {
            // pre-condition
            return;
        }
        int totalHeight = 20;

        if (listAdapter.getCount() % numColumns == 0) {
            for (int i = 0, len = listAdapter.getCount() / numColumns; i < len; i++) { // listAdapter.getCount()返回数据项的数目
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
            }
        } else {
            for (int i = 0, len = listAdapter.getCount() / numColumns; i <= len; i++) { // listAdapter.getCount()返回数据项的数目
                View listItem = listAdapter.getView(i, null, listView);
                listItem.measure(0, 0); // 计算子项View 的宽高
                totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度
            }
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight
                + (listView.getVerticalSpacing() * (listAdapter.getCount() / numColumns - 1));
        //	    listView.getVerticalSpacing()获取子项间分隔符占用的高度
        listView.setLayoutParams(params);
    }

    @SuppressLint("RestrictedApi")
    public static void showActionBar(Context context) {
        ActionBar ab = U_context.getAppCompActivity(context).getSupportActionBar();
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false);
            ab.show();
        }
        U_context.scanForActivity(context)
                .getWindow()
                .clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @SuppressLint("RestrictedApi")
    public static void hideActionBar(Context context) {
        ActionBar ab = U_context.getAppCompActivity(context).getSupportActionBar();
        if (ab != null) {
            ab.setShowHideAnimationEnabled(false);
            ab.hide();
        }
        U_context.scanForActivity(context).getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    /**
     * 设置view高度
     *
     * @param view   view
     * @param height 高度
     */
    public static void setViewHeight(View view, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null) {
            lp.height = height;
            view.setLayoutParams(lp);
        }
    }

    /**
     * 设置view宽度
     *
     * @param view  view
     * @param width 高度
     */
    public static void setViewWidth(View view, int width) {
        ViewGroup.LayoutParams lp = view.getLayoutParams();
        if (lp != null) {
            lp.width = width;
            view.setLayoutParams(lp);
        }
    }

    /**
     * 添加到父类并居中
     *
     * @param viewGroup viewGroup
     * @param view      view
     */
    public static void addToParentCenter(ViewGroup viewGroup, View view) {
        if (viewGroup instanceof RelativeLayout) {
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
            viewGroup.addView(view, layoutParams);
        } else if (viewGroup instanceof FrameLayout) {
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.gravity = Gravity.CENTER;
            viewGroup.addView(view, layoutParams);
        }
    }

    /**
     * 限制数字输入
     *
     * @param editText edittext
     * @param count    输入位数
     */
    public static void limitInputNumber(EditText editText, int count) {
        int editStart = editText.getSelectionStart();
        int editEnd = editText.getSelectionEnd();
        String[] lines = editText.getText().toString().split("\\.");
        if (lines.length > 1) {
            if (lines[1].length() > count && editStart > 0) {
                editText.setText(editText.getText().delete(editStart - 1, editEnd));
                editText.setSelection(editText.getText().toString().length());
            }
        }
    }

    /**
     * 设置圆角背景
     *
     * @param view   view
     * @param radius 角度
     * @param color  颜色
     */
    public static void setRoundBackground(View view, int radius, int color) {
        setRoundBackground(view, radius, radius, radius, radius, color);
    }

    public static void setRoundBackground(View view, int radius1, int radius2, int radius3, int radius4, int color) {
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE);
        drawable.setCornerRadii(new float[]{radius1, radius1, radius2, radius2, radius3, radius3, radius4, radius4});
        drawable.setColor(color);
        view.setBackground(drawable);
    }


    /**
     * View绘制监听，回调后可正常获取View的宽高属性
     * 注：未show的fragment中view进行监听时，返回的宽高还是0！！
     */
    public static void getViewDrawListen(final View view, final OnSimpleListener onSimpleListener) {
        if (view.getWidth() != 0) {
            if (onSimpleListener != null) {
                onSimpleListener.onListen();
            }
            return;
        }
        final ViewTreeObserver vto = view.getViewTreeObserver();
        vto.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                if (onSimpleListener != null) {
                    onSimpleListener.onListen();
                }
                return true;
            }
        });
    }
}
