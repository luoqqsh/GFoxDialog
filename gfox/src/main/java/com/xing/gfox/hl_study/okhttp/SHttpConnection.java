package com.xing.gfox.hl_study.okhttp;

import android.text.TextUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocketFactory;

public class SHttpConnection {
    static final String HTTPS = "https";
    Socket socket;
    InputStream is;
    OutputStream os;
    SRequest request;
    long lastUsetime;

    public SRequest getRequest() {
        return request;
    }

    public void setRequest(SRequest request) {
        this.request = request;
    }

    //输入流
    public InputStream call(SHttpCodec httpCodec) throws IOException {

        try {
            createSocket();
            httpCodec.writeRequest(os, request);
            return is;
        }catch (Exception e){
            closeQuietly();
            throw new IOException(e);
        }

    }

    public void closeQuietly() {
        if(null != socket){
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void createSocket() throws IOException {
        if (null == socket || socket.isClosed()) {
            SHttpUrl url = request.url();
            if(url.protocol.equalsIgnoreCase(HTTPS)){
                socket = SSLSocketFactory.getDefault().createSocket();
            } else {
                socket = new Socket();
            }
            socket.connect(new InetSocketAddress(url.host, url.port));
            os = socket.getOutputStream();
            is = socket.getInputStream();
        }
    }
    public void updateLastUseTime() {
        //更新最后使用时间
        lastUsetime = System.currentTimeMillis();
    }

    public boolean isSameAddress(String host, int port) {
        if (null == socket) {
            return false;
        }
        return TextUtils.equals(socket.getInetAddress().getHostName(), host) && port == socket
                .getPort();
    }
}