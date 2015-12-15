package com.agenthun.readingroutine.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorListenerAdapter;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.Book;
import com.agenthun.readingroutine.utils.ApiLevelHelper;
import com.agenthun.readingroutine.utils.TextSharedElementCallback;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/12/13 上午3:25.
 */
public class ProductActivity extends AppCompatActivity {

    private static final String TAG = "ProductActivity";

    private Book mBook;
    private ImageView mPic;
    private View mToolbarBack;
    private View mToolbar;
    private FloatingActionButton mFab;
    private Interpolator mInterpolator;
    private Palette palette;

    public static Intent getStartIntent(Context context, Book book) {
        Intent intent = new Intent(context, ProductActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable(Book.TAG, book);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_selection);
        mInterpolator = new FastOutSlowInInterpolator();
        mBook = getIntent().getParcelableExtra(Book.TAG);

        initLayout(mBook);
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

    private void initLayout(Book book) {
        mPic = (ImageView) findViewById(R.id.pic);

        ViewCompat.animate(mPic).scaleX(1).scaleY(1).alpha(1)
                .setInterpolator(mInterpolator)
                .setStartDelay(300)
                .start();
        ViewCompat.setTransitionName(mPic, getResources().getString(R.string.transition_imageview));
        Picasso.with(this).load(book.getBitmap()).into(mPic);

        palette = Palette.from(((BitmapDrawable) (mPic.getDrawable())).getBitmap()).generate();

        ((TextView) findViewById(R.id.title)).setText(book.getTitle());
        ((TextView) findViewById(R.id.author)).setText("作者: " + (TextUtils.isEmpty(book.getAuthor()) ? getString(R.string.text_author_null) : book.getAuthor()));
        ((TextView) findViewById(R.id.publisher)).setText(TextUtils.isEmpty(book.getPublisher()) ? getString(R.string.text_publisher_null) : book.getPublisher());
        ((TextView) findViewById(R.id.publishDate)).setText(book.getPublishDate());
        ((TextView) findViewById(R.id.rate)).setText(book.getRate() + "分");
        ((TextView) findViewById(R.id.reviewCount)).setText(book.getReviewCount() + "人评论");
        ((TextView) findViewById(R.id.price)).setText(book.getPrice());
        ((TextView) findViewById(R.id.content)).setText(TextUtils.isEmpty(book.getContent()) ? getString(R.string.text_content_null) : book.getContent());
        ((TextView) findViewById(R.id.summary)).setText(TextUtils.isEmpty(book.getSummary()) ? getString(R.string.text_content_null) : book.getSummary());

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setBackgroundTintList(ColorStateList.valueOf(
                palette.getVibrantColor(getResources().getColor(R.color.colorAccent))));
        mFab.show();
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(mFab, "onClick", Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private void initToolbar(Book book) {
        Palette.Swatch swatch;
        int color;
        if (ApiLevelHelper.isAtLeast(Build.VERSION_CODES.LOLLIPOP)) {
            getWindow().setStatusBarColor(
                    palette.getDarkVibrantColor(getResources().getColor(R.color.colorPrimaryDark)));
        }

        mToolbarBack = findViewById(R.id.back);
        mToolbarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        TextView titleView = (TextView) findViewById(R.id.product_title);
        titleView.setText(book.getTitle());

        swatch = palette.getLightVibrantSwatch();
        if (swatch != null) {
            color = swatch.getTitleTextColor();
        } else {
            color = getResources().getColor(R.color.color_white);
        }
        titleView.setTextColor(color);

        (findViewById(R.id.pic_background)).setBackgroundColor(palette.getLightMutedColor(color));

        mToolbar = findViewById(R.id.product_title);
        if (swatch != null) {
            color = swatch.getRgb();
        } else {
            color = getResources().getColor(R.color.colorPrimary);
        }
        mToolbar.setBackgroundColor(
                palette.getLightVibrantColor(getResources().getColor(R.color.colorPrimary)));
    }

    @Override
    public void onBackPressed() {
        if (mPic == null || mFab == null) {
            super.onBackPressed();
            return;
        }
        ViewCompat.animate(mToolbarBack).scaleX(0).scaleY(0).alpha(0)
                .setDuration(100).start();
        ViewCompat.animate(mPic).scaleX(0).scaleY(0).alpha(0)
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
