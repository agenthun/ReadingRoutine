package com.agenthun.readingroutine.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.transitionmanagers.TActivity;
import com.agenthun.readingroutine.views.FilePageFactory;
import com.agenthun.readingroutine.views.PageView;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Agent Henry on 2015/7/25.
 */
public class ReadingActivity extends TActivity {
    private static final String TAG = "ReadingActivity";

    @InjectView(R.id.horizontalScrollView)
    HorizontalScrollView scrollView;
    @InjectView(R.id.layout_inner_shortcut)
    LinearLayout layoutInnerShortcut;
    @InjectView(R.id.page_container)
    RelativeLayout layout;
    //@InjectView(R.id.page_view)
    PageView pageView;

    Bitmap curPageBitmap, nextPageBitmap;
    Canvas curPageCanvas, nextPageCanvas;
    FilePageFactory filePageFactory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reading);
        ButterKnife.inject(this);

/*        // for HorizontalScrollView
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(TAG, "onTouch() returned: " + v.getId());
                Log.d(TAG, "onTouch() returned: " + R.id.increase_font);
                return false;
            }
        });*/

        // for PageView
        pageView = new PageView(this);

        int width = pageView.getViewWidth();
        int height = pageView.getViewHeight();

        Log.d(TAG, "onCreate() returned: " + pageView.getViewWidth() + ", " + pageView.getViewHeight());

        curPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        nextPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        curPageCanvas = new Canvas(curPageBitmap);
        nextPageCanvas = new Canvas(nextPageBitmap);

        pageView.setBitmaps(curPageBitmap, curPageBitmap);
        layout.addView(pageView);

        filePageFactory = new FilePageFactory(width, height);
        //filePageFactory = pageView.getPageFactory();

        // open file
        try {
            filePageFactory.openFile("/sdcard/Download/test3.txt");
            filePageFactory.onDraw(curPageCanvas);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // PageView Listen
        pageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                if (v == pageView) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        pageView.abortAnimation();
                        pageView.calcCornerXY(event.getX(), event.getY());

                        filePageFactory.onDraw(curPageCanvas);
                        if (pageView.dragToRight()) {
                            // 向前翻页
                            try {
                                filePageFactory.prePage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (filePageFactory.isFirstPage()) return false;
                            filePageFactory.onDraw(nextPageCanvas);
                        } else {
                            // 向后翻页
                            try {
                                filePageFactory.nextPage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (filePageFactory.isLastPage()) return false;
                            filePageFactory.onDraw(nextPageCanvas);
                        }
                        pageView.setBitmaps(curPageBitmap, nextPageBitmap);
                    }
                    ret = pageView.doTouchEvent(event);
                    return ret;
                }
                return false;
            }
        });


        /*        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(curPageBitmap);
        layout.addView(imageView);

        layout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                boolean ret = false;
                if (v == layout) {
                    if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        //filePageFactory.onDraw(curPageCanvas);

                        int mCornerX = 0;
                        int mCornerY = 0;
                        float x = event.getX();
                        float y = event.getY();

                        if (x <= v.getWidth() / 2) {
                            mCornerX = 0;
                        } else {
                            mCornerX = v.getWidth();
                        }
                        if (y <= v.getHeight() / 2) {
                            mCornerY = 0;
                        } else {
                            mCornerY = v.getHeight();
                        }

                        Log.d(TAG, "onTouch() returned: " + mCornerX);
                        if (mCornerX > 0) {
                            // 向后翻页
                            try {
                                filePageFactory.nextPage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (filePageFactory.isLastPage()) return false;
                            filePageFactory.onDraw(curPageCanvas);
                        } else {
                            // 向前翻页
                            try {
                                filePageFactory.prePage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (filePageFactory.isFirstPage()) return false;
                            filePageFactory.onDraw(curPageCanvas);
                        }
                    }
                    layout.postInvalidate();
                    return ret;
                }
                return false;
            }
        });*/
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @OnClick(R.id.quitReadingBtn)
    public void onQuitReadingClick() {
        Log.d(TAG, "onQuitReadingClick() returned: ");
    }

    int fontSize = 24;

    @OnClick(R.id.decrease_font)
    public void onDecreaseFontClick() {
        Log.d(TAG, "onDecreaseFontClick() returned: ");
        fontSize -= 4;
        filePageFactory.setFontSize(fontSize);
        filePageFactory.onDraw(curPageCanvas);
        filePageFactory.onDraw(nextPageCanvas);
        pageView.refresh();
    }

    @OnClick(R.id.increase_font)
    public void onIncreaseFontClick() {
        Log.d(TAG, "onIncreaseFontClick() returned: ");
        fontSize += 4;
        filePageFactory.setFontSize(fontSize);
        filePageFactory.onDraw(curPageCanvas);
        filePageFactory.onDraw(nextPageCanvas);
        pageView.refresh();
    }

    @OnClick(R.id.reading_background)
    public void onReadingBackgroundClick() {
        Log.d(TAG, "onReadingBackgroundClick() returned: ");
    }

    @OnClick(R.id.marking)
    public void onMarkingClick() {
        Log.d(TAG, "onMarkingClick() returned: ");
    }

    @OnClick(R.id.copy)
    public void onCopyClick() {
        Log.d(TAG, "onCopyClick() returned: ");
    }

    @OnClick(R.id.reading_list)
    public void onReadingListClick() {
        Log.d(TAG, "onReadingListClick() returned: ");
    }

    @OnClick(R.id.reading_open)
    public void onReadingOpenClick() {
        Log.d(TAG, "onReadingOpenClick() returned: ");
    }
}
