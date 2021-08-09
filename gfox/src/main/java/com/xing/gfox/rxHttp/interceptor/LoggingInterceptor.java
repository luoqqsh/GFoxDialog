package com.xing.gfox.rxHttp.interceptor;

import android.text.TextUtils;
import android.util.Log;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import okhttp3.Connection;
import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.GzipSource;
import com.xing.gfox.log.ViseLog;

/**
 * 日志拦截器
 */
public class LoggingInterceptor implements Interceptor {
    private volatile Level level = Level.ALL;

    public enum Level {
        NONE, ALL, MAIN//无，所有，主要（不包含head部分）
    }

    /**
     * Change the level at which this interceptor logs.
     */
    public LoggingInterceptor setLevel(Level level) {
        if (level == null) this.level = Level.ALL;
        this.level = level;
        return this;
    }

    @NotNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        Level level = this.level;
        Request request = chain.request();
        if (level == Level.NONE) {
            return chain.proceed(request);
        }
        StringBuilder urlLog = new StringBuilder();
        List<String> url = request.url().pathSegments();
        for (String param : url) {
            urlLog.append(param).append("/");
        }
        String TAG = "HttpLog";
        StringBuilder requestlog = new StringBuilder("<=======================发起" + urlLog + " 请求=======================>\n");
        StringBuilder responseLog = new StringBuilder("<=======================收到" + urlLog + " 请求结果=======================>\n");
        RequestBody requestBody = request.body();
        Connection connection = chain.connection();
        requestlog.append(request.method()).append(' ').append(request.url())
                .append(connection != null ? " " + connection.protocol() + "\n" : "\n");
        getUrlParams(request.url().toString(), requestlog);
        //
        if (level == Level.ALL) {
            Headers requestHeaders = request.headers();
            requestlog.append(requestHeaders.size() == 0 ? "------没有Header参数------\n" : "------Header参数------\n");
            for (int i = 0, count = requestHeaders.size(); i < count; i++) {
                requestlog.append(requestHeaders.name(i)).append(": ").append(requestHeaders.value(i)).append("\n");
            }
        }
        if (requestBody != null) {
            Buffer buffer = new Buffer();
            requestlog.append("------Body参数------\n");
            requestBody.writeTo(buffer);
            MediaType mediaType = requestBody.contentType();
            String charset = (mediaType != null && mediaType.charset() != null) ? mediaType.charset().name() : StandardCharsets.UTF_8.name();
            String requestJson = buffer.readString(Charset.forName(charset));
            if (TextUtils.isEmpty(requestJson)) {
                requestlog.append("参数拼接：").append(request.url()).append("\n\n");
            } else {
                requestlog.append("参数拼接：").append(request.url()).append("?").append(requestJson).append("\n\n");
            }
            printJson(requestJson, requestlog);
            requestlog.append("\n");
        }

        //--------------------开始请求--------------------//
        long startNs = System.currentTimeMillis();
        Response response;
        try {
            response = chain.proceed(request);
            ViseLog.d(TAG, requestlog);
        } catch (Exception e) {
            responseLog.append("请求失败:").append(e.getMessage());
            ViseLog.d(TAG, requestlog + responseLog.toString());
            throw e;
        }
        long tookMs = System.currentTimeMillis() - startNs;
        //--------------------request中的header--------------------//
//        if (level == Level.ALL) {
//            Headers requestHeaders = response.request().headers();
//            requestlog.append(requestHeaders.size() == 0 ? "------没有Header参数------\n" : "------Header参数------\n");
//            for (int i = 0, count = requestHeaders.size(); i < count; i++) {
//                requestlog.append(requestHeaders.name(i)).append(": ").append(requestHeaders.value(i)).append("\n");
//            }
//        }
//        ViseLog.d(TAG, requestlog);
        //--------------------response日志处理--------------------//
        ResponseBody responseBody = response.body();
        BufferedSource source = Objects.requireNonNull(responseBody).source();
        Buffer responseBuffer = source.getBuffer();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        long contentLength = responseBuffer.size();
        responseLog.append(response.code()).append(response.message().isEmpty() ? "" : ' ' + response.message()).append(' ')
                .append(response.request().url()).append("\n")
                .append("请求耗时 : ").append(tookMs).append(" ms\n")
                .append("返回数据大小 : ").append(contentLength).append(" byte\n");
        Headers headers = response.headers();
        if (level == Level.ALL) {
            responseLog.append("------Headers------\n");
            for (int i = 0, count = headers.size(); i < count; i++) {
                responseLog.append(headers.name(i)).append(" : ").append(headers.value(i)).append("\n");
                if ("date".equals(headers.name(i).toLowerCase())) {
                    responseLog.append("服务器时间").append(" : ").append(toGMT8(headers.value(i))).append("\n");
                }
                if ("last-modified".equals(headers.name(i).toLowerCase())) {
                    responseLog.append("最后更新时间").append(" : ").append(toGMT8(headers.value(i))).append("\n");
                }
            }
        }
        responseLog.append("------Body------\n");
        String isZip = headers.get("Content-Encoding");
        Buffer buffer = new Buffer();
        if (isZip != null && "gzip".equals(isZip.toLowerCase())) {
            new GzipSource(responseBuffer.clone()).read(buffer, responseBuffer.size() * 10);
        } else {
            buffer = responseBuffer;
        }
        MediaType mediaType = responseBody.contentType();
        String charset = (mediaType != null && mediaType.charset() != null) ? mediaType.charset().name() : StandardCharsets.UTF_8.name();
        String content = decrypt(buffer.clone().readString(Charset.forName(charset)));
        if (contentLength != 0 && contentLength < 20000) {
            printJson(content, responseLog);
        } else {
            responseLog.append(content);
        }
        ViseLog.e(TAG, responseLog.toString());
        return response;
    }

    private void printJson(String json, StringBuilder log) {
        if (TextUtils.isEmpty(json)) {
            log.append("没有body参数\n");
            return;
        }
        if (isJson(json)) {
            try {
                if (json.startsWith("{")) {
                    JSONObject jsonObject = new JSONObject(json);
                    String msg = jsonObject.toString(2);
                    log.append(msg);
                } else if (json.startsWith("[")) {
                    JSONArray jsonArray = new JSONArray(json);
                    String msg = jsonArray.toString(2);
                    log.append(msg);
                }
                log.append("\n");
            } catch (JSONException e) {
                log.append("数据格式化错误，").append(e.getMessage()).append("以下是原始数据：\n");
                log.append(json);
            }
        } else {
            String replace = json.replace("&", "\n").replace("=", " = ");
            log.append(replace);
        }
    }

    private void getUrlParams(String url, StringBuilder log) {
        if (url.contains("?")) {
            log.append("------Url参数------\n");
            String[] argAry = url.substring(url.indexOf("?") + 1).split("&");
            for (String arg : argAry) {
                String[] argArtT = arg.split("=");
                if (!argArtT[0].isEmpty()) {
                    if (argArtT.length == 1) {
                        String[] argArts = Arrays.copyOf(argArtT, 2);
                        argArts[1] = "";
                        log.append(argArts[0]).append(" = ").append(argArts[1]).append("\n");
                    } else {
                        log.append(argArtT[0]).append(" = ").append(argArtT[1]).append("\n");
                    }
                }
            }
        }
    }

    private String toGMT8(String dateString) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("EEE,dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(sdf.parse(dateString));
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 判断是否是json结构
     */
    private boolean isJson(String value) {
        if (TextUtils.isEmpty(value)) return false;
        if (value.startsWith("{") || value.endsWith("}")) {
            try {
                new JSONObject(value);
            } catch (JSONException e) {
                return false;
            }
            return true;
        } else {
            return false;
        }
    }


    public String decrypt(String dataStr) {
        return dataStr;
    }

    private int logSubLength = 5000;

    private void logSplit(String explain, String message, int i) {
        if (i > 10) return;
        if (message.length() <= logSubLength) {
            Log.i("NetApiProvide", "OkHttpMessage: " + explain + i + "：     " + message);
            return;
        }

        String msg1 = message.substring(0, logSubLength);
        Log.i("NetApiProvide", "OkHttpMessage: " + explain + i + "：     " + msg1);
        String msg2 = message.substring(logSubLength);
        logSplit(explain, msg2, ++i);
    }
}