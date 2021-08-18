package com.xing.gfoxdialog;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;

import androidx.annotation.Nullable;

import com.xing.gfox.log.ViseLog;
import com.xing.gfoxdialog.BaseApp.BaseActivity;
import com.xing.gfoxdialog.Media.HHMedia3Activity;
import com.xing.gfoxdialog.databinding.ActivityMainBinding;


public class DialogMainJavaActivity extends BaseActivity {
    ActivityMainBinding mainBinding;

    @Override
    public View getLayoutView() {
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        return mainBinding.getRoot();
    }

    @Override
    protected int getBackgroundColorResource() {
        return R.color.brown;
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        init();
    }

    @Override
    public boolean isShowTitle() {
        return true;
    }

    private void init() {
        mTitle.setTitleText("Java版本首页");
        mTitle.setLeftButtonImage(R.mipmap.back_white, v -> finish());
        findViewById(R.id.mainDialog).setOnClickListener(v -> startActivity(new Intent(mActivity, DialogActivity.class)));
        findViewById(R.id.mainJava).setVisibility(View.GONE);
        findViewById(R.id.mainPlayer).setOnClickListener(view -> startActivity(new Intent(mActivity, HHMedia3Activity.class)));
        findViewById(R.id.mainWeb).setOnClickListener(v -> startActivity(new Intent(mActivity, H5Activity.class)));
        findViewById(R.id.mainLog).setOnClickListener(v -> startActivity(new Intent(mActivity, LogActivity.class)));
        findViewById(R.id.mainConfusion).setOnClickListener(v -> startActivity(new Intent(mActivity, MappingActivity.class)));
        findViewById(R.id.mainNotice).setOnClickListener(v -> startActivity(new Intent(mActivity, NotificationActivity.class)));
        findViewById(R.id.mainBBZ).setOnClickListener(v -> {

        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == RESULT_OK && null != data) {
            Uri selectedVideo = data.getData();
            String[] filePathColumn = {MediaStore.Video.Media.DATA};
            Cursor cursor = mActivity.getContentResolver().query(selectedVideo, filePathColumn, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String videoPath = cursor.getString(columnIndex);
            cursor.close();
            ViseLog.d(videoPath);
        }
    }
}
