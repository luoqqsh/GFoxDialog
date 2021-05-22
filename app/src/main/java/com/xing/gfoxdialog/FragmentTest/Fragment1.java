package com.xing.gfoxdialog.FragmentTest;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xing.gfox.base.fragment.HLBaseFragment;
import com.xing.gfoxdialog.R;
import com.xing.gfoxdialog.databinding.FragmentaBinding;

import java.util.HashMap;
import java.util.Map;


public class Fragment1 extends HLBaseFragment {
    FragmentaBinding binding;
    Wallet1 f1;

    Wallet2 g1;

    @Override
    public View getLayoutView() {
        binding = FragmentaBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void initUI(Bundle savedInstanceState) {
            f1 = new Wallet1();
            Bundle bundle1 = new Bundle();
            bundle1.putString("title", "wallet1");
            f1.setArguments(bundle1);
            g1 = new Wallet2();
            Bundle bundle = new Bundle();
            bundle.putString("title", "wallet2");
            g1.setArguments(bundle);


        showFragment(0);
        binding.wallet1.setOnClickListener(v -> showFragment(0));
        binding.wallet2.setOnClickListener(v -> showFragment(1));


    }

    private String[] mTags = {"首页", "积分中心"};
    private int mCurrentPosition = -1;


    private void showFragment(int position) {
        try {
            if (position < 0) position = 0;
            if (position > mTags.length - 1) position = mTags.length - 1;
            if (mCurrentPosition == position) {
                return;
            }
            mCurrentPosition = position;
            String tag = mTags[position];

            FragmentManager fm = getChildFragmentManager();
            Map<String, Fragment> fragmentMap = new HashMap<>();
            for (String t : mTags) {
                Fragment f = fm.findFragmentByTag(t);
                if (f != null) fragmentMap.put(t, f);
            }

            FragmentTransaction bt = fm.beginTransaction();
            if (!fragmentMap.containsKey(tag)) {
                Fragment fragment = null;
                switch (position) {
                    case 0:
                        fragment = f1;
                        break;
                    case 1:
                        fragment = g1;//积分中心
                        break;
                    default:
                        break;
                }
                if (fragment != null) {
                    bt.add(R.id.fragmentContainer, fragment, tag);
                    fragmentMap.put(tag, fragment);
                }
            }

            for (String t : fragmentMap.keySet()) {
                Fragment fragment = fragmentMap.get(t);
                if (fragment != null) {
                    if (t.equals(tag)) {
                        fragment.onResume();
                        bt.show(fragment);
                    } else {
                        fragment.onPause();
                        bt.hide(fragment);
                    }
                }
            }

//            bt.commitAllowingStateLoss();
            bt.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
