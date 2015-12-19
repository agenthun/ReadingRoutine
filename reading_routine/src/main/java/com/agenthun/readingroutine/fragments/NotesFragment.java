package com.agenthun.readingroutine.fragments;


import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
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
import com.agenthun.readingroutine.activities.NoteDetailsActivity;
import com.agenthun.readingroutine.adapters.NotesAdapter;
import com.agenthun.readingroutine.datastore.NoteInfo;
import com.agenthun.readingroutine.datastore.UserData;
import com.agenthun.readingroutine.datastore.db.NoteDatabaseUtil;
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
 * A simple {@link Fragment} subclass.
 */
public class NotesFragment extends TFragment implements RevealBackgroundView.OnStateChangeListener {

    private static final String TAG = "NotesFragment";
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();
    public static final int UPDATE_NOTE = 0;
    public static final int NEW_NOTE = 1;
    public static final int RENEW_NOTE = 2;
    public static final int DELETE_NOTE = 3;

    @InjectView(R.id.revealBackgroundView)
    RevealBackgroundView revealBackgroundView;
    @InjectView(R.id.itemRecyclerView)
    RecyclerView notesRecyclerView;
    @InjectView(R.id.addBtn)
    FloatingActionButton addNotesItemBtn;

    private NotesAdapter notesAdapter;
    private boolean pendingIntro;

    private ArrayList<NoteInfo> mDataSet;
    private int itemPosition = 1;

    public NotesFragment() {
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

        initAddItemBtn(addNotesItemBtn);

        return view;
    }

    private void setupDatabase() {
        mDataSet = NoteDatabaseUtil.getInstance(getContext()).queryNoteInfos();
        if (mDataSet == null && getIsTrial() != true) {
            BmobQuery<NoteInfo> bmobQuery = new BmobQuery<>();
            bmobQuery.setLimit(10);

            boolean isCache = bmobQuery.hasCachedResult(getContext(), NoteInfo.class);
            if (isCache) {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
            } else {
                bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
            }
            bmobQuery.findObjects(getContext(), new FindListener<NoteInfo>() {
                @Override
                public void onSuccess(List<NoteInfo> list) {
                    mDataSet = (ArrayList<NoteInfo>) NoteDatabaseUtil.getInstance(getContext()).setNotes(list);
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

            notesAdapter = new NotesAdapter(getContext().getApplicationContext(),
                    getString(R.string.text_note),
                    getResources().getDrawable(R.drawable.notes_badge),
                    mDataSet);

            notesRecyclerView.setAdapter(notesAdapter);
            //item点击
            notesAdapter.setOnItemClickListener(new NotesAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if (position == 0) return;
                    itemPosition = position;
                    NoteInfo getData = notesAdapter.getItemData(position - 1);
                    final Intent intent = new Intent(getContext(), NoteDetailsActivity.class);
                    intent.putExtra(NotesAdapter.NOTE_TITLE, getData.getNoteTitle());
                    intent.putExtra(NotesAdapter.NOTE_COMPOSE, getData.getNoteCompose());
                    intent.putExtra(NotesAdapter.NOTE_CREATE_TIME, getData.getNoteCreateTime());
                    intent.putExtra(NotesAdapter.NOTE_COLOR_INDEX, (int) getData.getNoteColor());
                    removeAddFab(new Runnable() {
                        @Override
                        public void run() {
                            startActivityForResult(intent, UPDATE_NOTE);
                        }
                    });
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
            notesRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    private void startIntroAnimation() {
        addNotesItemBtn.animate().translationY(0).setStartDelay(300).setDuration(400).setInterpolator(new OvershootInterpolator(1.0f)).start();
    }

    @OnClick(R.id.addBtn)
    public void onAddClick() {
        itemPosition = Integer.MAX_VALUE;
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        final Intent intent = new Intent(getContext(), NoteDetailsActivity.class);
        intent.putExtra(NotesAdapter.NOTE_CREATE_TIME, DATE_FORMAT.format(calendar.getTime()));
        intent.putExtra(NotesAdapter.NOTE_COLOR_INDEX, new Random().nextInt(4));
        removeAddFab(new Runnable() {
            @Override
            public void run() {
                startActivityForResult(intent, NEW_NOTE);
            }
        });
    }

    private void removeAddFab(@Nullable Runnable endAction) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            addNotesItemBtn.animate().scaleX(0).scaleY(0)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .withEndAction(endAction)
                    .start();
        } else {
            addNotesItemBtn.animate().scaleX(0).scaleY(0)
                    .setInterpolator(new FastOutSlowInInterpolator())
                    .start();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent data) {
        String title;
        String compose;
        String time;
        int colorIndex;

        switch (requestCode) {
            case UPDATE_NOTE:
                if (resultCode == RENEW_NOTE) {
                    title = data.getStringExtra(NotesAdapter.NOTE_TITLE);
                    compose = data.getStringExtra(NotesAdapter.NOTE_COMPOSE);
                    time = data.getStringExtra(NotesAdapter.NOTE_CREATE_TIME);
                    colorIndex = data.getIntExtra(NotesAdapter.NOTE_COLOR_INDEX, new Random().nextInt(4));
                    updateItem(itemPosition - 1, title, compose, time, colorIndex);
                }
                break;
            case NEW_NOTE:
                if (resultCode == RENEW_NOTE) {
                    title = data.getStringExtra(NotesAdapter.NOTE_TITLE);
                    compose = data.getStringExtra(NotesAdapter.NOTE_COMPOSE);
                    time = data.getStringExtra(NotesAdapter.NOTE_CREATE_TIME);
                    colorIndex = data.getIntExtra(NotesAdapter.NOTE_COLOR_INDEX, new Random().nextInt(4));
                    addItem(title, compose, time, colorIndex);
                }
                break;
            default:
                break;
        }

        addNotesItemBtn.animate().scaleX(1).scaleY(1)
                .setInterpolator(new LinearOutSlowInInterpolator())
                .start();
    }

    @Override
    public void onDestroy() {
        NoteDatabaseUtil.destory();
        super.onDestroy();
    }

    //addItem,deleteItem,updateItem 的position从0开始
    //添加
    private void addItem(String title, String compose, String time, int colorIndex) {
        final NoteInfo noteInfo = new NoteInfo();
        noteInfo.setUserData(UserData.getCurrentUser(getContext(), UserData.class));
        noteInfo.setNoteTitle(title);
        noteInfo.setNoteCompose(compose);
        noteInfo.setNoteCreateTime(time);
        noteInfo.setNoteColor(colorIndex);
        if (!getIsTrial()) {
            //服务器
            noteInfo.save(getContext(), new SaveListener() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "上传服务器成功");
                    Log.i(TAG, noteInfo.getObjectId());
                    NoteDatabaseUtil.getInstance(getContext()).insertNote(noteInfo);
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.i(TAG, "上传服务器失败: " + s);
                    NoteDatabaseUtil.getInstance(getContext()).insertNote(noteInfo, noteInfo, true); //无效invalid ObjectId
                }
            });
        } else {
            NoteDatabaseUtil.getInstance(getContext()).insertNote(noteInfo, noteInfo, true); //无效invalid ObjectId
        }
        mDataSet.add(0, noteInfo);
        notesAdapter.notifyDataSetChanged();
    }

    //删除
    private void deleteItem(int position, boolean setAnimator) {
        final NoteInfo noteInfo = mDataSet.get(position);
        if (!getIsTrial()) {
            //服务器
            noteInfo.delete(getContext(), noteInfo.getObjectId(), new DeleteListener() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "删除成功");
                    NoteDatabaseUtil.getInstance(getContext()).deleteNote(noteInfo);
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.i(TAG, "删除失败: " + s);
                    NoteDatabaseUtil.getInstance(getContext()).deleteNote(noteInfo, true);
                }
            });
        } else {
            NoteDatabaseUtil.getInstance(getContext()).deleteNote(noteInfo, true);
        }

        int size = mDataSet.size();
        if (size > 0 && position < size) {
            mDataSet.remove(position);
            if (setAnimator) {
                notesAdapter.notifyItemRemoved(position + 1);
                notesAdapter.notifyItemRangeChanged(position + 1, mDataSet.size());
            } else {
                notesAdapter.notifyDataSetChanged();
            }
        }
    }

    //更新
    private void updateItem(final int position, String title, String compose, String time, int colorIndex) {
        final NoteInfo noteInfo = mDataSet.get(position);
        final NoteInfo noteInfoOld = new NoteInfo(noteInfo.getUserData(), noteInfo.getNoteTitle(), noteInfo.getNoteCompose(), noteInfo.getNoteCreateTime(), noteInfo.getNoteColor());
        noteInfo.setUserData(UserData.getCurrentUser(getContext(), UserData.class));
        noteInfo.setNoteTitle(title);
        noteInfo.setNoteCompose(compose);
        noteInfo.setNoteCreateTime(time);
        noteInfo.setNoteColor(colorIndex);

        Log.i(TAG, "test id = " + noteInfo.getObjectId());

        if (noteInfo.getObjectId() == null) {
            Log.i(TAG, "into : test id = null");
            if (!getIsTrial()) {
                noteInfo.save(getContext(), new SaveListener() {
                    @Override
                    public void onSuccess() {
                        Log.i(TAG, "上传服务器成功");
                        Log.i(TAG, noteInfo.getObjectId());
                        NoteDatabaseUtil.getInstance(getContext()).insertNote(noteInfo, noteInfoOld, true);
                    }

                    @Override
                    public void onFailure(int i, String s) {
                        Log.i(TAG, "上传服务器失败: " + s);
                        NoteDatabaseUtil.getInstance(getContext()).insertNote(noteInfo, noteInfoOld, true); //无效invalid ObjectId
                    }
                });
            } else {
                NoteDatabaseUtil.getInstance(getContext()).insertNote(noteInfo, noteInfoOld, true); //无效invalid ObjectId
            }
        } else {
            //服务器
            noteInfo.update(getContext(), noteInfo.getObjectId(), new UpdateListener() {
                @Override
                public void onSuccess() {
                    Log.i(TAG, "更新服务器成功");
                    NoteDatabaseUtil.getInstance(getContext()).insertNote(noteInfo);
                }

                @Override
                public void onFailure(int i, String s) {
                    Log.i(TAG, "更新服务器失败: " + s);
                    switch (i) {
                        case 9010:
                        case 9016:
                            NoteDatabaseUtil.getInstance(getContext()).insertNote(noteInfo, noteInfoOld, true); //无效invalid ObjectId
                            break;
                    }
                }
            });
        }

        int size = mDataSet.size();
        if (position < size) {
            mDataSet.set(position, noteInfo);
            notesAdapter.notifyDataSetChanged();
        }
    }
}
