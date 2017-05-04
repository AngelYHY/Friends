package freestar.friends.adapter;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;

import freestar.friends.R;
import freestar.friends.bean.Atlas;
import freestar.friends.util.MyViewHolder;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/4/28 0028
 * github：
 */

public class ComAtlasAdapter extends BaseQuickAdapter<Atlas, MyViewHolder> {

    public ComAtlasAdapter(@LayoutRes int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected void convert(MyViewHolder helper, Atlas item) {
        helper.setSDV(R.id.imageView_tuji, item.getMainPic())
                .setText(R.id.time_tuji, item.getCreatedAt())
                .setText(R.id.tv_title_tuji, item.getTitle())
                .setText(R.id.num_tuji, item.getLikeNum() + "赞");
    }
}
