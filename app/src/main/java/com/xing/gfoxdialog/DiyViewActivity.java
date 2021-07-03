package com.xing.gfoxdialog;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.xing.gfoxdialog.BaseApp.BaseActivity;
import com.xing.gfoxdialog.databinding.ActivityDiyBinding;


public class DiyViewActivity extends BaseActivity {
    ActivityDiyBinding binding;

    @Override
    public boolean isShowTitle() {
        return true;
    }

    @Override
    protected int getBackgroundColorResource() {
        return R.color.white;
    }

    @Override
    public View getLayoutView() {
        binding = ActivityDiyBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        mTitle.setLeftButtonImage(R.mipmap.hl_back_black, v -> finish());
        mTitle.setTitleText("DIYView");
        binding.countDownBtn.setOnClickListener(v -> {
            Toast.makeText(mActivity, "click countdown button", Toast.LENGTH_SHORT).show();
            binding.countDownBtn.startCountDown();
        });

    }
}
