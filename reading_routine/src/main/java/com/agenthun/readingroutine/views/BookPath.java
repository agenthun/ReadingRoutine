package com.agenthun.readingroutine.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/10/17 上午2:28.
 */
public class BookPath extends BasePath {
    private Path mBodyPath;
    private Path mBorderPath;

    public BookPath() {
        mBodyPath = new Path();
        mBorderPath = new Path();
    }

    @Override
    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        super.init(context, attrs, defStyleAttr);
        mBorderPaint.setStrokeWidth(borderWidth * 2);
    }

    @Override
    public void draw(Canvas canvas, Paint imagePaint, Paint borderPaint) {
        canvas.drawPath(mBorderPath, borderPaint);
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
        mBodyPath.setFillType(Path.FillType.EVEN_ODD);

        RectF rectF = new RectF(-x, -y, x + bitmapWidth, y + bitmapHeight);
        mBodyPath.addRoundRect(rectF, 10.0f, 10.0f, Path.Direction.CW);
    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);

        RectF rectF = new RectF(borderWidth, borderWidth, mViewWidth - borderWidth, mViewHeight - borderWidth);
        mBorderPath.addRoundRect(rectF, 10.0f, 10.0f, Path.Direction.CW);
    }
}
