package com.xing.gfox.dialog;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.File;

import com.xing.gfox.R;
import com.xing.gfox.base.dialog.BaseNDialog;
import com.xing.gfox.base.interfaces.HOnListener;
import com.xing.gfox.fliepick.FileSelectorView;
import com.xing.gfox.log.ViseLog;
import com.xing.gfox.util.U_file;

public class CFileListDialog extends BaseNDialog {
    private TextView hl_list_path;
    private HOnListener<File> onSelectFileListener;
    private FileSelectorView fileSelectorView;
    private String path;

    @Override
    protected int getLayoutId() {
        return R.layout.ndialog_listfile;
    }

    @Override
    protected void initUI(View mView, Bundle bundle) {
        hl_list_path = mView.findViewById(R.id.hl_list_path);
        fileSelectorView = mView.findViewById(R.id.hl_list_file);
        if (path != null && U_file.isFolderExist(path)) {
            fileSelectorView.setCurrentDirectory(new File(path));
        }
        hl_list_path.setText("当前路径: " + fileSelectorView.getCurrentDirectory().getAbsolutePath());
        //设置文件过滤
//        fileSelectorView.setFileFilter(new FileExtendFilter(Arrays.asList("shp", "kml"))); // 设置过滤规则
//        fileSelectorView.setFileFilter(new FileContainsFieldsFilter(Arrays.asList("shp")));

        //自定义文件图标
//        fileSelectorView.setFileIconFactory(new FileSelectorView.FileIconCreator() {
//            public Drawable getIcon(File file) {
//                if (file == null) {
//                    return getResources().getDrawable(R.drawable.rotating);
//                } else {
//                    return getResources().getDrawable(R.drawable.layers3);
//                }
//            }
//        });

        //自定义部分文件图标
//        fileSelectorView.setFileIconCreator(new FileSelectorView.FileIconCreator() {
//            @Override
//            public Drawable getIcon(File file) {
//                if (file == null) {
//                    return getResources().getDrawable(R.mipmap.layers3);
//                } else {
//                    return new DefaultFileIconCreator(getActivity()).getIcon(file); //走默认逻辑
//                }
//            }
//        });

        //设置选择文件的监听
        fileSelectorView.setOnFileSelectedListener(new FileSelectorView.OnFileSelectedListener() {
            @Override
            public void onSelected(File selectedFile) {
                ViseLog.d(selectedFile.getAbsolutePath());
                if (onSelectFileListener != null) {
                    onSelectFileListener.onListen(selectedFile);
                }
            }

            @Override
            public void onFilePathChanged(File file) {
                hl_list_path.setText("当前路径: " + file.getAbsolutePath());
            }
        });
    }

    public void setOnSelectFileListener(HOnListener<File> onSelectFileListener) {
        this.onSelectFileListener = onSelectFileListener;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
