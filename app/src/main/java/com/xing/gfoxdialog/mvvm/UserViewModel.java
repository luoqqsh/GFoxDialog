package com.xing.gfoxdialog.mvvm;

import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.xing.gfoxdialog.BR;


/**
 * 这个类就相当于一个ViewModel
 */
public class UserViewModel extends BaseObservable {
    private String name;
    private String password;

    private String header;

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public UserViewModel(String name, String password, String header) {
        this.name = name;
        this.password = password;
        this.header = header;
    }

    @Bindable
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        notifyPropertyChanged(BR.name);

    }

    @Bindable
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
        notifyPropertyChanged(BR.password);
    }

    //自定义属性：提供一个静态方法来加载image
    @BindingAdapter("bind:header")
    public static void getImage(ImageView view, String url) {
        Glide.with(view.getContext()).load(url).into(view);
    }

    //点击事件
    public void clickMima(View view) {
        Toast.makeText(view.getContext(), getName(), Toast.LENGTH_SHORT).show();
    }
}
