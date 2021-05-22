package com.xing.gfoxdialog.api;


import com.xing.gfox.rxHttp.commonApi.LivedataService;
import com.xing.gfox.rxHttp.commonApi.ObservableService;

public class APIConstant {
    public static final int TIMEOUT_READ = 60;
    public static final int TIMEOUT_CONNECTION = 60;
    public static String baseUrl;
    public static String socketUrl;

    public static void changeEnv(int env) {
        switch (env) {
            case 1://测试环境
                baseUrl = "http://159.75.208.229:8081/";
                socketUrl = "";
                break;
        }
    }

    public static ObservableService getObservableApi() {
        return RetrofitCreateLiveDataHelper.getInstance().create(baseUrl, ObservableService.class);
    }

    public static LivedataService getLivedataApi() {
        return RetrofitCreateLiveDataHelper.getInstance().create(baseUrl, LivedataService.class);
    }
}