package com.agenthun.readingroutine.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

/**
 * @project TestView
 * @authors agenthun
 * @date 15/11/14 下午11:13.
 */
public class TagView extends FrameLayout {
    private static final String TAG = "TagView";

    private TagPath mTagPath;
    private Paint mTagPaint;
    private Paint mBorderPaint;
    private Paint mShadowPaint;
    private int mViewWidth;
    private int mViewHeight;

    float ratio = 1;
    private float borderWidth = 6f;
    private float triangleHeight = 80f;

    public TagView(Context context) {
        super(context);
        initView();
    }

    public TagView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public TagView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        initPaint();
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    private void initPaint() {
        mTagPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mTagPaint.setStyle(Paint.Style.FILL);
        mTagPaint.setColor(Color.parseColor("#F0E093"));

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        borderWidth = borderWidth * getResources().getDisplayMetrics().density;
        mBorderPaint.setStrokeWidth(borderWidth);
        mBorderPaint.setDither(true);
        mBorderPaint.setColor(Color.WHITE);

        mShadowPaint = new Paint();
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setColor(0x70000000);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(16, BlurMaskFilter.Blur.NORMAL));
    }

    private void initPath() {
        mTagPath = new TagPath();

        float width = (float) mViewWidth / 720;
        float height = (float) mViewHeight / 72;
        ratio = Math.min(width, height);
        //Log.d(TAG, "initPath() mViewWidth, mViewHeight: " + mViewWidth + ", " + mViewHeight);
        //Log.d(TAG, "initPath() ratio: " + ratio);
        mTagPath.setBorderWidth(borderWidth);
        triangleHeight = triangleHeight * ratio;
        mTagPath.setTriangleHeight(triangleHeight);
        mTagPath.setPath(mViewWidth, mViewHeight);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        mViewWidth = w;
        mViewHeight = h;
        initPath();
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //super.onDraw(canvas);
        canvas.drawPath(mTagPath.getShadowPath(), mShadowPaint);
        canvas.drawPath(mTagPath.getBorderPath(), mBorderPaint);
        canvas.drawPath(mTagPath.getTagPath(), mTagPaint);
        canvas.drawCircle(borderWidth * 4.2f, mViewHeight / 2f, borderWidth, mBorderPaint);
    }

    public void setBorderColor(int color) {
        mBorderPaint.setColor(color);
        postInvalidate();
    }

    public void setTagMaskColor(int color) {
        mTagPaint.setColor(color);
        postInvalidate();
    }
}
