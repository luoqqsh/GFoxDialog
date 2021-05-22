package com.xing.gfox.fliepick.media;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xing.gfox.R;
import com.xing.gfox.base.interfaces.HOnListener;
import com.xing.gfox.base.toast.U_Toast;
import com.xing.gfox.fliepick.bean.CPickType;
import com.xing.gfox.fliepick.bean.FileBean;
import com.xing.gfox.fliepick.bean.FolderBean;
import com.xing.gfox.fliepick.bean.MediaSelectListener;
import com.xing.gfox.log.ViseLog;
import com.xing.gfox.media.U_mediaList;
import com.xing.gfox.util.U_permissions;
import com.xing.gfox.view.Recyclerview.SpacesItemDecoration;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class CMediaPickDialog2 extends BasePickDialog {
    private RecyclerView nSelectItemList;
    private FileItemAdapter fileItemAdapter;
    protected boolean isOpenFolder;
    private View nSelectMasking;
    private TextView pickFolderNameText, pickConfirmText;
    private FolderPopWindow folderPopWindow;
    private FrameLayout adLayout;
    private int page;
    private boolean isEnd;
    private boolean isLoadingData;

    private CMediaPickDialog2() {
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

        public CMediaPickDialog2 startSelectImg(FragmentActivity mActivity) {
            CMediaPickDialog2 cMediaPickDialog = new CMediaPickDialog2();
            if (mediaConfig.getMediaType() == null) {
                mediaConfig.setMediaType(MediaConfig.imgType);
            }
            cMediaPickDialog.setMediaConfig(mediaConfig);
            cMediaPickDialog.setChooseType(CPickType.images);
            cMediaPickDialog.showNow(mActivity);
            return cMediaPickDialog;
        }

        public CMediaPickDialog2 startSelectVideo(FragmentActivity mActivity) {
            CMediaPickDialog2 cMediaPickDialog = new CMediaPickDialog2();
            if (mediaConfig.getMediaType() == null) {
                mediaConfig.setMediaType(MediaConfig.videoType);
            }
            cMediaPickDialog.setMediaConfig(mediaConfig);
            cMediaPickDialog.setChooseType(CPickType.videos);
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
        mediaConfig.setAdTipView(null);
        if (adLayout != null) {
            adLayout.removeAllViews();
            adLayout.setVisibility(View.GONE);
        }
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
            if (chooseTypeTemp == CPickType.images) {
                chooseType = CPickType.camera;
                startCamera();
            } else if (chooseTypeTemp == CPickType.videos) {
                chooseType = CPickType.record;
                startRecordVideo();
            }
        });
        nSelectItemList.setAdapter(fileItemAdapter);
        GridLayoutManager mLayoutManager = new GridLayoutManager(mActivitys.get(), mediaConfig.getSpanCount());
        nSelectItemList.addItemDecoration(new SpacesItemDecoration(mediaConfig.getItemDecoration()));
        nSelectItemList.setLayoutManager(mLayoutManager);
        initData(null, 0);
    }

    protected View getTitleView() {
        //确定按钮状态变化
        mediaConfig.setOnSelectChangeListener(fileBeans -> {
            if (fileBeans.size() == 0) {
                if (pickConfirmText != null) pickConfirmText.setEnabled(false);
            } else {
                if (pickConfirmText != null) pickConfirmText.setEnabled(true);
            }
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

    protected void initData(String dirPath, int type) {
        if (mediaConfig == null) {
            ViseLog.d("配置异常");
            dismiss();
            return;
        }
        if (isLoadingData) return;
        U_permissions.applyWriteStoragePermission(mActivitys.get(), new U_permissions.RequestPermissionCallBack() {
            @Override
            public void requestPermissionSuccess() {
                pickFolderNameText.setText(R.string.pick_all_pic);
                if (type == 0 || type == 1) {
                    page = 0;
                } else {
                    page++;
                }
                U_mediaList.getMediaFromLoaderByPage(mActivitys.get(), mediaConfig.getMediaType(), dirPath, page, 30, chooseType, new HOnListener<List<FileBean>>() {
                    @Override
                    public void onListen(List<FileBean> fileBeans) {
                        if (type == 0 || type == 1) {
                            mFileList.clear();
                        }
                        boolean isChange;
                        isEnd = fileBeans == null || fileBeans.size() == 0;
                        if (fileBeans != null && fileBeans.size() > 0) {
                            isChange = true;
                            mFileList.addAll(fileBeans);
                        } else {
                            isChange = false;
                        }
                        if (fileItemAdapter != null) {
                            if (type == 0 || type == 1) {
                                fileItemAdapter.notifyDataSetChanged();
                                nSelectItemList.scrollToPosition(0);
                            } else {
                                if (isChange) {
                                    fileItemAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                        if (type == 0) {
                        }
                        if (mFileList.size() == 0) {
                            U_Toast.show(mActivitys.get().getString(R.string.NoImg));
                        }
                        isLoadingData = false;
                    }
                });
            }

            @Override
            public void requestPermissionFail(Map<String, Boolean> failPermission) {
                U_Toast.show(mActivitys.get().getString(R.string.no_permission));
            }
        });
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
}
