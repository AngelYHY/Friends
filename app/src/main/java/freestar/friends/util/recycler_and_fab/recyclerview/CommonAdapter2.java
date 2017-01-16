package freestar.friends.util.recycler_and_fab.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

import freestar.friends.util.recycler_and_fab.recyclerview.base.ItemViewDelegate;
import freestar.friends.util.recycler_and_fab.recyclerview.base.ViewHolder;

/**
 * Created by zhy on 16/4/9.
 */
public abstract class CommonAdapter2<T> extends MultiItemTypeAdapter2<T> {
    protected Context mContext;
    protected int mLayoutId;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;

    public CommonAdapter2(final Context context, final int layoutId, List<T> datas) {
        super(context, datas);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mLayoutId = layoutId;
        mDatas = datas;

        addItemViewDelegate(new ItemViewDelegate<T>() {
            @Override
            public int getItemViewLayoutId() {
                return layoutId;
            }

            @Override
            public boolean isForViewType(T item, int position) {
                return true;
            }

            @Override
            public void convert(ViewHolder holder, T t, int position) {
                CommonAdapter2.this.convert(holder, t, position);
            }
        });
    }


    protected abstract void convert(ViewHolder holder, T t, int position);


}
