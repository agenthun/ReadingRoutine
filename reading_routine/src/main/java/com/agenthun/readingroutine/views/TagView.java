package com.agenthun.readingroutine.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.FrameLayout;

/**
 * @project TestView
 * @authors agenthun
 * @date 15/11/14 下午11:13.
 */
public class TagView extends FrameLayout {
    private TagPath mTagPath;
    private Paint mTagPaint;
    private Paint mBorderPaint;
    private Paint mShadowPaint;
    private int mViewWidth;
    private int mViewHeight;

    private float borderWidth = 20f;
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
        mTagPath.setBorderWidth(borderWidth);
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
        canvas.drawCircle(borderWidth * 4.2f, mViewHeight / 2f, 10f, mBorderPaint);
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
