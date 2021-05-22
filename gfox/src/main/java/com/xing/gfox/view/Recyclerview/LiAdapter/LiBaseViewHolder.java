package com.xing.gfox.view.Recyclerview.LiAdapter;

import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by zlx on 2020/9/23 11:47
 * Email: 1170762202@qq.com
 * Description:
 */
public class LiBaseViewHolder extends RecyclerView.ViewHolder {
    private Map<Integer, View> mViewMap;

    public LiBaseViewHolder(@NonNull View itemView) {
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

    public LiBaseViewHolder setOnClickListener(int view_id, View.OnClickListener listener) {
        View view = getView(view_id);
        view.setOnClickListener(listener);
        return this;
    }
}
