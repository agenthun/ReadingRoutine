package com.agenthun.readingroutine.views;

import android.graphics.Path;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/10/12 上午1:45.
 */
public class BookPath {
    private Path mBookPath;
    private Path mShadowPath;

    public BookPath() {
        mBookPath = new Path();
        mShadowPath = new Path();
    }

    public void setWidth(float borderPadding, float xPadding, float yPadding, float diameter, float centerX, float centerY) {

        double angleRadians = Math.toRadians(0);
        float pointX, pointY, rotatedPointX, rotatedPointY, currentPointX = 0f, currentPointY = 0f;
        int num = 5;
        int i = 0;
        do {
            pointX = centerX + diameter / 2f * (float) Math.cos(2 * Math.PI * i / num);
            pointY = centerY + diameter / 2f * (float) Math.sin(2 * Math.PI * i / num);

            rotatedPointX = (float) (Math.cos(angleRadians) * (pointX - centerX) -
                    Math.sin(angleRadians) * (pointY - centerY) + centerX);
            rotatedPointY = (float) (Math.sin(angleRadians) * (pointX - centerX) +
                    Math.cos(angleRadians) * (pointY - centerY) + centerY);

            if (i == 0)
                mBookPath.moveTo(rotatedPointX, rotatedPointY);
            else {
                addEffect(currentPointX, currentPointY, rotatedPointX, rotatedPointY);
            }
            currentPointX = rotatedPointX;
            currentPointY = rotatedPointY;
            i++;
        } while (i <= num);
        mBookPath.close();


/*        mBookPath.moveTo(0.038f * width, 0.025f * height);
        mBookPath.cubicTo(
                0.038f * width, 0.025f * height,
                0.993f * width, 0.024f * height,
                0.993f * width, 0.024f * height);
        mBookPath.cubicTo(
                0.994f * width, 0.117f * height,
                1.0f * width, 0.147f * height,
                0.988f * width, 0.501f * height);

        mBookPath.cubicTo(
                0.977f * width, 0.851f * height,
                0.953f * width, 0.888f * height,
                0.948f * width, 0.947f * height);
        mBookPath.cubicTo(
                0.948f * width, 0.947f * height,
                0.248f * width, 0.937f * height,
                0.003f * width, 0.922f * height);
        mBookPath.cubicTo(
                0.003f * width, 0.922f * height,
                0.023f * width, 0.797f * height,
                0.031f * width, 0.529f * height);
        mBookPath.cubicTo(
                0.041f * width, 0.196f * height,
                0.03f * width, 0.136f * height,
                0.038f * width, 0.025f * height);*/

/*        mShadowPath.moveTo(0.03f * width, 0.03f * height);
        mShadowPath.lineTo(0.03f * width, 0.96f * height);
        mShadowPath.lineTo(0.98f * width, 0.96f * height);
        mShadowPath.lineTo(0.98f * width, 0.03f * height);
        mShadowPath.close();*/
    }

    private void addEffect(float currentX, float currentY, float nextX, float nextY) {
        mBookPath.lineTo(nextX, nextY);
    }

    public Path getmBookPath() {
        return mBookPath;
    }

    public Path getmShadowPath() {
        return mShadowPath;
    }
}
