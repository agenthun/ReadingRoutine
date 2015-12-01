package com.agenthun.readingroutine.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.graphics.ColorUtils;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.adapters.NotesAdapter;
import com.agenthun.readingroutine.fragments.NotesFragment;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;

import java.util.Random;


public class NoteDetailsActivity extends AppCompatActivity {

    private static final String TAG = "NoteDetailsActivity";

    private MaterialMenuIconToolbar materialMenuIconToolbar;
    private EditText noteTitle;
    private EditText noteContent;
    private FloatingActionButton saveNotesItemBtn;
    private boolean pendingIntro;

    private Intent intent;

    private boolean isUpdate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        intent = getIntent();

        final String noteCrateTime = intent.getStringExtra(NotesAdapter.NOTE_CREATE_TIME);

        int[] colorNote = getResources().getIntArray(R.array.style_tag_color);
        final int getNoteColorIndex = intent.getIntExtra(NotesAdapter.NOTE_COLOR_INDEX, new Random().nextInt(4));
        //Log.d(TAG, "getNoteColorIndex() returned: " + getNoteColorIndex);
        materialMenuIconToolbar = new MaterialMenuIconToolbar(this, getResources().getColor(R.color.color_white), MaterialMenuDrawable.Stroke.REGULAR) {
            @Override
            public int getToolbarViewId() {
                return R.id.toolbar;
            }
        };
        materialMenuIconToolbar.setState(MaterialMenuDrawable.IconState.X);
        materialMenuIconToolbar.setNeverDrawTouch(false);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.text_reading_routine);
        toolbar.setBackgroundColor(colorNote[getNoteColorIndex]);
        setSupportActionBar(toolbar);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setStatusBarColor(ColorUtils.setAlphaComponent(colorNote[getNoteColorIndex], 0xaa));
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialMenuIconToolbar.animatePressedState(MaterialMenuDrawable.IconState.ARROW);
                toolbar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 200);
            }
        });

        noteTitle = (EditText) findViewById(R.id.edit_note_title);
        String title = intent.getStringExtra(NotesAdapter.NOTE_TITLE);
        if (title != null) {
            noteTitle.setText(title);
            isUpdate = true;
        } else {
            isUpdate = false;
        }

        noteContent = (EditText) findViewById(R.id.edit_note_content);
        String compose = intent.getStringExtra(NotesAdapter.NOTE_COMPOSE);
        if (compose != null) {
            noteContent.setText(compose);
            isUpdate = true;
        } else {
            isUpdate = false;
        }

        saveNotesItemBtn = (FloatingActionButton) findViewById(R.id.fab);
        initSaveItemBtn(saveNotesItemBtn);
        saveNotesItemBtn.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pendingIntro) {
                    saveNotesItemBtn.animate().scaleX(1).scaleY(1)
                            .setDuration(300).setStartDelay(300)
                            .setInterpolator(new LinearOutSlowInInterpolator())
                            .start();
                    pendingIntro = false;
                }
            }
        }, 100);

        saveNotesItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String compose = noteContent.getText().toString();
                if (compose.isEmpty()) {
                    Snackbar.make(saveNotesItemBtn, R.string.error_invalid_notecompose, Snackbar.LENGTH_SHORT).setAction("Error", null).show();
                    return;
                } else {
                    intent.putExtra(NotesAdapter.NOTE_TITLE, noteTitle.getText().toString());
                    intent.putExtra(NotesAdapter.NOTE_COMPOSE, compose);
                    intent.putExtra(NotesAdapter.NOTE_CREATE_TIME, noteCrateTime);
                    intent.putExtra(NotesAdapter.NOTE_COLOR_INDEX, getNoteColorIndex);
                    //Log.d(TAG, "onClick() returned:getNoteColorIndex = " + getNoteColorIndex);
                    setResult(NotesFragment.RENEW_NOTE, intent);
                    finish();
                }
            }
        });
    }

    private void initSaveItemBtn(final FloatingActionButton imageButton) {
        //初始化缩小Button
        imageButton.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageButton.getViewTreeObserver().removeOnPreDrawListener(this);
                pendingIntro = true;
                imageButton.setScaleX(0);
                imageButton.setScaleY(0);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
