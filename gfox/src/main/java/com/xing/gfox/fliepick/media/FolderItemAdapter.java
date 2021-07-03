package com.xing.gfox.fliepick.media;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

import com.xing.gfox.R;
import com.xing.gfox.fliepick.bean.FileBean;
import com.xing.gfox.fliepick.bean.FolderBean;

public class FolderItemAdapter extends RecyclerView.Adapter<FolderItemAdapter.ViewHolder> {

    private final Context mContext;
    private final List<FolderBean> mFolders;
    private int mSelectItem;
    private OnFolderSelectListener mListener;

    public FolderItemAdapter(Context context, List<FolderBean> folders) {
        mContext = context;
        mFolders = folders;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_select_folder, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        FolderBean folderBean = mFolders.get(position);
        holder.sFolderName.setText(folderBean.getFolderName());
        holder.sFolderState.setVisibility(mSelectItem == position ? View.VISIBLE : View.GONE);

        List<FileBean> mediaFileBeanList = folderBean.getMediaFileBeanList();
        if (mediaFileBeanList != null && !mediaFileBeanList.isEmpty()) {
            holder.sFolderSize.setText(mediaFileBeanList.size() + "");
            Glide.with(mContext).load(mediaFileBeanList.get(0).getFilePath())
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                    .into(holder.sFolderThumb);
        } else {
            holder.sFolderSize.setText("0");
            holder.sFolderThumb.setImageBitmap(null);
        }

        holder.itemView.setOnClickListener(v -> {
            mSelectItem = holder.getAdapterPosition();
            notifyDataSetChanged();
            if (mListener != null) {
                mListener.OnFolderSelect(mFolders.get(position));
            }
        });
    }

    @Override
    public int getItemCount() {
        return mFolders == null ? 0 : mFolders.size();
    }

    public void setOnFolderSelectListener(OnFolderSelectListener listener) {
        this.mListener = listener;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView sFolderThumb;
        ImageView sFolderState;
        TextView sFolderName;
        TextView sFolderSize;

        public ViewHolder(View itemView) {
            super(itemView);
            sFolderThumb = itemView.findViewById(R.id.sFolderThumb);
            sFolderState = itemView.findViewById(R.id.sFolderState);
            sFolderName = itemView.findViewById(R.id.sFolderName);
            sFolderSize = itemView.findViewById(R.id.sFolderSize);
        }
    }

    public interface OnFolderSelectListener {
        void OnFolderSelect(FolderBean folder);
    }
}
