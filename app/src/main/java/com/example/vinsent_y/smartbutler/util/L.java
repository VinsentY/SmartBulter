package com.example.vinsent_y.smartbutler.util;

import android.util.Log;

/**
 * 项目名： SmartButler
 * 包名：   com.example.vinsent_y.com.example.vinsent_y.smartbutler.util
 * 文件名： L
 * 创建者： Vincent_Y
 * 创建时间： 2018/10/28 16:18
 * 描述：    TODO
 */
public class L {
    //开关
    public static final boolean DEBUG = true;

    public static final String TAG = "SmartButler";

    //五个等级 DIWEF

    public static void d(String text) {
        if (DEBUG) {
            Log.d(TAG, text);
        }
    }

    public static void w(String text) {
        if (DEBUG) {
            Log.w(TAG, text);
        }
    }

    public static void i(String text) {
        if (DEBUG) {
            Log.i(TAG, text);
        }
    }

    public static void e(String text) {
        if (DEBUG) {
            Log.e(TAG, text);
        }
    }



}
