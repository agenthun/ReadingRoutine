package com.agenthun.readingroutine.activities;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.OvershootInterpolator;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.views.RevealBackgroundView;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;


public class NoteDetailsActivity extends AppCompatActivity implements RevealBackgroundView.OnStateChangeListener {

    private RevealBackgroundView revealBackgroundView;
    private MaterialMenuIconToolbar materialMenuIconToolbar;
    private FloatingActionButton saveNotesItemBtn;
    private boolean pendingIntro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);

        materialMenuIconToolbar = new MaterialMenuIconToolbar(this, getResources().getColor(R.color.color_white), MaterialMenuDrawable.Stroke.REGULAR) {
            @Override
            public int getToolbarViewId() {
                return R.id.toolbar;
            }
        };
        materialMenuIconToolbar.setState(MaterialMenuDrawable.IconState.X);
        materialMenuIconToolbar.setNeverDrawTouch(false);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                materialMenuIconToolbar.animatePressedState(MaterialMenuDrawable.IconState.ARROW);
                finish();
            }
        });

        revealBackgroundView = (RevealBackgroundView) findViewById(R.id.revealBackgroundView);
        setupRevealBackground(savedInstanceState);

        saveNotesItemBtn = (FloatingActionButton) findViewById(R.id.fab);
        initSaveItemBtn(saveNotesItemBtn);
/*        saveNotesItemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
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
        }
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
    public void onStateChange(int state) {
        if (RevealBackgroundView.STATE_FINISHED == state) {
            if (pendingIntro) {
                startIntroAnimation();
            }
        }
    }

    private void startIntroAnimation() {
        saveNotesItemBtn.animate().scaleX(1).scaleY(1).setStartDelay(50).setDuration(400).setInterpolator(new OvershootInterpolator(1.0f)).start();
    }
}
