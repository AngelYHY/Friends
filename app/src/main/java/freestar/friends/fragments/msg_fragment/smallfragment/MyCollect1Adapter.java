package freestar.friends.fragments.msg_fragment.smallfragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import freestar.friends.R;
import freestar.friends.bean.Article;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.CollectArticle;
import freestar.friends.bean.CollectAtlas;

/**
 * Created by 28655 on 2016/8/26.
 */
public class MyCollect1Adapter extends BaseAdapter {
    Context mContext;
    List<CollectArticle> mList;
    LayoutInflater mInflater;

    public MyCollect1Adapter(List mList, Context mContext) {
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
        TextView mTextView_time;
        TextView mTextView_tit;
        TextView mTextView_num;
        ImageView  mImageView;

    }
    ViewHolder mViewHolder;
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_tujicollect, null);
            mViewHolder.mTextView_time = (TextView) convertView.findViewById(R.id.time_tuji);
            mViewHolder.mTextView_tit  = (TextView) convertView.findViewById(R.id.tv_title_tuji);
            mViewHolder.mTextView_num  = (TextView) convertView.findViewById(R.id.num_tuji);
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.imageView_tuji);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        CollectArticle a = mList.get(position);
        Article at= a.getSources();
        Log.e("FreeStar", "MyCollect1Adapter→→→done:" + at.toString());
        mViewHolder.mTextView_tit.setText(at.getTitle());
        mViewHolder.mTextView_num.setText(at.getLikeNum()+"赞");
        mViewHolder.mTextView_time.setText(at.getCreatedAt());
//        Glide.with(mContext).load(a.getMainPic()).placeholder(R.drawable.ic_default_image).into(mViewHolder.mImageView);
//        mViewHolder.mImageView.setImageURI(a.getMainPic());
        Picasso.with(mContext).load(at.getMainPic()).placeholder(R.drawable.rc_ic_admin_selector).into(mViewHolder.mImageView);

        return convertView;
    }
}
