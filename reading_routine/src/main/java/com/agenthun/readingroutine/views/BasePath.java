package com.agenthun.readingroutine.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.agenthun.readingroutine.R;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/10/17 上午1:28.
 */
public abstract class BasePath {
    protected Paint mImagePaint;
    protected Paint mBorderPaint;
    protected BitmapShader shader;
    protected Drawable drawable;
    protected Matrix matrix = new Matrix();

    protected int mViewWidth;
    protected int mViewHeight;

    protected int borderColor = Color.WHITE;
    protected int mBorderWidth = 10;
    protected boolean square = false;

    public BasePath() {
        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);

        mImagePaint = new Paint();
        mImagePaint.setAntiAlias(true);
    }

    public abstract void draw(Canvas canvas, Paint imagePaint, Paint borderPaint);

    public abstract void reset();

    public abstract void calculate(int bitmapWidth, int bitmapHeight, float width, float height, float scale, float x, float y);

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BaseView, defStyleAttr, 0);
            mBorderWidth = typedArray.getDimensionPixelOffset(R.styleable.BaseView_border_width, mBorderWidth);
            borderColor = typedArray.getColor(R.styleable.BaseView_border_color, borderColor);
            square = typedArray.getBoolean(R.styleable.BaseView_is_square, square);
            typedArray.recycle();
        }

        mBorderPaint.setColor(borderColor);
        mBorderPaint.setStrokeWidth(mBorderWidth);
        mBorderPaint.setAlpha(255);
    }

    public boolean onDraw(Canvas canvas) {
        if (shader == null) {
            createShader();
        }
        if (shader != null && mViewWidth > 0 && mViewHeight > 0) {
            draw(canvas, mImagePaint, mBorderPaint);
            return true;
        }
        return false;
    }

    private void createShader() {
        Bitmap bitmap = calculateDrawableSize();
        if (bitmap != null && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
            shader = new BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mImagePaint.setShader(shader);
        }
    }

    private Bitmap calculateDrawableSize() {
        Bitmap bitmap = null;
        if (drawable != null) {
            if (drawable instanceof BitmapDrawable) {
                bitmap = ((BitmapDrawable) drawable).getBitmap();
            }
        }

        if (bitmap != null) {
            int bitmapWidth = bitmap.getWidth();
            int bitmapHeight = bitmap.getHeight();

            if (bitmapWidth > 0 && bitmapHeight > 0) {
                float width = Math.round(mViewWidth - 2f * mBorderWidth);
                float height = Math.round(mViewHeight - 2f * mBorderWidth);

                float scale;
                float x = 0, y = 0;
                if (bitmapWidth * height > width * bitmapHeight) {
                    scale = height / (float) bitmapHeight;
                    x = Math.round((width / scale - bitmapWidth) / 2f);
                } else {
                    scale = width / (float) bitmapWidth;
                    y = Math.round((height / scale - bitmapHeight) / 2f);
                }
                matrix.setScale(scale, scale);
                matrix.preTranslate(x, y);
                matrix.postTranslate(mBorderWidth, mBorderWidth);

                calculate(bitmapWidth, bitmapHeight, width, height, scale, x, y);
                return bitmap;
            }
        }
        reset();
        return null;
    }

    public void onSizeChanged(int width, int height) {
        if (mViewWidth == width && mViewHeight == height) return;
        mViewWidth = width;
        mViewHeight = height;
        if (isSquare()) {
            mViewWidth = mViewHeight = Math.min(width, height);
        }
        if (shader != null) {
            calculateDrawableSize();
        }
    }

    public void onImageDrawableReset(Drawable drawable) {
        this.drawable = drawable;
        shader = null;
        mImagePaint.setShader(null);
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
        if (mBorderPaint != null) {
            mBorderPaint.setColor(borderColor);
        }
    }

    public float getmBorderWidth() {
        return mBorderWidth;
    }

    public void setmBorderWidth(int mBorderWidth) {
        this.mBorderWidth = mBorderWidth;
        if (mBorderPaint != null) {
            mBorderPaint.setStrokeWidth(mBorderWidth);
        }
    }

    public boolean isSquare() {
        return square;
    }

    public void setIsSquare(boolean isSquare) {
        this.square = isSquare;
    }
}
