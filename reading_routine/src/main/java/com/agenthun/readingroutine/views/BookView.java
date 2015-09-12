package com.agenthun.readingroutine.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Agent Henry on 2015/9/12.
 */
public class BookView extends View {
    private Paint mBookPaint;
    private Paint mLinePaint;
    private Paint mShadowPaint;
    private int mViewWidth;
    private int mViewHeight;

    public BookView(Context context) {
        super(context);
        initView();
    }

    public BookView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BookView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        initPaint();
    }

    private void initPaint() {
        mBookPaint = new Paint();
        mBookPaint.setStyle(Paint.Style.FILL);
        mBookPaint.setAntiAlias(true);
        mBookPaint.setColor(0xfffafafa);

        mLinePaint = new Paint();
        mLinePaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mLinePaint.setStrokeWidth(2.0f * getResources().getDisplayMetrics().density);
        mLinePaint.setAntiAlias(true);
        mLinePaint.setDither(true);
        mLinePaint.setColor(0xffe9e7d2);

        mShadowPaint = new Paint();
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setColor(0x70000000);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(16, BlurMaskFilter.Blur.NORMAL));
    }
}
