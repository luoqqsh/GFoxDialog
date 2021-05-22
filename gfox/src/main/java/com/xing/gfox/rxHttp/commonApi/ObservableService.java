package com.xing.gfox.rxHttp.commonApi;

import java.util.Map;

import io.reactivex.Observable;
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
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface ObservableService {

    //encoded true---URL不转码，默认的false表示URL转码
    //@FormUrlEncoded 必须配@FieldMap参数，且参数不能为空，此时，日志url不附带参数

    @FormUrlEncoded
    @POST("{path}")
    Observable<String> httpPostForm(@Path(value = "path", encoded = true) String path, @FieldMap Map<String, Object> params);

    @POST("{path}")
    Observable<String> httpPost(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, Object> params);

    @GET("{path}")
    Observable<String> httpGet(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, Object> params);


    @FormUrlEncoded
    @POST
    Observable<String> httpPostFormUseUrl(@Url String url, @FieldMap Map<String, Object> params);

    @POST
    Observable<String> httpPostUseUrl(@Url String url, @QueryMap Map<String, Object> params);

    @GET
    Observable<String> httpGetUseUrl(@Url String url, @QueryMap Map<String, Object> params);


    @POST("{path}")
    Observable<String> httpPostForm(@Path(value = "path", encoded = true) String path);

    @POST("{path}")
    Observable<String> httpPost(@Path(value = "path", encoded = true) String path);

    @GET("{path}")
    Observable<String> httpGet(@Path(value = "path", encoded = true) String path);


    @POST
    Observable<String> httpPostFormUseUrl(@Url String url);

    @POST
    Observable<String> httpPostUseUrl(@Url String url);

    @GET
    Observable<String> httpGetUseUrl(@Url String url);

    @GET
    Observable<ResponseBody> httpGetDownloadUseUrl(@Url String url);

    @POST
    Observable<ResponseBody> httpPostDownloadUseUrl(@Url String url);

    @GET
    Observable<ResponseBody> httpGetDownloadUseUrl(@Url String url, @HeaderMap Map<String, String> headerMap);

    @POST
    Observable<ResponseBody> httpPostDownloadUseUrl(@Url String url, @HeaderMap Map<String, String> headerMap);


    @Multipart
    @POST("{path}")
    Observable<String> httpPostSingleFile(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, Object> params, @PartMap Map<String, RequestBody> bodies);

    @POST("{path}")
    Observable<String> httpPostMultipleFile(@Path(value = "path", encoded = true) String path, @QueryMap Map<String, Object> params, @Body MultipartBody body);

    @Multipart
    @POST
    Observable<String> httpPostSingleFileUseUrl(@Url String url, @QueryMap Map<String, Object> params, @PartMap Map<String, RequestBody> bodies);

    @POST
    Observable<String> httpPostMultipleFileUseUrl(@Url String url, @QueryMap Map<String, Object> params, @Body MultipartBody body);

    @Multipart
    @POST("{path}")
    Observable<String> httpPostSingleFile(@Path(value = "path", encoded = true) String path, @PartMap Map<String, RequestBody> bodies);

    @POST("{path}")
    Observable<String> httpPostMultipleFile(@Path(value = "path", encoded = true) String path, @Body MultipartBody body);

    @Multipart
    @POST
    Observable<String> httpPostSingleFileUseUrl(@Url String url, @PartMap Map<String, RequestBody> bodies);

    @POST
    Observable<String> httpPostMultipleFileUseUrl(@Url String url, @Body MultipartBody body);

}

