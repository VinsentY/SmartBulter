package com.example.vinsent_y.smartbutler.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Base64;
import android.widget.ImageView;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

/**
 * 项目名： SmartButler
 * 包名：   com.example.vinsent_y.com.example.vinsent_y.smartbutler.util
 * 文件名： ShareUtils
 * 创建者： Vincent_Y
 * 创建时间： 2018/10/28 16:38
 * 描述：    TODO
 */
public class ShareUtils {

    public static final String NAME = "config";
    //键 值
    public static void putString(Context mContext, String key, String value) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putString(key,value).apply();
    }
    //键 默认值
    public static String getString(Context mContext, String key, String defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getString(key, defValue);
    }

    public static void putInt(Context mContext, String key, int value) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putInt(key,value).apply();
    }

    public static int getInt(Context mContext, String key, int defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getInt(key, defValue);
    }
    public static void putBoolean(Context mContext, String key, boolean value) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().putBoolean(key,value).apply();
    }

    public static Boolean getBoolean(Context mContext, String key, boolean defValue) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defValue);
    }

    public static boolean delete(Context mContext, String key) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sp.edit().remove(key).commit();
    }

    public static void deleteAll(Context mContext) {
        SharedPreferences sp = mContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        sp.edit().clear().apply();
    }

    public static void putBitMap(Context mContext, String key, ImageView imageView) {
        BitmapDrawable drawable = (BitmapDrawable) imageView.getDrawable();

        //将ImageView转换为Bitmap
        imageView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(imageView.getDrawingCache());
        imageView.setDrawingCacheEnabled(false);
        //使用base64将图片转换为二进制
        ByteArrayOutputStream btStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,50,btStream);
        String imgString = new String(Base64.encodeToString(btStream.toByteArray(),Base64.DEFAULT));
        ShareUtils.putString(mContext,key, imgString);
    }

    public static Bitmap getBitMap(Context mContext, String key, Bitmap defValue) {
        String imgString = ShareUtils.getString(mContext,"image_title","");
        if (!imgString.equals("")) {
            ByteArrayInputStream biStream = new ByteArrayInputStream(Base64.decode(imgString.getBytes(),Base64.DEFAULT));
            return BitmapFactory.decodeStream(biStream);
        } else {
            return null;
        }
    }

}
