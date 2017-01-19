package freestar.friends.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

import freestar.friends.R;


/**
 * Author:  ljo_h
 * Date:    2016/9/28
 * Description:
 */
public class XRecyclerView extends RecyclerView {
    private Context context;

    public XRecyclerView(Context context) {
        this(context, null);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public XRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        init(attrs);
    }

    //只做了添加分割线的操作
    private void init(AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.XRecyclerView);
        if (array.getBoolean(R.styleable.XRecyclerView_item_decoration, true)) {
            this.addItemDecoration(new DividerItemDecoration(context,
                    DividerItemDecoration.VERTICAL_LIST));
        }
        array.recycle();
    }

}
