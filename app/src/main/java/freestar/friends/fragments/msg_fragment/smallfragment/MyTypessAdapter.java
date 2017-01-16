package freestar.friends.fragments.msg_fragment.smallfragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import freestar.friends.R;
import freestar.friends.bean.Article;
import freestar.friends.bean.Message;
import freestar.friends.bean.User;

/**
 * Created by 28655 on 2016/8/29.
 */
public class MyTypessAdapter extends BaseAdapter {
    Context mContext;
    List<Message> mList;
    LayoutInflater mInflater;

    public MyTypessAdapter(List mList, Context mContext) {
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
       // TextView mTextView_num;
        TextView mTextView_niname;
        CircleImageView mTextView_imageView_ss;

    }
    ViewHolder mViewHolder;
    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.item_tujiss, null);
            mViewHolder.mTextView_time = (TextView) convertView.findViewById(R.id.time_ss);
            mViewHolder.mTextView_tit  = (TextView) convertView.findViewById(R.id.tv_title_ss);
            mViewHolder.mTextView_niname  = (TextView) convertView.findViewById(R.id.tv_niname_ss);
            mViewHolder.mTextView_imageView_ss = (CircleImageView) convertView.findViewById(R.id.iv_ss);
            //mViewHolder.mTextView_num  = (TextView) convertView.findViewById(R.id.num_ss);
            convertView.setTag(mViewHolder);
        }else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        Message at = mList.get(position);
        Log.e("FreeStar", "MyTypessAdapter→→→done:" + at.toString());
        User user= at.getUser();
        mViewHolder.mTextView_niname.setText(user.getNiname());
        Picasso.with(mContext).load(user.getHeadUrl()).placeholder(R.drawable.rc_ic_admin_selector).into(mViewHolder.mTextView_imageView_ss);
        mViewHolder.mTextView_tit.setText(at.getM_message());
       //mViewHolder.mTextView_num.setText(at.getLikeNum()+"赞");
        mViewHolder.mTextView_time.setText(at.getCreatedAt());
//        Glide.with(mContext).load(a.getMainPic()).placeholder(R.drawable.ic_default_image).into(mViewHolder.mImageView);
//        mViewHolder.mImageView.setImageURI(a.getMainPic());
       // Picasso.with(mContext).load(at.getMainPic()).placeholder(R.drawable.rc_ic_admin_selector).into(mViewHolder.mImageView);

        return convertView;
    }
}
