package com.agenthun.readingroutine.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.agenthun.readingroutine.R;
import com.daimajia.swipe.adapters.RecyclerSwipeAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Agent Henry on 2015/9/11.
 */
public abstract class BaseTAdapter extends RecyclerSwipeAdapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_BODY = 1;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    private Context context;
    private CharSequence mTitle;
    private Drawable mDrawable;
    private HeaderViewHolder headerViewHolder;
    private long headerAnimationStartTime = 0;
    private boolean lockedAnimations = false;

    protected abstract RecyclerView.ViewHolder createBodyViewHolder(Context context, ViewGroup parent);

    protected abstract void bindBodyViewHolder(RecyclerView.ViewHolder viewHolder, int position);

    public BaseTAdapter(Context context, CharSequence title, Drawable drawable) {
        this.context = context;
        this.mTitle = title;
        this.mDrawable = drawable;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else {
            return TYPE_BODY;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(context).inflate(R.layout.view_header, parent, false);
            StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
            layoutParams.setFullSpan(true);
            view.setLayoutParams(layoutParams);
            headerViewHolder = new HeaderViewHolder(view);
            headerViewHolder.setHeaderTitle(mTitle);
            headerViewHolder.setHeaderIcon(mDrawable);
            return headerViewHolder;
        } else if (viewType == TYPE_BODY) {
            createBodyViewHolder(context, parent);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        int viewType = getItemViewType(position);
        if (viewType == TYPE_HEADER) {
            bindHeaderViewHolder((HeaderViewHolder) viewHolder);
        } else if (viewType == TYPE_BODY) {
            bindBodyViewHolder(viewHolder, position);
        }
    }

    private void bindHeaderViewHolder(final HeaderViewHolder viewHolder) {
        viewHolder.header.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                viewHolder.header.getViewTreeObserver().removeOnPreDrawListener(this);
                animateHeader(viewHolder);
                return false;
            }
        });
    }

    private void animateHeader(HeaderViewHolder viewHolder) {
        if (!lockedAnimations) {
            headerAnimationStartTime = System.currentTimeMillis();
            viewHolder.header.setTranslationY(-viewHolder.header.getHeight());
            viewHolder.header.animate().translationY(0).setDuration(300).setInterpolator(INTERPOLATOR);
        }
    }

    public long getHeaderAnimationStartTime() {
        return headerAnimationStartTime;
    }

    public boolean isLockedAnimations() {
        return lockedAnimations;
    }

    public void setLockedAnimations(boolean lockedAnimations) {
        this.lockedAnimations = lockedAnimations;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.header)
        View header;
        @InjectView(R.id.headerTitle)
        TextView headerTitle;
        @InjectView(R.id.headerIcon)
        ImageView headerIcon;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.inject(this, itemView);
        }

        public void setHeaderTitle(CharSequence text) {
            headerTitle.setText(text);
        }

        public void setHeaderIcon(Drawable drawable) {
            headerIcon.setImageDrawable(drawable);
        }
    }
}
