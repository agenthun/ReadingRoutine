package com.agenthun.readingroutine.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.Book;
import com.agenthun.readingroutine.fragments.ProductFragment;
import com.agenthun.readingroutine.utils.ApiLevelHelper;
import com.agenthun.readingroutine.utils.TextSharedElementCallback;

import java.util.List;

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
    private FloatingActionButton mFab;
    private ProductFragment mProductFragment;
    private Interpolator mInterpolator;
    private Animator animator;

    public static Intent getStartIntent(Context context, Book book) {
        Intent intent = new Intent(context, ProductActivity.class);
        intent.putExtra(Book.TAG, book.getId());
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_selection);
        mInterpolator = new FastOutSlowInInterpolator();
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
//            getWindow().setStatusBarColor(ContextCompat.getColor(this,));
        }
        mBook = new Book();
        mBook.setId("test id");
        mBook.setTitle("test book");

        initLayout(mBook.getId());
        initToolbar(mBook);

        int textSize = getResources().getDimensionPixelSize(R.dimen.abc_text_size_small_material);
        int paddingStart = getResources().getDimensionPixelSize(R.dimen.margin_vertical);
        ActivityCompat.setEnterSharedElementCallback(this, new TextSharedElementCallback(textSize, paddingStart) {
            @Override
            public void onSharedElementStart(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementStart(sharedElementNames, sharedElements, sharedElementSnapshots);
                mToolbarBack.setScaleX(0);
                mToolbarBack.setScaleY(0);
            }

            @Override
            public void onSharedElementEnd(List<String> sharedElementNames, List<View> sharedElements, List<View> sharedElementSnapshots) {
                super.onSharedElementEnd(sharedElementNames, sharedElements, sharedElementSnapshots);
                ViewCompat.animate(mToolbarBack).scaleX(1).scaleY(1).alpha(1)
                        .setStartDelay(300);
            }
        });
    }

    private void initLayout(String id) {
        mIcon = (ImageView) findViewById(R.id.icon);
//        int resId = getResources().getIdentifier();
//        mIcon.setImageResource(resId);
        ViewCompat.animate(mIcon).scaleX(1).scaleY(1).alpha(1)
                .setInterpolator(mInterpolator)
                .setStartDelay(300)
                .start();
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.show();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mFab, "onClick", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar(Book book) {
        mToolbarBack = findViewById(R.id.back);
        mToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView titleView = (TextView) findViewById(R.id.product_title);
        titleView.setText(book.getTitle());
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
        mProductFragment = ProductFragment.newInstance(mBook.getId(), getSolvedStateListener());
        setToolbarElevation(false);
    }

    private ProductFragment.SolvedStateListener getSolvedStateListener() {
        return new ProductFragment.SolvedStateListener() {
            @Override
            public void onProductSolved() {
                setResultSolved();
                setToolbarElevation(true);
                displayFab();
            }
        };
    }

    private void setResultSolved() {
        Intent productIntent = new Intent();
        productIntent.putExtra(Book.ID, mBook.getId());
        setResult(ShoppingActivity.RESULT_PRODUCT, productIntent);
    }

    @SuppressLint("NewApi")
    public void setToolbarElevation(boolean shouldElevate) {
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            mToolbarBack.setElevation(shouldElevate ?
                    getResources().getDimension(R.dimen.default_elevation) : 0);
        }
    }

    private void displayFab() {
        if (animator != null && animator.isRunning()) {
            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    showFabWithIcon();
                    animator.removeListener(this);
                }
            });
        } else {
            showFabWithIcon();
        }
    }

    private void showFabWithIcon() {
        mFab.setVisibility(View.VISIBLE);
        mFab.setScaleX(0);
        mFab.setScaleY(0);
        ViewCompat.animate(mFab).scaleX(1).scaleY(1)
                .setInterpolator(mInterpolator)
                .setListener(null)
                .start();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        if (mIcon == null || mFab == null) {
            super.onBackPressed();
            return;
        }
        ViewCompat.animate(mToolbarBack).scaleX(0).scaleY(0).alpha(0)
                .setDuration(100).start();
        ViewCompat.animate(mIcon).scaleX(0.6f).scaleY(0.6f).alpha(0)
                .setInterpolator(mInterpolator)
                .start();
        ViewCompat.animate(mFab).scaleX(0).scaleY(0)
                .setInterpolator(mInterpolator)
                .setStartDelay(100)
                .setListener(new ViewPropertyAnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(View view) {
                        if (isFinishing() || (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.JELLY_BEAN_MR1) && isDestroyed())) {
                            return;
                        }
                        ProductActivity.super.onBackPressed();
                    }
                })
                .start();
    }
}
