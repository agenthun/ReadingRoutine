package net.grobas.shapes;

import android.graphics.Path;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/10/12 下午11:54.
 */
public class BookPolygonShape extends BasePolygonShape {
    private Path mPath;

    public BookPolygonShape() {
        mPath = new Path();
    }

    @Override
    protected void addEffect(float currentX, float currentY, float nextX, float nextY) {

    }

    @Override
    public Path getPolygonPath(PolygonShapeSpec spec) {

        float width = spec.getDiameter();
        float height = spec.getDiameter();

        mPath.moveTo(0.038f * width, 0.025f * height);
        mPath.cubicTo(
                0.038f * width, 0.025f * height,
                0.993f * width, 0.024f * height,
                0.993f * width, 0.024f * height);
        mPath.cubicTo(
                0.994f * width, 0.117f * height,
                1.0f * width, 0.147f * height,
                0.988f * width, 0.501f * height);

        mPath.cubicTo(
                0.977f * width, 0.851f * height,
                0.953f * width, 0.888f * height,
                0.948f * width, 0.947f * height);
        mPath.cubicTo(
                0.948f * width, 0.947f * height,
                0.248f * width, 0.937f * height,
                0.003f * width, 0.922f * height);
        mPath.cubicTo(
                0.003f * width, 0.922f * height,
                0.023f * width, 0.797f * height,
                0.031f * width, 0.529f * height);
        mPath.cubicTo(
                0.041f * width, 0.196f * height,
                0.03f * width, 0.136f * height,
                0.038f * width, 0.025f * height);

        mPath.close();

        return mPath;
    }
}
