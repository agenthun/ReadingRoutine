package com.agenthun.readingroutine.views;

import android.graphics.Path;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/9/24 上午12:27.
 */
public interface ViewShape {
    Path getViewPath(Shape shape);
}
