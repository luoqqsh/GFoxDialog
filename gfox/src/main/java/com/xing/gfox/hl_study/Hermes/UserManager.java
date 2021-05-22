package com.xing.gfox.hl_study.Hermes;

public class UserManager {

    private Friend friend;
    private static UserManager sInstance = null;

    public UserManager() {
    }

    public static synchronized UserManager getInstance() {
        if (sInstance == null) {
            sInstance = new UserManager();
        }
        return sInstance;
    }

    public Friend getFriend() {
        return friend;
    }

    public void setFriend(Friend friend) {
        this.friend = friend;
    }

    public static UserManager getsInstance() {
        return sInstance;
    }

    public static void setsInstance(UserManager sInstance) {
        UserManager.sInstance = sInstance;
    }
}
