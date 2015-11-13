package com.agenthun.readingroutine.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/10/17 上午2:26.
 */
public class BookViewGroup extends BaseViewGroup {
    private BookPath bookPath;

    public BookViewGroup(Context context) {
        super(context);
    }

    public BookViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BookViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected BasePath createBasePath() {
        bookPath = new BookPath();
        return bookPath;
    }
}
