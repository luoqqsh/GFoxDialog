package com.xing.gfox.rxHttp.test;

import com.xing.gfox.log.ViseLog;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class okhttpTest {
    //异步GET请求
    private void okGet() {
        OkHttpClient okHttpClient = new OkHttpClient();
        Request request = new Request.Builder()
                .url("url")
                .get()
                .build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ViseLog.d(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                ViseLog.d(response.body().string());
            }
        });
    }

    //同步POST请求
    private void okPost() throws IOException {
        OkHttpClient okHttpClient = new OkHttpClient();
        FormBody formbody = new FormBody.Builder().add("username", "tangwenjing").build();
        Request.Builder builder = new Request.Builder();
        Request request = builder.url("url").post(formbody).build();
        Call call = okHttpClient.newCall(request);
        Response response = call.execute();
        ViseLog.d(response.body().string());
    }

    //同步POST请求
    private void okFileUp() {
        OkHttpClient okHttpClient = new OkHttpClient();
        File file = new File("filePath");
        RequestBody body = RequestBody.create(MediaType.parse("image/*"), file);
        Request.Builder builder = new Request.Builder();
        Request request = builder.url("url").post(body).build();
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                ViseLog.d(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                if (response.isSuccessful()) {
                    //成功
                }
            }
        });
    }
}
