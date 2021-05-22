package com.xing.gfoxdialog.zuoye.javajvm.责任链;

import android.util.Log;

public class Leader3 extends Leader {

    @Override
    public void handler(int level, String content) {
        //Leader3领导只能执行level大于-1的事件
        if (level > -1) {
            Log.e("zdh", "-----------Learder3 主管同意 " + content);
        } else {
            Log.e("zdh", "------------Learder3 主管未能处理 " + content + "转交给上级");
            if (getLeader() != null)
                getLeader().handler(level, content);//事件分发
        }
    }
}
