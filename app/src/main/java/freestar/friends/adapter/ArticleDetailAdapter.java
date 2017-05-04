package freestar.friends.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;

import freestar.friends.R;
import freestar.friends.bean.ItemU;
import freestar.friends.util.MyViewHolder;

/**
 * Created by freestar on 2017/1/18 0018.
 */

public class ArticleDetailAdapter extends BaseMultiItemQuickAdapter<ItemU, MyViewHolder> {

    public ArticleDetailAdapter() {
        super(null);
        //图片
        addItemType(0, R.layout.iv_pic);
        //文字
        addItemType(1, R.layout.context);

    }

    @Override
    protected void convert(MyViewHolder holder, ItemU item) {
        switch (holder.getItemViewType()) {
            case 0:
                holder.setSDV(R.id.sdv_pic, item.getUrl());
                break;
            case 1:
                holder.setText(R.id.tv_context, item.getContext());
                break;
        }
    }
}
