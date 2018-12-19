package com.example.vinsent_y.smartbutler.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

/**
 * 包名：   com.example.vinsent_y.com.example.vinsent_y.smartbutler.ui
 * 文件名： BaseActivity.java
 * 创建者： Vinsent_Y
 * 创建时间： 2018/10/27 20:21
 * 描述：    带返回按键的活动
 */

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //显示ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //菜单栏操作

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
