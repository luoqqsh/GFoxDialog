package com.xing.gfox.fliepick.bean;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.SOURCE)
public @interface CPickType {
    int files = 0;//文件
    int images = 1;//图片
    int videos = 2;//视频
    int audios = 3;//音乐
    int camera = 4;//拍照
    int record = 5;//录像
    int crop = 6;//裁剪图片
    int byType = 7;//根据指定格式类型,需配置一下参数

    String mediaImg = "image/*";//调用图库，获取所有本地图片
    String mediaAudio = "audio/*";//调用音乐，获取所有本地音乐文件
    String mediaVideo = "video/*";//调用图库，获取所有本地视频文件
}
