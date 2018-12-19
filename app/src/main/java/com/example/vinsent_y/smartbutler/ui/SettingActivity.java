package com.example.vinsent_y.smartbutler.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.service.SmsService;
import com.example.vinsent_y.smartbutler.util.DownloadUtil.Download;
import com.example.vinsent_y.smartbutler.util.L;
import com.example.vinsent_y.smartbutler.util.PermissionUtils;
import com.example.vinsent_y.smartbutler.util.ShareUtils;
import com.example.vinsent_y.smartbutler.util.StaticClass;
import com.xys.libzxing.zxing.activity.CaptureActivity;

import org.json.JSONException;
import org.json.JSONObject;

public class SettingActivity extends BaseActivity implements View.OnClickListener {

    private Switch sw_sms;
    private boolean isSms = false;
    public static final int SMS_REQUEST_CODE = 101;
    public static final int QR_REQUEST_CODE = 102;


    private LinearLayout ll_update;
    private TextView tv_update;
    private LinearLayout ll_scan;
    private LinearLayout ll_qr_code;
    private LinearLayout ll_map;
    private LinearLayout ll_about;

    private String versionName;
    private int versionCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();

    }

    private void initView() {
        isSms = ShareUtils.getBoolean(this, "isSms", false);
        if (isSms) {
            startService(new Intent(this, SmsService.class));
        }
        sw_sms = findViewById(R.id.sw_sms);
        sw_sms.setChecked(isSms);
        sw_sms.setOnClickListener(this);
        ll_update = findViewById(R.id.ll_update);
        tv_update = findViewById(R.id.tv_update);
        ll_update.setOnClickListener(this);
        ll_scan = findViewById(R.id.ll_scan);
        ll_scan.setOnClickListener(this);
        ll_map = findViewById(R.id.ll_map);
        ll_map.setOnClickListener(this);
        ll_qr_code = findViewById(R.id.ll_qr_code);
        ll_qr_code.setOnClickListener(this);
        ll_about = findViewById(R.id.ll_about);
        ll_about.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sw_sms:
                isSms = !isSms;
                sw_sms.setChecked(isSms);
                ShareUtils.putBoolean(this, "isSms", isSms);
                if (isSms) {
//                    startService(new Intent(this,SmsService.class));
                    applyPermissionAndOpenSms();
                } else {
                    stopService(new Intent(this, SmsService.class));
                }
                break;

            case R.id.ll_update:
                checkVersionUpdate();
                break;
            case R.id.ll_scan:
                startActivityForResult(new Intent(this,CaptureActivity.class),QR_REQUEST_CODE);
                break;
            case R.id.ll_qr_code:
                startActivity(new Intent(this,QrCodeActivity.class));
                break;
            case R.id.ll_map:
                startActivity(new Intent(this,LocationActivity.class));
                break;
            case R.id.ll_about:
                startActivity(new Intent(this,AboutUsActivity.class));
                break;
        }
    }

    private void checkVersionUpdate() {
        try {
            getVersionInfo();
            tv_update.setText("检测版本 " + versionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "检测版本失败", Toast.LENGTH_SHORT).show();
        }
//        HttpUtil.get(CHECK_UPDATE_URL, new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//            }
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                String t = response.body().string();
//                L.i(t);
//                parsingJson(t);
//            }
//        });
        parsingJson(StaticClass.UPDATE_JSON);
    }

    private void parsingJson(String t) {
        try {
            JSONObject jsonObject = new JSONObject(t);
            String versionName = jsonObject.getString("versionName");
            int versionCode = jsonObject.getInt("versionCode");
            String content = jsonObject.getString("content");
            final String url = jsonObject.getString("url");

            if (versionCode > this.versionCode) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("更新提示");
                builder.setMessage(content);
                builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //下载
                        Download.startDownload(SettingActivity.this,url);
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //会自动执行dismiss方法
                    }
                });
                builder.show();
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case QR_REQUEST_CODE:
                    L.e(data.getExtras().getString("result"));
                    break;
            }
        }
    }

    private void getVersionInfo() throws PackageManager.NameNotFoundException {
        PackageInfo info = getPackageManager().getPackageInfo(getPackageName(),0);
        versionName = info.versionName;
        versionCode = info.versionCode;
    }

    //TODO 动态申请权限封装
    private void applyPermissionAndOpenSms() {
        PermissionUtils.requestPermission(this, new String[]{PermissionUtils.PERMISSION_SMS_RECEIVE}, startService);
    }

    private PermissionUtils.PermissionGrant startService = new PermissionUtils.PermissionGrant() {
        @Override
        public void execute() {
            startService(new Intent(SettingActivity.this, SmsService.class));
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        PermissionUtils.requestPermissionsResult(this, permissions, requestCode, grantResults, startService);
    }
}
