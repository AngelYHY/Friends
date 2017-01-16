package freestar.friends.fragments.msg_fragment;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import freestar.friends.R;
import freestar.friends.bean.AtlasDis;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class ChildAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<AtlasDis> mList;
    LayoutInflater mInflater;

    public ChildAdapter(Context context, ArrayList<AtlasDis> list) {
        mContext = context;
        mList = list;
        mInflater = LayoutInflater.from(context);
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
        TextView tv_item_pinglun, tv_item_huifu, tv_item_beipinglun, tv_item_comment;
    }

    ViewHolder mViewHolder;

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.lv_item, null);
            mViewHolder.tv_item_pinglun = (TextView) convertView.findViewById(R.id.tv_item_pinglun);
            mViewHolder.tv_item_huifu = (TextView) convertView.findViewById(R.id.tv_item_huifu);
            mViewHolder.tv_item_beipinglun = (TextView) convertView.findViewById(R.id.tv_item_beipinglun);
            mViewHolder.tv_item_comment = (TextView) convertView.findViewById(R.id.tv_item_comment);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        AtlasDis atlasDis = mList.get(i);
        Log.e("子评论评论页面的信息", "子评论评论页面的信息" + atlasDis.toString());


        if (atlasDis.getFather_user() == null) {
            if (atlasDis.getCuser().getNiname() != null) {
                mViewHolder.tv_item_pinglun.setText(atlasDis.getCuser().getNiname());
            } else {
                mViewHolder.tv_item_pinglun.setText("默认人的id为：" + atlasDis.getCuser().getObjectId());
            }

            if (atlasDis.getComment() != null) {
                mViewHolder.tv_item_comment.setText("："+atlasDis.getComment());
            } else {
                mViewHolder.tv_item_comment.setText("空评论]");
            }


        } else {

            mViewHolder.tv_item_huifu.setVisibility(View.VISIBLE);
            mViewHolder.tv_item_beipinglun.setVisibility(View.VISIBLE);
            if (atlasDis.getCuser().getNiname() != null) {
                mViewHolder.tv_item_pinglun.setText(atlasDis.getCuser().getNiname());
            } else {
                mViewHolder.tv_item_pinglun.setText("默认评论者的id为：" + atlasDis.getCuser().getObjectId());
            }

            if (atlasDis.getComment() != null) {
                mViewHolder.tv_item_comment.setText("："+atlasDis.getComment());
            } else {
                mViewHolder.tv_item_comment.setText("空评论]");
            }

            if (atlasDis.getFather_user().getNiname()!= null) {
                mViewHolder.tv_item_beipinglun.setText(atlasDis.getFather_user().getNiname());
            }else{
                mViewHolder.tv_item_beipinglun.setText("默认被评论者的id为" + atlasDis.getFather_user().getObjectId());
            }

        }


        return convertView;
    }
}