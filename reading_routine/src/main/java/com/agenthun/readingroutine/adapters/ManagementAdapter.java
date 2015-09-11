package com.agenthun.readingroutine.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.BookInfo;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Henry on 2015/5/20.
 */
public class ManagementAdapter extends BaseTAdapter {

    private static final int MAX_ITEM_ANIMATION_DELAY = 500;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    private int cellHeight;
    private int cellWidth;

    private int lastAnimatedItem = 0;
    private int itemsCount = 0;
    private List<BookInfo> mDataset;

    public ManagementAdapter(Context context, CharSequence title, Drawable drawable) {
        super(context, title, drawable);
    }


    @Override
    protected RecyclerView.ViewHolder createBodyViewHolder(Context context, ViewGroup parent) {
        return null;
    }

    @Override
    protected void bindBodyViewHolder(RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return 0;
    }

    static class ManagementViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.managementHeader)
        View managementHeader;

        public ManagementViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
