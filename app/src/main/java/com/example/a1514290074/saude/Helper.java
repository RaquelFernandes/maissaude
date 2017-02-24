package com.example.a1514290074.saude;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;

public class Helper {

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

}
