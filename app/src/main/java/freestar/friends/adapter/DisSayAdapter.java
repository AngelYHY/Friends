package freestar.friends.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import freestar.friends.R;
import freestar.friends.bean.DisSay;
import freestar.friends.bean.User;

/**
 * Created by freestar on 2017/1/19 0019.
 */

public class DisSayAdapter extends BaseQuickAdapter<DisSay, BaseViewHolder> {

    public DisSayAdapter() {
        super(R.layout.comment_adapter2, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, DisSay item) {
        User cuser = item.getCuser();
        holder.setText(R.id.tv_name_comment, cuser.getNiname())
                .setText(R.id.tv_time_comment, item.getCreatedAt())
                .setText(R.id.tv_context, item.getComment())
                .setText(R.id.tv_user_message, item.getMessage().getM_message())
                .addOnClickListener(R.id.img_user_comment)
                .addOnClickListener(R.id.btn_reply)
                .addOnClickListener(R.id.img_message)
                .addOnClickListener(R.id.source);

        ((SimpleDraweeView) holder.getView(R.id.img_user_comment)).setImageURI(cuser.getHeadUrl());
        ((SimpleDraweeView) holder.getView(R.id.img_message)).setImageURI(item.getAuthor().getHeadUrl());

    }
}
