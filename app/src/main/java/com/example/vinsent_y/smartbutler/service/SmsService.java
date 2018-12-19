package com.example.vinsent_y.smartbutler.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.SmsMessage;
import android.view.View;
import android.view.WindowManager;

import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.util.L;
import com.example.vinsent_y.smartbutler.util.StaticClass;

public class SmsService extends Service {

    private SmsReceiver smsReceiver;
    private String smsPhone;
    private String smsContent;
    private WindowManager wm;
    private WindowManager.LayoutParams layoutParams;
    private View mView;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        L.i("init Service");
        //动态注册
        smsReceiver = new SmsReceiver();
        IntentFilter intent = new IntentFilter();
        //添加Action
        intent.addAction(StaticClass.SMS_ACTION);
        //设置权限
        intent.setPriority(Integer.MAX_VALUE);
        //注册
        registerReceiver(smsReceiver, intent);
//        showWindow();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        L.i("stop Service");
        unregisterReceiver(smsReceiver);
    }

    //短信广播
    public class SmsReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(StaticClass.SMS_ACTION)) {
                L.i("来短信啦!");
                Object[] objects = (Object[]) intent.getExtras().get("pdus");
                for (Object o : objects) {
                    SmsMessage sms = SmsMessage.createFromPdu((byte[]) o);
                    //发件人
                    smsPhone = sms.getOriginatingAddress();
                    smsContent = sms.getMessageBody();
                    L.i("联系人:" + smsPhone + " 内容: " + smsContent);
//                    showWindow();

                }
            }
        }

        private void showWindow() {
            //获取系统服务
            wm = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
            //获取布局参数
            layoutParams = new WindowManager.LayoutParams();
            //定义宽高
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;
            //定义标记
            layoutParams.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
            //定义格式
            layoutParams.format = PixelFormat.TRANSPARENT;
            //定义类型
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
            //加载布局
            mView = View.inflate(getApplicationContext(), R.layout.sms_item, null);
            //添加View到窗口
            wm.addView(mView, layoutParams);
        }
    }
}
