package com.agenthun.readingroutine.views;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by Agent Henry on 2015/9/12.
 */
public class BookView extends ImageView {
    private Paint mBookPaint;
    private Paint mLinePaint;

    private Path mPath;
    private Shape shape;
    private ViewShape viewShape;

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
        shape = new Shape();

        shape.setNumVertex(6);
        shape.setRotation(0.0f);
        shape.setCornerRadius(0.0f);
        shape.setHasShadow(true);
        shape.setHasBorder(true);
        shape.setBorderColor(Color.WHITE);
        shape.setBorderWidth(4.0f);

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
    }
}
