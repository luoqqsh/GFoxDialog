package com.xing.gfox.fliepick.media;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.xing.gfox.R;
import com.xing.gfox.base.dialog.BaseNDialog;
import com.xing.gfox.fliepick.bean.FileBean;
import com.xing.gfox.view.photoView.PhotoView;

public class ImgLocalPreviewDialog extends BaseNDialog {
    private List<FileBean> imageList = new ArrayList<>();
    private int index = 0;
    private PhotoViewPager mViewPager;
    private SamplePagerAdapter adapter;
    private boolean isNeedDownload = false;

    @Override
    protected int getLayoutId() {
        return R.layout.ndialog_imgs;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    public float getWidthScale() {
        return 1;
    }

    @Override
    protected void initUI(View mView, Bundle bundle) {
        mViewPager = mView.findViewById(R.id.iv_ViewPager);
        adapter = new SamplePagerAdapter();
        mViewPager.setAdapter(adapter);
        if (index > 0) mViewPager.setCurrentItem(index, false);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                index = position;
            }
        });
        View imgDownloadBtn = mView.findViewById(R.id.imgDownloadBtn);
        if (isNeedDownload) {
            imgDownloadBtn.setVisibility(View.VISIBLE);
            imgDownloadBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        } else {
            imgDownloadBtn.setVisibility(View.GONE);
        }
    }

//    @Override
//    public int getDialogAnim() {
//        return R.style.anim_bottom2;
//    }

    public void setImageList(List<FileBean> imageList) {
        this.imageList = new ArrayList<>();
        for (FileBean file : imageList) {
            if (file.getFilePathUri() != null) {
                this.imageList.add(file);
            }
        }
    }

    public void setImageList(FileBean... imageList) {
        setImageList(Arrays.asList(imageList));
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void setNeedDownload(boolean needDownload) {
        isNeedDownload = needDownload;
    }

    class SamplePagerAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return imageList.size();
        }

        @NonNull
        @Override
        public View instantiateItem(@NonNull ViewGroup container, final int position) {
            return getItemView(container, position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            destroyItemView(container, position, (View) object);
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }
    }

    protected int getCount() {
        return imageList == null ? 0 : imageList.size();
    }

    private ObjectCacheUtil<ViewHolder> viewHolderObjectCacheUtil;

    class ViewHolder {
        private final View itemView;
        private PhotoView photoView;

        ViewHolder(View itemView) {
            this.itemView = itemView;
            photoView = itemView.findViewById(R.id.rela_viewpager_photoView);
        }
    }

    @NonNull
    protected View getItemView(ViewGroup container, int position) {
        if (viewHolderObjectCacheUtil == null) viewHolderObjectCacheUtil = new ObjectCacheUtil<>();
        ViewHolder h = viewHolderObjectCacheUtil.getCacheObject();
        if (h == null) {
            View itemView = LayoutInflater.from(mActivitys.get()).inflate(R.layout.jj_rela_viewpager, container, false);
            h = new ViewHolder(itemView);
            itemView.setTag(h);
        }
        Glide.with(h.photoView).load(getUrl(position).getFilePathUri())
//                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE))
                .into(h.photoView);
        h.photoView.setOnPhotoTapListener((arg0, arg1, arg2) -> dismiss());
        container.addView(h.itemView, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        return h.itemView;
    }

    protected void destroyItemView(@NonNull ViewGroup container, int position, @NonNull View view) {
        container.removeView(view);
        Object tag = view.getTag();
        if (tag != null && tag instanceof ViewHolder && viewHolderObjectCacheUtil != null) {
            ViewHolder h = (ViewHolder) tag;
            viewHolderObjectCacheUtil.addCacheObject(h);
        }
    }

    protected FileBean getUrl(int position) {
        if (imageList == null || position > imageList.size() - 1) return null;
        return imageList.get(position);
    }

    protected void notifyDataSetChanged() {
        adapter.notifyDataSetChanged();
    }
}
