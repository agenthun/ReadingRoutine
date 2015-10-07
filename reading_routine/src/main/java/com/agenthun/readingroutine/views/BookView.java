package com.agenthun.readingroutine.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.support.annotation.ColorRes;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.agenthun.readingroutine.R;

/**
 * Created by Agent Henry on 2015/9/12.
 */
public class BookView extends ImageView {
    private Paint mBookPaint;
    private Paint mLinePaint;

    private Path mPath;
    private BaseShape baseShape;
    private Shape shape;

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
        mBookPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBookPaint.setPathEffect(new CornerPathEffect(shape.getCornerRadius()));

        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setPathEffect(new CornerPathEffect(shape.getCornerRadius()));

        if (shape.isHasBorder()) {
            mLinePaint.setColor(shape.getBorderColor());
            mLinePaint.setStrokeWidth(shape.getBorderWidth());
        }

        if (shape.isHasShadow()) {
            mLinePaint.setShadowLayer(
                    shape.getShadowRadius(),
                    shape.getShadowXOffset(),
                    shape.getShadowYOffset(),
                    shape.getShadowColor());
        }

        if (Build.VERSION.SDK_INT > 13) setLayerType(LAYER_TYPE_SOFTWARE, null);

        baseShape = new BaseShape() {
            @Override
            protected void addEffect(float fromX, float fromY, float toX, float toY) {
                getPath().lineTo(toX, toY);
            }
        };
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
        //Force do not square measure to solve bug (use base 2 better performance)
        return (measure(measureSpecHeight) + 2);
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
    protected void onDraw(Canvas canvas) {
        if (getDrawable() == null || getDrawable().getIntrinsicWidth() == 0 || getDrawable().getIntrinsicHeight() == 0)
            return;

        switch (shape.getNumVertex()) {
            case 1:
                super.onDraw(canvas);
                break;
            case 2:
                if (shape.isHasShadow() || shape.isHasBorder()) {
                    canvas.drawRect(
                            shape.getCenterX() - shape.getDiameter() / 2.0f,
                            shape.getCenterY() - shape.getDiameter() / 2.0f,
                            shape.getCenterX() + shape.getDiameter() / 2.0f,
                            shape.getCenterY() + shape.getDiameter() / 2.0f,
                            mLinePaint
                    );
                }
                canvas.drawRect(
                        shape.getCenterX() - shape.getDiameter() / 2.0f,
                        shape.getCenterY() - shape.getDiameter() / 2.0f,
                        shape.getCenterX() + shape.getDiameter() / 2.0f,
                        shape.getCenterY() + shape.getDiameter() / 2.0f,
                        mBookPaint
                );
            default:
                if (shape.isHasShadow() || shape.isHasBorder())
                    canvas.drawPath(mPath, mLinePaint);
                canvas.drawPath(mPath, mBookPaint);
                break;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mViewWidth = w;
        mViewHeight = h;

        if (Math.min(mViewWidth, mViewHeight) != Math.min(oldw, oldh)) {
            refreshImage();
        }
    }

    private void refreshImage() {
        Bitmap bitmap = drawableToBitmap(getDrawable());
        int canvasSize = Math.min(mViewWidth, mViewHeight);
        if (canvasSize > 0 && bitmap != null) {
            BitmapShader shader = new BitmapShader(ThumbnailUtils.extractThumbnail(bitmap, canvasSize, canvasSize),
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
    public void setImageResource(int resId) {
        super.setImageResource(resId);
        refreshImage();
        invalidate();
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        updateViewSize(left, top, right, bottom);
        invalidate(left, top, right, bottom);
    }


    @Override
    public void setPaddingRelative(int start, int top, int end, int bottom) {
        super.setPaddingRelative(start, top, end, bottom);
        updateViewSize();
        invalidate();
    }

    private void updateViewSize() {
        updateViewSize(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
    }

    private void updateViewSize(int left, int top, int right, int bottom) {
        if (shape == null) return;
        float borderPadding = shape.isHasBorder() ? shape.getBorderWidth() : 0f;
        float shadowPadding = shape.isHasShadow() ? shape.getShadowRadius() : 0f;
        float xPadding = (left + right + borderPadding * 2 + shadowPadding * 2);
        float yPadding = (top + bottom + borderPadding * 2 + shadowPadding * 2);
        float diameter = Math.min((float) mViewWidth - xPadding, (float) mViewHeight - yPadding);

        if (diameter != shape.getDiameter()) {
            shape.setDiameter(diameter);
            rebuildShape();
        }
    }

    private void rebuildShape() {
        float border = shape.isHasBorder() ? shape.getBorderWidth() : 0f;
        float shadow = shape.isHasShadow() ? shape.getShadowRadius() : 0f;
        shape.setCenterX(shape.getDiameter() / 2 + (float) (getPaddingLeft() + getPaddingRight()) / 2 + border + shadow);
        shape.setCenterY(shape.getDiameter() / 2 + (float) (getPaddingTop() + getPaddingBottom()) / 2 + border + shadow);
        if (shape.getNumVertex() < 3) return;
        mPath = baseShape.getViewPath(shape);
    }


    @Override
    public void setColorFilter(ColorFilter cf) {
        mBookPaint.setColorFilter(cf);
        invalidate();
    }

    public void setColorFilterWidthBorder(ColorFilter cf) {
        mBookPaint.setColorFilter(cf);
        mLinePaint.setColorFilter(cf);
        invalidate();
    }

    public BaseShape getBaseShape() {
        return baseShape;
    }

    public void setBaseShape(BaseShape baseShape) {
        this.baseShape = baseShape;
        rebuildShape();
        invalidate();
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public float getRotationAngle() {
        return shape.getRotation();
    }

    public void setRotationAngle(float angle) {
        shape.setRotation(angle);
        rebuildShape();
        invalidate();
    }

    public int getVertices() {
        return shape.getNumVertex();
    }

    public void setVertices(int numVertex) {
        shape.setNumVertex(numVertex);
        rebuildShape();
        invalidate();
    }

    public boolean isHasBorder() {
        return shape.isHasBorder();
    }

    public void setHasBorder(boolean border) {
        shape.setHasBorder(border);
        updateBorderSpecs();
    }

    private void updateBorderSpecs() {
        if (shape.isHasBorder()) {
            mLinePaint.setStrokeWidth(shape.getBorderWidth());
            mLinePaint.setColor(shape.getBorderColor());
        } else {
            mLinePaint.setStrokeWidth(0);
            mLinePaint.setColor(0);
        }
        updateViewSize();
        invalidate();
    }

    public void setBorderWidth(float width) {
        shape.setBorderWidth(width * (getResources().getDisplayMetrics().density));
        updateBorderSpecs();
    }

    public void setBorderColor(int color) {
        shape.setBorderColor(color);
        mLinePaint.setColor(color);
        invalidate();
    }

    public void setBorderColorResource(@ColorRes int colorResource) {
        setBorderColor(getResources().getColor(colorResource));
    }

    public void addBorder(float width, int color) {
        shape.setHasBorder(true);
        shape.setBorderWidth(width * (getResources().getDisplayMetrics().density));
        shape.setBorderColor(color);
        updateBorderSpecs();
    }

    public void setBorderResource(float width, @ColorRes int colorResource) {
        addBorder(width, getResources().getColor(colorResource));
    }

    public void setCornerRadius(float radius) {
        shape.setCornerRadius(radius);
        mLinePaint.setPathEffect(new CornerPathEffect(radius));
        mBookPaint.setPathEffect(new CornerPathEffect(radius));
        invalidate();
    }

    public void addShadow() {
        shape.setHasShadow(true);
        mLinePaint.setShadowLayer(shape.getShadowRadius(), shape.getShadowXOffset(), shape.getShadowYOffset(), shape.getShadowColor());
        updateViewSize();
        invalidate();
    }

    public void addShadow(float radius, float offsetX, float offsetY, int color) {
        shape.setShadowRadius(radius);
        shape.setShadowXOffset(offsetX);
        shape.setShadowYOffset(offsetY);
        shape.setShadowColor(color);
        addShadow();
    }

    public void addShadowResource(float radius, float offsetX, float offsetY, @ColorRes int color) {
        addShadow(radius, offsetX, offsetY, getResources().getColor(color));
    }

    public void clearShadow() {
        if (!shape.isHasShadow()) return;
        shape.setHasShadow(false);
        mLinePaint.clearShadowLayer();
        updateViewSize();
        invalidate();
    }
}
