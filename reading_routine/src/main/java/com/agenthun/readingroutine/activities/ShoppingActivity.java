package com.agenthun.readingroutine.activities;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.adapters.ShoppingAdapter;
import com.agenthun.readingroutine.datastore.Book;
import com.agenthun.readingroutine.net.BaseAsyncHttp;
import com.agenthun.readingroutine.net.HttpResponseHandler;
import com.agenthun.readingroutine.views.CircularProgressView;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/11/30 下午12:20.
 */
public class ShoppingActivity extends AppCompatActivity {

    private static final String TAG = "ShoppingActivity";

    private MaterialMenuIconToolbar materialMenuIconToolbar;
    private Toolbar toolbar;
    private ContentLoadingProgressBar progressBar;
    private CircularProgressView progressView;
    private RecyclerView shoppingRecyclerView;
    private ShoppingAdapter shoppingAdapter;
    private FloatingActionButton fab;
    private ArrayList<Book> mDataSet;
    private boolean pendingIntro;
    private Thread updateThread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
//        ButterKnife.inject(this);

        /*        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
                progressBar.onDetachedFromWindow();*/

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        materialMenuIconToolbar = new MaterialMenuIconToolbar(this, getResources().getColor(R.color.color_white), MaterialMenuDrawable.Stroke.REGULAR) {
            @Override
            public int getToolbarViewId() {
                return R.id.toolbar;
            }
        };
        materialMenuIconToolbar.setState(MaterialMenuDrawable.IconState.ARROW);
/*        toolbar.postDelayed(new Runnable() {
            @Override
            public void run() {
                materialMenuIconToolbar.animateState(MaterialMenuDrawable.IconState.ARROW);
            }
        }, 300);*/
        toolbar.setTitle(R.string.text_shopping);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
/*                materialMenuIconToolbar.animatePressedState(MaterialMenuDrawable.IconState.BURGER);
                toolbar.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 200);*/
                finish();
            }
        });

        // for test
        mDataSet = new ArrayList<>();
/*
        Book book = new Book();
        book.setBitmap("http://i.imgur.com/DvpvklR.png");
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setBitmap("http://img1.douban.com/spic/s1747553.jpg");
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setBitmap("http://img3.douban.com/lpic/s28316760.jpg");
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setBitmap("http://img3.douban.com/lpic/s28315660.jpg");
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);

        book = new Book();
        book.setTitle("Notebook");
        book.setAuthor("Nigolas Sparks");
        book.setPrice("21");
        book.setRate(5.0);
        book.setReviewCount(100);
        mDataSet.add(book);
*/


        shoppingRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        setupGridLayout();

        fab = (FloatingActionButton) findViewById(R.id.fab);
        initSearchItemBtn(fab);
        fab.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (pendingIntro) {
                    fab.animate().scaleX(1).scaleY(1)
                            .setDuration(300).setStartDelay(300)
                            .setInterpolator(new LinearOutSlowInInterpolator())
                            .start();
                    pendingIntro = false;
                }
            }
        }, 100);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startSearchAnimation(100);
                getRequestData("第三种爱情");
            }
        });

        progressView = (CircularProgressView) findViewById(R.id.progressBar);
    }

    private void setupGridLayout() {
        shoppingAdapter = new ShoppingAdapter(ShoppingActivity.this, mDataSet);
        shoppingAdapter.setOnItemClickListener(new ShoppingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position);
                }
                Log.d(TAG, "onItemClick() returned: " + position);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(ShoppingActivity.this);
        shoppingRecyclerView.setLayoutManager(layoutManager);
        shoppingRecyclerView.setAdapter(shoppingAdapter);
    }

    private void initSearchItemBtn(final FloatingActionButton imageButton) {
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

    public void getRequestData(String str) {
        RequestParams params = new RequestParams();
        params.put("q", str.trim());
        BaseAsyncHttp.getReq("/v2/book/search", params, new HttpResponseHandler() {
            @Override
            public void onJsonHttpSuccess(JSONObject response) {
                mDataSet.clear();
                progressView.setVisibility(View.GONE);
                JSONArray jsonArrays = response.optJSONArray("books");
                for (int i = 0; i < jsonArrays.length(); i++) {
                    Book book = new Book();
                    book.setId(jsonArrays.optJSONObject(i).optString("id"));
                    book.setTitle(jsonArrays.optJSONObject(i).optString("title"));

                    String authors = "";
                    for (int j = 0; j < jsonArrays.optJSONObject(i).optJSONArray("author").length(); j++) {
                        authors += " " + jsonArrays.optJSONObject(i).optJSONArray("author").optString(j);
                    }
                    book.setAuthor(authors.trim());

                    book.setAuthorInfo(jsonArrays.optJSONObject(i).optString("author_intro"));
                    book.setPublisher(jsonArrays.optJSONObject(i).optString("publisher"));
                    book.setPublishDate(jsonArrays.optJSONObject(i).optString("pubdate"));
                    book.setPrice(jsonArrays.optJSONObject(i).optString("price"));
                    book.setPage(jsonArrays.optJSONObject(i).optString("pages"));
                    book.setRate(jsonArrays.optJSONObject(i).optJSONObject("rating").optDouble("average"));

                    String tags = "";
                    for (int j = 0; j < jsonArrays.optJSONObject(i).optJSONArray("tags").length(); j++) {
                        tags = tags + " " + jsonArrays.optJSONObject(i).optJSONArray("tags").optJSONObject(j).optString("name");
                    }
                    book.setTag(tags.trim());

                    book.setContent(jsonArrays.optJSONObject(i).optString("catalog"));
                    book.setSummary(jsonArrays.optJSONObject(i).optString("summary"));
                    book.setBitmap(jsonArrays.optJSONObject(i).optString("image"));
                    book.setReviewCount(jsonArrays.optJSONObject(i).optJSONObject("rating").optInt("numRaters"));
                    book.setUrl(jsonArrays.optJSONObject(i).optString("ebook_url"));

                    mDataSet.add(book);
                    Log.d(TAG, "onJsonHttpSuccess() returned: " + book.toString());
                }
                updateItem();
            }

            @Override
            public void onJsonHttpFailure(JSONObject response) {
                //Snackbar.make(saveNotesItemBtn, R.string.error_invalid_network, Snackbar.LENGTH_SHORT).setAction("Error", null).show();
                Toast.makeText(ShoppingActivity.this, R.string.error_invalid_network, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void updateItem() {
        shoppingAdapter.setItems(mDataSet);
        shoppingAdapter.notifyDataSetChanged();
    }

    private void startSearchAnimation(long delayTime) {
        if (updateThread != null && updateThread.isAlive())
            updateThread.interrupt();
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                progressView.setVisibility(View.VISIBLE);
                progressView.setProgress(0f);
                progressView.startAnimation();
                updateThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (progressView.getProgress() < progressView.getMaxProgress() && !Thread.interrupted()) {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    progressView.setProgress(progressView.getProgress() + 10);
                                }
                            });
                            SystemClock.sleep(200);
                        }
                    }
                });
                updateThread.start();
            }
        }, delayTime);
    }

    //itemClick interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
