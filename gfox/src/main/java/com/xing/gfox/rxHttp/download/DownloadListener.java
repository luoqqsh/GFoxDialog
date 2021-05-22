package com.xing.gfox.rxHttp.download;

public interface DownloadListener {
    //由于支持断点续传，刚开始的进度不一定是0
    void onDownLoadStart(String filePath, int startProgress);

    void onDownLoadProgress(String filePath, int progress);

    void onDownLoadError(String filePath, String error);

    void onDownloadFinish(String filePath);
}
