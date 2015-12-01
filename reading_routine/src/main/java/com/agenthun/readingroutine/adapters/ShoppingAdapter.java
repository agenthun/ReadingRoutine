package com.agenthun.readingroutine.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.datastore.Book;

import java.util.List;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/12/2 上午12:45.
 */
public class ShoppingAdapter extends RecyclerView.Adapter<ShoppingAdapter.ShoppingViewHolder> {
    private List<Book> mDataset;
    private Context mContext;

    public ShoppingAdapter(Context context, List<Book> objects) {
        mContext = context;
        mDataset = objects;
    }

    @Override
    public ShoppingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        return new ShoppingViewHolder(view, mOnItemClickListener);
    }

    @Override
    public void onBindViewHolder(ShoppingViewHolder holder, int position) {
        //com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(mDataset.get(position).getBitmap(), holder.pic);
        holder.title.setText(mDataset.get(position).getTitle());
        holder.author.setText("作者:" + mDataset.get(position).getAuthor() + " / " + mDataset.get(position).getPrice() + "元");
        holder.rate.setText(mDataset.get(position).getRate() + "分");
        holder.reviewCount.setText("(" + mDataset.get(position).getReviewCount() + "人评论)");
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public class ShoppingViewHolder extends RecyclerView.ViewHolder {
        private ImageView pic;
        private TextView title;
        private TextView author;
        private TextView rate;
        private TextView reviewCount;

        public ShoppingViewHolder(View itemView, final OnItemClickListener clickListener) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onItemClick(v, getAdapterPosition());
                }
            });

            pic = (ImageView) itemView.findViewById(R.id.pic);
            title = (TextView) itemView.findViewById(R.id.title);
            author = (TextView) itemView.findViewById(R.id.author);
            rate = (TextView) itemView.findViewById(R.id.rate);
            reviewCount = (TextView) itemView.findViewById(R.id.reviewCount);
        }
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
