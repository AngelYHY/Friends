package freestar.friends.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;

import freestar.friends.R;
import freestar.friends.bean.Article;
import freestar.friends.util.MyViewHolder;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class ArticleAdapter extends BaseQuickAdapter<Article, MyViewHolder> {

    public ArticleAdapter() {
        super(R.layout.article_layout, null);
    }

    @Override
    protected void convert(MyViewHolder holder, Article item) {
        holder.setSDV(R.id.sdv_mainPic, item.getMainPic())
                .setText(R.id.tv_time, item.getCreatedAt())
                .setText(R.id.tv_like, item.getLikeNum() + "赞")
                .setText(R.id.tv_title, item.getTitle());
    }
}
