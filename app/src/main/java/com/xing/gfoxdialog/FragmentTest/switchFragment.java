package com.xing.gfoxdialog.FragmentTest;

import android.os.Bundle;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xing.gfox.base.activity.HLBaseActivity;
import com.xing.gfox.liveEventBus.LiveEventBus;
import com.xing.gfoxdialog.R;
import com.xing.gfoxdialog.databinding.FragmentActBinding;

import java.util.HashMap;
import java.util.Map;


public class switchFragment extends HLBaseActivity {
    FragmentActBinding binding;
    Fragment1 f1;
    Fragment2 f2;
    Fragment3 f3;

    @Override
    public View getLayoutView() {
        binding = FragmentActBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    protected int getBackgroundColor() {
        return getResources().getColor(R.color.white);
    }

    boolean wallet1 = true;
    boolean wallet2 = true;

    @Override
    public void initUI(Bundle savedInstanceState) {
        f1 = new Fragment1();
        f2 = new Fragment2();
        f3 = new Fragment3();
        showFragment(0);
        binding.tv1.setOnClickListener(v -> showFragment(0));
        binding.tv2.setOnClickListener(v -> {
            LiveEventBus.get("wallet1").post(wallet1);
            wallet1 = !wallet1;
            showFragment(1);
        });
        binding.tv3.setOnClickListener(v -> {
            LiveEventBus.get("wallet2").post(wallet2);
            wallet2 = !wallet2;
            showFragment(2);
        });

    }


    private String[] mTags = {"首页", "积分中心", "发现"};
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

            FragmentManager fm = getSupportFragmentManager();
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
                        fragment = f2;//积分中心
                        break;
                    case 2:
                        fragment = f3;
                        break;
                    default:
                        break;
                }
                if (fragment != null) {
                    bt.add(R.id.loadFragment, fragment, tag);
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
