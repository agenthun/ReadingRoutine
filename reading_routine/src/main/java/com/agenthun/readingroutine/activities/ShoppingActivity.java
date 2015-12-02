package com.agenthun.readingroutine.activities;

import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.adapters.ShoppingAdapter;
import com.agenthun.readingroutine.datastore.Book;
import com.agenthun.readingroutine.net.BaseAsyncHttp;
import com.agenthun.readingroutine.net.HttpResponseHandler;
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

    private ContentLoadingProgressBar progressBar;
    private RecyclerView shoppingRecyclerView;
    private ShoppingAdapter shoppingAdapter;
    private ArrayList<Book> mDataSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
//        ButterKnife.inject(this);

        /*        progressBar = (ContentLoadingProgressBar) findViewById(R.id.progressBar);
                progressBar.onDetachedFromWindow();*/

        // for test
        mDataSet = new ArrayList<>();
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


        shoppingRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        setupGridLayout();
    }

    private void setupGridLayout() {
        shoppingAdapter = new ShoppingAdapter(getBaseContext(), mDataSet);
        shoppingAdapter.setOnItemClickListener(new ShoppingAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(view, position);
                }
                Log.d(TAG, "onItemClick() returned: " + position);
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(getBaseContext());
        shoppingRecyclerView.setLayoutManager(layoutManager);
        shoppingRecyclerView.setAdapter(shoppingAdapter);
    }

    public void getRequestData(String str) {
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
                        authors = authors + ", " + jsonArrays.optJSONObject(i).optJSONArray("author").optString(j);
                    }
                    book.setAuthor(authors);

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
                    book.setTag(tags);

                    book.setContent(jsonArrays.optJSONObject(i).optString("catalog"));
                    book.setSummary(jsonArrays.optJSONObject(i).optString("summary"));
                    book.setBitmap(jsonArrays.optJSONObject(i).optString("image"));
                    book.setReviewCount(jsonArrays.optJSONObject(i).optJSONObject("rating").optInt("numRaters"));
                    book.setUrl(jsonArrays.optJSONObject(i).optString("ebook_url"));

                    mDataSet.add(book);
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

    //itemClick interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
