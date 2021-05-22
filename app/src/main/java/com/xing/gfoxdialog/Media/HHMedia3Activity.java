package com.xing.gfoxdialog.Media;

import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.cz.cvplayer_ali.HHALiPlayer;
import com.xing.gfox.base.activity.HLBaseActivity;
import com.xing.gfox.log.ViseLog;
import com.xing.gfox.util.U_screen;
import com.xing.gfox.util.U_time;
import com.xing.gfoxdialog.databinding.ActivityMediaviewBinding;
import com.xing.hhplayer.common.CVPlayer;
import com.xing.hhplayer.common.base.listener.OnPlayerListener;
import com.xing.hhplayer.common.bean.TvList.TvData;
import com.xing.hhplayer.common.bean.TvList.TvDataList;
import com.xing.hhplayer.common.bean.TvList.TvView;
import com.xing.hhplayer.common.bean.TvList.TypeClarity;
import com.xing.hhplayer.common.bean.TvList.TypeClass;
import com.xing.hhplayer.common.bean.TvList.TypeState;

import java.util.ArrayList;


public class HHMedia3Activity extends HLBaseActivity {
    private CVPlayer hhPlayer3;
    private boolean isMute = false;
    private TvData tvUrl;
    private ActivityMediaviewBinding binding;

    @Override
    public View getLayoutView() {
        binding = ActivityMediaviewBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        initList();
        initUI();
        U_screen.setViewSize(binding.hhPlayerView, ViewGroup.LayoutParams.MATCH_PARENT, U_screen.getHeightFromWidthAtPort(mActivity));
        hhPlayer3 = new CVPlayer(this, new HHALiPlayer(this), TvView.NEWSURFACE, binding.hhPlayerView);
        hhPlayer3.addLiveView();
        hhPlayer3.setPlayWhenPrepared(true);
        hhPlayer3.prepare("rtsp://27.148.219.129:554/live/ch17092208080814677315.sdp?playtype=1&boid=001&backupagent=27.148.219.129:554&clienttype=1&time=20191117235503+08&life=172800&ifpricereqsnd=1&vcdnid=001&userid=5922751202156&mediaid=ch17092208080814677315&ctype=2&TSTVTimeLife=7200&contname=&authid=0&terminalflag=1&UserLiveType=0&stbid=65100499007034400000288CB89A9654&nodelevel=3&AuthInfo=BESeR9eMRDi8RsZJsJm5ubp8oBukTMa0qFw9c8POQwJfeqIynThSnMQUlmZG4XzktcJ7NgDL0pa6L%2FNky0Uo7A%3D%3D&bitrate=2000");
        hhPlayer3.setOnPreparedListener(new OnPlayerListener<Object>() {
            @Override
            public void onListen(Object o) {
                hhPlayer3.start();
                binding.testSeekVolume.setProgress((int) (hhPlayer3.getVolume() * 10));
                binding.testTotalTime.setText(" / " + U_time.formatTime(hhPlayer3.getMediaDurationMS()));
                binding.testSeekProgress.setMax((int) (hhPlayer3.getMediaDurationMS() / 1000));
            }
        });
        hhPlayer3.setOnProgressListener(new OnPlayerListener<Long>() {
            @Override
            public void onListen(Long time) {//当前是timer线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        binding.testCurrentTime.setText(U_time.formatTime(time));
                        binding.testSeekVolume.setProgress((int) (time / 1000));
                    }
                });
            }
        });
        hhPlayer3.setOnBufferingUpdateListener(new OnPlayerListener<Integer>() {
            @Override
            public void onListen(Integer integer) {
                binding.testSeekProgress.setSecondaryProgress(integer);
            }
        });
    }

    private void initList() {
        TvDataList tvDataList = new TvDataList(TypeClass.厦门台, "厦门电视台1", "http://cstvpull.live.wscdns.com/live/xiamen1.flv", TypeClarity.H4k, TypeState.流畅);
        TvDataList tvDataList2 = new TvDataList(TypeClass.厦门台, "厦门电视台2", "http://cstvpull.live.wscdns.com/live/xiamen2.flv", TypeClarity.H4k, TypeState.流畅);
        TvDataList tvDataList3 = new TvDataList(TypeClass.厦门台, "厦门电视台3", "http://cstvpull.live.wscdns.com/live/xiamen3.flv", TypeClarity.H4k, TypeState.流畅);
        TvDataList tvDataList4 = new TvDataList(TypeClass.厦门台, "厦门电视台4", "http://cstvpull.live.wscdns.com/live/xiamen4.flv", TypeClarity.H4k, TypeState.流畅);
        TvDataList tvDataList5 = new TvDataList(TypeClass.厦门台, "厦门卫视", "http://cstvpull.live.wscdns.com/live/xiamen.flv", TypeClarity.H4k, TypeState.流畅);
        TvDataList tvDataList6 = new TvDataList(TypeClass.厦门台, "厦门移动电视", "http://cstvpull.live.wscdns.com/live/xiamenyidong.flv", TypeClarity.H4k, TypeState.流畅);
        ArrayList<TvDataList> tvDataLists = new ArrayList<>();
        tvDataLists.add(tvDataList);
        tvDataLists.add(tvDataList2);
        tvDataLists.add(tvDataList3);
        tvDataLists.add(tvDataList4);
        tvDataLists.add(tvDataList5);
        tvDataLists.add(tvDataList6);
        tvUrl = new TvData(1, "20190719", "itv", tvDataLists);
    }

    float i = 0f;

    private void initUI() {
        startOriListener();
        binding.testSeekProgress.setMax(100);
        binding.testSeekVolume.setMax(10);

        binding.testSelect.setOnClickListener(v -> {
//            hhPlayer3.prepare(tvUrl.getLinks(), 0);
//            hhPlayer3.prepare(hl_file.SDROOT + "/local_video.mp4");
            hhPlayer3.prepare("http://cstvpull.live.wscdns.com/live/xiamen1.flv");
//            hhPlayer3.prepare("http://s.bizhijingling.com/uploadfile/coustom/2019/309/20190613170125729.mp4");
        });

        binding.testPlay.setOnClickListener(v -> hhPlayer3.start());
        binding.testPause.setOnClickListener(v -> hhPlayer3.pause());
        binding.testStop.setOnClickListener(v -> hhPlayer3.stop());
        binding.testReset.setOnClickListener(v -> {
            hhPlayer3.resetPlayer();
            hhPlayer3.prepare("http://s.bizhijingling.com/uploadfile/coustom/2019//309/20190613170125729.mp4");
        });
        binding.testFull.setOnClickListener(v -> {
            hhPlayer3.fullScreen();
            hideBar();
        });
        binding.testExit.setOnClickListener(v -> hhPlayer3.exitFullScreen());
        binding.testMute.setOnClickListener(v -> hhPlayer3.setMute(!isMute));
        binding.testFast.setOnClickListener(v -> hhPlayer3.setSpeed(2));
        binding.testSlow.setOnClickListener(v -> hhPlayer3.setSpeed(0.5f));
        binding.testRelease.setOnClickListener(v -> hhPlayer3.release());
        binding.testGo5.setOnClickListener(v -> {
            if (hhPlayer3.getCurrentPositionMS() + 5000 < hhPlayer3.getMediaDurationMS()) {
                hhPlayer3.seekTo(hhPlayer3.getCurrentPositionMS() + 5000);
            }
        });
        binding.testBack5.setOnClickListener(v -> {
            if (hhPlayer3.getCurrentPositionMS() > 5000) {
                hhPlayer3.seekTo(hhPlayer3.getCurrentPositionMS() - 5000);
            }
        });
        binding.testGet.setOnClickListener(v -> {
//            hhPlayer3.changeScale(i);
            i += 0.1;
            if (i > 2) {
                i = 0;
            }
            ViseLog.d(i);
//            hhPlayer3.takeBitmap(bitmap -> runOnUiThread(() -> binding.imgvTest.setImageBitmap(bitmap)));
//            hhPlayer3.setEffect(new VignetteEffect(i));
        });
        binding.testRotate.setOnClickListener(v -> {
            //判断当前是否为横屏,判断是否旋转
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                U_screen.setViewSize(hhPlayer3.getPlayerView(), U_screen.getDeviceWidth(mActivity), U_screen.getDeviceHeight(mActivity));
                hideBar();
            } else {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
                U_screen.setViewSize(hhPlayer3.getPlayerView(), ViewGroup.LayoutParams.MATCH_PARENT, U_screen.getHeightFromWidthAtLand(mActivity));
                showBar();
            }
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//横屏
//            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏
        });
        binding.testSeekProgress.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progess, boolean fromUser) {
                if (fromUser) {
                    hhPlayer3.seekTo(progess * 1000);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        binding.testSeekVolume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progess, boolean fromUser) {
                if (fromUser) {
                    hhPlayer3.setVolume((float) progess / 10);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.testKeyUp.setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_DPAD_UP));
        binding.testKeyDown.setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_DPAD_DOWN));
        binding.testKeyLeft.setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_DPAD_LEFT));
        binding.testKeyRight.setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_DPAD_RIGHT));
        binding.testKeyMenu.setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_MENU));
        binding.testKeyBack.setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_BACK));
        binding.testKeyEnter.setOnClickListener(v -> hhPlayer3.sendKeyDown(KeyEvent.KEYCODE_DPAD_CENTER));
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        hhPlayer3.sendKeyDown(keyCode);
        return false;//return false 会调用系统的方法
    }

    @Override
    public void changeToLand() {
        if (hhPlayer3.getPlayerView() != null) {
            U_screen.setViewSize(hhPlayer3.getPlayerView(), U_screen.getDeviceWidth(mActivity), U_screen.getDeviceHeight(mActivity));
            hideBar();
        }
    }

    @Override
    public void changeToPort() {
        if (hhPlayer3.getPlayerView() != null) {
            U_screen.setViewSize(hhPlayer3.getPlayerView(), ViewGroup.LayoutParams.MATCH_PARENT, U_screen.getHeightFromWidthAtLand(mActivity));
            showBar();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (hhPlayer3 != null) {
            hhPlayer3.release();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (hhPlayer3 != null) {
            hhPlayer3.pause();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (hhPlayer3 != null) {
            hhPlayer3.start();
        }
    }
}
