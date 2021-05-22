package com.xing.gfoxdialog.zuoye.javajvm.责任链;

public class test {
    private void test() {
        LeaderManager leaderManager = new LeaderManager(0, "小明 申请日本旅游");//传递下发任务可执行等级
        leaderManager.addLeaders(new Leader1());
        leaderManager.addLeaders(new Leader2());
        leaderManager.addLeaders(new Leader3());
        leaderManager.exit();//开始执行
        //aaa();
    }
}
