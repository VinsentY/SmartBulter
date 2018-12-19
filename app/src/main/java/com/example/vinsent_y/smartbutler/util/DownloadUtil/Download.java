package com.example.vinsent_y.smartbutler.util.DownloadUtil;

import android.content.Context;
import android.content.Intent;

public class Download {

    public static  void startDownload(Context context, String url) {
        Intent intent = new Intent(context, DownloadService.class);
        intent.putExtra("url",url);
        context.startService(intent);   //启动服务
    }
}
