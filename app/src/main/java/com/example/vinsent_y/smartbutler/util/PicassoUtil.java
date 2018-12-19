package com.example.vinsent_y.smartbutler.util;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

public class PicassoUtil {

    public static void loadImageView(String url, ImageView imageView) {
        Picasso.get().load(url).into(imageView);
    }

    public static void loadImageViewSize(String url, ImageView imageView, int width, int height) {
        Picasso.get().load(url).resize(width, height).centerCrop().into(imageView);
    }

    public static void loadImageViewCrop(String url, ImageView imageView, int width, int height) {
        Picasso.get().load(url).transform(new CropSquareTransformation()).into(imageView);
    }

    public static class CropSquareTransformation implements Transformation {
        @Override public Bitmap transform(Bitmap source) {
            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;
            Bitmap result = Bitmap.createBitmap(source, x, y, size, size);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override public String key() { return "vincent"; }
    }

}
