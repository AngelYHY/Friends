package freestar.friends.adapter;

import android.app.Activity;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import freestar.friends.R;
import freestar.friends.activities.HomePageA;
import freestar.friends.bean.Message;
import freestar.friends.bean.User;

/**
 * Created by freestar on 2017/1/18 0018.
 */

public class DynamicAdapter extends BaseMultiItemQuickAdapter<Message, BaseViewHolder> implements BGANinePhotoLayout.Delegate {

    private final HomePageA mActivity;

    public DynamicAdapter(Activity activity) {
        super(null);
        addItemType(0, R.layout.listview_dongtai_no);
        addItemType(1, R.layout.listview_dongtai);
        mActivity = (HomePageA) activity;
    }

    @Override
    protected void convert(BaseViewHolder holder, Message item) {
        final User user = item.getUser();

        SimpleDraweeView head = holder.getView(R.id.dongtai_touxiang);
        head.setImageURI(user.getHeadUrl());
        holder.addOnClickListener(R.id.dongtai_touxiang);

        holder.setText(R.id.dongtai_name, user.getNiname())
                .setText(R.id.dongtai_time, item.getCreatedAt())
                .setText(R.id.daongtai_content, item.getM_message())
                .setText(R.id.counzan, String.valueOf(item.getLikeNum()))
                .setText(R.id.countpinglun, String.valueOf(item.getComNum()));

        if (holder.getItemViewType() == 1) {
            ArrayList<String> urls = (ArrayList<String>) item.getUrls();
            BGANinePhotoLayout photoLayout = holder.getView(R.id.ninegridview);
            holder.addOnClickListener(R.id.ninegridview);
            photoLayout.init(mActivity);
            photoLayout.setDelegate(this);
            photoLayout.setData(urls);
        }

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

}
