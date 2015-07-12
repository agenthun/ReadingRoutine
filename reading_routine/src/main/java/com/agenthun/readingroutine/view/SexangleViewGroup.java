package com.agenthun.readingroutine.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Agent Henry on 2015/5/9.
 */
public class SexangleViewGroup extends ViewGroup {

    public SexangleViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

        double radian30 = 30 * Math.PI / 180;
        float radius = (float) (getWidth() / (2 * (1 + 3 * Math.cos(radian30)))); //每个子View半径

        int sexangleHalfWidth = (int) (3 * radius * Math.cos(radian30));
        int sexangleQuarterHeight = (int) (radius * 3 / 2);
        int offsetX = 0; //X轴每次偏移的长度
        int offsetY = 0; //y轴每次偏移的长度

        int startLeft = sexangleHalfWidth;
        int startTop = (int) (getHeight() / 2 - (2 * sexangleQuarterHeight + 1.2 * radius));
        int startRight = startLeft + (int) (2 * radius);
        int startBottom = startTop + (int) (2 * radius);

        //System.out.println(startLeft + "," + startTop + "," + startRight + "," + startBottom);

        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);

            if (i < 6) {
                int index = i % 6;
                if ((index > 0) && (index < 3)) {
                    offsetX = sexangleHalfWidth;
                } else if (index > 3) {
                    offsetX = -sexangleHalfWidth;
                } else {
                    offsetX = 0;
                }

                if (index == 1 || index == 5) {
                    offsetY = sexangleQuarterHeight;
                } else if (index == 2 || index == 4) {
                    offsetY = 3 * sexangleQuarterHeight;
                } else if (index == 3) {
                    offsetY = 4 * sexangleQuarterHeight;
                } else {
                    offsetY = 0;
                }
            }
            if (i == 6) {
                offsetX = 0;
                offsetY = 2 * sexangleQuarterHeight;
            }

            child.layout(startLeft + offsetX, startTop + offsetY, startRight + offsetX, startBottom + offsetY);
        }
    }
}
