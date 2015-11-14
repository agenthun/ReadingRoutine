package com.agenthun.readingroutine.views;

import android.graphics.Path;
import android.graphics.RectF;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/11/14 下午10:44.
 */
public class TagPath {
    private Path mTagPath;
    private Path mBorderPath;
    private Path mShadowPath;

    private float radius = 10f;
    private float triangleHeight = 80f;
    private float borderWidth = 20f;

    public TagPath() {
        mTagPath = new Path();
        mBorderPath = new Path();
        mShadowPath = new Path();

        mTagPath.setFillType(Path.FillType.EVEN_ODD);
        mBorderPath.setFillType(Path.FillType.EVEN_ODD);
    }

    public void setPath(float width, float height) {
        float shadowWidth = width * 0.998f;
        float shadowHeight = height * 0.968f;
        float tagWidth = width * 0.993f;
        float tagHeight = height * 0.92f;

        mShadowPath.moveTo(0, 0);
        mShadowPath.lineTo(tagWidth - triangleHeight, 0);
        mShadowPath.lineTo(shadowWidth, height / 2f);
        mShadowPath.lineTo(shadowWidth - triangleHeight, shadowHeight);
        mShadowPath.lineTo(0, shadowHeight);
        mShadowPath.close();

        mBorderPath.moveTo(0, 0);
        mBorderPath.lineTo(tagWidth - triangleHeight, 0);
        mBorderPath.lineTo(tagWidth, tagHeight / 2f);
        mBorderPath.lineTo(tagWidth - triangleHeight, tagHeight);
        mBorderPath.lineTo(0, tagHeight);
        mBorderPath.close();

        mTagPath.moveTo(borderWidth, borderWidth);
        mTagPath.lineTo(tagWidth - triangleHeight - borderWidth / 2.3f, borderWidth);
        mTagPath.lineTo(tagWidth - borderWidth, tagHeight / 2f);
        mTagPath.lineTo(tagWidth - triangleHeight - borderWidth / 2.3f, tagHeight - borderWidth / 1.3f);
        mTagPath.lineTo(borderWidth, tagHeight - borderWidth / 1.3f);
        mTagPath.close();
    }

    public Path getTagPath() {
        return mTagPath;
    }

    public Path getBorderPath() {
        return mBorderPath;
    }

    public Path getShadowPath() {
        return mShadowPath;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public float getTriangleHeight() {
        return triangleHeight;
    }

    public void setTriangleHeight(float triangleHeight) {
        this.triangleHeight = triangleHeight;
    }
}
