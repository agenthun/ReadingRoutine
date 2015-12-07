package com.agenthun.readingroutine.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Checkable;
import android.widget.ImageView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.utils.UiUtils;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/12/6 下午10:31.
 */
public class AvatarView extends ImageView implements Checkable {
    private Context mContext;
    private boolean mChecked;

    public AvatarView(Context context) {
        this(context, null);
        mContext = context;
    }

    public AvatarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        mContext = context;
    }

    public AvatarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
    }

    @Override
    public void setChecked(boolean checked) {
        mChecked = checked;
        invalidate();
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mChecked) {
            Paint mBorderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            mBorderPaint.setStyle(Paint.Style.STROKE);
            mBorderPaint.setColor(mContext.getResources().getColor(R.color.colorAccent));
            mBorderPaint.setStrokeWidth(UiUtils.dipToPx(mContext, 4));
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2 - UiUtils.dipToPx(mContext, 2), mBorderPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }
}
