package com.xing.gfoxdialog.api;


import com.xing.gfox.rxHttp.commonApi.LivedataService;

public class TestEnv {

    /**
     * 测试图片上传
     *
     * @return
     */
    public static LivedataService getUploadApi() {
        return RetrofitCreateLiveDataHelper.getInstance().create("http://8.134.60.146:8082/", LivedataService.class);
    }
}
