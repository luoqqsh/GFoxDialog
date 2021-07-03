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
        mTitle.setLeftButtonImage(R.mipmap.hl_back_white, v -> finish());
        findViewById(R.id.mainDialog).setOnClickListener(v -> startActivity(new Intent(mActivity, DialogActivity.class)));
        findViewById(R.id.mainJava).setVisibility(View.GONE);
        findViewById(R.id.mainPlayer).setOnClickListener(view -> startActivity(new Intent(mActivity, HHMedia3Activity.class)));
        findViewById(R.id.mainWeb).setOnClickListener(v -> startActivity(new Intent(mActivity, H5Activity.class)));
        findViewById(R.id.mainLog).setOnClickListener(v -> startActivity(new Intent(mActivity, LogActivity.class)));
        findViewById(R.id.mainOpenGL).setOnClickListener(v -> startActivity(new Intent(mActivity, GLActivity.class)));
        findViewById(R.id.mainConfusion).setOnClickListener(v -> startActivity(new Intent(mActivity, MappingActivity.class)));
        findViewById(R.id.mainNotice).setOnClickListener(v -> startActivity(new Intent(mActivity, NotificationActivity.class)));
        findViewById(R.id.mainBBZ).setOnClickListener(v -> {
//            OkHttpClient okHttpClient = new OkHttpClient.Builder().build();
//            Request request = new Request.Builder().url("114.114.114.114").build();
//            WebSocket webSocket = okHttpClient.newWebSocket(request, new WebSocketListener() {
//                @Override
//                public void onClosed(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
//                    super.onClosed(webSocket, code, reason);
//                }
//
//                @Override
//                public void onClosing(@NotNull WebSocket webSocket, int code, @NotNull String reason) {
//                    super.onClosing(webSocket, code, reason);
//                }
//
//                @Override
//                public void onFailure(@NotNull WebSocket webSocket, @NotNull Throwable t, @org.jetbrains.annotations.Nullable Response response) {
//                    super.onFailure(webSocket, t, response);
//                }
//
//                @Override
//                public void onMessage(@NotNull WebSocket webSocket, @NotNull String text) {
//                    super.onMessage(webSocket, text);
//                }
//
//                @Override
//                public void onMessage(@NotNull WebSocket webSocket, @NotNull ByteString bytes) {
//                    super.onMessage(webSocket, bytes);
//                }
//
//                @Override
//                public void onOpen(@NotNull WebSocket webSocket, @NotNull Response response) {
//                    super.onOpen(webSocket, response);
//                }
//            });
//            okHttpClient.dispatcher().executorService().shutdown();
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
