package com.agenthun.readingroutine.views;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/11/14 下午9:42.
 */
public class TagViewGroup extends BaseViewGroup {
    private TagPath tagPath;

    public TagViewGroup(Context context) {
        super(context);
    }

    public TagViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TagViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected BasePath createBasePath() {
        tagPath = new TagPath();
        return tagPath;
    }
}
