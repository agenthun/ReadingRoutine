package com.agenthun.readingroutine.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.agenthun.readingroutine.R;

import java.util.ArrayList;

/**
 * Created by Agent Henry on 2015/8/24.
 */
public class PaperTextView extends View {
    private PaperPath mPaperPath;
    private Paint mPaperPaint;
    private Paint mShadowPaint;
    private int mViewWidth;
    private int mViewHeight;

    public PaperTextView(Context context) {
        super(context);
        initView();
    }

    public PaperTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public PaperTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        initPaint();
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    private void initPaint() {
        mPaperPaint = new Paint();
        mPaperPaint.setStyle(Paint.Style.FILL);
        mPaperPaint.setAntiAlias(true);
        mPaperPaint.setColor(0xfffafafa);

        mShadowPaint = new Paint();
        mShadowPaint.setStyle(Paint.Style.FILL);
        mShadowPaint.setColor(0x70000000);
        mShadowPaint.setMaskFilter(new BlurMaskFilter(16, BlurMaskFilter.Blur.NORMAL));
    }

    private void initPath() {
        mPaperPath = new PaperPath();
        mPaperPath.setWidth(mViewWidth, mViewHeight);
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
        super.onDraw(canvas);
        canvas.drawPath(mPaperPath.getShadowPath(), mShadowPaint);
        canvas.drawPath(mPaperPath.getPaperPath(), mPaperPaint);
    }
}
