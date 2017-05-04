package freestar.friends.adapter;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;

import freestar.friends.R;
import freestar.friends.bean.DisSay;
import freestar.friends.bean.User;
import freestar.friends.util.MyViewHolder;

/**
 * Created by freestar on 2017/1/19 0019.
 */
public class SayDisAdapter extends BaseMultiItemQuickAdapter<DisSay, MyViewHolder> {

    public SayDisAdapter() {
        super(null);
        addItemType(0, R.layout.dis_item_layout_no);
        addItemType(1, R.layout.dis_item_layout);
    }

    @Override
    protected void convert(MyViewHolder holder, DisSay item) {
        User cuser = item.getCuser();
        holder.setSDV(R.id.sdv_head, cuser.getHeadUrl())
                .setText(R.id.tv_user_name, cuser.getNiname())
                .setText(R.id.tv_data, item.getCreatedAt())
                .setText(R.id.tv_disNum, item.getComment())
                .addOnClickListener(R.id.line_reply)
                .addOnClickListener(R.id.sdv_head)
                .addOnClickListener(R.id.btn_name);

        if (holder.getItemViewType() == 1) {
            DisSay disSay = item.getComment_father_user();
            User father_user = item.getFather_user();
            holder.setText(R.id.btn_name, father_user.getNiname())
                    .setText(R.id.tv_father_comment, "    " + disSay.getComment())
                    .setText(R.id.tv_father_data, disSay.getCreatedAt());
        }
    }
}
