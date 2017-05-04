package freestar.friends.util;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/4/26 0026
 * github：
 */

public class MyViewHolder extends BaseViewHolder {
    public MyViewHolder(View view) {
        super(view);
    }

    public MyViewHolder setSDV(int viewId, String url) {
        SimpleDraweeView sdv = getView(viewId);
        sdv.setImageURI(url);
        return this;
    }

}
