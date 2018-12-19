package com.example.vinsent_y.smartbutler.application;

import android.app.Application;

import com.baidu.mapapi.SDKInitializer;
import com.example.vinsent_y.smartbutler.util.StaticClass;

import cn.bmob.v3.Bmob;

/**
 * 项目名： SmartButler4
 * 包名：   com.example.vinsent_y.com.example.vinsent_y.smartbutler.application
 * 文件名： BaseApplication
 * 创建者： Vincent_Y
 * 创建时间： 2018/10/30 10:09
 * 描述：    TODO
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //Bmod初始化
        Bmob.initialize(this, StaticClass.BMOB_APP_ID);

        SDKInitializer.initialize(this);

    }
}
