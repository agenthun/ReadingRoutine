package com.agenthun.readingroutine.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.agenthun.readingroutine.R;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/9/28 下午12:49.
 */
public class BookViewX extends ImageView {

    private GradientDrawable mMaskDrawable;
    private Paint mMaskPaint;

    private Path mCornerShadowPath;
    private Paint mCornerShadowPaint;
    private Paint mEdgeShadowPaint;
    private Paint mPaint;

    public BookViewX(Context context) {
        super(context);
    }

    public BookViewX(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookViewX(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.BookView, defStyleAttr, 0);
        int radius = a.getInt(R.styleable.BookView_radius_size, 16);
        int shadowSize = a.getInt(R.styleable.BookView_shadow_size, 8);
        a.recycle();
    }

    private void initPaint() {

    }
}
