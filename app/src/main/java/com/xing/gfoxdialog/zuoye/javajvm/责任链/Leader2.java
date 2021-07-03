package com.xing.gfoxdialog.zuoye.javajvm.责任链;

import android.util.Log;

import java.net.URL;

public class Leader2 extends Leader {

    @Override
    public void handler(int level,String content) {
        //Leader2领导只能执行level大于0的事件
        if (level > 0) {
            Log.e("zdh", "------------Leader2主管同意 " + content);
        } else {
            Log.e("zdh", "------------Leader2主管未能处理 " + content + "转交给上级");
            if (getLeader() != null)
                getLeader().handler(level,content);//事件分发
        }

    }
}
