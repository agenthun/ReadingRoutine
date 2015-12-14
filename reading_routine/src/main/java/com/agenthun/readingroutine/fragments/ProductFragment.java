package com.agenthun.readingroutine.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.Book;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/12/13 上午6:02.
 */
public class ProductFragment extends Fragment {
    private Book mBook;
    private SolvedStateListener mSolvedStateListener;

    public static ProductFragment newInstance(String productId, SolvedStateListener solvedStateListener) {
        if (productId == null) {
            throw new IllegalArgumentException("the productId is null !!!");
        }
        Bundle args = new Bundle();
        args.putString(Book.TAG, productId);
        ProductFragment fragment = new ProductFragment();
        if (solvedStateListener != null) {
            fragment.mSolvedStateListener = solvedStateListener;
        }
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        String productId = getArguments().getString(Book.TAG);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_product_selection, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
    }

    public interface SolvedStateListener {
        void onProductSolved();
    }
}
