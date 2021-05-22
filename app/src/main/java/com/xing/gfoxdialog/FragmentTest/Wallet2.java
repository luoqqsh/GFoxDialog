package com.xing.gfoxdialog.FragmentTest;

import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.Observer;

import com.xing.gfox.base.fragment.HLBaseFragment;
import com.xing.gfox.liveEventBus.LiveEventBus;
import com.xing.gfoxdialog.R;
import com.xing.gfoxdialog.databinding.FragmentOginBinding;


public class Wallet2 extends HLBaseFragment {
    FragmentOginBinding binding;
    Wallet2Login g1;

    @Override
    public View getLayoutView() {
        binding = FragmentOginBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        LiveEventBus.get("wallet2", Boolean.class).observe(mActivity, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                login(aBoolean);
            }
        });
        login(false);
    }

    private void login(boolean isLogin) {
        if (isLogin) {
            g1 = new Wallet2Login();
            Bundle bundle1 = new Bundle();
            bundle1.putString("title", "wallet2已登录");
            g1.setArguments(bundle1);
        } else {
            g1 = new Wallet2Login();
            Bundle bundle = new Bundle();
            bundle.putString("title", "wallet2未登录");
            g1.setArguments(bundle);
        }
        getChildFragmentManager().beginTransaction().replace(R.id.fragmentlog, g1).commit();
    }
}
