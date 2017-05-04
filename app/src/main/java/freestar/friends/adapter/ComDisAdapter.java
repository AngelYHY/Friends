package freestar.friends.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;

import freestar.friends.R;
import freestar.friends.bean.ArticleDis;
import freestar.friends.bean.AtlasDis;
import freestar.friends.bean.User;
import freestar.friends.util.MyViewHolder;

/**
 * 描述：
 * 作者：一颗浪星
 * 日期：2017/4/26 0026
 * github：
 */

public class ComDisAdapter extends BaseQuickAdapter<Object, MyViewHolder> {

    public ComDisAdapter(int layoutResId) {
        super(layoutResId, null);
    }

    @Override
    protected void convert(MyViewHolder holder, Object item) {
        ArticleDis articleDis;
        AtlasDis atlasDis;
        if (item instanceof ArticleDis) {
            articleDis = (ArticleDis) item;
            User cuser = articleDis.getCuser();
            holder.setSDV(R.id.img_user_comment, cuser.getHeadUrl())
                    .setSDV(R.id.img_message, articleDis.getArticle().getMainPic())
                    .setText(R.id.tv_name_comment, cuser.getNiname())
                    .setText(R.id.tv_time_comment, articleDis.getCreatedAt())
                    .setText(R.id.tv_context, articleDis.getComment())
                    .setText(R.id.tv_user_message, articleDis.getArticle().getTitle());
        } else {
            atlasDis = (AtlasDis) item;
            User cuser = atlasDis.getCuser();
            holder.setSDV(R.id.img_user_comment, cuser.getHeadUrl())
                    .setSDV(R.id.img_message, atlasDis.getAtlas().getMainPic())
                    .setText(R.id.tv_name_comment, cuser.getNiname())
                    .setText(R.id.tv_time_comment, atlasDis.getCreatedAt())
                    .setText(R.id.tv_context, atlasDis.getComment())
                    .setText(R.id.tv_user_message, atlasDis.getAtlas().getTitle());
        }
        holder.addOnClickListener(R.id.btn_reply)
                .addOnClickListener(R.id.source)
                .addOnClickListener(R.id.img_user_comment);
    }
}
