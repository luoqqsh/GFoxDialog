package com.xing.gfox.fliepick.media;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import com.xing.gfox.R;
import com.xing.gfox.base.interfaces.OnSimpleListener;
import com.xing.gfox.fliepick.bean.CPickType;
import com.xing.gfox.fliepick.bean.FileBean;

public class FileItemAdapter extends RecyclerView.Adapter<FileItemAdapter.ViewHolder> {
    private final FragmentActivity mActivity;
    private final MediaConfig mediaConfig;
    private List<FileBean> mediaFileBeanList;
    private ViewHolder singleSelectHolder;
    private OnSimpleListener cameraListener;

    public FileItemAdapter(FragmentActivity mActivity, MediaConfig mediaConfig, List<FileBean> mediaFileBeanList) {
        this.mActivity = mActivity;
        this.mediaConfig = mediaConfig;
        this.mediaFileBeanList = mediaFileBeanList;
        if (mediaConfig.isShowCamera()) {
            mediaFileBeanList.add(new FileBean());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (mediaConfig.isShowCamera() && position == 0) {
            return CPickType.camera;
        } else {
            return CPickType.images;
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case CPickType.images:
            case CPickType.videos:
                View img = LayoutInflater.from(mActivity).inflate(R.layout.item_select_img, parent, false);
                return new ViewHolder(img);
            case CPickType.camera:
                View photo = LayoutInflater.from(mActivity).inflate(R.layout.item_select_camera, parent, false);
                return new ViewHolder(photo);
            default:
                View view = LayoutInflater.from(mActivity).inflate(R.layout.item_select_camera, parent, false);
                return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case CPickType.images:
            case CPickType.videos:
                FileBean mediaFileBean = mediaFileBeanList.get(position);
                Glide.with(mActivity).load(mediaFileBean.getFilePathUri())
//                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                        .into(holder.sFileThumb);
                setItemSelect(holder, mediaConfig.getSelectList().contains(mediaFileBean));

                //点击选中/取消选中图片
                holder.sFileState.setOnClickListener(v -> checkedImage(holder, mediaFileBean));
                holder.itemView.setOnClickListener(v -> {
                    if (mediaConfig.isNeedPreview()) {//跳转到预览页面
                        ImgLocalPreviewDialog dialog = new ImgLocalPreviewDialog();
                        dialog.setImageList(mediaFileBeanList);
                        dialog.setIndex(position - 1);
                        dialog.showNow(mActivity);
                    } else {
                        checkedImage(holder, mediaFileBean);
                    }
                });
                break;
            case CPickType.camera:
                holder.itemView.setOnClickListener(v -> {
                    if (cameraListener != null) {
                        cameraListener.onListen();
                    }
                });
                break;
        }
    }

    /**
     * 设置图片选中和未选中的效果
     */
    private void setItemSelect(ViewHolder holder, boolean isSelect) {
        if (mediaConfig.isSingleSelect()) {
            if (singleSelectHolder != null) {
                singleSelectHolder.sFileState.setImageResource(R.mipmap.icon_image_un_select);
                singleSelectHolder.sFileMasking.setAlpha(0.2f);
            }
            singleSelectHolder = holder;
        }
        if (isSelect) {
            holder.sFileState.setImageResource(R.mipmap.icon_image_select);
            holder.sFileMasking.setAlpha(0.5f);
        } else {
            holder.sFileState.setImageResource(R.mipmap.icon_image_un_select);
            holder.sFileMasking.setAlpha(0.2f);
        }
    }

    /**
     * 选中 未选中
     */
    private void checkedImage(ViewHolder holder, FileBean mediaFileBean) {
        if (mediaConfig.getSelectList().contains(mediaFileBean)) {
            //如果图片已经选中，就取消选中
            mediaConfig.getSelectList().remove(mediaFileBean);
            setItemSelect(holder, false);
        } else if (mediaConfig.isSingleSelect()) {
            //如果是单选，就先清空已经选中的图片，再选中当前图片
            mediaConfig.getSelectList().clear();
            mediaConfig.getSelectList().add(mediaFileBean);
            setItemSelect(holder, true);
        } else if (mediaConfig.getSelectList().size() < mediaConfig.getMaxSelectCount()) {
            mediaConfig.getSelectList().add(mediaFileBean);
            setItemSelect(holder, true);
        }
        mediaConfig.listChange();
    }

    @Override
    public int getItemCount() {
        return mediaFileBeanList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView sFileThumb;
        ImageView sFileState;
        ImageView sFileMasking;
        ImageView sFileCamera;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sFileThumb = itemView.findViewById(R.id.sFileThumb);
            sFileState = itemView.findViewById(R.id.sFileState);
            sFileMasking = itemView.findViewById(R.id.sFileMasking);
            sFileCamera = itemView.findViewById(R.id.sFileCamera);
        }
    }

    public void selectAll() {
        clearSelect();
        mediaConfig.getSelectList().addAll(mediaFileBeanList);
    }

    public void clearSelect() {
        mediaConfig.getSelectList().clear();
    }

    public void notifyDataSetChanged(List<FileBean> mediaFileBeanList) {
        if (mediaConfig.isShowCamera()) {
            mediaFileBeanList.add(0, new FileBean());
        }
        this.mediaFileBeanList = mediaFileBeanList;
        notifyDataSetChanged();
    }

    public void setCameraListener(OnSimpleListener cameraListener) {
        this.cameraListener = cameraListener;
    }
}
