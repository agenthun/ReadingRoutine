package com.agenthun.readingroutine.activities;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.HorizontalScrollView;
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

    @InjectView(R.id.layout_shortcut)
    LinearLayout layoutShortcut;
    @InjectView(R.id.horizontalScrollView)
    HorizontalScrollView scrollView;
    @InjectView(R.id.layout_inner_shortcut)
    LinearLayout layoutInnerShortcut;
    @InjectView(R.id.page_container)
    RelativeLayout pageContainerLayout;
    @InjectView(R.id.page_view)
    PageView pageView;

    Bitmap curPageBitmap, nextPageBitmap;
    Canvas curPageCanvas, nextPageCanvas;
    FilePageFactory filePageFactory;
    private boolean pendingIntro;
    private boolean isFistOrLastPage = true;
    private float lastX;
    private float moveLenght;// 手指滑动的距离
    private float moveLenghtThreshold = 5;// 手指滑动的距离阈值
    private int mEvents;// 判断触摸事件

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.fragment_reading);
        ButterKnife.inject(this);

        //for Shortcut
        initShortcut(layoutShortcut);
/*        pageContainerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pendingIntro) {
                    layoutShortcut.animate().translationY(0).setStartDelay(300).setDuration(400).setInterpolator(new OvershootInterpolator(0.72f)).start();
                    pendingIntro = false;
                    Log.d(TAG, "onClick() returned: on" + pendingIntro);
                } else {
                    layoutShortcut.animate().translationY(-layoutShortcut.getHeight()).setStartDelay(300).setDuration(400).setInterpolator(new DecelerateInterpolator()).start();
                    pendingIntro = true;
                    Log.d(TAG, "onClick() returned: off" + pendingIntro);
                }
            }
        });*/
/*        pageView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (pendingIntro) {
                    layoutShortcut.animate().translationY(0).setStartDelay(300).setDuration(400).setInterpolator(new OvershootInterpolator(0.72f)).start();
                    pendingIntro = false;
                    Log.d(TAG, "onClick() returned: on" + pendingIntro);
                } else {
                    layoutShortcut.animate().translationY(-layoutShortcut.getHeight()).setStartDelay(300).setDuration(400).setInterpolator(new DecelerateInterpolator()).start();
                    pendingIntro = true;
                    Log.d(TAG, "onClick() returned: off" + pendingIntro);
                }
                return false;
            }
        });*/

        // for PageView
        //pageView = new PageView(this);

        int width = pageView.getViewWidth();
        int height = pageView.getViewHeight();

        //Log.d(TAG, "onCreate() returned: " + pageView.getViewWidth() + ", " + pageView.getViewHeight());

        curPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        nextPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        curPageCanvas = new Canvas(curPageBitmap);
        nextPageCanvas = new Canvas(nextPageBitmap);

        pageView.setBitmaps(curPageBitmap, curPageBitmap);
        //pageContainerLayout.addView(pageView);

        filePageFactory = new FilePageFactory(width, height);

        // open file
        try {
            filePageFactory.openFile("/sdcard/Download/The Third Way of Love.txt");
            filePageFactory.onDraw(curPageCanvas);
        } catch (IOException e) {
            e.printStackTrace();
        }


        // PageView Listen
        pageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
/*                //Version 1
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        pageView.abortAnimation();
                        pageView.calcCornerXY(event.getX(), event.getY());

                        if (pageView.dragToRight()) {
                            // 向前翻页
                            try {
                                filePageFactory.prePage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (filePageFactory.isFirstPage()) return false;
                        } else {
                            // 向后翻页
                            try {
                                filePageFactory.nextPage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (filePageFactory.isLastPage()) return false;
                        }
                        filePageFactory.onDraw(nextPageCanvas);
                        pageView.setBitmaps(curPageBitmap, nextPageBitmap);

                        pageView.setTouch(event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_MOVE:
                        pageView.setTouch(event.getX(), event.getY());
                        break;
                    case MotionEvent.ACTION_UP:
                        filePageFactory.onDraw(curPageCanvas);
                        Log.d(TAG, "onTouch() returned: canDragOver()=" + pageView.canDragOver());
                        if (pageView.canDragOver()) {
                            pageView.startAnimation(1200);
                            pageView.refresh();
                        } else {
                            pageView.setTouch(pageView.getCornerX() - 0.09f, pageView.getCornerY() - 0.09f);
                        }
                        break;
                }
                return true;*/

                //Version 2
                switch (event.getActionMasked()) {
                    case MotionEvent.ACTION_DOWN:
                        pageView.abortAnimation();
                        pageView.calcCornerXY(event.getX(), event.getY());

                        lastX = event.getX();
                        mEvents = 0;
                        break;
                    case MotionEvent.ACTION_POINTER_DOWN:
                    case MotionEvent.ACTION_POINTER_UP:
                        mEvents = -1;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        //Log.d(TAG, "onTouch() returned: mEvents=" + mEvents);

                        moveLenght = event.getX() - lastX;

                        if (moveLenght > moveLenghtThreshold && mEvents == 0) {
                            mEvents = 1;
//                            Log.d(TAG, "onTouch() returned: right");
                            if (!pendingIntro) {
                                layoutShortcut.animate().translationY(-layoutShortcut.getHeight()).setStartDelay(300).setDuration(400).setInterpolator(new DecelerateInterpolator()).start();
                                pendingIntro = true;
                            }

                            // 向前翻页
                            try {
                                filePageFactory.prePage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (filePageFactory.isFirstPage()) {
                                isFistOrLastPage = true;
                                return false;
                            } else {
                                isFistOrLastPage = false;
                            }

                            filePageFactory.onDraw(nextPageCanvas);
                            pageView.setBitmaps(curPageBitmap, nextPageBitmap);

                            pageView.setTouch(event.getX(), event.getY());
                        } else if (moveLenght < -moveLenghtThreshold && mEvents == 0) {
                            mEvents = 1;
//                            Log.d(TAG, "onTouch() returned: left");
                            if (!pendingIntro) {
                                layoutShortcut.animate().translationY(-layoutShortcut.getHeight()).setStartDelay(300).setDuration(400).setInterpolator(new DecelerateInterpolator()).start();
                                pendingIntro = true;
                            }

                            // 向后翻页
                            try {
                                filePageFactory.nextPage();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            if (filePageFactory.isLastPage()) {
                                isFistOrLastPage = true;
                                return false;
                            } else {
                                isFistOrLastPage = false;
                            }
                            filePageFactory.onDraw(nextPageCanvas);
                            pageView.setBitmaps(curPageBitmap, nextPageBitmap);

                            pageView.setTouch(event.getX(), event.getY());
                        }
                        if (mEvents == 1 && isFistOrLastPage == false) {
                            pageView.setTouch(event.getX(), event.getY());
                        }
                        lastX = event.getX();
                        break;
                    case MotionEvent.ACTION_UP:
                        if (mEvents == 1) {
                            filePageFactory.onDraw(curPageCanvas);
//                            Log.d(TAG, "onTouch() returned: canDragOver()=" + pageView.canDragOver());
                            if (pageView.canDragOver()) {
                                pageView.startAnimation(1200);
                                pageView.refresh();
                            } else {
                                pageView.setTouch(pageView.getCornerX() - 0.09f, pageView.getCornerY() - 0.09f);
                            }
                        } else if (mEvents == 0) {
                            if (pendingIntro) {
                                layoutShortcut.animate().translationY(0).setStartDelay(300).setDuration(400).setInterpolator(new OvershootInterpolator(0.72f)).start();
                                pendingIntro = false;
                            } else {
                                layoutShortcut.animate().translationY(-layoutShortcut.getHeight()).setStartDelay(300).setDuration(400).setInterpolator(new DecelerateInterpolator()).start();
                                pendingIntro = true;
                            }
                        }
                        break;
                }
                return true;
            }
        });
    }

    private void initShortcut(final LinearLayout layoutShortcut) {
        layoutShortcut.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                layoutShortcut.getViewTreeObserver().removeOnPreDrawListener(this);
                layoutShortcut.setTranslationY(-layoutShortcut.getHeight());
                pendingIntro = true;
                return true;
            }
        });
    }

    @Override
    protected int getFragmentContainerId() {
        return 0;
    }

    @OnClick(R.id.quitReadingBtn)
    public void onQuitReadingClick() {
        Log.d(TAG, "onQuitReadingClick() returned: ");
    }

    int fontSize = 34;

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
