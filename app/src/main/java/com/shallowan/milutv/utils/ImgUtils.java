package com.shallowan.milutv.utils;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.shallowan.milutv.MiluApplication;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by ShallowAn on 2018/1/2.
 */

public class ImgUtils {
    public static void load(String url, ImageView targetView) {
        Glide.with(MiluApplication.getContext())
                .load(url)
                .into(targetView);
    }

    public static void load(int resId, ImageView targetView) {
        Glide.with(MiluApplication.getContext())
                .load(resId)
                .into(targetView);
    }

    public static void loadRound(String url, ImageView targetView) {
        Glide.with(MiluApplication.getContext())
                .load(url)
                .bitmapTransform(new CropCircleTransformation(MiluApplication.getContext()))
                .into(targetView);
    }

    public static void loadRound(int resId, ImageView targetView) {
        Glide.with(MiluApplication.getContext())
                .load(resId)
                .bitmapTransform(new CropCircleTransformation(MiluApplication.getContext()))
                .into(targetView);
    }
}
