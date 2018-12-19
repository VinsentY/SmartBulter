package com.example.vinsent_y.smartbutler.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.vinsent_y.smartbutler.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 项目名： SmartButler4
 * 包名：   com.example.vinsent_y.com.example.vinsent_y.smartbutler.ui
 * 文件名： GuideActivity
 * 创建者： Vincent_Y
 * 创建时间： 2018/10/29 19:48
 * 描述：    TODO
 */
public class GuideActivity extends AppCompatActivity implements View.OnClickListener {

    private ViewPager mViewPager;
    //容器
    private List<View> mList = new ArrayList<>();
    View view1, view2, view3;
    //小圆点
    ImageView point1, point2, point3;
    //跳过
    ImageView btn_back;
    //进入主页
    Button btn_start;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        initView();
    }

    private void initView() {
        mViewPager = findViewById(R.id.mViewPager);
        btn_back = findViewById(R.id.btn_back);
        btn_back.setOnClickListener(this);

        point1 = findViewById(R.id.point1);
        point2 = findViewById(R.id.point2);
        point3 = findViewById(R.id.point3);
        setPointStatus(0);
        view1 = View.inflate(this, R.layout.page_item_one, null);
        view2 = View.inflate(this, R.layout.page_item_two, null);
        view3 = View.inflate(this, R.layout.page_item_three, null);

        btn_start = view3.findViewById(R.id.btn_start);
        btn_start.setOnClickListener(this);

        mList.add(view1);
        mList.add(view2);
        mList.add(view3);

        //适配ViewPager
        mViewPager.setAdapter(new GuideAdapter());
        //监听ViewPager滑动
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                setPointStatus(i);
                if (i == mList.size() - 1) {
                    btn_back.setVisibility(View.GONE);
                } else {
                    btn_back.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
            case R.id.btn_start:
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
        }
    }

    class GuideAdapter extends PagerAdapter {
        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object o) {
            return view == o;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            container.addView(mList.get(position));
            return mList.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mList.get(position));
            //super.destroyItem(container, position, object);
        }
    }

    public void setPointStatus(int i) {
        switch (i) {
            case 0:
                point1.setBackgroundResource(R.drawable.point_on);
                point2.setBackgroundResource(R.drawable.point_off);
                point3.setBackgroundResource(R.drawable.point_off);
                break;
            case 1:
                point1.setBackgroundResource(R.drawable.point_off);
                point2.setBackgroundResource(R.drawable.point_on);
                point3.setBackgroundResource(R.drawable.point_off);
                break;
            case 2:
                point1.setBackgroundResource(R.drawable.point_off);
                point2.setBackgroundResource(R.drawable.point_off);
                point3.setBackgroundResource(R.drawable.point_on);
                break;
        }
    }
}
