package com.example.vinsent_y.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.vinsent_y.smartbutler.R;
import com.example.vinsent_y.smartbutler.util.UtilTools;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.vinsent_y.smartbutler.util.StaticClass.MY_BLOG;

public class AboutUsActivity extends BaseActivity {

    private CircleImageView profile_image;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        //去除阴影
        getSupportActionBar().setElevation(0);

        initView();
    }

    private void initView() {
        profile_image = findViewById(R.id.profile_image);
        mListView = findViewById(R.id.mListView);

        mList = new ArrayList<>();
        mList.add("应用名： " + getString(R.string.app_name));
        mList.add("版本号： " + UtilTools.getVersion(this));
        mList.add("官网：" + MY_BLOG);

        mAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, mList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 2) {
                    startActivity(new Intent(AboutUsActivity.this,WebViewActivity.class));
                }
            }
        });
    }

}
