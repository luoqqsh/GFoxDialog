package com.xing.gfoxdialog.api;

import androidx.lifecycle.LiveData;

import com.xing.gfox.rxHttp.commonApi.LivedataService;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

/**
 * 用户相关
 */
public interface UpLoadApi extends LivedataService {
    @Multipart
    @POST("user/uploadHead")
    LiveData<BaseApiResponse<String>> uploadHead(
            @Part MultipartBody.Part file
    );
}
