package com.xing.gfox.fliepick.media;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xing.gfox.R;
import com.xing.gfox.base.interfaces.HOnListener;
import com.xing.gfox.base.pop.BaseNPop;
import com.xing.gfox.fliepick.bean.FolderBean;
import com.xing.gfox.util.U_screen;

import java.util.List;


public class FolderPopWindow extends BaseNPop {
    private RecyclerView popFolderList;

    public FolderPopWindow(FragmentActivity mActivity, List<FolderBean> folderBeanList, HOnListener<FolderBean> mediaFolderBeanHOnListener) {
        super(mActivity, ViewGroup.LayoutParams.MATCH_PARENT, (int) (U_screen.getScreenHeight(mActivity) * 0.6));
        FolderItemAdapter folderItemAdapter = new FolderItemAdapter(getActivity(), folderBeanList);
        folderItemAdapter.setOnFolderSelectListener(folderBean -> {
            if (mediaFolderBeanHOnListener != null) {
                mediaFolderBeanHOnListener.onListen(folderBean);
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        popFolderList.setLayoutManager(mLayoutManager);
        popFolderList.setAdapter(folderItemAdapter);
    }

    @Override
    protected void initUI(View layout) {
        popFolderList = layout.findViewById(R.id.popFolderList);
    }

    @Override
    public View getLayout(Context context) {
        return null;
    }

    @Override
    public int getLayoutId() {
        return R.layout.pop_select_folder;
    }
}
