package com.example.vinsent_y.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;

import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.entity.WeChatData;
import com.example.vinsent_y.smartbutler.util.L;

import static com.example.vinsent_y.smartbutler.util.StaticClass.MY_BLOG;

public class WebViewActivity extends BaseActivity {

    private ProgressBar mProgressBar;
    private WebView mWebView;

    private WeChatData data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        data = (WeChatData) intent.getSerializableExtra("wechat_data");
        if (data == null) {
            data = new WeChatData("Vinsent_Y","Vinsent_Y",MY_BLOG);
        }
        L.i(data.getTitle());
        L.i(data.getSource());
        L.i(data.getUrl());
    }

    private void initView() {
        mProgressBar = findViewById(R.id.mProgressBar);
        mWebView = findViewById(R.id.mWebView);

        //设置标题
        getSupportActionBar().setTitle(data.getTitle());

        //进行加载网页的逻辑

        //支持JS
        mWebView.getSettings().setJavaScriptEnabled(true);
        //支持缩放
        mWebView.getSettings().setSupportZoom(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        //接口回调
        mWebView.setWebChromeClient(new WebViewClient());
        //加载网页
        mWebView.loadUrl(data.getUrl());

        //本地显示
        mWebView.setWebViewClient(new android.webkit.WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(data.getUrl());
                //我接受这个事件
                return true;
            }
        });
    }

    public class WebViewClient extends WebChromeClient {

        //进度变化的监听
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            if (newProgress == 100) {
                mProgressBar.setVisibility(View.GONE);
            } else {
                mProgressBar.setProgress(newProgress);
            }
            super.onProgressChanged(view, newProgress);
        }
    }
}
