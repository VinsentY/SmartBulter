package com.example.vinsent_y.smartbutler.util.DownloadUtil;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.FileProvider;
import android.widget.Toast;

import com.example.vinsent_y.smartbutler.R;

import java.io.File;

/**
 * 类名：下载服务
 * 功能：提供APP新版本下载并更新方法
 * 注意事项：记得注册服务和FileProvider! 记得注册服务和FileProvider!  记得注册服务和FileProvider!
 */

public class DownloadService extends Service {

    private DownloadTask downloadTask;

    private String downloadUrl;

    public static String CHANNEL_ONE_ID = "com.yikai.cn";

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String url = intent.getStringExtra("url");
        if (downloadTask == null) {
            downloadUrl = url;
            downloadTask = new DownloadTask(listener);
            downloadTask.execute(downloadUrl);
            startForeground(1, getNotification("Downloading...", 0));
            Toast.makeText(DownloadService.this, "Downloading...", Toast.LENGTH_SHORT).show();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private DownloadListener listener = new DownloadListener() {
        @Override
        public void onProgress(int progress) {
            getNotificationManager().notify(1, getNotification("Downloading...", progress));
        }

        @Override
        public void onSuccess(String path) {
            downloadTask = null;

            // 下载成功时将前台服务通知关闭，并创建一个下载成功的通知.
            stopForeground(true);
            stopSelf();
            startInstallApk(path);
            getNotificationManager().notify(1, getNotification("Download Success", -1));
            Toast.makeText(DownloadService.this, "Download Success", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onFailed() {
            downloadTask = null;

            // 下载成功时将前台服务通知关闭，并创建一个下载成功的通知.
            stopForeground(true);
            getNotificationManager().notify(1, getNotification("Download Failed", -1));
            Toast.makeText(DownloadService.this, "Download Failed", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onPaused() {
            downloadTask = null;
            Toast.makeText(DownloadService.this, "Download Paused", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCanceled() {
            downloadTask = null;
            stopForeground(true);
            Toast.makeText(DownloadService.this, "Download Canceled", Toast.LENGTH_SHORT).show();

        }
    };

    private void startInstallApk(String path) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //判读版本是否在7.0以上 TODO
            Uri apkUri = FileProvider.getUriForFile(this, "com.example.vinsent_y.com.example.vinsent_y.smartbutler.fileprovider", new File(path));//在AndroidManifest中的android:authorities值
            Intent install = new Intent(Intent.ACTION_VIEW);
            install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            install.setDataAndType(apkUri, "application/vnd.android.package-archive");
            startActivity(install);
        } else {
            //以前的启动方法
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.setDataAndType(Uri.fromFile(new File(path)), "application/vnd.android.package-archive");
            startActivity(intent);
        }
    }

    private NotificationManager getNotificationManager() {
        return (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
    }

    private Notification getNotification(String title, int progress)
    {
        String CHANNEL_ONE_ID = "com.yikai.cn";
        String CHANNEL_ONE_NAME = "Channel One";
        NotificationChannel notificationChannel = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(CHANNEL_ONE_ID,
                    CHANNEL_ONE_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setShowBadge(true);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
            getNotificationManager().createNotificationChannel(notificationChannel);
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        builder.setContentTitle(title);
        builder.setChannelId(CHANNEL_ONE_ID);
        if (progress > 0) {
            builder.setContentText(progress + "%");
            builder.setProgress(100, progress, false);
        }
        return builder.build();
    }

    public DownloadService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

//    private DownloadBinder mBinder = new DownloadBinder();

//    public class DownloadBinder extends Binder{
//
//        public void startDownload(String url) {
//            if (downloadTask == null) {
//                downloadUrl = url;
//                downloadTask = new DownloadTask(listener);
//                downloadTask.execute(downloadUrl);
//                startForeground(1, getNotification("Downloading...", 0));
//                Toast.makeText(DownloadService.this,"Downloading...",Toast.LENGTH_SHORT).show();
//            }
//        }
//
//        public void pauseDownload() {
//            if (downloadTask != null) {
//                downloadTask.pauseDownload();
//            }
//        }
//
//        public void cancelDownload() {
//            if (downloadTask != null) {
//                downloadTask.cancelDownload();
//            } else {
//                //不解
//                if (downloadUrl != null) {
//                    //取消下载时须将文件删除，并将通知关闭
//                    String fileName = downloadUrl.substring(downloadUrl.lastIndexOf("/"));
////                    String directory = Environment.getExternalStorageDirectory().getPath();
//
//                    String directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getPath();
//                    File file = new File(directory + fileName);
//                    if (file.exists()) {
//                        file.delete();
//                    }
//                    // 重复？
//                    getNotificationManager().cancel(1);
//                    stopForeground(true);
//                    Toast.makeText(DownloadService.this,"Canceled", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
}
