package com.xing.gfoxdialog.zuoye.javajvm.责任链;

import java.util.ArrayList;
import java.util.List;

public class LeaderManager {
    private List<Leader> mList = new ArrayList<>();
    private int mLear;//能执行事件的等级
    private String mContent;//执行的事件

    public LeaderManager(int lear,  String content) {
        mLear = lear;
        mContent = content;
    }

    public void addLeaders(Leader Leader) {
        if (mList != null)
            mList.add(Leader);
    }

    public void removeLeaders(Leader Leader) {
        if (mList != null)
            mList.remove(Leader);
    }

    //执行
    public void exit() {
        if (mList.size() == 0) {
            return;
        }
        for (int i = 0; i < mList.size(); i++) {
            if (i != mList.size() - 1) {
                //向上级领导分发事件
                if (i + 1 < mList.size()) {
                    mList.get(i).setLeader(mList.get(i + 1));
                }
            }
        }
        //开始执行事件
        mList.get(0).handler(mLear,mContent);
    }

}
