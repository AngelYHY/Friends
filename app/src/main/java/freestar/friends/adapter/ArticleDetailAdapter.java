package freestar.friends.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import freestar.friends.R;
import freestar.friends.bean.ItemU;

/**
 * Created by freestar on 2017/1/18 0018.
 */

public class ArticleDetailAdapter extends BaseMultiItemQuickAdapter<ItemU, BaseViewHolder> {

    public ArticleDetailAdapter() {
        super(null);
        //图片
        addItemType(0, R.layout.iv_pic);
        //文字
        addItemType(1, R.layout.context);

    }

    @Override
    protected void convert(BaseViewHolder holder, ItemU item) {
        switch (holder.getItemViewType()) {
            case 0:
                ((SimpleDraweeView)holder.getView(R.id.sdv_pic)).setImageURI(item.getUrl());
                break;
            case 1:
                holder.setText(R.id.tv_context, item.getContext());
                break;
        }
    }
}
