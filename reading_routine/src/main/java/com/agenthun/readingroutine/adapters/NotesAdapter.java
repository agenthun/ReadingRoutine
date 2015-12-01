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
import android.widget.TextView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.NoteInfo;
import com.agenthun.readingroutine.views.TagView;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.daimajia.swipe.SimpleSwipeListener;
import com.daimajia.swipe.SwipeLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Henry on 2015/5/20.
 */
public class NotesAdapter extends BaseTAdapter {

    private static final String TAG = "NotesAdapter";

    public static final String NOTE_TITLE = "NOTE_TITLE";
    public static final String NOTE_COMPOSE = "NOTE_COMPOSE";
    public static final String NOTE_CREATE_TIME = "NOTE_CREATE_TIME";
    public static final String NOTE_COLOR_INDEX = "NOTE_COLOR_INDEX";

    private static final int MIN_ITEM_COUNT = 1;
    private static final int MAX_ITEM_ANIMATION_DELAY = 500;
    private static final Interpolator INTERPOLATOR = new DecelerateInterpolator();

    private int cellHeight;
    private int cellWidth;

    private int lastAnimatedItem = 0;
    private List<NoteInfo> mDataset;
    private Context mContext;
    private int briefComposeLength = 50;

    public NotesAdapter(Context context, CharSequence title, Drawable icon, List<NoteInfo> objects) {
        super(context, title, icon);
        this.mContext = context;
        /*        this.cellHeight = (int) (UiUtils.getScreenWidthPixels(context) / 2.3f);
                this.cellWidth = UiUtils.getScreenWidthPixels(context);*/
        this.mDataset = objects;
    }

    @Override
    protected RecyclerView.ViewHolder createBodyViewHolder(Context context, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_note, parent, false);
        StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
        /*        layoutParams.height = cellHeight;
                layoutParams.width = cellWidth;*/
        layoutParams.setFullSpan(false);
        view.setLayoutParams(layoutParams);
        RecyclerView.ViewHolder notesViewHolder = new NotesViewHolder(view);
        return notesViewHolder;
    }

    @Override
    protected void bindBodyViewHolder(final RecyclerView.ViewHolder viewHolder, final int position) {
        animateNotes((NotesViewHolder) viewHolder);
        NoteInfo item = mDataset.get(position - 1);

        ((NotesViewHolder) viewHolder).swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);
        ((NotesViewHolder) viewHolder).swipeLayout.addSwipeListener(new SimpleSwipeListener() {
            @Override
            public void onOpen(SwipeLayout layout) {
                YoYo.with(Techniques.Tada).duration(500).delay(100).playOn(layout.findViewById(R.id.ic_trash));
            }
        });

        //click listener
        ((NotesViewHolder) viewHolder).deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemManger.removeShownLayouts(((NotesViewHolder) viewHolder).swipeLayout);
                mItemManger.closeAllItems();

                int pos = viewHolder.getLayoutPosition();
                mOnItemClickListener.onItemDeleteClick(((NotesViewHolder) viewHolder).deleteButton, pos);
//                Log.d(TAG, "onItemDeleteClick() returned: " + pos);
            }
        });

        if (mOnItemClickListener != null) {
            ((NotesViewHolder) viewHolder).tag.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = viewHolder.getLayoutPosition();
                    mOnItemClickListener.onItemClick(((NotesViewHolder) viewHolder).tag, pos);
//                    Log.d(TAG, "onClick() returned: " + pos);
                }
            });
        }

        // set tag title
        ((NotesViewHolder) viewHolder).noteTitle.setText(item.getNoteTitle());

        // set tag brief compose
        ((NotesViewHolder) viewHolder).noteContent.setText(item.getNoteCompose());

        // set tag create time
        String createTime = item.getNoteCreateTime();
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Calendar calendar = Calendar.getInstance();
        String time = crateTimeForm(createTime, DATE_FORMAT.format(calendar.getTime()));
        ((NotesViewHolder) viewHolder).noteTime.setText(time);

        // set tag color
        int[] colorBook = mContext.getResources().getIntArray(R.array.style_tag_color);
        int colorIndex = item.getNoteColor();
        ((NotesViewHolder) viewHolder).tag.setTagMaskColor(colorBook[colorIndex]);

        if (lastAnimatedItem < position) lastAnimatedItem = position;
    }

    private String crateTimeForm(String createTime, String currentTime) {
        if (!createTime.substring(0, 4).equals(currentTime.substring(0, 4))) {
            return createTime.substring(0, 4);
        } else if (!createTime.substring(5, 10).equals(currentTime.substring(5, 10))) {
            return createTime.substring(5, 10);
        } else {
            return createTime.substring(11);
        }
    }

    private void animateNotes(NotesViewHolder holder) {
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

        holder.swipeLayout.setScaleY(0);
        holder.swipeLayout.setScaleX(0);
        holder.swipeLayout.animate().scaleY(1).scaleX(1).setDuration(200).setInterpolator(INTERPOLATOR).setStartDelay(animationDelay).start();
    }

    @Override
    public int getItemCount() {
        return MIN_ITEM_COUNT + mDataset.size();
    }

    @Override
    public int getSwipeLayoutResourceId(int i) {
        return R.id.swipe;
    }

    static class NotesViewHolder extends RecyclerView.ViewHolder {
        @InjectView(R.id.swipe)
        SwipeLayout swipeLayout;
        @InjectView(R.id.ic_trash)
        ImageView deleteButton;
        @InjectView(R.id.tag)
        TagView tag;
        @InjectView(R.id.note_title)
        TextView noteTitle;
        @InjectView(R.id.note_time)
        TextView noteTime;
        @InjectView(R.id.note_content)
        TextView noteContent;

        public NotesViewHolder(View view) {
            super(view);
            ButterKnife.inject(this, view);
        }
    }

    public NoteInfo getItemData(int position) {
        return mDataset.get(position);
    }

    //itemClick interface
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemDeleteClick(View view, int position);
    }

    private OnItemClickListener mOnItemClickListener;

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
