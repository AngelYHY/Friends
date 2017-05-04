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

public class SearchFriendAdapter extends BaseQuickAdapter<User, MyViewHolder> {

    public SearchFriendAdapter(@LayoutRes int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected void convert(MyViewHolder helper, User item) {
        helper.setSDV(R.id.img_chat_friend, item.getHeadUrl() == null ? "http://www.ld12.com/upimg358/20160130/000604738158232.jpg" : item.getHeadUrl())
                .setText(R.id.tv_chat_friend, item.getNiname());
    }
}
