package freestar.friends.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import freestar.friends.R;
import freestar.friends.bean.AtlasDis;
import freestar.friends.bean.User;

/**
 * Created by freestar on 2017/1/18 0018.
 */
public class DiscussAtlasDetailAdapter extends BaseMultiItemQuickAdapter<AtlasDis, BaseViewHolder> {

    public DiscussAtlasDetailAdapter() {
        super(null);
        addItemType(0, R.layout.dis_item_layout_no);
        addItemType(1, R.layout.dis_item_layout);
    }

    @Override
    protected void convert(BaseViewHolder holder, AtlasDis item) {
        User cuser = item.getCuser();
        holder.setText(R.id.tv_user_name, cuser.getNiname())
                .setText(R.id.tv_data, item.getCreatedAt())
                .setText(R.id.tv_disNum, item.getComment())
                .addOnClickListener(R.id.line_reply)
                .addOnClickListener(R.id.sdv_head)
                .addOnClickListener(R.id.btn_name);

        ((SimpleDraweeView) holder.getView(R.id.sdv_head)).setImageURI(cuser.getHeadUrl());
        if (holder.getItemViewType() == 1) {
            AtlasDis atlasDis = item.getComment_father_user();
            User father_user = item.getFather_user();
            holder.setText(R.id.btn_name, father_user.getNiname())
                    .setText(R.id.tv_father_comment, "    " + atlasDis.getComment())
                    .setText(R.id.tv_father_data, atlasDis.getCreatedAt());
        }

    }
}
