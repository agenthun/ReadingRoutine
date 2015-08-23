package com.agenthun.readingroutine.views;

import android.graphics.Path;

/**
 * Created by Agent Henry on 2015/8/24.
 */
public class PaperPath {
    private Path mPaperPath;
    private Path mShadowPath;

    public PaperPath() {
        mPaperPath = new Path();
        mShadowPath = new Path();
    }

    public void setWidth(float width, float height) {
        mPaperPath.moveTo(0.038f * width, 0.025f * height);
        mPaperPath.cubicTo(
                0.038f * width, 0.025f * height,
                0.993f * width, 0.024f * height,
                0.993f * width, 0.024f * height);
        mPaperPath.cubicTo(
                0.994f * width, 0.117f * height,
                1.0f * width, 0.147f * height,
                0.988f * width, 0.501f * height);

        mPaperPath.cubicTo(
                0.977f * width, 0.851f * height,
                0.953f * width, 0.888f * height,
                0.948f * width, 0.947f * height);
        mPaperPath.cubicTo(
                0.948f * width, 0.947f * height,
                0.248f * width, 0.937f * height,
                0.003f * width, 0.922f * height);
        mPaperPath.cubicTo(
                0.003f * width, 0.922f * height,
                0.023f * width, 0.797f * height,
                0.031f * width, 0.529f * height);
        mPaperPath.cubicTo(
                0.041f * width, 0.196f * height,
                0.03f * width, 0.136f * height,
                0.038f * width, 0.025f * height);

        mShadowPath.moveTo(0.03f * width, 0.03f * height);
        mShadowPath.lineTo(0.03f * width, 0.96f * height);
        mShadowPath.lineTo(0.98f * width, 0.96f * height);
        mShadowPath.lineTo(0.98f * width, 0.03f * height);
        mShadowPath.close();
    }

    public Path getPaperPath() {
        return mPaperPath;
    }

    public Path getShadowPath() {
        return mShadowPath;
    }
}
