package com.agenthun.readingroutine.views;

import android.content.Context;
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
import android.media.ThumbnailUtils;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.support.annotation.DrawableRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.agenthun.readingroutine.R;

/**
 * Created by Agent Henry on 2015/9/12.
 */
public class BookView extends ImageView {
    private Paint mPaperPaint;
    private Paint mLinePaint;
    private Paint mShadowPaint;

    private BookPath mBookPath;
    private Path mPath;
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
        setLayerType(LAYER_TYPE_SOFTWARE, null);
    }

    private void initPaint() {
        mPaperPaint = new Paint();
        mPaperPaint.setStyle(Paint.Style.FILL);
        mPaperPaint.setAntiAlias(true);
        mPaperPaint.setColor(0xfffafafa);

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


        mBookPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBookPaint.setStyle(Paint.Style.STROKE);
        mBookPaint.setPathEffect(new CornerPathEffect(5.0f));
        mBookPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBorderPaint.setStyle(Paint.Style.STROKE);
        mBorderPaint.setColor(Color.WHITE);
        mBorderPaint.setStrokeWidth(12);
        mBorderPaint.setPathEffect(new CornerPathEffect(5.0f));
        mBorderPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        initPath();
    }

    private void initPath() {
        mBookPath = new BookPath();
        mBookPath.setWidth(borderPadding, xPadding, yPadding, diameter, centerX, centerY);

/*        float borderPadding = 12.0f;
        float xPadding = getPaddingLeft() + getPaddingRight() + borderPadding * 2;
        float yPadding = getPaddingTop() + getPaddingBottom() + borderPadding * 2;
        float diameter = Math.min((float) mViewWidth - xPadding, (float) mViewHeight - yPadding);

        float centerX = diameter / 2 + (float) (getPaddingLeft() + getPaddingRight()) / 2 + borderPadding;
        float centerY = diameter / 2 + (float) (getPaddingTop() + getPaddingBottom()) / 2 + borderPadding;

        mPath = new Path();
        double angleRadians = Math.toRadians(0);
        float pointX, pointY, rotatedPointX, rotatedPointY, currentPointX = 0f, currentPointY = 0f;
        int num = 5;
        int i = 0;
        do {
            pointX = centerX + diameter / 2f * (float) Math.cos(2 * Math.PI * i / num);
            pointY = centerY + diameter / 2f * (float) Math.sin(2 * Math.PI * i / num);

            rotatedPointX = (float) (Math.cos(angleRadians) * (pointX - centerX) -
                    Math.sin(angleRadians) * (pointY - centerY) + centerX);
            rotatedPointY = (float) (Math.sin(angleRadians) * (pointX - centerX) +
                    Math.cos(angleRadians) * (pointY - centerY) + centerY);

            if (i == 0)
                mPath.moveTo(rotatedPointX, rotatedPointY);
            else {
                addEffect(currentPointX, currentPointY, rotatedPointX, rotatedPointY);
            }
            currentPointX = rotatedPointX;
            currentPointY = rotatedPointY;
            i++;
        } while (i <= num);
        mPath.close();*/
    }

    private void addEffect(float currentX, float currentY, float nextX, float nextY) {
        mPath.lineTo(nextX, nextY);
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
        /*canvas.drawPath(mPaperPath.getShadowPath(), mShadowPaint);
        canvas.drawPath(mPaperPath.getPaperPath(), mPaperPaint);
        canvas.drawPath(mPaperPath.getLinePath(), mLinePaint);*/

        canvas.drawPath(mBookPath.getmBookPath(), mBorderPaint);
        canvas.drawPath(mBookPath.getmBookPath(), mBookPaint);

/*        canvas.drawCircle(centerX, centerY, diameter / 2, mBorderPaint);
        canvas.drawCircle(centerX, centerY, diameter / 2, mBookPaint);*/
    }

    public void setColor(int color) {
        mPaperPaint.setColor(color);
        invalidate();
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
