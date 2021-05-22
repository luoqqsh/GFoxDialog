package com.xing.gfox.hl_study.okhttp;

import android.text.TextUtils;

import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.Map;

public class SRequest {
    //请求头
    public Map<String, String> headers;
    //请求方式 get/post
    public String method;
    //请求体
    public SRequestBody body;
    //解析url 成HttpUrl 对象
    public SHttpUrl url;

    public SRequest(Builder builder) {
        this.url = builder.url;
        this.method = builder.method;
        this.headers = builder.headers;
        this.body = builder.body;
    }

    public String method() {
        return method;
    }

    public SHttpUrl url() {
        return url;
    }

    public SRequestBody body() {
        return body;
    }

    public Map<String, String> headers() {
        return headers;
    }

    /**
     * 构建者模式，不必了解数据细节
     */
    public final static class Builder {

        SHttpUrl url;
        Map<String, String> headers = new HashMap<>();
        String method;

        SRequestBody body;

        public Builder url(String url) {
            try {
                this.url = new SHttpUrl(url);
                return this;
            } catch (MalformedURLException e) {
                throw new IllegalStateException("Failed Http Url", e);
            }
        }

        public Builder addHeader(String name, String value) {
            headers.put(name, value);
            return this;
        }

        public Builder removeHeader(String name) {
            headers.remove(name);
            return this;
        }

        public Builder get() {
            method = "GET";
            return this;
        }


        public Builder post(SRequestBody body) {
            this.body = body;
            method = "POST";
            return this;
        }

        public SRequest build() {
            if (url == null) {
                throw new IllegalStateException("url == null");
            }
            if (TextUtils.isEmpty(method)) {
                method = "GET";
            }
            return new SRequest(this);
        }
    }
}
