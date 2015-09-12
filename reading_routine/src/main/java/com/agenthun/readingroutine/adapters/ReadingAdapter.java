package com.agenthun.readingroutine.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.UiUtils;
import com.agenthun.readingroutine.datastore.BookInfo;
import com.agenthun.readingroutine.datastore.db.DatabaseUtil;
import com.agenthun.readingroutine.views.PaperView;
import com.daimajia.swipe.SwipeLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Henry on 2015/5/20.
 */
public class ReadingAdapter extends BaseTAdapter {

    private static final int MAX_ITEM_ANIMATION_DELAY = 500;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    private int cellHeight;
    private int cellWidth;

    private int lastAnimatedItem = 0;
    private List<String> mDataset;

    public ReadingAdapter(Context context, CharSequence title, Drawable icon) {
        super(context, title, icon);
        this.cellHeight = (int) (UiUtils.getScreenWidthPixels(context) / 2 * 1.2);
        this.cellWidth = UiUtils.getScreenWidthPixels(context) / 2;
        mDataset = new ArrayList<>();
        mDataset.add("a");
        mDataset.add("b");
        mDataset.add("c");
        mDataset.add("d");
        mDataset.add("e");
        mDataset.add("f");
        mDataset.add("g");
        mDataset.add("h");
        mDataset.add("i");
    }

    @Override
    protected RecyclerView.ViewHolder createBodyViewHolder(Context context, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_reading, parent, false);
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        layoutParams.height = cellHeight;
        layoutParams.width = cellWidth;
        layoutParams.setFullSpan(false);
        view.setLayoutParams(layoutParams);
        RecyclerView.ViewHolder readingViewHolder = new ReadingViewHolder(view);
        return readingViewHolder;
    }

    @Override
    protected void bindBodyViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        animateReading((ReadingViewHolder) viewHolder);
        String item = mDataset.get(position - 1);
        ((ReadingViewHolder) viewHolder).textViewData.setText(item);
        if (lastAnimatedItem < position) lastAnimatedItem = position;
    }

    private void animateReading(ReadingViewHolder holder) {
        if (!isLockedAnimations()) {
            if (lastAnimatedItem == holder.getPosition()) {
                setLockedAnimations(true);
            }
        }

        long animationDelay = getHeaderAnimationStartTime() + MAX_ITEM_ANIMATION_DELAY - System.currentTimeMillis();

        if (getHeaderAnimationStartTime() == 0) {
            animationDelay = holder.getPosition() * 30 + MAX_ITEM_ANIMATION_DELAY;
        } else if (animationDelay < 0) {
            animationDelay = holder.getPosition() * 30;
        } else {
            animationDelay += holder.getPosition() * 30;
        }

        holder.block.setScaleY(0);
        holder.block.setScaleX(0);
        holder.block.animate().scaleY(1).scaleX(1).setDuration(200).setInterpolator(INTERPOLATOR).setStartDelay(animationDelay).start();
    }

    @Override
    public int getItemCount() {
        return 1 + mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return 0;
    }

    static class ReadingViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.text_data)
        TextView textViewData;
        @InjectView(R.id.block)
        RelativeLayout block;

        public ReadingViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }
}
