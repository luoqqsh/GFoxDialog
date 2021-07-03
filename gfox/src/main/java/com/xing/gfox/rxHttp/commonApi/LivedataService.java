package com.xing.gfox.rxHttp.commonApi;

import androidx.lifecycle.LiveData;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface LivedataService {

    //encoded true---URL不转码，默认的false表示URL转码
    //@FormUrlEncoded 必须配@FieldMap参数，且参数不能为空，此时，日志url不附带参数

    @FormUrlEncoded
    @POST("{path}")
    LiveData<String> httpPostForm(@Path(value = "path", encoded = true) String path, @FieldMap Map<String, Object> params);

    @POST("{path}")
    LiveData<String> httpPost(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, Object> params);

    @GET("{path}")
    LiveData<String> httpGet(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, Object> params);


    @FormUrlEncoded
    @POST
    LiveData<String> httpPostFormUseUrl(@Url String url, @FieldMap Map<String, Object> params);

    @POST
    LiveData<String> httpPostUseUrl(@Url String url, @QueryMap Map<String, Object> params);

    @GET
    LiveData<String> httpGetUseUrl(@Url String url, @QueryMap Map<String, Object> params);


    @POST("{path}")
    LiveData<String> httpPostForm(@Path(value = "path", encoded = true) String path);

    @POST("{path}")
    LiveData<String> httpPost(@Path(value = "path", encoded = true) String path);

    @GET("{path}")
    LiveData<String> httpGet(@Path(value = "path", encoded = true) String path);


    @POST
    LiveData<String> httpPostFormUseUrl(@Url String url);

    @POST
    LiveData<String> httpPostUseUrl(@Url String url);

    @GET
    LiveData<String> httpGetUseUrl(@Url String url);

    @GET
    LiveData<ResponseBody> httpGetDownloadUseUrl(@Url String url);

    @POST
    LiveData<ResponseBody> httpPostDownloadUseUrl(@Url String url);

    @GET
    LiveData<ResponseBody> httpGetDownloadUseUrl(@Url String url, @HeaderMap Map<String, String> headerMap);

    @POST
    LiveData<ResponseBody> httpPostDownloadUseUrl(@Url String url, @HeaderMap Map<String, String> headerMap);


    @Multipart
    @POST("{path}")
    LiveData<String> httpPostSingleFile(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, Object> params, @PartMap Map<String, RequestBody> bodies);

    @POST("{path}")
    LiveData<String> httpPostMultipleFile(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, Object> params, @Body MultipartBody body);

    @Multipart
    @POST
    LiveData<String> httpPostSingleFileUseUrl(@Url String url, @QueryMap Map<String, Object> params, @PartMap Map<String, RequestBody> bodies);

    @POST
    LiveData<String> httpPostMultipleFileUseUrl(@Url String url, @QueryMap Map<String, Object> params, @Body MultipartBody body);

    @Multipart
    @POST("{path}")
    LiveData<String> httpPostSingleFile(@Path(value = "path", encoded = true) String path, @PartMap Map<String, RequestBody> bodies);

    @POST("{path}")
    LiveData<String> httpPostMultipleFile(@Path(value = "path", encoded = true) String path, @Body MultipartBody body);

    @Multipart
    @POST
    LiveData<String> httpPostSingleFileUseUrl(@Url String url, @PartMap Map<String, RequestBody> bodies);

    @POST
    LiveData<String> httpPostMultipleFileUseUrl(@Url String url, @Body MultipartBody body);

    @Multipart
    @POST("{path}")
    LiveData<String> uploadHead(
            @Path(value = "path", encoded = true) String path,
            @Part MultipartBody.Part file
    );
}

