package com.agenthun.readingroutine.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
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
    private static final String TAG = "BookPath";
    private Path mBodyPath;
    private Path mBorderPath;
    private Path mLinePath;
    private Path mTagPath;
    private Paint mLinePaint;
    private Paint mTagPaint;

    public BookPath() {
        mBodyPath = new Path();
        mBorderPath = new Path();
        mLinePath = new Path();
        mTagPath = new Path();

        mLinePaint = new Paint();
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStrokeWidth(mBorderWidth * 1.2f);
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setDither(true);
        mLinePaint.setColor(Color.WHITE);

        mTagPaint = new Paint();
        mTagPaint.setAntiAlias(true);
        mTagPaint.setStrokeWidth(mBorderWidth);
        mTagPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mTagPaint.setDither(true);
        mTagPaint.setColor(Color.parseColor("#DADAC0"));
    }

    @Override
    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        super.init(context, attrs, defStyleAttr);
    }

    @Override
    public void draw(Canvas canvas, Paint imagePaint, Paint borderPaint) {
        canvas.drawPath(mBorderPath, borderPaint);
        canvas.drawPath(mTagPath, mTagPaint);
        canvas.save();
        canvas.concat(matrix);
        canvas.drawPath(mBodyPath, imagePaint);
        canvas.drawPath(mLinePath, mLinePaint);

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
        mBodyPath.close();

        mLinePath.moveTo(mBorderWidth, -y);
        mLinePath.lineTo(mBorderWidth, y + bitmapHeight);
        mLinePath.close();
    }

    @Override
    public void onSizeChanged(int width, int height) {
        super.onSizeChanged(width, height);

        RectF rectF = new RectF(mBorderWidth, mBorderWidth, mViewWidth - mBorderWidth, mViewHeight - mBorderWidth);
        mBorderPath.addRoundRect(rectF, 10.0f, 10.0f, Path.Direction.CW);

        float tempY = mViewHeight / 5.0f;
        float r = tempY;
        rectF = new RectF(mViewWidth, tempY, mViewWidth + r, tempY + r * 0.72f);
        mTagPath.addRect(rectF, Path.Direction.CW);

        rectF = new RectF(mViewWidth, tempY * 2, mViewWidth + r, tempY * 2 + r * 0.78f);
        mTagPath.addRect(rectF, Path.Direction.CW);

        rectF = new RectF(mViewWidth, tempY * 3, mViewWidth + r, tempY * 3 + r * 0.68f);
        mTagPath.addRect(rectF, Path.Direction.CW);
    }
}
