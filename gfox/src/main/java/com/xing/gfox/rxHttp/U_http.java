package com.xing.gfox.rxHttp;

import android.net.Uri;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import okio.BufferedSource;

import com.xing.gfox.base.interfaces.ProgressListener;
import com.xing.gfox.log.ViseLog;
import com.xing.gfox.rxHttp.bean.HttpMethod;
import com.xing.gfox.rxHttp.interceptor.LoggingInterceptor;
import com.xing.gfox.rxHttp.upload.ProgressRequestBody;
import com.xing.gfox.util.U_gson;
import com.xing.gfox.util.U_thread;
import com.xing.gfox.util.U_uri;

public class U_http {
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static int connectTimeout = 10;

    /**
     * 简单get请求
     *
     * @param url      地址
     * @param callback 回调
     */
    public static void sendGetRequest(String url, Map<String, String> headers, Callback callback) {
        sendRequest(HttpMethod.HttpGet, url, null, headers, callback);
    }

    public static Response sendGetRequest(String url, Map<String, String> headers) {
        return sendRequest(HttpMethod.HttpGet, url, null, headers);
    }

    /**
     * 简单post请求
     *
     * @param url      地址
     * @param data     参数
     * @param callback 回调,不传为同步的方法
     */
    public static void sendPostRequest(String url, String data, Map<String, String> headers, Callback callback) {
        sendRequest(HttpMethod.HttpPost, url, data, headers, callback);
    }

    public static Response sendPostRequest(String url, String data, Map<String, String> headers) {
        return sendRequest(HttpMethod.HttpPost, url, data, headers);
    }

    public static void sendRequest(HttpMethod method, String url, String data, Map<String, String> headers, Callback callback) {
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS).addInterceptor(new LoggingInterceptor()).build();
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

    public static Response sendRequest(HttpMethod method, String url, String data, Map<String, String> headers) {
        if (U_thread.isRunInUIThread()) {
            ViseLog.e("请在子线程中执行网络请求！！！");
            return null;
        }
        OkHttpClient.Builder builder = new OkHttpClient().newBuilder();
        if (url.contains("https://www.google.com")) {
            builder.connectTimeout(2, TimeUnit.SECONDS);
        } else {
            builder.connectTimeout(connectTimeout, TimeUnit.SECONDS);
        }
        builder.addInterceptor(new LoggingInterceptor()).build();
        OkHttpClient client = builder.build();
        Request.Builder request = new Request.Builder();
        request.url(url);
        if (headers != null) {
            for (Map.Entry<String, String> entry : headers.entrySet()) {
                request.addHeader(entry.getKey(), entry.getValue());
            }
        }
        try {
            switch (method) {
                case HttpPost:
                    RequestBody requestBody = RequestBody.create(data, JSON);
                    return client.newCall(request.post(requestBody).build()).execute();
                case HttpGet:
                    return client.newCall(request.build()).execute();
                default:
                    return null;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * uri转MultipartBody，上传文件用
     *
     * @param param 对应参数
     * @param uri   文件uri
     * @return MultipartBody
     */
    public static MultipartBody.Part uriToMultipartRequestBody(String param, Uri uri) {
        return uriToMultipartRequestBody(param, uri, null);
    }

    public static MultipartBody.Part uriToMultipartRequestBody(String param, Uri uri, ProgressListener progressListener) {
        MediaType mediaType = MediaType.parse("form-data");
        //这里是通过file路径解析出RequestBody再生成MultipartBody.Part。
        File file = U_uri.uri2File(uri);
        ProgressRequestBody body = new ProgressRequestBody(file, mediaType, progressListener);
        return MultipartBody.Part.createFormData(param, file.getName(), body);
    }

    /**
     * 根据model生成RequestBody
     *
     * @param param model
     * @return RequestBody
     */
    public static RequestBody getReqBody(Object param) {
        return RequestBody.create(U_gson.instance().toJson(param), JSON);
    }

    public static String getBodyFromResponse(Response response) {
        if (response == null) return "";
        try {
            BufferedSource source = response.body().source();
            Buffer responseBuffer = source.getBuffer();
            source.request(Long.MAX_VALUE);// Buffer the entire body.
            return responseBuffer.clone().readString(
                    Charset.forName(StandardCharsets.UTF_8.name()));
        } catch (Exception e) {
            return "";
        }
    }
}
