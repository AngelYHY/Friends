package freestar.friends.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import freestar.friends.R;
import freestar.friends.bean.DisSay;

//评论表的适配器 用来存放评论信息

public class CommentAdapter extends BaseAdapter {
    Context mContext;
    List<DisSay> mList;
    LayoutInflater minflate;

    public CommentAdapter(Context mContext, List<DisSay> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int i) {
        return mList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    ViewHolder mViewHolder;

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            mViewHolder = new ViewHolder();
//            view = minflate.inflate(R.layout.citem,null);

            view = minflate.inflate(R.layout.dis_item_layout, null);

            //评论者
            mViewHolder.user_content = (TextView) view.findViewById(R.id.c_author);
            //评论内容
            mViewHolder.message_content = (TextView) view.findViewById(R.id.c_content);
            view.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) view.getTag();
        }
        //评论表
        DisSay bean = mList.get(i);
        //評論人
//        mViewHolder.user_content.setText(bean.getPldeuser().getNiname());
//        //評論內容
//        mViewHolder.message_content.setText(bean.getConment());
        return view;
    }

    class ViewHolder {
        TextView user_content;
        TextView message_content;
    }

}


