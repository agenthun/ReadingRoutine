package com.agenthun.readingroutine.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.agenthun.readingroutine.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Henry on 2015/5/20.
 */
public class ManagementAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_MANAGEMENT_HEADER = 0;
    private static final int MIN_ITEM_COUNT = 1;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    private Context context;
    private long managementHeaderAnimationStartTime = 0;
    private boolean lockedAnimations = false;

    public ManagementAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_MANAGEMENT_HEADER;
        }
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_MANAGEMENT_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_management_header, parent, false);
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            layoutParams.setFullSpan(true);
            view.setLayoutParams(layoutParams);
            return new ManagementViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_MANAGEMENT_HEADER) {
            bindManagementHeader((ManagementViewHolder) holder);
        }
    }

    private void bindManagementHeader(final ManagementViewHolder holder) {
        holder.managementHeader.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                holder.managementHeader.getViewTreeObserver().removeOnPreDrawListener(this);
                animateManagementHeader(holder);
                return false;
            }
        });
    }

    private void animateManagementHeader(ManagementViewHolder holder) {
        if (!lockedAnimations) {
            managementHeaderAnimationStartTime = System.currentTimeMillis();
            holder.managementHeader.setTranslationY(-holder.managementHeader.getHeight());
            holder.managementHeader.animate().translationY(0).setDuration(300).setInterpolator(INTERPOLATOR);
        }
    }

    @Override
    public int getItemCount() {
        return MIN_ITEM_COUNT;
    }

    static class ManagementViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.managementHeader)
        View managementHeader;

        public ManagementViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public void setLockedAnimations(boolean lockedAnimations) {
        this.lockedAnimations = lockedAnimations;
    }
}
