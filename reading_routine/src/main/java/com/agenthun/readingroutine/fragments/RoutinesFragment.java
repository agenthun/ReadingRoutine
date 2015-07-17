package com.agenthun.readingroutine.fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.activity.BookActivity;
import com.agenthun.readingroutine.adapter.RoutinesAdapter;
import com.agenthun.readingroutine.transitionmanagers.TFragment;
import com.agenthun.readingroutine.view.RecyclerViewScrollManager;
import com.agenthun.readingroutine.view.RevealBackgroundView;

import java.util.ArrayList;
import java.util.Arrays;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public class RoutinesFragment extends TFragment implements RevealBackgroundView.OnStateChangeListener {

    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
    private static final int NEW_BOOK = 1;

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

    public RoutinesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base_item, container, false);
        ButterKnife.inject(this, view);

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

            String[] data = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z"};
            ArrayList<String> mDataSet = new ArrayList<String>(Arrays.asList(data));

            routinesAdapter = new RoutinesAdapter(getContext().getApplicationContext(), mDataSet);
            routinesRecyclerView.setAdapter(routinesAdapter);
            if (pendingIntro) {
                startIntroAnimation();
            }
        } else {
            routinesRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void startIntroAnimation() {
        addRoutinesItemBtn.animate().translationY(0).setStartDelay(300).setDuration(400).setInterpolator(new OvershootInterpolator(1.0f)).start();
//        addRoutinesItemBtn.animate().translationY(0).setDuration(400).setInterpolator(INTERPOLATOR).start();
    }

    @OnClick(R.id.addBtn)
    public void onAddClick() {
        System.out.println("onAddClick");
//        routinesAdapter.addItem();
        Intent intent = new Intent(getContext(), BookActivity.class);
        startActivityForResult(intent, NEW_BOOK);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        switch (requestCode){
//        }
    }
}
