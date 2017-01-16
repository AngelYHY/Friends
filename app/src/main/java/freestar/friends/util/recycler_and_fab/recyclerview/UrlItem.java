package freestar.friends.util.recycler_and_fab.recyclerview;

import freestar.friends.R;
import freestar.friends.bean.ItemU;
import freestar.friends.util.recycler_and_fab.recyclerview.base.ItemViewDelegate;
import freestar.friends.util.recycler_and_fab.recyclerview.base.ViewHolder;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class UrlItem implements ItemViewDelegate<ItemU> {
    @Override
    public int getItemViewLayoutId() {
        return R.layout.iv_pic;
    }

    @Override
    public boolean isForViewType(ItemU item, int position) {
        return item.isUrl();
    }

    @Override
    public void convert(ViewHolder holder, ItemU s, int position) {
        holder.setSDV(R.id.sdv_pic, s.getUrl());
    }
}
