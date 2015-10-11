package com.agenthun.readingroutine.views;

import android.graphics.Path;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/10/12 上午1:45.
 */
public class BookPath {
    private Path mBookPath;
    private Path mShadowPath;

    public BookPath() {
        mBookPath = new Path();
        mShadowPath = new Path();
    }
}
