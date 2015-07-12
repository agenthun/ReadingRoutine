package com.agenthun.readingroutine.activity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.fragments.MenuFragment;
import com.agenthun.readingroutine.transitionmanagers.TActivity;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends TActivity implements MenuFragment.OnMenuInteractionListener {

    private MaterialMenuIconToolbar materialMenuIconToolbar;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private boolean direction;

    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @InjectView(R.id.navigation_view)
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        materialMenuIconToolbar = new MaterialMenuIconToolbar(this, getResources().getColor(R.color.color_white), MaterialMenuDrawable.Stroke.REGULAR) {
            @Override
            public int getToolbarViewId() {
                return R.id.toolbar;
            }
        };
        materialMenuIconToolbar.setNeverDrawTouch(false);

        toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
        toolbar.setTitle(R.string.text_main);
        toolbar.setBackgroundColor(getResources().getColor(R.color.background_daytime_material_blue));
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int cnt = getCountBackStackEntryAt();
//                System.out.println("test1: " + cnt + "\r\n" + isTaskRoot());

                if (cnt <= 1) {
                    if (!direction) {
                        drawerLayout.openDrawer(Gravity.LEFT);
                    } else {
                        drawerLayout.closeDrawer(Gravity.LEFT);
                    }
                } else {
                    toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
                    toolbar.setTitle(R.string.text_main);
                    toolbar.setBackgroundColor(getResources().getColor(R.color.background_daytime_material_blue));
                    materialMenuIconToolbar.setColor(getResources().getColor(R.color.color_white));
                    materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.BURGER);
                }
            }
        });

/*
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close);
        actionBarDrawerToggle.syncState();*/

        drawerLayout.setScrimColor(Color.parseColor("#66000000"));
//        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        drawerLayout.setDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                    materialMenuIconToolbar.setTransformationOffset(MaterialMenuDrawable.AnimationState.BURGER_ARROW, direction ? 2 - slideOffset : slideOffset);
                }
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                direction = true;
                if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                    toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
                } else {
                    toolbar.setTitleTextColor(getResources().getColor(R.color.background_daytime_material_blue));
                }
                toolbar.setTitle(getString(R.string.text_reading_routine));
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                direction = false;
                if (getSupportFragmentManager().getBackStackEntryCount() <= 1) {
                    toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
                    toolbar.setTitle(R.string.text_main);
                } else {
                    toolbar.setTitleTextColor(getResources().getColor(R.color.background_daytime_material_blue));
                    toolbar.setTitle(R.string.text_null);
                }
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

        if (savedInstanceState == null) {
            pushFragmentToBackStack(MenuFragment.class, null);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
/*        if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onPostCreate(savedInstanceState, persistentState);
        refreshDrawerState();
        materialMenuIconToolbar.syncState(savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        materialMenuIconToolbar.onSaveInstanceState(outState);
    }

    private void refreshDrawerState() {
        direction = drawerLayout.isDrawerOpen(Gravity.START);
    }

    @Override
    public void onFragmentInteraction(int fragmentIndex) {
        switch (fragmentIndex) {
            case MenuFragment.MANAGEMEUNT_FRAGMENT:
                openManagementFragment();
                break;
            case MenuFragment.ROUTINES_FRAGMENT:
                openRoutinesFragment();
                break;
            case MenuFragment.SHOPPING_FRAGMENT:
                openShoppingFragment();
                break;
            case MenuFragment.ABOUT_FRAGMENT:
                openAboutFragment();
                break;
            case MenuFragment.NOTES_FRAGMENT:
                openNotesFragment();
                break;
            case MenuFragment.SETTINGS_FRAGMENT:
                openSettingsFragment();
                break;
        }
    }

    private void openManagementFragment() {
        materialMenuIconToolbar.setColor(getResources().getColor(R.color.background_daytime_material_blue));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setTitle(R.string.text_null);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
    }

    private void openRoutinesFragment() {
        materialMenuIconToolbar.setColor(getResources().getColor(R.color.background_daytime_material_blue));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setTitle(R.string.text_null);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
    }

    private void openShoppingFragment() {
//        toolbar.setBackgroundColor(getResources().getColor(R.color.color_accent));
/*        materialMenuIconToolbar.setColor(getResources().getColor(R.color.background_daytime_material_blue));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.ARROW);*/
    }

    private void openAboutFragment() {
//        toolbar.setBackgroundColor(getResources().getColor(R.color.color_accent));
/*        materialMenuIconToolbar.setColor(getResources().getColor(R.color.background_daytime_material_blue));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.ARROW);*/
    }

    private void openNotesFragment() {
//        toolbar.setBackgroundColor(getResources().getColor(R.color.color_accent));
/*        materialMenuIconToolbar.setColor(getResources().getColor(R.color.background_daytime_material_blue));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.ARROW);*/
    }

    private void openSettingsFragment() {
        materialMenuIconToolbar.setColor(getResources().getColor(R.color.background_daytime_material_blue));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.ARROW);
        toolbar.setTitle(R.string.text_null);
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_white));
//        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
    }

    @Override
    protected int getFragmentContainerId() {
        return R.id.container;
    }

    @Override
    public void onBackPressed() {
        materialMenuIconToolbar.setColor(getResources().getColor(R.color.color_white));
        materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.BURGER);
        toolbar.setTitleTextColor(getResources().getColor(R.color.color_white));
        toolbar.setTitle(R.string.text_main);
        toolbar.setBackgroundColor(getResources().getColor(R.color.background_daytime_material_blue));
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
