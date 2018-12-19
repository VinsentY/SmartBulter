package com.example.vinsent_y.smartbutler.ui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;

import com.example.vinsent_y.smartbutler.R;
import com.xys.libzxing.zxing.encoding.EncodingUtils;

import static com.example.vinsent_y.smartbutler.util.StaticClass.MY_BLOG;


public class QrCodeActivity extends BaseActivity {

    private ImageView iv_qr_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);
        
        initView();
    }

    private void initView() {
        iv_qr_code = findViewById(R.id.iv_qr_code);
        int width = getResources().getDisplayMetrics().widthPixels;
        String str = MY_BLOG;
        Bitmap qrCodeBitmap = EncodingUtils.createQRCode(str,width / 2,width / 2,
                BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        iv_qr_code.setImageBitmap(qrCodeBitmap);
    }
}
