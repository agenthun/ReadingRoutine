package com.agenthun.readingroutine.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.activities.BookActivity;
import com.agenthun.readingroutine.adapters.RoutinesAdapter;
import com.agenthun.readingroutine.datastore.BookInfo;
import com.agenthun.readingroutine.datastore.UserData;
import com.agenthun.readingroutine.datastore.db.BookDatabaseUtil;
import com.agenthun.readingroutine.transitionmanagers.TFragment;
import com.agenthun.readingroutine.views.RevealBackgroundView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.DeleteListener;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class RoutinesFragment extends TFragment implements RevealBackgroundView.OnStateChangeListener {

    private static final String TAG = "RoutinesFragment";
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
    public static final int UPDATE_BOOK = 0;
    public static final int NEW_BOOK = 1;
    public static final int RENEW_BOOK = 2;
    public static final int DELETE_BOOK = 3;

    @InjectView(R.id.revealBackgroundView)
    RevealBackgroundView revealBackgroundView;
    @InjectView(R.id.itemRecyclerView)
    RecyclerView routinesRecyclerView;
    @InjectView(R.id.addBtn)
    FloatingActionButton addRoutinesItemBtn;

    private RoutinesAdapter routinesAdapter;
    private boolean pendingIntro;
    private ArrayList<BookInfo> mDataSet;
    private int itemPosition = 1;

    public RoutinesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base_item, container, false);
        ButterKnife.inject(this, view);

        setupDatabase();
        setupGridLayout();
        setupRevealBackground(savedInstanceState);

        initAddItemBtn(addRoutinesItemBtn);

        return view;
    }

    private void setupDatabase() {
        mDataSet = BookDatabaseUtil.getInstance(getContext()).queryBookInfos();
        if (mDataSet == null && getIsTrial() != true) {
            BmobQuery<BookInfo> bmobQuery = new BmobQuery<>();
            bmobQuery.setLimit(10);

            boolean isCache = bmobQuery.hasCachedResult(getContext(), BookInfo.class);
            if (isCache) {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
            } else {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
            }
            bmobQuery.findObjects(getContext(), new FindListener<BookInfo>() {
                @Override
                public void onSuccess(List<BookInfo> list) {
                    mDataSet = (ArrayList<BookInfo>) BookDatabaseUtil.getInstance(getContext()).setBookInfos(list);
                }

                @Override
                public void onError(int i, String s) {
                    Log.i(TAG, "获取服务端数据失败");
                    mDataSet = new ArrayList<>();
                }
            });
        }
    }

    private void setupGridLayout() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        routinesRecyclerView.setLayoutManager(layoutManager);
        routinesRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                routinesAdapter.setLockedAnimations(true);
            }
        });
        /*        RecyclerViewScrollManager scrollManager = new RecyclerViewScrollManager();
                scrollManager.attach(routinesRecyclerView);
                scrollManager.addView(addRoutinesItemBtn, RecyclerViewScrollManager.Direction.DOWN); //下滑动画*/
    }

    private void setupRevealBackground(Bundle savedInstanceState) {
        revealBackgroundView.setFillPaintColor(getResources().getColor(R.color.background_daytime_material_blue));
        revealBackgroundView.setOnStateChangeListener(this);
        if (savedInstanceState == null) {
            revealBackgroundView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    revealBackgroundView.getViewTreeObserver().removeOnPreDrawListener(this);
                    revealBackgroundView.startFromLocation(new int[]{0, 0});
                    return true;
                }
            });
        } else {
            revealBackgroundView.setToFinishedFrame();
            routinesAdapter.setLockedAnimations(true);
        }
    }

    private void initAddItemBtn(final FloatingActionButton imageButton) {
        imageButton.setImageResource(R.drawable.ic_add_white_36dp);
        //初始化隐藏Button
        imageButton.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageButton.getViewTreeObserver().removeOnPreDrawListener(this);
                pendingIntro = true;
                imageButton.setTranslationY(2 * imageButton.getHeight());
                return true;
            }
        });
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            routinesRecyclerView.setVisibility(View.VISIBLE);

            routinesAdapter = new RoutinesAdapter(getContext().getApplicationContext(), mDataSet);
            routinesRecyclerView.setAdapter(routinesAdapter);
            routinesRecyclerView.setItemAnimator(new DefaultItemAnimator());
            //item点击
            routinesAdapter.setOnItemClickListener(new RoutinesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (position == 0) return;
                    itemPosition = position;
                    BookInfo getData = routinesAdapter.getItemData(position - 1);
                    Intent intent = new Intent(getContext(), BookActivity.class);
                    intent.putExtra(RoutinesAdapter.BOOK_NAME, getData.getBookName());
                    intent.putExtra(RoutinesAdapter.BOOK_COLOR_INDEX, (int) getData.getBookColor());
                    intent.putExtra(RoutinesAdapter.BOOK_ALARM_TIME, getData.getBookAlarmTime());
                    startActivityForResult(intent, UPDATE_BOOK);
                }

                @Override
                public void onItemDeleteClick(View view, int position) {
                    deleteItem(position - 1, true);
                }
            });

            if (pendingIntro) {
                startIntroAnimation();
            }
        } else {
            routinesRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void startIntroAnimation() {
        addRoutinesItemBtn.animate().translationY(0).setStartDelay(300).setDuration(400).setInterpolator(new OvershootInterpolator(1.0f)).start();
    }

    @OnClick(R.id.addBtn)
    public void onAddClick() {
        itemPosition = Integer.MAX_VALUE;
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SS");
        Calendar calendar = Calendar.getInstance();
        Intent intent = new Intent(getContext(), BookActivity.class);
        intent.putExtra(RoutinesAdapter.BOOK_NAME, getResources().getString(R.string.text_book));
        intent.putExtra(RoutinesAdapter.BOOK_COLOR_INDEX, new Random().nextInt(4));
        intent.putExtra(RoutinesAdapter.BOOK_ALARM_TIME, DATE_FORMAT.format(calendar.getTime()));
        startActivityForResult(intent, NEW_BOOK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        String name;
        int colorIndex;
        String time = "";

        switch (requestCode) {
            case UPDATE_BOOK:
                if (resultCode == RENEW_BOOK) {
                    name = data.getStringExtra(RoutinesAdapter.BOOK_NAME);
                    colorIndex = data.getIntExtra(RoutinesAdapter.BOOK_COLOR_INDEX, new Random().nextInt(4));
                    time = data.getStringExtra(RoutinesAdapter.BOOK_ALARM_TIME);
                    updateItem(itemPosition - 1, name, colorIndex, time);
                } else if (resultCode == DELETE_BOOK) {
                    deleteItem(itemPosition - 1, false);
                }
                break;
            case NEW_BOOK:
                if (resultCode == RENEW_BOOK) {
                    name = data.getStringExtra(RoutinesAdapter.BOOK_NAME);
                    colorIndex = data.getIntExtra(RoutinesAdapter.BOOK_COLOR_INDEX, new Random().nextInt(4));
                    time = data.getStringExtra(RoutinesAdapter.BOOK_ALARM_TIME);
                    addItem(name, colorIndex, time);
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onDestroy() {
        BookDatabaseUtil.destory();
        super.onDestroy();
    }

    //addItem,deleteItem,updateItem 的position从0开始
    //添加
    private void addItem(String name, int colorIndex, String time) {
        final BookInfo bookInfo = new BookInfo();
        bookInfo.setUserData(UserData.getCurrentUser(getContext(), UserData.class));
        bookInfo.setBookName(name);
        bookInfo.setBookColor(colorIndex);
        bookInfo.setBookAlarmTime(time);

        if (!getIsTrial()) {
            //服务器
            bookInfo.save(getContext(), new SaveListener() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "上传服务器成功");
                    Log.i(TAG, bookInfo.getObjectId());
                    BookDatabaseUtil.getInstance(getContext()).insertBookInfo(bookInfo);
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.i(TAG, "上传服务器失败: " + s);
                    BookDatabaseUtil.getInstance(getContext()).insertBookInfo(bookInfo, bookInfo, true); //无效invalid ObjectId
                }
            });
        } else {
            BookDatabaseUtil.getInstance(getContext()).insertBookInfo(bookInfo, bookInfo, true); //无效invalid ObjectId
        }

        mDataSet.add(0, bookInfo);
        routinesAdapter.notifyDataSetChanged();
        callRoutineService();
    }

    //删除
    private void deleteItem(int position, boolean setAnimator) {
        final BookInfo bookInfo = mDataSet.get(position);

        if (!getIsTrial()) {
            //服务器
            bookInfo.delete(getContext(), bookInfo.getObjectId(), new DeleteListener() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "删除成功");
                    BookDatabaseUtil.getInstance(getContext()).deleteBookInfo(bookInfo);
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.i(TAG, "删除失败: " + s);
                    BookDatabaseUtil.getInstance(getContext()).deleteBookInfo(bookInfo, true);
                }
            });
        } else {
            BookDatabaseUtil.getInstance(getContext()).deleteBookInfo(bookInfo, true);
        }

        int size = mDataSet.size();
        if (size > 0 && position < size) {
            mDataSet.remove(position);
            if (setAnimator) {
                routinesAdapter.notifyItemRemoved(position + 1);
                routinesAdapter.notifyItemRangeChanged(position + 1, mDataSet.size());
            } else {
                routinesAdapter.notifyDataSetChanged();
            }
        }
        callRoutineService();
    }

    //更新
    private void updateItem(final int position, String name, int colorIndex, String time) {
        final BookInfo bookInfo = mDataSet.get(position);
        final BookInfo bookInfoOld = new BookInfo(bookInfo.getUserData(), bookInfo.getBookName(), bookInfo.getBookColor(), bookInfo.getBookAlarmTime());
        bookInfo.setUserData(UserData.getCurrentUser(getContext(), UserData.class));
        bookInfo.setBookName(name);
        bookInfo.setBookColor(colorIndex);
        bookInfo.setBookAlarmTime(time);

        Log.i(TAG, "test id = " + bookInfo.getObjectId());

        if (bookInfo.getObjectId() == null) {
            Log.i(TAG, "into : test id = null");
            if (!getIsTrial()) {
                bookInfo.save(getContext(), new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "上传服务器成功");
                        Log.i(TAG, bookInfo.getObjectId());
                        BookDatabaseUtil.getInstance(getContext()).insertBookInfo(bookInfo, bookInfoOld, true);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.i(TAG, "上传服务器失败: " + s);
                        BookDatabaseUtil.getInstance(getContext()).insertBookInfo(bookInfo, bookInfoOld, true); //无效invalid ObjectId
                    }
                });
            } else {
                BookDatabaseUtil.getInstance(getContext()).insertBookInfo(bookInfo, bookInfoOld, true); //无效invalid ObjectId
            }
        } else {
            //服务器
            bookInfo.update(getContext(), bookInfo.getObjectId(), new UpdateListener() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "更新服务器成功");
                    BookDatabaseUtil.getInstance(getContext()).insertBookInfo(bookInfo);
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.i(TAG, "更新服务器失败: " + s);
                    switch (i) {
                        case 9010:
                        case 9016:
                            BookDatabaseUtil.getInstance(getContext()).insertBookInfo(bookInfo, bookInfoOld, true); //无效invalid ObjectId
                            break;
                    }
                }
            });
        }

        int size = mDataSet.size();
        if (position < size) {
            mDataSet.set(position, bookInfo);
            routinesAdapter.notifyDataSetChanged();
        }
        callRoutineService();
    }
}
