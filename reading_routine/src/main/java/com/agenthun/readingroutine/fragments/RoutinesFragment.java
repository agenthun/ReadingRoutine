package com.agenthun.readingroutine.fragments;


import android.content.Intent;
import android.os.Bundle;
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
import android.widget.ImageButton;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.activities.BookActivity;
import com.agenthun.readingroutine.adapters.RoutinesAdapter;
import com.agenthun.readingroutine.transitionmanagers.TFragment;
import com.agenthun.readingroutine.views.RevealBackgroundView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Random;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class RoutinesFragment extends TFragment implements RevealBackgroundView.OnStateChangeListener {

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
    ImageButton addRoutinesItemBtn;
    /*    @InjectView(R.id.addBtn2)
        FloatingActionButton addRoutinesItemBtn2;*/

    private RoutinesAdapter routinesAdapter;
    private boolean pendingIntro;
    private ArrayList<HashMap<String, Object>> mDataSet;
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

        mDataSet = new ArrayList<HashMap<String, Object>>();

        setupGridLayout();
        setupRevealBackground(savedInstanceState);

        initAddItemBtn(addRoutinesItemBtn);

        return view;
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

    private void initAddItemBtn(final ImageButton imageButton) {
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

            //测试数据
            HashMap<String, Object> hashMap;

            hashMap = new HashMap<String, Object>();
            hashMap.put(RoutinesAdapter.BOOK_NAME, "The Best of Me");
            hashMap.put(RoutinesAdapter.BOOK_COLOR_INDEX, 0);
            hashMap.put(RoutinesAdapter.BOOK_ALARM_TIME, "2015-09-10");
            mDataSet.add(hashMap);

            hashMap = new HashMap<String, Object>();
            hashMap.put(RoutinesAdapter.BOOK_NAME, "Dear John");
            hashMap.put(RoutinesAdapter.BOOK_COLOR_INDEX, 1);
            hashMap.put(RoutinesAdapter.BOOK_ALARM_TIME, "2015-01-12");
            mDataSet.add(hashMap);

            hashMap = new HashMap<String, Object>();
            hashMap.put(RoutinesAdapter.BOOK_NAME, "The Lucky One");
            hashMap.put(RoutinesAdapter.BOOK_COLOR_INDEX, 2);
            hashMap.put(RoutinesAdapter.BOOK_ALARM_TIME, "2015-02-14");
            mDataSet.add(hashMap);

            hashMap = new HashMap<String, Object>();
            hashMap.put(RoutinesAdapter.BOOK_NAME, "Safe Haven");
            hashMap.put(RoutinesAdapter.BOOK_COLOR_INDEX, 3);
            hashMap.put(RoutinesAdapter.BOOK_ALARM_TIME, "2015-03-16");
            mDataSet.add(hashMap);

            hashMap = new HashMap<String, Object>();
            hashMap.put(RoutinesAdapter.BOOK_NAME, "A Walk to Remember");
            hashMap.put(RoutinesAdapter.BOOK_COLOR_INDEX, 2);
            hashMap.put(RoutinesAdapter.BOOK_ALARM_TIME, "2015-04-18");
            mDataSet.add(hashMap);

            hashMap = new HashMap<String, Object>();
            hashMap.put(RoutinesAdapter.BOOK_NAME, "Message in a Bottle");
            hashMap.put(RoutinesAdapter.BOOK_COLOR_INDEX, 0);
            hashMap.put(RoutinesAdapter.BOOK_ALARM_TIME, "2015-05-20");
            mDataSet.add(hashMap);

            hashMap = new HashMap<String, Object>();
            hashMap.put(RoutinesAdapter.BOOK_NAME, "Nights in Rodanthe");
            hashMap.put(RoutinesAdapter.BOOK_COLOR_INDEX, 1);
            hashMap.put(RoutinesAdapter.BOOK_ALARM_TIME, "2015-06-22");
            mDataSet.add(hashMap);

            hashMap = new HashMap<String, Object>();
            hashMap.put(RoutinesAdapter.BOOK_NAME, "The Notebook");
            hashMap.put(RoutinesAdapter.BOOK_COLOR_INDEX, 3);
            hashMap.put(RoutinesAdapter.BOOK_ALARM_TIME, "2015-07-24");
            mDataSet.add(hashMap);


            routinesAdapter = new RoutinesAdapter(getContext().getApplicationContext(), mDataSet);
            routinesRecyclerView.setAdapter(routinesAdapter);
            routinesRecyclerView.setItemAnimator(new DefaultItemAnimator());
            //item点击
            routinesAdapter.setOnItemClickListener(new RoutinesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    itemPosition = position;
                    HashMap<String, Object> getData = routinesAdapter.getItemData(position);
                    Intent intent = new Intent(getContext(), BookActivity.class);
                    intent.putExtra(RoutinesAdapter.BOOK_NAME, (String) getData.get(RoutinesAdapter.BOOK_NAME));
                    intent.putExtra(RoutinesAdapter.BOOK_COLOR_INDEX, (int) getData.get(RoutinesAdapter.BOOK_COLOR_INDEX));
                    intent.putExtra(RoutinesAdapter.BOOK_ALARM_TIME, (String) getData.get(RoutinesAdapter.BOOK_ALARM_TIME));
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
        System.out.println("onAddClick");
//        routinesAdapter.addItem();
        itemPosition = Integer.MAX_VALUE;
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
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
        String time;

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

    //addItem,deleteItem,updateItem 的position从0开始
    //添加
    private void addItem(String name, int colorIndex, String time) {
        HashMap<String, Object> hashMap = new HashMap<String, Object>();
        hashMap.put(RoutinesAdapter.BOOK_NAME, name);
        hashMap.put(RoutinesAdapter.BOOK_COLOR_INDEX, colorIndex);
        hashMap.put(RoutinesAdapter.BOOK_ALARM_TIME, time);
        mDataSet.add(0, hashMap);
        routinesAdapter.notifyDataSetChanged();
/*        routinesAdapter.notifyItemInserted(1);
        routinesAdapter.notifyItemRangeChanged(1, mDataSet.size());*/
    }

    //删除
    private void deleteItem(int position, boolean setAnimator) {
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
    }

    //更新
    private void updateItem(int position, String name, int colorIndex, String time) {
        int size = mDataSet.size();
        if (position < size) {
            HashMap<String, Object> hashMap = mDataSet.get(position);
            hashMap.put(RoutinesAdapter.BOOK_NAME, name);
            hashMap.put(RoutinesAdapter.BOOK_COLOR_INDEX, colorIndex);
            hashMap.put(RoutinesAdapter.BOOK_ALARM_TIME, time);
            mDataSet.set(position, hashMap);
            routinesAdapter.notifyDataSetChanged();
        }
    }
}
