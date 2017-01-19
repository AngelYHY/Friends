package freestar.friends.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import freestar.friends.R;
import freestar.friends.bean.Article;

/**
 * Created by Administrator on 2016/8/26 0026.
 */
public class ArticleAdapter extends BaseQuickAdapter<Article, BaseViewHolder> {

    public ArticleAdapter() {
        super(R.layout.article_layout, null);
    }

    @Override
    protected void convert(BaseViewHolder holder, Article item) {
        holder.setText(R.id.tv_time, item.getCreatedAt())
                .setText(R.id.tv_like, item.getLikeNum() + "赞")
                .setText(R.id.tv_title, item.getTitle());
        ((SimpleDraweeView) holder.getView(R.id.sdv_mainPic)).setImageURI(item.getMainPic());
    }
}
