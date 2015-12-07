package com.agenthun.readingroutine.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.agenthun.readingroutine.R;
import com.agenthun.readingroutine.utils.Avatar;
import com.agenthun.readingroutine.utils.CircleTransformation;
import com.agenthun.readingroutine.views.AvatarView;
import com.squareup.picasso.Picasso;

/**
 * @project ReadingRoutine
 * @authors agenthun
 * @date 15/12/6 下午10:27.
 */
public class AvatarAdapter extends BaseAdapter {
    private static final String TAG = "AvatarAdapter";

    private static final Avatar[] avatars = Avatar.values();

    private LayoutInflater layoutInflater;
    private Context mContext;

    public AvatarAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        mContext = context;
    }

    @Override
    public int getCount() {
        return avatars.length;
    }

    @Override
    public Object getItem(int position) {
        return avatars[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_avatar, parent, false);
        }
        setAvatar((AvatarView) convertView, avatars[position]);
        return convertView;
    }

    private void setAvatar(AvatarView mIcon, Avatar avatar) {
        mIcon.setContentDescription(avatar.getNameForAccessibility());
        Picasso.with(mContext).load(avatar.getDrawableId()).transform(new CircleTransformation()).into(mIcon);
    }
}
