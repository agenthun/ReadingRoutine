package com.agenthun.readingroutine.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.util.AttributeSet;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/10/17 上午2:28.
 */
public class TagPath extends BasePath {
    private Path mBodyPath;
    private Path mBorderPath;

    private int triangleHeight = 50;

    public TagPath() {
        mBodyPath = new Path();
        mBorderPath = new Path();
    }

    @Override
    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        super.init(context, attrs, defStyleAttr);
//        mBorderPaint.setStrokeWidth(mBorderWidth * 2);
    }

    @Override
    public void draw(Canvas canvas, Paint imagePaint, Paint borderPaint) {
//        canvas.drawPath(mBorderPath, borderPaint);
        canvas.save();
        canvas.concat(matrix);
        canvas.drawPath(mBodyPath, imagePaint);
        canvas.restore();
    }

    @Override
    public void reset() {
        mBodyPath.reset();
    }

    @Override
    public void calculate(int bitmapWidth, int bitmapHeight, float width, float height, float scale, float x, float y) {
        mBodyPath.reset();
        float x1 = -x;
        float y1 = -y;
        float scaledTriangleHeight = triangleHeight / scale;
        float currWidth = bitmapWidth + 2 * x;
        float currHeight = bitmapHeight + 2 * y;
        float centerY = currHeight / 2f + y;

        mBodyPath.setFillType(Path.FillType.EVEN_ODD);

        float rectLeft, rectRight;
        rectLeft = x;
        float imgRight = currWidth + rectLeft;
        rectRight = imgRight - scaledTriangleHeight;

        RectF rectF = new RectF(rectLeft, y, rectRight, y + currHeight);
        mBodyPath.addRoundRect(rectF, 10.0f, 10.0f, Path.Direction.CW);
        mBodyPath.moveTo(imgRight, centerY);
        mBodyPath.lineTo(rectRight, centerY - scaledTriangleHeight);
        mBodyPath.lineTo(rectRight, centerY + scaledTriangleHeight);
        mBodyPath.lineTo(imgRight, centerY);
    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);

        RectF rectF = new RectF(mBorderWidth, mBorderWidth, mViewWidth - mBorderWidth, mViewHeight - mBorderWidth);
        mBorderPath.addRoundRect(rectF, 10.0f, 10.0f, Path.Direction.CW);
    }
}
