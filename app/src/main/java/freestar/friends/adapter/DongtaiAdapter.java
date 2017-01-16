package freestar.friends.adapter;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import freestar.friends.R;
import freestar.friends.activities.HomePageA;
import freestar.friends.activities.UserDataActivity;
import freestar.friends.bean.Message;
import freestar.friends.bean.User;


/**
 * 在适配器中做回调 自定义一个接口
 */
public class DongtaiAdapter extends BaseAdapter implements BGANinePhotoLayout.Delegate {
    private final HomePageA mActivity;
    private List<Message> list;

    public DongtaiAdapter(Activity activity, List<Message> list) {
        this.list = list;
        mActivity = (HomePageA) activity;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mActivity).inflate(R.layout.listview_dongtai, null);
            //十个
            //头像
            viewHolder.mtouxiang = (SimpleDraweeView) view.findViewById(R.id.dongtai_touxiang);
            //动态
            viewHolder.mcontent = (TextView) view.findViewById(R.id.daongtai_content);
            //昵称
            viewHolder.mname = (TextView) view.findViewById(R.id.dongtai_name);
            //评论图标
            viewHolder.mpinglun = (ImageView) view.findViewById(R.id.dongtai_pinglun);
            //时间
            viewHolder.mtime = (TextView) view.findViewById(R.id.dongtai_time);
            //赞图标
            viewHolder.mzan = (ImageButton) view.findViewById(R.id.dongtai_zan);
            //赞的数量
            viewHolder.zancount = (TextView) view.findViewById(R.id.counzan);
            //评论的数量
            viewHolder.pingluncount = (TextView) view.findViewById(R.id.countpinglun);
            //九宫格
            viewHolder.nineGridView = (BGANinePhotoLayout) view.findViewById(R.id.ninegridview);
            //关联
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        final Message message = list.get(i);
        //头像 内容 名称 时间 点赞数 评论数
        viewHolder.mtouxiang.setImageURI(message.getUser().getHeadUrl());
        //头像跳转事件
        viewHolder.mtouxiang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User cuser1 = message.getUser();
                Intent intent = new Intent(mActivity, UserDataActivity.class);
                intent.putExtra("user", cuser1);
                Log.e("FreeStar", "SearchActivity→→→onClick:" + cuser1);
                mActivity.startActivity(intent);
            }
        });
        viewHolder.mcontent.setText(message.getM_message());
        viewHolder.mname.setText(message.getUser().getNiname());
        viewHolder.mtime.setText(message.getCreatedAt());
        viewHolder.zancount.setText(message.getLikeNum() + "");
        viewHolder.pingluncount.setText(message.getComNum() + "");
        //九宮格
        ArrayList<String> urls = (ArrayList<String>) message.getUrls();

        if (urls != null) {
//            List<ImageInfo> imgs = new ArrayList<>();
//            for (int j = 0; j < urls.size(); j++) {
//                ImageInfo img = new ImageInfo();
//                img.setBigImageUrl(urls.get(j));
//                img.setThumbnailUrl(urls.get(j));
//                imgs.add(img);
//            }
            viewHolder.nineGridView.init(mActivity);
            viewHolder.nineGridView.setVisibility(View.VISIBLE);
            viewHolder.nineGridView.setData(urls);
            viewHolder.nineGridView.setDelegate(this);
//            viewHolder.nineGridView.setAdapter(new NineGridViewClickAdapter(context, imgs));
        } else {
            viewHolder.nineGridView.setVisibility(View.GONE);
//            viewHolder.nineGridView.setAdapter(new NineGridViewClickAdapter(context, null));
        }
        return view;
    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        mActivity.setCurrentClickNpl(ninePhotoLayout);
        mActivity.photoPreviewWrapper();
    }

    @Override
    public boolean onLongClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        return false;
    }


    class ViewHolder {
        TextView mname, mtime, mcontent, zancount, pingluncount, zancontent;
        ImageView mpinglun;
        ImageButton mzan;
        SimpleDraweeView mtouxiang;
        BGANinePhotoLayout nineGridView;
    }
}
