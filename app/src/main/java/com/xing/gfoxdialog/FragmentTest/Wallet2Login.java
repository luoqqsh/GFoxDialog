package com.xing.gfoxdialog.FragmentTest;

import android.os.Bundle;
import android.view.View;

import com.xing.gfox.base.fragment.HLBaseFragment;
import com.xing.gfox.util.U_statusbar;
import com.xing.gfoxdialog.R;
import com.xing.gfoxdialog.databinding.FragmentOginOntentBinding;


public class Wallet2Login extends HLBaseFragment {
    FragmentOginOntentBinding binding;


    @Override
    public View getLayoutView() {
        binding = FragmentOginOntentBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
        U_statusbar.setStatusBarColor(mActivity, getResources().getColor(R.color.pink));
        binding.getRoot().setBackgroundColor(getResources().getColor(R.color.yellow));

        String title = getArguments().getString("title");
        binding.text1.setText(title);

    }
}
