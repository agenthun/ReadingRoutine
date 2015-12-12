package com.agenthun.readingroutine.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.Book;
import com.agenthun.readingroutine.fragments.ProductFragment;
import com.agenthun.readingroutine.utils.ApiLevelHelper;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/12/13 上午3:25.
 */
public class ProductActivity extends AppCompatActivity {

    private static final String TAG = "ProductActivity";

    private Book mBook;
    private ImageView mIcon;
    private View mToolbarBack;
    private FloatingActionButton fab;
    private ProductFragment mProductFragment;
    private Interpolator mInterpolator;

    public static Intent getStartIntent(Context context, Book book) {
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra(Book.TAG, book.getId());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_selection);
        initLayout(mBook.getId());
        initToolbar(mBook);
    }

    private void initLayout(String id) {
        mIcon = (ImageView) findViewById(R.id.icon);
//        int resId = getResources().getIdentifier();
//        mIcon.setImageResource(resId);
        ViewCompat.animate(mIcon).scaleX(1).scaleY(1).alpha(1)
                .setInterpolator()
                .setStartDelay(300)
                .start();
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.show();
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(fab, "onClick", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar(Book book) {

    }

    @Override
    protected void onResume() {
        initProductFragment();
        super.onResume();
    }

    private void initProductFragment() {
        if (mProductFragment != null) {
            return;
        }
        mProductFragment = mProductFragment.newInstance(mBook.getId(), getSolvedStateListener());
        setToolbarElevation(false);
    }

    @SuppressLint("NewApi")
    public void setToolbarElevation(boolean shouldElevate) {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            mToolbarBack.setElevation(shouldElevate ?
                    getResources().getDimension(R.dimen.default_elevation) : 0);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
