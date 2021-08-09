package com.xing.gfox.util;

import android.net.Uri;

import com.xing.gfox.base.interfaces.ProgressListener;
import com.xing.gfox.rxHttp.LoggingInterceptor;
import com.xing.gfox.rxHttp.bean.HttpMethod;
import com.xing.gfox.rxHttp.upload.ProgressRequestBody;

import java.io.File;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;


public class U_http {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    /**
     * 简单get请求
     *
     * @param url      地址
     * @param callback 回调
     */
    public static void sendGetRequest(String url, Map<String, String> headers, Callback callback) {
        sendRequest(HttpMethod.HttpGet, url, null, headers, callback);
    }

    /**
     * 简单post请求
     *
     * @param url      地址
     * @param data     参数
     * @param callback 回调
     */
    public static void sendPostRequest(String url, String data, Map<String, String> headers, Callback callback) {
        sendRequest(HttpMethod.HttpPost, url, data, headers, callback);
    }

    public static void sendRequest(HttpMethod method, String url, String data, Map<String, String> headers, Callback callback) {
        OkHttpClient client = new OkHttpClient().newBuilder().addInterceptor(new LoggingInterceptor()).build();
        Request.Builder builder = new Request.Builder();
        builder.url(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                builder.addHeader(entry.getKey(), entry.getValue());
            }
        }
        switch (method) {
            case HttpPost:
                RequestBody requestBody = RequestBody.create(data, JSON);
                client.newCall(builder.post(requestBody).build()).enqueue(callback);
                break;
            case HttpGet:
                client.newCall(builder.build()).enqueue(callback);
                break;
        }
    }

    /**
     * uri转MultipartBody，上传文件用
     *
     * @param param 对应参数
     * @param uri   文件uri
     * @return
     */
    public static MultipartBody.Part uriToMultipartRequestBody(String param, Uri uri) {
        return uriToMultipartRequestBody(param, uri, null);
    }

    public static MultipartBody.Part uriToMultipartRequestBody(String param, Uri uri, ProgressListener progressListener) {
        MediaType mediaType = MediaType.parse("form-data");
        //这里是通过file路径解析出RequestBody再生成MultipartBody.Part。
        File file = U_uri.uri2File(uri);
        ProgressRequestBody body = new ProgressRequestBody(file, mediaType, progressListener);
//        RequestBody requestBody = RequestBody.create(file, mediaType);
//        ProgressRequestBody body = new ProgressRequestBody(requestBody);
        return MultipartBody.Part.createFormData(param, file.getName(), body);
    }

    /**
     * 根据model生成RequestBody
     *
     * @param param model
     * @return
     */
    public static RequestBody getReqBody(Object param) {
        return RequestBody.create(U_gson.instance().toJson(param), JSON);
    }
}
