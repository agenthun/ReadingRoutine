package com.agenthun.readingroutine.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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

import com.agenthun.readingroutine.activities.NoteDetailsActivity;
import com.agenthun.readingroutine.adapters.NotesAdapter;
import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.BookInfo;
import com.agenthun.readingroutine.datastore.db.DatabaseUtil;
import com.agenthun.readingroutine.transitionmanagers.TFragment;
import com.agenthun.readingroutine.views.RevealBackgroundView;
import com.agenthun.readingroutine.views.TagView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends TFragment implements RevealBackgroundView.OnStateChangeListener {

    private static final String TAG = "NotesFragment";
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    @InjectView(R.id.revealBackgroundView)
    RevealBackgroundView revealBackgroundView;
    @InjectView(R.id.itemRecyclerView)
    RecyclerView notesRecyclerView;
    @InjectView(R.id.addBtn)
    FloatingActionButton addNotesItemBtn;

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

        initAddItemBtn(addNotesItemBtn);

        return view;
    }

    private void setupGridLayout() {
        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        notesRecyclerView.setLayoutManager(layoutManager);
        notesRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

    private void initAddItemBtn(final FloatingActionButton imageButton) {
        imageButton.setImageResource(R.drawable.ic_mode_edit_white_36dp);
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
            //item点击
            notesAdapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    //Log.d(TAG, "onItemClick() returned: " + position);

                    Intent intent = new Intent(getContext(), NoteDetailsActivity.class);
                    startActivity(intent);
                }

                @Override
                public void onItemDeleteClick(View view, int position) {
                    //Log.d(TAG, "onItemDeleteClick() returned: " + position);
                }
            });

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

    @OnClick(R.id.addBtn)
    public void onAddClick() {
        Log.d(TAG, "onAddClick()");
    }
}
