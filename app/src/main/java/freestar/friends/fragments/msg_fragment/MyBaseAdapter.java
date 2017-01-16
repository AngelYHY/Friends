package freestar.friends.fragments.msg_fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import freestar.friends.R;
import freestar.friends.activities.QurryFriendIdActivity;
import freestar.friends.bean.User;


public class MyBaseAdapter extends BaseAdapter {

    Context mContext;
    List<User> mList;
    LayoutInflater mInflater;

    public MyBaseAdapter(List mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
        mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {

        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }


    class ViewHolder {
        TextView mTextView;
        SimpleDraweeView draweeView;
    }

    ViewHolder mViewHolder;

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.chat_lv_friend, null);

            mViewHolder.draweeView = (SimpleDraweeView) convertView.findViewById(R.id.img_chat_friend);
            mViewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_chat_friend);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        User u = mList.get(position);


        mViewHolder.mTextView.setText(u.getNiname());

        if (u.getHeadUrl() == null) {
            //默认的图片
            Uri uri = Uri.parse("http://www.ld12.com/upimg358/20160130/000604738158232.jpg");

            mViewHolder.draweeView.setImageURI(uri);
        } else {
            Uri uri = Uri.parse(u.getHeadUrl());

            mViewHolder.draweeView.setImageURI(uri);
        }


        return convertView;
    }
}
