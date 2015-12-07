package com.agenthun.readingroutine.utils;

import android.support.annotation.DrawableRes;

import com.agenthun.readingroutine.R;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/12/6 下午10:13.
 */
public enum Avatar {

    ONE(R.drawable.avatar_1_raster),
    TWO(R.drawable.avatar_2_raster),
    THREE(R.drawable.avatar_3_raster),
    FOUR(R.drawable.avatar_4_raster),
    FIVE(R.drawable.avatar_5_raster),
    SIX(R.drawable.avatar_6_raster),
    SEVEN(R.drawable.avatar_7_raster),
    EIGHT(R.drawable.avatar_8_raster),
    NINE(R.drawable.avatar_9_raster),
    TEN(R.drawable.avatar_10_raster),
    ELEVEN(R.drawable.avatar_11_raster),
    TWELVE(R.drawable.avatar_12_raster),
    THIRTEEN(R.drawable.avatar_13_raster),
    FOURTEEN(R.drawable.avatar_14_raster),
    FIFTEEN(R.drawable.avatar_15_raster),
    SIXTEEN(R.drawable.avatar_16_raster);

    private int mResId;

    Avatar(@DrawableRes int resId) {
        mResId = resId;
    }

    @DrawableRes
    public int getDrawableId() {
        return mResId;
    }

    public String getNameForAccessibility() {
        return ordinal() + 1 + "";
    }
}
