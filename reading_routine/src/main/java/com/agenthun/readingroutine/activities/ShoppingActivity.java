package com.agenthun.readingroutine.activities;

import android.os.Bundle;
import android.support.v4.widget.ContentLoadingProgressBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.adapters.ShoppingAdapter;
import com.agenthun.readingroutine.datastore.Book;

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

    //itemClick interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
