package freestar.friends.widget;

import android.view.View;

import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by freestar on 2017/1/17 0017.
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
