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
import android.view.animation.OvershootInterpolator;
import android.widget.ImageButton;

import com.agenthun.readingroutine.adapters.NotesAdapter;
import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.BookInfo;
import com.agenthun.readingroutine.datastore.db.DatabaseUtil;
import com.agenthun.readingroutine.transitionmanagers.TFragment;
import com.agenthun.readingroutine.views.RevealBackgroundView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends TFragment implements RevealBackgroundView.OnStateChangeListener {

    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    @InjectView(R.id.revealBackgroundView)
    RevealBackgroundView revealBackgroundView;
    @InjectView(R.id.itemRecyclerView)
    RecyclerView notesRecyclerView;
    @InjectView(R.id.addBtn)
    ImageButton addNotesItemBtn;

    private NotesAdapter notesAdapter;
    private boolean pendingIntro;

    private ArrayList<BookInfo> mDataSet;
    DatabaseUtil databaseUtil;

    public NotesFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_base_item, container, false);
        ButterKnife.inject(this, view);

        databaseUtil = DatabaseUtil.getInstance(getContext());
        mDataSet = databaseUtil.queryBookInfos();

        setupGridLayout();
        setupRevealBackground(savedInstanceState);

        addNotesItemBtn.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                addNotesItemBtn.getViewTreeObserver().removeOnPreDrawListener(this);
                pendingIntro = true;
                addNotesItemBtn.setTranslationY(2 * addNotesItemBtn.getHeight());
                return true;
            }
        });

        return view;
    }

    private void setupGridLayout() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        notesRecyclerView.setLayoutManager(layoutManager);
        notesRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                notesAdapter.setLockedAnimations(true);
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
            notesAdapter.setLockedAnimations(true);
        }
    }

    @Override
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            notesRecyclerView.setVisibility(View.VISIBLE);
            List<String> mDataset = new ArrayList<>();
            mDataset.add("a");
            mDataset.add("b");
            mDataset.add("c");
            mDataset.add("d");
            mDataset.add("e");
            mDataset.add("f");
            mDataset.add("g");
            mDataset.add("h");

            notesAdapter = new NotesAdapter(getContext().getApplicationContext(),
                    getString(R.string.text_note),
                    getResources().getDrawable(R.drawable.notes_badge),
                    mDataset);
            notesRecyclerView.setAdapter(notesAdapter);
            if (pendingIntro) {
                startIntroAnimation();
            }
        } else {
            notesRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void startIntroAnimation() {
        addNotesItemBtn.animate().translationY(0).setStartDelay(300).setDuration(400).setInterpolator(new OvershootInterpolator(1.0f)).start();
    }

}
