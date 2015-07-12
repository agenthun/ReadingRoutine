package com.agenthun.readingroutine.fragments;


import android.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageButton;

import com.agenthun.readingroutine.adapter.ManagementAdapter;
import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.transitionmanagers.TFragment;
import com.agenthun.readingroutine.view.RevealBackgroundView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class ManagementFragment extends TFragment implements RevealBackgroundView.OnStateChangeListener {

    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    @InjectView(R.id.revealBackgroundView)
    RevealBackgroundView revealBackgroundView;
    @InjectView(R.id.itemRecyclerView)
    RecyclerView managementRecyclerView;
    @InjectView(R.id.addBtn)
    ImageButton addManagementItemBtn;

    private ManagementAdapter managementAdapter;
    private boolean pendingIntro;

    public ManagementFragment() {
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

        addManagementItemBtn.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                addManagementItemBtn.getViewTreeObserver().removeOnPreDrawListener(this);
                pendingIntro = true;
                addManagementItemBtn.setTranslationY(addManagementItemBtn.getHeight());
                return true;
            }
        });

        return view;
    }

    private void setupGridLayout() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        managementRecyclerView.setLayoutManager(layoutManager);
        managementRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                managementAdapter.setLockedAnimations(true);
            }
        });
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
            managementAdapter.setLockedAnimations(true);
        }
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            managementRecyclerView.setVisibility(View.VISIBLE);
            managementAdapter = new ManagementAdapter(getContext().getApplicationContext());
            managementRecyclerView.setAdapter(managementAdapter);
            if (pendingIntro) {
                startIntroAnimation();
            }
        } else {
            managementRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void startIntroAnimation() {
        addManagementItemBtn.animate().translationY(0).setDuration(400).setInterpolator(INTERPOLATOR).start();
    }

}
