package freestar.friends.adapter;

import android.support.annotation.LayoutRes;

import com.chad.library.adapter.base.BaseQuickAdapter;

import freestar.friends.R;
import freestar.friends.bean.Message;
import freestar.friends.bean.User;
import freestar.friends.util.MyViewHolder;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/5/3 0003
 * github：
 */

public class MySayAdapter extends BaseQuickAdapter<Message, MyViewHolder> {

    public MySayAdapter(@LayoutRes int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected void convert(MyViewHolder helper, Message item) {
        User user = item.getUser();
        helper.setSDV(R.id.iv_ss, user.getHeadUrl())
                .setText(R.id.time_ss, item.getCreatedAt())
                .setText(R.id.tv_title_ss, item.getM_message())
                .setText(R.id.tv_niname_ss, user.getNiname());
    }
}
