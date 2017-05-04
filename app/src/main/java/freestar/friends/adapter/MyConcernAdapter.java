package freestar.friends.adapter;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;

import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.util.MyViewHolder;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/4/28 0028
 * github：
 */

public class MyConcernAdapter extends BaseQuickAdapter<User, MyViewHolder> {

    public MyConcernAdapter(@LayoutRes int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected void convert(MyViewHolder holder, User item) {
        holder.setSDV(R.id.img_chat_friend, item.getHeadUrl())
                .setText(R.id.text_data, item.getNiname())
                .addOnClickListener(R.id.img_chat_friend);
    }
}
