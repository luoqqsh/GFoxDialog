package com.xing.gfox.view.Recyclerview.LiAdapter;

import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class LiBaseRecyclerViewAdapter<T> extends
        RecyclerView.Adapter<LiBaseRecyclerViewAdapter.BaseViewHolder> {


    protected List<T> datas;

    public List<T> getDatas() {
        return datas;
    }

    public Context context;

    public LiBaseRecyclerViewAdapter(List<T> datas) {
        this.datas = datas;
    }

    protected LiBaseRecyclerViewAdapter() {
    }


    /**
     * 获取子item
     *
     * @return
     */
    protected abstract int getLayoutId();

    @NonNull
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(getLayoutId(), parent, false);
//        ViewGroup.LayoutParams params = view.getLayoutParams();

//            if (isVertical()){
//                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
//            }else {
//                view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
//                        ViewGroup.LayoutParams.WRAP_CONTENT));
//            }
        return new BaseViewHolder(view);
    }

    protected boolean isVertical() {
        return true;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        bindData(holder, datas.get(position), position);
    }

    /**
     * 绑定数据
     *
     * @param holder   具体的viewHolder
     * @param position 对应的索引
     */
    protected abstract void bindData(BaseViewHolder holder, T t, int position);


    /**
     * 刷新数据
     *
     * @param list
     */
    public void refresh(List<T> list) {
        this.datas.clear();
        if (list != null && list.size() > 0) {
            this.datas.addAll(list);
        }
        notifyDataSetChanged();
    }

    public void delete(int pos) {
        this.datas.remove(pos);
        this.notifyItemRemoved(pos);
    }

    public void clear() {
        this.datas.clear();
        notifyDataSetChanged();
    }


    /**
     * 添加数据
     *
     * @param datas
     */
    public void addData(List<T> datas) {
        this.datas.addAll(datas);
        notifyDataSetChanged();
    }

    public void addData(int pos, List<T> datas) {
        this.datas.addAll(0, datas);
        notifyDataSetChanged();
    }

    public void addData(int pos, T datas) {
        this.datas.add(0, datas);
        notifyDataSetChanged();
    }

    public void addData(T datas) {
        this.datas.add(datas);
        notifyDataSetChanged();
    }
 public void removeData(T datas) {
        this.datas.remove(datas);
        notifyDataSetChanged();
    }

    public Context getContext() {
        return context;
    }

    @Override
    public int getItemCount() {

        return datas == null ? 0 : datas.size();
    }

    public void swipeToFirst(int pos) {
        T t = datas.get(pos);
        datas.remove(pos);
        datas.add(0, t);
        notifyDataSetChanged();
    }


    /**
     * 封装ViewHolder ,子类可以直接使用
     */
    public static class BaseViewHolder extends RecyclerView.ViewHolder {

        private Map<Integer, View> mViewMap;

        public CountDownTimer countDownTimer;

        public BaseViewHolder(View itemView) {
            super(itemView);
            mViewMap = new HashMap<>();
        }

        public View getItemView() {
            return itemView;
        }

        /**
         * 获取设置的view
         *
         * @param id
         * @return
         */
        public <T extends View> T getView(int id) {
            View view = mViewMap.get(id);
            if (view == null) {
                view = itemView.findViewById(id);
                mViewMap.put(id, view);
            }
            return (T) view;
        }

        public BaseViewHolder setOnClickListener(int view_id, View.OnClickListener listener) {
            View view = getView(view_id);
            view.setOnClickListener(listener);
            return this;
        }
    }

}
