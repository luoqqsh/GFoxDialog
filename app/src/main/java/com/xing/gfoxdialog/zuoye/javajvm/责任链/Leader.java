package com.xing.gfoxdialog.zuoye.javajvm.责任链;

public abstract class Leader {
    private Leader leader;

    public Leader getLeader() {
        return leader;
    }

    public void setLeader(Leader leader) {
        this.leader = leader;
    }

    public abstract void handler(int level, String content);
}
