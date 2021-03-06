package freestar.friends.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;

import freestar.friends.R;
import freestar.friends.bean.Atlas;
import freestar.friends.util.MyViewHolder;

/**
 * Created by freestar on 2017/1/17 0017.
 */

public class AtlasAdapter extends BaseQuickAdapter<Atlas, MyViewHolder> {

    public AtlasAdapter() {
        super(R.layout.rv_atlas, null);
    }

    @Override
    protected void convert(MyViewHolder holder, Atlas item) {
        holder.setSDV(R.id.iv_main, item.getMainPic())
                .setText(R.id.tv_time, item.getCreatedAt())
                .setText(R.id.tv_disNum, item.getLikeNum() + "赞")
                .setText(R.id.tv_title, item.getTitle() + "(" + item.getNum() + " 图)");

    }

}
