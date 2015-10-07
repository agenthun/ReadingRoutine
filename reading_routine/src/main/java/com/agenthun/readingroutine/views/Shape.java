package com.agenthun.readingroutine.views;

import android.graphics.Color;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/9/24 上午12:28.
 */
public class Shape {
    
    private static final float DEFAULT_SHADOW_RADIUS = 7.5f;
    private static final int DEFAULT_SHADOW_COLOR = Color.BLACK;
    private static final float DEFAULT_SHADOW_XOFFSET = 0.0f;
    private static final float DEFAULT_SHADOW_YOFFSET = 0.0f;
    private float diameter;
    private float centerX;
    private float centerY;
    private float rotation;
    private int numVertex;

    private boolean hasBorder;
    private float cornerRadius;
    private int borderColor;
    private float borderWidth;

    private boolean hasShadow;
    private float shadowRadius;
    private int shadowColor;
    private float shadowXOffset;
    private float shadowYOffset;

    public Shape() {
        defaultShadow();
    }

    public Shape(float diameter, float centerX, float centerY, float rotation, int numVertex) {
        this.diameter = diameter;
        this.centerX = centerX;
        this.centerY = centerY;
        this.rotation = rotation;
        this.numVertex = numVertex;
        defaultShadow();
    }

    private void defaultShadow() {
        shadowRadius = DEFAULT_SHADOW_RADIUS;
        shadowColor = DEFAULT_SHADOW_COLOR;
        shadowXOffset = DEFAULT_SHADOW_XOFFSET;
        shadowYOffset = DEFAULT_SHADOW_YOFFSET;
    }

    public void updatePosition(float centerX, float centerY, float diameter) {
        setCenterX(centerX);
        setCenterY(centerY);
        setDiameter(diameter);
    }

    public float getDiameter() {
        return diameter;
    }

    public void setDiameter(float diameter) {
        this.diameter = diameter;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }

    public int getNumVertex() {
        return numVertex;
    }

    public void setNumVertex(int numVertex) {
        this.numVertex = numVertex;
    }

    public boolean isHasBorder() {
        return hasBorder;
    }

    public void setHasBorder(boolean hasBorder) {
        this.hasBorder = hasBorder;
    }

    public float getCornerRadius() {
        return cornerRadius;
    }

    public void setCornerRadius(float cornerRadius) {
        this.cornerRadius = cornerRadius;
    }

    public int getBorderColor() {
        return borderColor;
    }

    public void setBorderColor(int borderColor) {
        this.borderColor = borderColor;
    }

    public float getBorderWidth() {
        return borderWidth;
    }

    public void setBorderWidth(float borderWidth) {
        this.borderWidth = borderWidth;
    }

    public boolean isHasShadow() {
        return hasShadow;
    }

    public void setHasShadow(boolean hasShadow) {
        this.hasShadow = hasShadow;
    }

    public float getShadowRadius() {
        return shadowRadius;
    }

    public void setShadowRadius(float shadowRadius) {
        this.shadowRadius = shadowRadius;
    }

    public int getShadowColor() {
        return shadowColor;
    }

    public void setShadowColor(int shadowColor) {
        this.shadowColor = shadowColor;
    }

    public float getShadowXOffset() {
        return shadowXOffset;
    }

    public void setShadowXOffset(float shadowXOffset) {
        this.shadowXOffset = shadowXOffset;
    }

    public float getShadowYOffset() {
        return shadowYOffset;
    }

    public void setShadowYOffset(float shadowYOffset) {
        this.shadowYOffset = shadowYOffset;
    }
}
