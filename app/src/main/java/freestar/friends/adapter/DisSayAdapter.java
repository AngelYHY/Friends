package freestar.friends.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;

import freestar.friends.R;
import freestar.friends.bean.DisSay;
import freestar.friends.bean.User;
import freestar.friends.util.MyViewHolder;

/**
 * Created by freestar on 2017/1/19 0019.
 */

public class DisSayAdapter extends BaseQuickAdapter<DisSay, MyViewHolder> {

    public DisSayAdapter() {
        super(R.layout.comment_adapter2, null);
    }

    @Override
    protected void convert(MyViewHolder holder, DisSay item) {
        User cuser = item.getCuser();
        holder.setSDV(R.id.img_user_comment, cuser.getHeadUrl())
                .setSDV(R.id.img_message, item.getAuthor().getHeadUrl())
                .setText(R.id.tv_name_comment, cuser.getNiname())
                .setText(R.id.tv_time_comment, item.getCreatedAt())
                .setText(R.id.tv_context, item.getComment())
                .setText(R.id.tv_user_message, item.getMessage().getM_message())
                .addOnClickListener(R.id.img_user_comment)
                .addOnClickListener(R.id.btn_reply)
                .addOnClickListener(R.id.img_message)
                .addOnClickListener(R.id.source);

    }
}
