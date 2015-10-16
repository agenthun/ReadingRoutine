package com.agenthun.readingroutine.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * Created by Agent Henry on 2015/9/12.
 */
public abstract class BaseView extends ImageView {

    private BasePath basePath;

    public BaseView(Context context) {
        super(context);
        initView(context, null, 0);
    }

    public BaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public BaseView(Context context, AttributeSet attrs, int defStyleAttr) {
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
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        getBasePath().onImageDrawableReset(getDrawable());
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        getBasePath().onImageDrawableReset(getDrawable());
    }

    @Override
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        getBasePath().onImageDrawableReset(getDrawable());
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
        return getBasePath().getBorderWidth();
    }

    public void setBorderWidth(int borderWidth) {
        getBasePath().setBorderWidth(borderWidth);
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
