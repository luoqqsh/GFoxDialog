package com.xing.gfox.fliepick.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xing.gfox.R;
import com.xing.gfox.base.toast.U_Toast;
import com.xing.gfox.fliepick.bean.CPickType;
import com.xing.gfox.fliepick.bean.FileBean;
import com.xing.gfox.fliepick.bean.FolderBean;
import com.xing.gfox.fliepick.bean.MediaSelectListener;
import com.xing.gfox.media.U_mediaList;
import com.xing.gfox.util.U_permissions;
import com.xing.gfox.view.Recyclerview.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CMediaPickDialog extends BasePickDialog {
    private RecyclerView nSelectItemList;
    private FileItemAdapter fileItemAdapter;
    protected boolean isOpenFolder;
    private View nSelectMasking;
    private TextView pickFolderNameText, pickConfirmText;
    private FolderPopWindow folderPopWindow;
    private FrameLayout adLayout;

    private CMediaPickDialog() {
    }

    public static MediaSelectBuilder builder() {
        return new MediaSelectBuilder();
    }

    public static class MediaSelectBuilder {
        private final MediaConfig mediaConfig;

        private MediaSelectBuilder() {
            mediaConfig = new MediaConfig();
        }

        //单选两个值都传1，默认不写为单选
        public MediaSelectBuilder setSelectCount(int minSelectCount, int maxSelectCount) {
            mediaConfig.setMinSelectCount(minSelectCount);
            mediaConfig.setMaxSelectCount(maxSelectCount);
            return this;
        }

        //添加类似广告的信息view
        public MediaSelectBuilder addAdTip(View view) {
            mediaConfig.setAdTipView(view);
            return this;
        }

        //添加提示信息
        public MediaSelectBuilder addTip(Context context, String tip) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_pick_ad, null);
            TextView tipText = view.findViewById(R.id.tipText);
            tipText.setText(tip);
            addAdTip(view);
            return this;
        }

        public MediaSelectBuilder setSelectList(ArrayList<FileBean> mDataList) {
            mediaConfig.setSelectList(mDataList);
            return this;
        }

        public MediaSelectBuilder setOnSingleListener(MediaSelectListener<FileBean> onSingleListener) {
            mediaConfig.setOnSingleListener(onSingleListener);
            return this;
        }

        public MediaSelectBuilder setShowCamera(boolean showCamera) {
            mediaConfig.setShowCamera(showCamera);
            return this;
        }

        public MediaSelectBuilder setNeedPreview(boolean needPreview) {
            mediaConfig.setNeedPreview(needPreview);
            return this;
        }

        public MediaSelectBuilder setOnMoreSelectListener(MediaSelectListener<ArrayList<FileBean>> onMoreSelectListener) {
            mediaConfig.setOnMoreSelectListener(onMoreSelectListener);
            return this;
        }

        public CMediaPickDialog startSelectImg(FragmentActivity mActivity) {
            mediaConfig.setPickType(CPickType.images);
            CMediaPickDialog cMediaPickDialog = new CMediaPickDialog();
            cMediaPickDialog.setMediaConfig(mediaConfig);
            cMediaPickDialog.setChooseType(CPickType.images);
            cMediaPickDialog.showNow(mActivity);
            return cMediaPickDialog;
        }

        public CMediaPickDialog startSelectVideo(FragmentActivity mActivity) {
            mediaConfig.setPickType(CPickType.videos);
            CMediaPickDialog cMediaPickDialog = new CMediaPickDialog();
            cMediaPickDialog.setMediaConfig(mediaConfig);
            cMediaPickDialog.setChooseType(CPickType.videos);
            cMediaPickDialog.showNow(mActivity);
            return cMediaPickDialog;
        }

        public CMediaPickDialog startSelectMusic(FragmentActivity mActivity) {
            mediaConfig.setPickType(CPickType.audios);
            mediaConfig.setNeedPreview(false);
            mediaConfig.setShowCamera(false);
            mediaConfig.setSpanCount(1);
            CMediaPickDialog cMediaPickDialog = new CMediaPickDialog();
            cMediaPickDialog.setMediaConfig(mediaConfig);
            cMediaPickDialog.setChooseType(CPickType.audios);
            cMediaPickDialog.showNow(mActivity);
            return cMediaPickDialog;
        }
    }

    public void refreshAd(View view) {
        mediaConfig.setAdTipView(view);
        if (adLayout != null) {
            adLayout.removeAllViews();
            adLayout.addView(view);
            adLayout.setVisibility(View.VISIBLE);
        }
    }

    public void removeAd() {
        if (adLayout != null) {
            adLayout.removeAllViews();
            adLayout.setVisibility(View.GONE);
        }
        mediaConfig.setAdTipView(null);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.ndialog_pick;
    }

    @Override
    public float getWidthScale() {
        return 1;
    }

    @Override
    public int getHeight() {
        return ViewGroup.LayoutParams.MATCH_PARENT;
    }

    @Override
    protected void initUI(View mView, Bundle bundle) {
        if (mediaConfig.getAdTipView() != null) {
            adLayout = mView.findViewById(R.id.pickAdTipLayout);
            adLayout.addView(mediaConfig.getAdTipView());
        }
        FrameLayout pickTitleLayout = mView.findViewById(R.id.pickTitleLayout);
        pickTitleLayout.addView(getTitleView());
        nSelectMasking = mView.findViewById(R.id.nSelectMasking);
        nSelectItemList = mView.findViewById(R.id.nSelectItemList);
        fileItemAdapter = new FileItemAdapter(mActivitys.get(), mediaConfig, mFileList);
        fileItemAdapter.setCameraListener(() -> {
            chooseTypeTemp = chooseType;
            if (chooseTypeTemp == CPickType.images || chooseTypeTemp == CPickType.camera) {
                chooseType = CPickType.camera;
                startCamera();
            } else if (chooseTypeTemp == CPickType.videos || chooseTypeTemp == CPickType.record) {
                chooseType = CPickType.record;
                startRecordVideo();
            }
        });
        nSelectItemList.setAdapter(fileItemAdapter);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mActivitys.get(), mediaConfig.getSpanCount());
        nSelectItemList.addItemDecoration(new SpacesItemDecoration(mediaConfig.getItemDecoration()));
        nSelectItemList.setLayoutManager(mLayoutManager);
        initData();
    }

    protected View getTitleView() {
        //确定按钮状态变化
        mediaConfig.setOnSelectChangeListener(fileBeans -> {
            if (pickConfirmText != null) pickConfirmText.setEnabled(fileBeans.size() != 0);
        });
        View view = LayoutInflater.from(mActivitys.get()).inflate(R.layout.item_pick_title, null);
        view.findViewById(R.id.pickBackBtn).setOnClickListener(v -> dismiss());
        view.findViewById(R.id.pickConfirmBtn).setOnClickListener(v -> {
            if (mediaConfig.callback(mActivitys.get())) dismiss();
        });
        pickConfirmText = view.findViewById(R.id.pickConfirmText);
        pickFolderNameText = view.findViewById(R.id.pickFolderNameText);
        pickFolderNameText.setOnClickListener(v -> openFolder());
        return view;
    }

    protected void initData() {
        U_permissions.applyWriteStoragePermission(dialog.get().getContext(), new U_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {
                switch (chooseType) {
                    case CPickType.images:
                        pickFolderNameText.setText(R.string.pick_all_pic);
                        U_mediaList.getImgListByContentResolver(dialog.get(), mediaFolderMap -> {
                            dealData(mediaFolderMap);
                        });
                        break;
                    case CPickType.videos:
                        pickFolderNameText.setText(R.string.pick_all_video);
                        U_mediaList.getVideoListByContentResolver(mActivitys.get(), mediaFolderMap -> {
                            dealData(mediaFolderMap);
                        });
                        break;
                    case CPickType.audios:
                        pickFolderNameText.setText(R.string.pick_all_audio);
                        U_mediaList.getAudioListByContentResolver(mActivitys.get(), mediaFolderMap -> {
                            dealData(mediaFolderMap);
                        });
                        break;
                }
            }

            @Override
            public void requestPermissionFail(List<String> failPermission) {
                U_Toast.show(mActivitys.get().getString(R.string.no_write_permission));
            }
        });
    }

    public void dealData(Map<String, FolderBean> mediaFolderMap) {
        List<FolderBean> folderBeanList = new ArrayList<>();
        for (String key : mediaFolderMap.keySet()) {
            folderBeanList.add(mediaFolderMap.get(key));
        }
        initFolderList(folderBeanList);
        mFileList.clear();
        if (folderBeanList.size() > 0) {
            mFileList.addAll(folderBeanList.get(0).getMediaFileBeanList());
        }
        fileItemAdapter.notifyDataSetChanged(mFileList);
    }

    protected void initFolderList(List<FolderBean> folderBeanList) {
        if (folderBeanList != null && !folderBeanList.isEmpty()) {
            folderPopWindow = new FolderPopWindow(mActivitys.get(), folderBeanList, folderBean -> {
                setFolder(folderBean);
                closeFolder();
            });
            folderPopWindow.setOnDismissListener(this::closeFolder);
        }
    }

    protected void setFolder(FolderBean folder) {
        mFileList.clear();
        mFileList.addAll(folder.getMediaFileBeanList());
        pickFolderNameText.setText(folder.getFolderName());
        nSelectItemList.scrollToPosition(0);
        fileItemAdapter.notifyDataSetChanged(mFileList);
    }

    /**
     * 弹出文件夹列表
     */
    protected void openFolder() {
        if (!isOpenFolder) {
            nSelectMasking.setVisibility(View.VISIBLE);
            isOpenFolder = true;
            folderPopWindow.showAsDropDown(pickFolderNameText, 0, 25);
        }
    }

    /**
     * 收起文件夹列表
     */
    protected void closeFolder() {
        if (isOpenFolder) {
            nSelectMasking.setVisibility(View.GONE);
            isOpenFolder = false;
            folderPopWindow.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            chooseType = chooseTypeTemp;
        }
    }

    @Override
    protected void onDialogDismiss() {
        super.onDialogDismiss();
        fileItemAdapter.releasePlayer();
        removeAd();
        adLayout = null;
    }
}
