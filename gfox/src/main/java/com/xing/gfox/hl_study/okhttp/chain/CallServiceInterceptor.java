package com.xing.gfox.hl_study.okhttp.chain;

import android.util.Log;

import com.xing.gfox.hl_study.okhttp.SHttpCodec;
import com.xing.gfox.hl_study.okhttp.SHttpConnection;
import com.xing.gfox.hl_study.okhttp.SResponse;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


public class CallServiceInterceptor implements SInterceptor {
    @Override
    public SResponse intercept(InterceptorChain chain) throws IOException {
        Log.e("interceprot", "通信拦截器....");
        SHttpCodec httpCodec = chain.httpCodec;
        SHttpConnection connection = chain.connection;
        InputStream is = connection.call(httpCodec);
        //HTTP/1.1 200 OK 空格隔开的响应状态
        String readLine = httpCodec.readLine(is);

        Map<String, String> map = httpCodec.readHeaders(is);
        //是否保持连接
        boolean isKeepAlive = false;
        if (map.containsKey(SHttpCodec.HEAD_CONNECTION)) {
            isKeepAlive = map.get(SHttpCodec.HEAD_CONNECTION).equalsIgnoreCase(SHttpCodec.HEAD_VALUE_KEEP_ALIVE);
        }
        int contentLength = -1;
        if (map.containsKey(SHttpCodec.HEAD_CONTENT_LENGTH)) {
            contentLength = Integer.valueOf(map.get(SHttpCodec.HEAD_CONTENT_LENGTH));
        }
        //分块编码数据
        boolean isChunked = false;
        if (map.containsKey(SHttpCodec.HEAD_TRANSFER_ENCODING)) {
            isChunked = map.get(SHttpCodec.HEAD_TRANSFER_ENCODING).equalsIgnoreCase(SHttpCodec
                    .HEAD_VALUE_CHUNKED);
        }

        String body = null;
        if (contentLength > 0) {
            byte[] bytes = httpCodec.readBytes(is, contentLength);
            body = new String(bytes);
        } else if (isChunked) {
            body = httpCodec.readChunked(is);
        }

        String[] split = readLine.split(" ");
        connection.updateLastUseTime();

        return new SResponse(Integer.valueOf(split[1]), contentLength, map, body, isKeepAlive);
    }
}
