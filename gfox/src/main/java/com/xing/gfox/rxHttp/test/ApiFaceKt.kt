package com.xing.gfox.rxHttp.test

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers

interface ApiFaceKt {
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
    @get:GET("")
    val tvList: Call<ResponseBody?>?

    //retrofit2请求方式
    @get:GET("https://lenovodns.com/IPTV-List.json")
    val tvList2: Call<String?>?

    //retrofit2+rxjava请求方式
    @get:GET("https://lenovodns.com/IPTV-List.json")
    val tvList3: Observable<String?>?

    //retrofit2+协程
    @Headers("Cache-Control11: max-age=640000")
    @GET("https://lenovodns.com/IPTV-List.json")
    suspend fun tvList4(): String

    @GET("https://lenovodns.com/IPTV-List.json")
    suspend fun tvList5(@Header("Content-Range") contentRange: String): String
}
