package com.example.vinsent_y.smartbutler.util.DownloadUtil;

interface DownloadListener {

    void onProgress(int progress);

    void onSuccess(String path);

    void onFailed();

    void onPaused();

    void onCanceled();
}
