package com.agenthun.readingroutine.views;

import android.graphics.Path;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/9/24 下午9:26.
 */
public abstract class BaseShape {
    private Path path;
    private Shape shape;

    public BaseShape() {
        this.path = path;
    }

    public Path getViewPath(Shape shape) {
        this.shape = shape;
        float pointX, pointY, rotatePointX, rotatePointY, currentPointX = 0f, currentPointY = 0f;
        double angleRadians = Math.toRadians(shape.getRotation());

        path.reset();
        for (int i = 0; i < shape.getNumVertex(); i++) {
            pointX = shape.getCenterX() - shape.getDiameter() / 2f * (float) Math.cos(2 * Math.PI * i / shape.getNumVertex());
            pointY = shape.getCenterY() - shape.getDiameter() / 2f * (float) Math.sin(2 * Math.PI * i / shape.getNumVertex());
            rotatePointX = (float) (Math.cos(angleRadians) * (pointX - shape.getCenterX()) - Math.sin(angleRadians) * (pointY - shape.getCenterY()) + shape.getCenterX());
            rotatePointY = (float) (Math.sin(angleRadians) * (pointX - shape.getCenterX()) - Math.cos(angleRadians) * (pointY - shape.getCenterY()) + shape.getCenterY());

            if (i > 0) {
                addEffect(currentPointX, currentPointY, rotatePointX, rotatePointY);
            } else {
                path.moveTo(rotatePointX, rotatePointY);
            }
        }
        path.close();
        return path;
    }

    public Path getPath() {
        return path;
    }

    public Shape getShape() {
        return shape;
    }

    abstract protected void addEffect(float fromX, float fromY, float toX, float toY);
}
