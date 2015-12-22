package com.agenthun.readingroutine.activities;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.adapters.ShoppingAdapter;
import com.agenthun.readingroutine.datastore.Book;
import com.agenthun.readingroutine.net.BaseAsyncHttp;
import com.agenthun.readingroutine.net.HttpResponseHandler;
import com.agenthun.readingroutine.utils.TransitionHelper;
import com.agenthun.readingroutine.views.CircularProgressView;
import com.balysv.materialmenu.MaterialMenuDrawable;
import com.balysv.materialmenu.extras.toolbar.MaterialMenuIconToolbar;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;


/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/11/30 下午12:20.
 */
public class ShoppingActivity extends AppCompatActivity {

    private static final String TAG = "ShoppingActivity";
    private static final int REQUEST_PRODUCT = 1;
    public static final int RESULT_PRODUCT = 2;

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
        toolbar.setTitle(R.string.text_shopping);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // for dataset
        mDataSet = new ArrayList<>();

        shoppingRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        setupGridLayout();

/*        fab = (FloatingActionButton) findViewById(R.id.fab);
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
                getSearchRequestData("第三种爱情");
            }
        });*/

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startSearchAnimation(100);
                getBookListRequestData("https://book.douban.com/chart?subcat=F&icn=index-topchart-fiction");
            }
        }, 200);

        progressView = (CircularProgressView) findViewById(R.id.progressBar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_shopping, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            SearchManager searchManager = (SearchManager) getSystemService(SEARCH_SERVICE);
            final SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            searchView.setIconifiedByDefault(false);
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    startSearchAnimation(100);
                    getSearchRequestData(query);
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(searchView.getWindowToken(), 0);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            searchView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startSearchAnimation(100);
                    getSearchRequestData(String.valueOf(searchView.getQuery()));
                }
            });
        }

        return true;
    }

    private void setupGridLayout() {
        shoppingAdapter = new ShoppingAdapter(ShoppingActivity.this, mDataSet);
        shoppingAdapter.setOnItemClickListener(new ShoppingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final View view, int position) {
//                Log.d(TAG, "onItemClick() returned: " + position);
                final Book mBook = shoppingAdapter.getItem(position);
                if (TextUtils.isEmpty(mBook.getContent()) && TextUtils.isEmpty(mBook.getSummary())) {
                    getSearchByIdRequestDataAndStartActivity(position, mBook.getId(), mBook);
                    Log.d(TAG, "onItemClick() returned: getSearchByIdRequestDataAndStartActivity");
                } else {
                    startProductActivityWithTransition(ShoppingActivity.this, view.findViewById(R.id.pic), mBook);
                }
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
        finish();
    }

    public void getSearchRequestData(String str) {
        RequestParams params = new RequestParams();
        params.put("q", str.trim());
        BaseAsyncHttp.getReq("/v2/book/search", params, new HttpResponseHandler() {
            @Override
            public void onJsonHttpSuccess(JSONObject response) {
                mDataSet.clear();

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
                    book.setBitmap(jsonArrays.optJSONObject(i).optJSONObject("images").optString("large"));
                    book.setReviewCount(jsonArrays.optJSONObject(i).optJSONObject("rating").optInt("numRaters"));
                    book.setUrl(jsonArrays.optJSONObject(i).optString("ebook_url"));

                    mDataSet.add(book);
                    Log.d(TAG, "onJsonHttpSuccess() returned: " + book.toString());
                }
                progressView.setVisibility(View.GONE);
                updateItem();
            }

            @Override
            public void onJsonHttpFailure(JSONObject response) {
                progressView.setVisibility(View.GONE);
                Snackbar.make(toolbar, R.string.error_invalid_network, Snackbar.LENGTH_SHORT).show();
//                Toast.makeText(ShoppingActivity.this, R.string.error_invalid_network, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void getSearchByIdRequestDataAndStartActivity(final int position, String id, final Book book) {
        BaseAsyncHttp.getAsyncHttpResponseData("https://api.douban.com/v2/book/" + id, new HttpResponseHandler() {
            @Override
            public void onJsonHttpSuccess(JSONObject response) {
                book.setAuthorInfo(response.optString("author_intro"));
                book.setPage(response.optString("pages"));

                String tags = "";
                for (int j = 0; j < response.optJSONArray("tags").length(); j++) {
                    tags = tags + " " + response.optJSONArray("tags").optJSONObject(j).optString("name");
                }
                book.setTag(tags.trim());

                book.setContent(response.optString("catalog"));
                book.setSummary(response.optString("summary"));

                Log.d(TAG, "onJsonHttpSuccess() returned: " + book.toString());

                shoppingAdapter.setItem(position, book);
                shoppingAdapter.notifyItemChanged(position);
                startProductActivityWithTransition(ShoppingActivity.this, findViewById(R.id.pic), book);
            }

            @Override
            public void onJsonHttpFailure(JSONObject response) {
                startProductActivityWithTransition(ShoppingActivity.this, findViewById(R.id.pic), book);
//                Snackbar.make(toolbar, R.string.error_invalid_network, Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    public void getBookListRequestData(String str) {
        BaseAsyncHttp.getAsyncHttpResponseData(str, new TextHttpResponseHandler() {
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                        progressView.setVisibility(View.GONE);
                        Snackbar.make(toolbar, R.string.error_invalid_network, Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, String responseString) {
                        mDataSet.clear();

                        Document document = Jsoup.parse(responseString);
                        Elements elements = document.select("#content");
//                        elements = elements.get(0).select("ul > li");
                        Elements bookElements = elements.select("li.media");

                        for (Element bookElement :
                                bookElements) {
                            Book book = new Book();

                            String id = bookElement.select("div.media__img a").attr("href");
                            id = id.substring(id.indexOf("subject") + 8, id.length() - 1);
                            book.setId(id);

                            book.setBitmap(bookElement.select("img.subject-cover").attr("src").replace("spic", "lpic"));

                            book.setTitle(bookElement.select("div.media__body a.fleft").text());

                            String temps = bookElement.select("div.media__body p.subject-abstract").text();
                            String[] temp = temps.split(" / ");
                            book.setAuthor(temp[0]);
                            book.setPublisher(temp[2]);
                            book.setPublishDate(temp[1]);
                            book.setPrice(temp[3]);

                            String rate = bookElement.select("div.media__body span.font-small").text();
                            book.setRate(Double.parseDouble(rate));

                            String reviewCount = bookElement.select("div.media__body span.ml8").text();
                            reviewCount = reviewCount.substring(1, reviewCount.indexOf("人"));
                            book.setReviewCount(Integer.parseInt(reviewCount));

                            book.setAuthorInfo("");
                            book.setPage("");
                            book.setTag("");
                            book.setContent("");
                            book.setSummary("");

                            try {
                                book.setUrl(bookElement.select("div.ebook-link a").attr("href"));
                            } catch (Exception e) {
                                book.setUrl("");
                            }

                            mDataSet.add(book);
                            Log.d(TAG, "getBookList onSuccess() returned: " + book.toString());
                        }
                        progressView.setVisibility(View.GONE);
                        updateItem();
                    }
                }
        );
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

    private void startProductActivityWithTransition(Activity activity, View view, Book book) {
        final Pair[] pairs = TransitionHelper.createSafeTransitionParticipants(activity,
                false,
                new Pair<>(view, activity.getString(R.string.transition_imageview)));

        @SuppressWarnings("unchecked")
        ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, pairs);

        Intent intent = ProductActivity.getStartIntent(activity, book);
        Bundle transitionBundle = optionsCompat.toBundle();
        ActivityCompat.startActivity(activity, intent, transitionBundle);
    }
}
