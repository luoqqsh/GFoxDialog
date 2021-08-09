package com.xing.gfox.view.Recyclerview;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HLBaseRecyclerViewAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = -10;
    private static final int TYPE_HEADER = -11;
    public List<T> dataList;
    private View footer;
    private View header;
    private FooterViewHolder footerViewHolder;
    private HeaderViewHolder headerViewHolder;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_FOOTER) {
            if (footerViewHolder == null) {
                footer.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                footerViewHolder = new FooterViewHolder(footer);
            }
            return footerViewHolder;
        } else if (viewType == TYPE_HEADER) {
            if (headerViewHolder == null) {
                header.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                headerViewHolder = new HeaderViewHolder(header);
            }
            return headerViewHolder;
        } else {
            return null;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    public int getListCount() {
        return dataList == null ? 0 : dataList.size();
    }

    @Override
    public final int getItemCount() {
        int count = getListCount();
        if (header != null) {
            count++;
        }
        if (footer != null) {
            count++;
        }
        return count;
    }
    private static class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View view) {
            super(view);
        }
    }

    private static class HeaderViewHolder extends RecyclerView.ViewHolder {
        HeaderViewHolder(View view) {
            super(view);
        }
    }
}
