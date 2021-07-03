package com.xing.gfoxdialog.FragmentTest;

import android.os.Bundle;
import android.view.View;

import com.xing.gfox.base.fragment.HLBaseFragment;
import com.xing.gfoxdialog.databinding.FragmentbBinding;


public class Fragment2 extends HLBaseFragment {
    FragmentbBinding binding;

    @Override
    public View getLayoutView() {
        binding = FragmentbBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {

    }
}
