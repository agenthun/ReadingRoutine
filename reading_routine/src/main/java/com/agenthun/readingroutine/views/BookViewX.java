package com.agenthun.readingroutine.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.media.ThumbnailUtils;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.agenthun.readingroutine.R;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/9/28 下午12:49.
 */
public class BookViewX extends ImageView {

    private BookPath mBookPath;
    private Paint mBookPaint;
    private Paint mBorderPaint;

    private int mViewWidth;
    private int mViewHeight;

    private float borderPadding;
    private float xPadding;
    private float yPadding;
    private float diameter;
    private float centerX;
    private float centerY;

    public BookViewX(Context context) {
        super(context);
        initView();
    }

    public BookViewX(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public BookViewX(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        initPaint();
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    private void initPaint() {
        mBookPaint = new Paint();
        mBookPaint.setAntiAlias(true);
        mBookPaint.setStyle(Paint.Style.STROKE);
        mBookPaint.setPathEffect(new CornerPathEffect(5.0f));
        //mBookPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mBorderPaint = new Paint();
        mBorderPaint.setAntiAlias(true);
        mBorderPaint.setFilterBitmap(true);
        mBorderPaint.setDither(true);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setStrokeWidth(12);
        mBorderPaint.setPathEffect(new CornerPathEffect(5.0f));
        mBorderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
    }

    private void initPath() {
        mBookPath = new BookPath();
        mBookPath.setWidth(borderPadding, xPadding, yPadding, diameter, centerX, centerY);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mViewWidth = w;
        mViewHeight = h;
        updatePolygonSize();

        initPath();

        if (mViewWidth != oldw || mViewHeight != oldh) {
            refreshImage();
        }
    }

    private void updatePolygonSize() {
        updatePolygonSize(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    private void updatePolygonSize(int l, int t, int r, int b) {
        borderPadding = 6.0f;
        xPadding = l + r + borderPadding * 2;
        yPadding = t + b + borderPadding * 2;
        diameter = Math.min((float) mViewWidth - xPadding, (float) mViewHeight - yPadding);

        centerX = diameter / 2 + (float) (l + r) / 2 + borderPadding;
        centerY = diameter / 2 + (float) (t + b) / 2 + borderPadding;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getDrawable() == null || getDrawable().getIntrinsicWidth() == 0 || getDrawable().getIntrinsicHeight() == 0)
            return;

        /*canvas.drawPath(mPaperPath.getShadowPath(), mShadowPaint);
        canvas.drawPath(mPaperPath.getPaperPath(), mPaperPaint);
        canvas.drawPath(mPaperPath.getLinePath(), mLinePaint);*/

        canvas.drawPath(mBookPath.getmBookPath(), mBorderPaint);
        canvas.drawPath(mBookPath.getmBookPath(), mBookPaint);

        /*        canvas.drawCircle(centerX, centerY, diameter / 2, mBorderPaint);
        canvas.drawCircle(centerX, centerY, diameter / 2, mBookPaint);*/
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = measureWidth(widthMeasureSpec);
        int height = measureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int measureSpecWidth) {
        return measure(measureSpecWidth);
    }

    private int measureHeight(int measureSpecHeight) {
        return (measure(measureSpecHeight));
    }

    private int measure(int measureSpec) {
        int result;
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY || specMode == MeasureSpec.AT_MOST) {
            result = specSize;
        } else {
            result = Math.min(mViewWidth, mViewHeight);
        }

        return result;
    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        refreshImage();
        invalidate();
    }

    @Override
    public void setImageDrawable(Drawable drawable) {
        super.setImageDrawable(drawable);
        refreshImage();
        invalidate();
    }

    @Override
    public void setImageResource(@DrawableRes int resId) {
        super.setImageResource(resId);
        refreshImage();
        invalidate();
    }

    private void refreshImage() {
        Bitmap bitmap = drawableToBitmap(getDrawable());
        int size = Math.min(mViewWidth, mViewHeight);
        if (size > 0 && bitmap != null) {
            BitmapShader shader = new BitmapShader(ThumbnailUtils.extractThumbnail(bitmap, size, size),
                    Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
            mBookPaint.setShader(shader);
        }
    }

    private Bitmap drawableToBitmap(Drawable drawable) {
        if (drawable == null) {
            return null;
        } else if (drawable instanceof BitmapDrawable) {
            return ((BitmapDrawable) drawable).getBitmap();
        }

        int width = drawable.getIntrinsicWidth();
        width = width > 0 ? width : 1;
        int height = drawable.getIntrinsicHeight();
        height = height > 0 ? height : 1;

        Bitmap bitmap;
        try {
            bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError e) {
            return null;
        }

        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        drawable.draw(canvas);
        return bitmap;
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        mBookPaint.setColorFilter(cf);
        invalidate();
    }

    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        updatePolygonSize();
        invalidate();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        updatePolygonSize(left, top, right, bottom);
        invalidate(left, top, right, bottom);
    }
}
