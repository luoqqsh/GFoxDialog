package com.xing.gfoxdialog.mvvm;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import com.xing.gfoxdialog.BR;
import com.xing.gfoxdialog.R;
import com.xing.gfoxdialog.databinding.MvvmActivityMainBinding;

import java.util.ArrayList;
import java.util.List;


public class MVVMActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MvvmActivityMainBinding viewDataBinding = DataBindingUtil.setContentView(this, R.layout.mvvm_activity_main);
        UserViewModel user = new UserViewModel("jett", "123", "http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg");
        viewDataBinding.setUser(user);
        //加载列表数据
        List<UserViewModel> data = new ArrayList<>();
        data.add(new UserViewModel("http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg", "1", "1"));
        data.add(new UserViewModel("http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg", "2", "2"));
        data.add(new UserViewModel("http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg", "3", "3"));
        data.add(new UserViewModel("http://e.hiphotos.baidu.com/image/pic/item/4610b912c8fcc3cef70d70409845d688d53f20f7.jpg", "4", "4"));

        viewDataBinding.listView.setAdapter(new UserAdapter<>(this,getLayoutInflater(),R.layout.mvvm_item, BR.user,data));
    }
}
