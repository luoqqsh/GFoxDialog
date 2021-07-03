package com.xing.gfoxdialog.FragmentTest;

import android.os.Bundle;
import android.view.View;

import com.xing.gfox.base.fragment.HLBaseFragment;
import com.xing.gfoxdialog.databinding.FragmentcBinding;


public class Fragment3 extends HLBaseFragment {
    FragmentcBinding binding;

    @Override
    public View getLayoutView() {
        binding = FragmentcBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {

    }
}
