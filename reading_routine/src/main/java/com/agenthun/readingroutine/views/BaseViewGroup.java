package com.agenthun.readingroutine.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Agent Henry on 2015/9/12.
 */
public abstract class BaseViewGroup extends RelativeLayout {

    private BasePath basePath;

    public BaseViewGroup(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public BaseViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public BaseViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        getBasePath().init(context, attrs, defStyleAttr);
    }

    protected BasePath getBasePath() {
        if (basePath == null) {
            basePath = createBasePath();
        }
        return basePath;
    }

    protected abstract BasePath createBasePath();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getBasePath().isSquare()) {
            super.onMeasure(widthMeasureSpec, widthMeasureSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        getBasePath().onSizeChanged(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (!getBasePath().onDraw(canvas)) {
            super.onDraw(canvas);
        }
    }

    public int getBorderColor() {
        return getBasePath().getBorderColor();
    }

    public void setBorderColor(int borderColor) {
        getBasePath().setBorderColor(borderColor);
        invalidate();
    }

    public float getBorderWidth() {
        return getBasePath().getmBorderWidth();
    }

    public void setBorderWidth(int borderWidth) {
        getBasePath().setmBorderWidth(borderWidth);
        invalidate();
    }

    public boolean isSquare() {
        return getBasePath().isSquare();
    }

    public void setIsSquare(boolean isSquare) {
        getBasePath().setIsSquare(isSquare);
        invalidate();
    }
}
