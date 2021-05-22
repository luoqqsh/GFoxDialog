package com.xing.gfox.rxHttp.test;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiFace {
    //encoded true---URL不转码，默认的false表示URL转码
    //@FormUrlEncoded 必须配@FieldMap参数，且参数不能为空，此时，日志url不附带参数
    /*
        @GET(URL)
        Observable<返回数据类型> 方法名(
        @Query（"uid"） String uid...);
    */
    /*
        @GET("{path}")
        Call<ResponseBody> getTvList(@Path(value = "path", encoded = true) String path);//<ResponseBody>直接返回内容
    */
    /*
        @POST(URL)
        @FormUrlEncoded //表单形式必须加转码
        Observable<返回数据类型> 方法名(
        @Field（"uid"） String uid...);
    */ 
    /*
        @POST(URL)
        @Multipart //文件上传
        Observable<返回数据类型> 方法名(
        @Part（"uid"） String uid...);
    */
    @GET("")
    Call<ResponseBody> getTvList();

    //retrofit2请求方式
    @GET("https://lenovodns.com/IPTV-List.json")
    Call<String> getTvList2();

//    retrofit2+rxjava请求方式
    @GET("https://lenovodns.com/IPTV-List.json")
    Observable<String> getTvList3();
}
