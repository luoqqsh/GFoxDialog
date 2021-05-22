package com.xing.gfoxdialog;//package x.com.dialogdialog;
//
//import com.google.gson.Gson;
//import com.xunrui.h5game.model.CommonResponse;
//import com.xunrui.h5game.model.TokenResponse2;
//import com.xunrui.h5game.tools.StrUtil;
//
//import org.checkerframework.checker.signedness.qual.Constant;
//
//import java.io.IOException;
//import java.nio.charset.Charset;
//import java.util.TreeMap;
//
//import okhttp3.HttpUrl;
//import okhttp3.Interceptor;
//import okhttp3.MediaType;
//import okhttp3.MultipartBody;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//import okhttp3.ResponseBody;
//import okio.Buffer;
//
///**
// * Created by gxxing on 2017/8/9.
// * 签名信息
// */
//
//public class SignInterceptor implements Interceptor {
//    private MediaType JSON = MediaType.parse("application/json; charset=utf-8");
//
//    @Override
//    public Response intercept(Chain chain) throws IOException {
//        Request request = chain.request();
//        RequestBody requestBody = request.body();
//        Request.Builder requestBuilder = request.newBuilder();//构建新的请求
//
//        if ("POST".equals(request.method()) && !request.url().host().contains("api/v1/member/uploadAvatar")) {
//            Buffer buffer = new Buffer();
//            if (requestBody != null) {
//                requestBody.writeTo(buffer);
//            }
//            Charset charset = Charset.forName("UTF-8");
//            String paramsStr = buffer.readString(charset);
//            String json;
//            if (StrUtil.isJson(paramsStr)) {
//                json = paramsStr;
//            } else {
//                //获取参数列表
//                //TreeMap里面的数据会按照key值自动升序排列
//                TreeMap<String, String> param_map = new TreeMap<>();
//                //获取参数对
//                String[] param_pairs = paramsStr.split("&");
//                for (String pair : param_pairs) {
//                    String[] param = pair.split("=");
//                    if (param.length != 2) {
//                        //没有value的参数不进行处理
//                        continue;
//                    }
//                    param_map.put(param[0], param[1]);
//                }
//                json = new Gson().toJson(param_map);
//            }
//            RequestBody body = RequestBody.create(JSON, json);
//            //重新拼接url
//            HttpUrl.Builder httpUrlBuilder = request.url().newBuilder();
//            //添加参数
//            requestBuilder.url(httpUrlBuilder.build());
//            requestBuilder.post(body);
//        }
//
//        if (!"".equals(Constant.token)) {
//            requestBuilder.addHeader("accesstoken", Constant.token);
//        }
//        requestBuilder.addHeader("appkey", Constant.Appkey);
//        RequestBody newRequestBody;
//        if (request.url().url().getPath().contains("api/v1/member/uploadAvatar")) {
//            request.header("multipart/form-data");
//            if (requestBody instanceof MultipartBody) { // 文件
//                MultipartBody requestBody1 = (MultipartBody) request.body();
//                MultipartBody.Builder multipartBodybuilder = new MultipartBody.Builder();
//                if (requestBody1 != null) {
//                    for (int i = 0; i < requestBody1.size(); i++) {
//                        MultipartBody.Part part = requestBody1.part(i);
//                        multipartBodybuilder.addPart(part);
//                    }
//                }
//                newRequestBody = multipartBodybuilder.build();
//                request.newBuilder().method(request.method(), newRequestBody);
//            }
//        } else {
//            request = requestBuilder.build();
//        }
//        Response response = chain.proceed(request);
//        Response clone = response.newBuilder().build();
//        ResponseBody responseBody = clone.body();
//        Gson g = new Gson();
//        String oldresponse = responseBody.string();
//        int code = g.fromJson(oldresponse, CommonResponse.class).getCode();
//        if (code == 401 || code == 402 && !request.url().host().contains("api/token/get")) {
//            //同步请求方式，获取最新的Token
//            try {
//                Constant.token = getNewToken();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            //使用新的Token，创建新的请求
//            Request newRequest = chain.request()
//                    .newBuilder()
//                    .removeHeader("accesstoken")
//                    .header("accesstoken", Constant.token)
//                    .header("appkey", Constant.Appkey)
//                    .build();
//            //重新请求
//            return chain.proceed(newRequest);
//        } else {
//            return response.newBuilder()
//                    .body(ResponseBody.create(MediaType.parse("json"), oldresponse))
//                    .build();
//        }
//    }
//
//    private synchronized String getNewToken() throws Exception {
//        // 通过获取token的接口，同步请求接口
//        Request request = new Request.Builder().url(Constant.URL + Constant.getToken + "user_id=" + Constant.user_id + "&phone=" + Constant.phone + "&appkey=" + Constant.Appkey).build();
//        Response newResponse = new OkHttpClient().newCall(request).execute();
//        if (newResponse.code() != 200) {
//            //退出登录并返回到登录页面的逻辑
//            return null;
//        }
//        String responseBody = newResponse.body().string();
//        TokenResponse2 token = new Gson().fromJson(responseBody, TokenResponse2.class);
//        return token.getData().getAccesstoken();
//    }
//}
