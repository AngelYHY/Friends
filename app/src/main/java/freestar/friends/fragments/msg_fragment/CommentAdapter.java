package freestar.friends.fragments.msg_fragment;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.R;
import freestar.friends.bean.AtlasDis;


public class CommentAdapter extends BaseAdapter {
    Context mContext;
    List<AtlasDis> fList;
    ArrayList<AtlasDis> mList;
    LayoutInflater mInflater;
    ChildAdapter childAdapter;
    ArrayList<AtlasDis> ziList = new ArrayList<AtlasDis>();
    AtlasDis articleDis;

    public CommentAdapter(ArrayList mList, Context mContext, List fList) {
        this.mList = mList;
        this.fList = fList;
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
        ListView lv;
        TextView tv_name, tv_time, tv_pinglun, tv_user_message;
        SimpleDraweeView img_user, img_message;
    }

    ViewHolder mViewHolder;


    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {


        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.comment_adapter, null);

            mViewHolder.img_user = (SimpleDraweeView) convertView.findViewById(R.id.img_user_comment);
            mViewHolder.img_message = (SimpleDraweeView) convertView.findViewById(R.id.img_message);
            mViewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name_comment);
            mViewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time_comment);

            mViewHolder.tv_user_message = (TextView) convertView.findViewById(R.id.tv_user_message);
            mViewHolder.lv = (ListView) convertView.findViewById(R.id.lv_item);

            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }

        //  赋值
        articleDis = mList.get(i);


        if (articleDis.getAtlas() != null) {
            if (articleDis.getAuthor() != null) {
                if (articleDis.getAuthor().getHeadUrl() != null) {
                    mViewHolder.img_user.setImageURI(Uri.parse(articleDis.getAuthor().getHeadUrl()));
                }
            } else {
                mViewHolder.img_user.setImageURI(Uri.parse(""));
            }


            mViewHolder.img_message.setImageURI(Uri.parse(articleDis.getAtlas().getMainPic()));


            if (articleDis.getAuthor() != null) {
                mViewHolder.tv_name.setText(articleDis.getAuthor().getNiname());
            } else {
                mViewHolder.tv_name.setText("默认");
            }

            if (articleDis.getAtlas() != null) {
                mViewHolder.tv_time.setText(articleDis.getAtlas().getCreatedAt());

            }
            if (articleDis.getAtlas() != null) {
                mViewHolder.tv_user_message.setText(articleDis.getAtlas().getTitle());
            }
        }


        for (AtlasDis a : fList) {

            if (a.getAtlas().getObjectId().equals(articleDis.getAtlas().getObjectId())) {
                ziList.add(new AtlasDis(a.getCuser(), a.getComment_father_user(), a.getComment(), a.getAtlas(), a.getFather_user()));
            }
        }


        Log.e("父适配器对应图集的评论", "父适配器对应图集的评论---" + ziList.toString());
        if (childAdapter == null) {
            childAdapter = new ChildAdapter(mContext, ziList);
            mViewHolder.lv.setAdapter(childAdapter);
            MeasureUtil.setListViewHeightBasedOnChildren(mViewHolder.lv);
        }


//        BmobQuery<AtlasDis> query = new BmobQuery<AtlasDis>();
//
//        //找出 对应图集id的所有评论
//        query.addWhereEqualTo("atlas", articleDis.getAtlas().getObjectId());
//        Log.e("对应图集的id", "对应图集的id---" + articleDis.getAtlas().getObjectId());
//
//
//        query.include("cuser,father_user,comment");
//        query.order("-createdAt");
//        query.findObjects(new FindListener<AtlasDis>() {
//            @Override
//            public void done(List<AtlasDis> list, BmobException e) {
//                ziList.addAll(list);
//                Log.e("子评论信息", "子评论信息" + ziList.toString());
//                //设置评论
//
//                ChildAdapter childAdapter = new ChildAdapter(mContext, ziList);
//                if (childAdapter == null) {
//                    mViewHolder.lv.setAdapter(childAdapter);
//                    MeasureUtil.setListViewHeightBasedOnChildren(mViewHolder.lv);
//                } else {
//                    childAdapter.notifyDataSetChanged();
//                }
//            }
//        });


        //找出 对应图集id的所有评论
//       if(){
//           articleDis.getAtlas().getObjectId().equals(articleDis.getComment_father_user().)
//       }

        return convertView;


    }
}