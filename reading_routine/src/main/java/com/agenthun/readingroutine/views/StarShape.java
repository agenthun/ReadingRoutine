package com.agenthun.readingroutine.views;

import android.graphics.Path;

import java.util.List;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/9/24 下午9:52.
 */
public class StarShape extends BaseShape {
    private float radiusScale;
    private boolean isConcave;

    public StarShape() {
    }

    public StarShape(float radiusScale, boolean isConcave) {
        this.radiusScale = radiusScale;
        this.isConcave = isConcave;
    }

    @Override
    protected void addEffect(float fromX, float fromY, float toX, float toY) {
        float cX = getShape().getCenterX();
        float cY = getShape().getCenterY();
        float radius = (getShape().getDiameter() / 2f) * radiusScale;

        float pX = (fromX + toX) / 2f;
        float pY = (fromY + toY) / 2f;

        List<GeometryUtil.Point> p = GeometryUtil.getCircleLineIntersectionPoint(new GeometryUtil.Point(pX, pY), new GeometryUtil.Point(cX, cY), new GeometryUtil.Point(cX, cY), radius);

        if (isConcave) {
            getPath().quadTo((float) p.get(0).x, (float) p.get(0).y, toX, toY);
        } else {
            getPath().lineTo((float) p.get(0).x, (float) p.get(0).y);
            getPath().lineTo(toX, toY);
        }
    }

    public float getRadiusScale() {
        return radiusScale;
    }

    public void setRadiusScale(float radiusScale) {
        this.radiusScale = radiusScale;
    }

    public boolean isConcave() {
        return isConcave;
    }

    public void setIsConcave(boolean isConcave) {
        this.isConcave = isConcave;
    }
}
