package com.agenthun.readingroutine.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.transitionmanagers.TActivity;
import com.agenthun.readingroutine.views.FilePageFactory;
import com.agenthun.readingroutine.views.PageWidget;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Agent Henry on 2015/7/25.
 */
public class ReadingActivity extends TActivity {
    @InjectView(R.id.layout_inner_shortcut)
    LinearLayout layoutInnerShortcut;
    @InjectView(R.id.page_container)
    RelativeLayout layout;
    FilePageFactory filePageFactory;
    Bitmap curPageBitmap, nextPageBitmap;
    Canvas curPageCanvas, nextPageCanvas;

    PageWidget pageWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reading);
        ButterKnife.inject(this);

        int[] shortcut = new int[]{
                R.drawable.ic_decrease_font,
                R.drawable.ic_increase_font,
                R.drawable.ic_reading_background,
                R.drawable.ic_marking,
                R.drawable.ic_copy,
                R.drawable.ic_reading_list,
                R.drawable.ic_reading_open
        };

        ImageButton imageButton;
        for (int i = 0; i < shortcut.length; i++) {
            imageButton = (ImageButton) getLayoutInflater().inflate(R.layout.button_shortcut, layoutInnerShortcut, false);
            imageButton.setImageResource(shortcut[i]);
            layoutInnerShortcut.addView(imageButton);
        }

        filePageFactory = new FilePageFactory(1080, 1920);

        curPageBitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888);
        nextPageBitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888);
        curPageCanvas = new Canvas(curPageBitmap);
        nextPageCanvas = new Canvas(nextPageBitmap);

        try {
            filePageFactory.openFile("/sdcard/Download/test3.txt");
            filePageFactory.onDraw(curPageCanvas);
        } catch (IOException e) {
            e.printStackTrace();
        }

/*        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(curPageBitmap);
        layout.addView(imageView);*/

        pageWidget = new PageWidget(this);
        pageWidget.setBitmaps(curPageBitmap, curPageBitmap);
        layout.addView(pageWidget);

/*        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                if (v == layout) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        filePageFactory.onDraw(curPageCanvas);
                    }
                }
                return false;
            }
        });*/

        pageWidget.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                if (v == pageWidget) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        pageWidget.abortAnimation();
                        pageWidget.calcCornerXY(event.getX(), event.getY());

                        filePageFactory.onDraw(curPageCanvas);
                        if (pageWidget.DragToRight()) {
                            try {
                                filePageFactory.prePage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (filePageFactory.isFirstPage()) return false;
                            filePageFactory.onDraw(nextPageCanvas);
                        } else {
                            try {
                                filePageFactory.nextPage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (filePageFactory.isLastPage()) return false;
                            filePageFactory.onDraw(nextPageCanvas);
                        }
                        pageWidget.setBitmaps(curPageBitmap, nextPageBitmap);
                    }
                    ret = pageWidget.doTouchEvent(event);
                    return ret;
                }
                return false;
            }
        });
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }
}
