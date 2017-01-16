package freestar.friends.fragments.msg_fragment;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

import freestar.friends.R;
import freestar.friends.bean.User;

/**
 * Created by 28655 on 2016/8/30.
 */
public class MyconcernAdapter extends BaseAdapter {
    Context mContext;
    List<User> mList;
    LayoutInflater mInflater;

    public MyconcernAdapter(List mList, Context mContext) {
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
            convertView = mInflater.inflate(R.layout.item_follow_concern, null);

            mViewHolder.draweeView = (SimpleDraweeView) convertView.findViewById(R.id.img_concern);
            mViewHolder.mTextView = (TextView) convertView.findViewById(R.id.tv_concern);

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
