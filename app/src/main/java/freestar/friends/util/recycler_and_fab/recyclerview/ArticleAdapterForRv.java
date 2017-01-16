package freestar.friends.util.recycler_and_fab.recyclerview;


import android.content.Context;

import java.util.List;

import freestar.friends.bean.ItemU;

/**
 * Created by zhy on 15/9/4.
 */
public class ArticleAdapterForRv extends MultiItemTypeAdapter<ItemU> {
    public ArticleAdapterForRv(Context context, List<ItemU> datas) {
        super(context, datas);

        addItemViewDelegate(new UrlItem());
        addItemViewDelegate(new ContextItem());
    }
}
