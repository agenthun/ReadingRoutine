package com.agenthun.readingroutine.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.animation.FastOutLinearInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.BookInfo;
import com.agenthun.readingroutine.datastore.UserData;
import com.agenthun.readingroutine.datastore.db.BookDatabaseUtil;
import com.agenthun.readingroutine.transitionmanagers.TActivity;
import com.agenthun.readingroutine.views.FilePageFactory;
import com.agenthun.readingroutine.views.PageView;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by Agent Henry on 2015/7/25.
 */
public class ReadingActivity extends TActivity {
    private static final String TAG = "ReadingActivity";

    public static final int OPEN_FILE = 1;

    @InjectView(R.id.layout_shortcut)
    LinearLayout layoutShortcut;
    @InjectView(R.id.horizontalScrollView)
    HorizontalScrollView scrollView;
    @InjectView(R.id.layout_inner_shortcut)
    LinearLayout layoutInnerShortcut;
    @InjectView(R.id.page_view)
    PageView pageView;
    @InjectView(R.id.reading_empty_view)
    View mEmptyView;

    @InjectView(R.id.reading_list)
    ImageButton readingListImageButton;

    Bitmap curPageBitmap, nextPageBitmap;
    Canvas curPageCanvas, nextPageCanvas;
    FilePageFactory filePageFactory;
    private boolean pendingIntro;
    private boolean isFistOrLastPage = true;
    private float lastX;
    private float moveLenght;// 手指滑动的距离
    private float moveLenghtThreshold = 5;// 手指滑动的距离阈值
    private int mEvents;// 判断触摸事件

    private String fileName = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);
        ButterKnife.inject(this);

        //for Shortcut
        initShortcut(layoutShortcut);

        checkIfEmpty();

        if (fileName == null || fileName.length() == 0) {
            layoutShortcut.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (pendingIntro) {
                        layoutShortcut.animate().translationY(0).setStartDelay(300).setDuration(400).setInterpolator(new LinearOutSlowInInterpolator()).start();
                        pendingIntro = false;
                    }
                }
            }, 100);
        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void checkIfEmpty() {
        if (mEmptyView != null) {
            mEmptyView.setVisibility((fileName != null && fileName.length() > 0) ? View.GONE : View.VISIBLE);
            pageView.setVisibility((fileName != null && fileName.length() > 0) ? View.VISIBLE : View.GONE);
        }
    }

    @OnClick(R.id.quitReadingBtn)
    public void onQuitReadingClick() {
        finish();
        super.onBackPressed();
    }

    int fontSize = 34;

    @OnClick(R.id.decrease_font)
    public void onDecreaseFontClick() {
        //Log.d(TAG, "onDecreaseFontClick() returned: ");
        if (fileName != null && fileName.length() > 0) {
            fontSize -= 4;
            filePageFactory.setFontSize(fontSize);
            filePageFactory.onDraw(curPageCanvas);
            filePageFactory.onDraw(nextPageCanvas);
            pageView.refresh();
        }
    }

    @OnClick(R.id.increase_font)
    public void onIncreaseFontClick() {
        //Log.d(TAG, "onIncreaseFontClick() returned: ");
        if (fileName != null && fileName.length() > 0) {
            fontSize += 4;
            filePageFactory.setFontSize(fontSize);
            filePageFactory.onDraw(curPageCanvas);
            filePageFactory.onDraw(nextPageCanvas);
            pageView.setBitmaps(curPageBitmap, nextPageBitmap);
            pageView.refresh();
        }
    }

    @OnClick(R.id.reading_background)
    public void onReadingBackgroundClick() {
        Log.d(TAG, "onReadingBackgroundClick() returned: ");
        if (fileName != null && fileName.length() > 0) {
            Snackbar.make(layoutShortcut, "更换背景功能 - 敬请期待", Snackbar.LENGTH_SHORT).show();
        }
    }

    @OnClick(R.id.marking)
    public void onMarkingClick() {
        Log.d(TAG, "onMarkingClick() returned: ");
        if (fileName != null && fileName.length() > 0) {
            Snackbar.make(layoutShortcut, "添加笔记功能 - 敬请期待", Snackbar.LENGTH_SHORT).show();
        }
    }

    /*    @OnClick(R.id.copy)
        public void onCopyClick() {
            Log.d(TAG, "onCopyClick() returned: ");
            if (fileName != null && fileName.length() > 0) {
            }
        }*/

    @OnClick(R.id.reading_list)
    public void onReadingListClick() {
        if (fileName != null && fileName.length() > 0) {
            String bookName = fileName.substring(fileName.lastIndexOf('/') + 1, fileName.lastIndexOf('.'));
            Log.d(TAG, "queryHasBookInfo() returned: " + BookDatabaseUtil.getInstance(ReadingActivity.this).queryHasBookInfo(bookName));
            if (BookDatabaseUtil.getInstance(ReadingActivity.this).queryHasBookInfo(bookName)) {
                Snackbar.make(readingListImageButton, R.string.add_routine_already_exists, Snackbar.LENGTH_SHORT).setAction("Done", null).show();
            } else {
                final BookInfo bookInfo = new BookInfo();
                bookInfo.setUserData(UserData.getCurrentUser(ReadingActivity.this, UserData.class));
                bookInfo.setBookName(bookName);
                bookInfo.setBookColor(new Random().nextInt(4));
                SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
                Calendar calendar = Calendar.getInstance();
                bookInfo.setBookAlarmTime(DATE_FORMAT.format(calendar.getTime()));

                if (!getIsTrial()) {
                    //服务器
                    bookInfo.save(ReadingActivity.this, new SaveListener() {
                        @Override
                        public void onSuccess() {
                            //Log.i(TAG, "上传服务器成功");
                            //Log.i(TAG, bookInfo.getObjectId());
                            BookDatabaseUtil.getInstance(ReadingActivity.this).insertBookInfo(bookInfo);
                            BookDatabaseUtil.getInstance(ReadingActivity.this).queryBookInfos().add(0, bookInfo);
                            Snackbar.make(readingListImageButton, R.string.add_routine_success, Snackbar.LENGTH_SHORT).setAction("Success", null).show();
                        }

                        @Override
                        public void onFailure(int i, String s) {
                            //Log.i(TAG, "上传服务器失败: " + s);
                            BookDatabaseUtil.getInstance(ReadingActivity.this).insertBookInfo(bookInfo, bookInfo, true); //无效invalid ObjectId
                            BookDatabaseUtil.getInstance(ReadingActivity.this).queryBookInfos().add(0, bookInfo);
                            Snackbar.make(readingListImageButton, R.string.add_routine_error, Snackbar.LENGTH_SHORT).setAction("Error", null).show();
                        }
                    });
                } else {
                    BookDatabaseUtil.getInstance(ReadingActivity.this).insertBookInfo(bookInfo, bookInfo, true); //无效invalid ObjectId
                    BookDatabaseUtil.getInstance(ReadingActivity.this).queryBookInfos().add(0, bookInfo);
                    Snackbar.make(readingListImageButton, R.string.add_routine_success, Snackbar.LENGTH_SHORT).setAction("Success", null).show();
                }
            }
        }
    }

    @OnClick(R.id.reading_open)
    public void onReadingOpenClick() {
        Intent intent = new Intent(this, FilePickerActivity.class);
        startActivityForResult(intent, OPEN_FILE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == OPEN_FILE && resultCode == RESULT_OK) {
            //filePageFactory.closeFile(fileName);

            fileName = String.valueOf(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH));
            Log.d(TAG, "onActivityResult() returned: " + fileName);

            // open file
            if (fileName.endsWith(".txt")) {
                checkIfEmpty();

                try {
                    layoutShortcut.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (pendingIntro) {
                                layoutShortcut.animate().translationY(0).setDuration(400).setInterpolator(new LinearOutSlowInInterpolator()).start();
                                pendingIntro = false;
                            } else {
                                layoutShortcut.animate().translationY(-layoutShortcut.getHeight()).setDuration(400).setInterpolator(new FastOutLinearInInterpolator()).start();
                                pendingIntro = true;
                            }
                            //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
                        }
                    }, 100);

                    int width = pageView.getViewWidth();
                    int height = pageView.getViewHeight();

                    curPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    nextPageBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
                    curPageCanvas = new Canvas(curPageBitmap);
                    nextPageCanvas = new Canvas(nextPageBitmap);

                    pageView.setBitmaps(curPageBitmap, curPageBitmap);

                    filePageFactory = new FilePageFactory(width, height);

                    // open file
                    filePageFactory.openFile(fileName);
                    filePageFactory.onDraw(curPageCanvas);

                    pageView.setOnTouchListener(mOnTouchListener);
                } catch (IOException e) {
                    //getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    e.printStackTrace();
                }
            }
        }
    }

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
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
                        //SpannableString ss = filePageFactory.getSpannableString();

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
                            layoutShortcut.animate().translationY(0).setStartDelay(300).setDuration(400).setInterpolator(new LinearOutSlowInInterpolator()).start();
                            pendingIntro = false;
                        } else {
                            layoutShortcut.animate().translationY(-layoutShortcut.getHeight()).setStartDelay(300).setDuration(400).setInterpolator(new FastOutLinearInInterpolator()).start();
                            pendingIntro = true;
                        }
                    }
                    break;
            }
            return true;
        }
    };
}
