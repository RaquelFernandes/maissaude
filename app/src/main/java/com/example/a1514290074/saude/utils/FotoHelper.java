package com.example.a1514290074.saude.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import java.io.ByteArrayOutputStream;

public class FotoHelper {

    public static Drawable imagemCircular(Resources res, int src) {
        Bitmap bmp = BitmapFactory.decodeResource(res, src);
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(res, bmp);
        drawable.setCircular(true);
        return drawable;
    }

    public static Drawable imagemCircular(Resources res, Bitmap bmp) {
        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory.create(res, bmp);
        drawable.setCircular(true);
        return drawable;
    }

    public static byte[] imageViewToByteArray(ImageView imageView) {
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }
}
