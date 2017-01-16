package freestar.friends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.Article;
import freestar.friends.bean.ItemU;
import freestar.friends.bean.User;
import freestar.friends.util.fly_heat.PeriscopeLayout;
import freestar.friends.util.recycler_and_fab.recyclerview.ArticleAdapterForRv;
import freestar.friends.util.recycler_and_fab.recyclerview.CommonAdapter;
import freestar.friends.util.recycler_and_fab.recyclerview.DividerItemDecoration;
import freestar.friends.util.recycler_and_fab.recyclerview.base.ViewHolder;
import freestar.friends.util.recycler_and_fab.recyclerview.wrapper.HeaderAndFooterWrapper;
import freestar.friends.util.status_bar.StatusBarUtil;

public class ArticleItemActivity extends AppCompatActivity {


    @Bind(R.id.ib_back)
    ImageButton ibBack;
    @Bind(R.id.ib_collect)
    ImageButton ibCollect;
    @Bind(R.id.rela_head)
    RelativeLayout relaHead;
    @Bind(R.id.rv)
    RecyclerView rv;
    @Bind(R.id.line_com)
    LinearLayout lineCom;
    @Bind(R.id.tv_disNum)
    TextView tvDisNum;
    @Bind(R.id.line_com_num)
    LinearLayout lineComNum;
    @Bind(R.id.ib_like)
    ImageButton ibLike;
    @Bind(R.id.line_bottom)
    LinearLayout lineBottom;
    @Bind(R.id.periscope)
    PeriscopeLayout periscope;
    private Article article;
    private ArticleAdapterForRv forRv;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private ArrayList<Object> list1;
    private CommonAdapter<ItemU> mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0x000000, 0);
        article = (Article) getIntent().getSerializableExtra("article");
        list1 = new ArrayList<>();
        initDate();
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
    }

    private void initDate() {
        Log.e("FreeStar", "ArticleItemActivity→→→queryL:开始查询");
//
        BmobQuery<ItemU> query = new BmobQuery<>();
        Article article1 = new Article();
        article1.setObjectId(article.getObjectId());
//        atlas.setObjectId("219fa20483");
        Log.e("FreeStar", "ArticleItemActivity→→→initDate:" + article.toString() + "---" + article.getObjectId());
        query.include("article.auther");
        query.addWhereEqualTo("article", new BmobPointer(article1));
        query.findObjects(new FindListener<ItemU>() {
            @Override
            public void done(List<ItemU> list, BmobException e) {
                Log.e("FreeStar", "ArticleItemActivity→→→done:");
                if (e == null) {
                    Log.e("FreeStar", "ArticleItemActivity→→→done:" + list.size());
                    if (list.size() > 0) {
                        for (ItemU itemU : list) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:" + itemU.toString());
                        }
                    } else {
                        Log.e("FreeStar", "ArticleItemActivity→→→done:没有数据");
                    }
                    setCAdapter(list);
                } else {

                    Log.e("FreeStar", "ArticleItemActivity→→→done:11111" + e.getMessage());
                }
            }
        });
    }

    private void setCAdapter(List<ItemU> list) {
//        mAdapter = new CommonAdapter<Article>(this, R.layout.article_layout, mDatas) {
//            @Override
//            protected void convert(ViewHolder holder, Article s, int position) {
//                Log.e("FreeStar", "PageFragmentA11111→→→convert:" + s.toString());
//                holder.setText(R.id.tv_time, s.getCreatedAt()).setSDV(R.id.sdv_mainPic, s.getMainPic()).setText(R.id.tv_like, s.getLikeNum() + "赞").setText(R.id.tv_title, s.getTitle());
//            }
//        };
        mAdapter = new CommonAdapter<ItemU>(this, R.layout.article_layout1, list) {

            @Override
            protected void convert(ViewHolder holder, ItemU itemU, int position) {
                if (itemU.isUrl()) {
                    holder.setVis(R.id.sdv_pic).setSDV(R.id.sdv_pic, itemU.getUrl());
                } else {
                    holder.setVis(R.id.tv_context).setText(R.id.tv_context, itemU.getContext());
                }
            }
        };

        //        forRv = new ArticleAdapterForRv(this, mDatas);
        initHeaderAndFooter();

        rv.setAdapter(mHeaderAndFooterWrapper);
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener<Article>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, Article article, int position) {
                Intent intent = new Intent(ArticleItemActivity.this, ArticleItemActivity.class);
                Log.e("FreeStar", "PageFragmentA11111→→→onItemClick:" + article.toString());
                intent.putExtra("article", article);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Article o, int position) {
                return false;
            }
        });
    }

    @OnClick({R.id.line_com, R.id.line_com_num, R.id.ib_like})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.line_com:
                Intent intent = new Intent(this, DiscussActivity.class);
                intent.putExtra("object", article);
                startActivity(intent);
                break;
            case R.id.line_com_num:
                Intent intent1 = new Intent(this, SearchActivity1.class);
                intent1.putExtra("object", article);
                startActivity(intent1);
                break;
            case R.id.ib_like:
                Log.e("FreeStar", "ArticleItemActivity→→→onClick:" + ibLike.isSelected());
                if (!ibLike.isSelected()) {
                    periscope.addHeart();
                    periscope.addHeart();
                    periscope.addHeart();
                    ibLike.setSelected(true);
                    Log.e("FreeStar", "ArticleItemActivity→→→onClick:" + ibLike.isSelected());
                } else {
                    ibLike.setSelected(false);
                    Log.e("FreeStar", "ArticleItemActivity→→→onClick:" + ibLike.isSelected());
                }
                break;
        }
    }

    private void initHeaderAndFooter() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(forRv);
        View view = LayoutInflater.from(this).inflate(R.layout.head_view, null);
        TextView name = (TextView) view.findViewById(R.id.tv_name);
        SimpleDraweeView head = (SimpleDraweeView) view.findViewById(R.id.sdv_head);

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArticleItemActivity.this, UserDataActivity.class);
                Log.e("FreeStar", "ArticleItemActivity→→→onClick:" + article.getAuthor().toString());
                intent.putExtra("user", article.getAuthor());
                startActivity(intent);
            }
        });
        setDate(name, head);
        mHeaderAndFooterWrapper.addHeaderView(view);
    }

    private void setDate(TextView name, SimpleDraweeView head) {
        Log.e("FreeStar", "ArticleItemActivity→→→setDate:" + article.toString());
        name.setText(article.getAuthor().getNiname());
        head.setImageURI(article.getMainPic());
    }

    @Override
    protected void onDestroy() {
        Log.e("FreeStar", "ArticleItemActivity→→→onDestroy:" + ibLike.isSelected());
        if (ibLike.isSelected()) {

            article.increment("likeNum"); // 分数递增1
            article.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    Log.e("FreeStar", "ArticleItemActivity→→→done:成功咯" + article.toString());
                }
            });
        }

        if (ibCollect.isSelected()) {

            User user = new User();
            user.setObjectId(App.userId);

            BmobRelation relation = new BmobRelation();
            relation.add(article);

            user.setCollect_atlas(relation);

            user.update(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        Log.e("FreeStar", "ArticleItemActivity→→→done:成功");
                    } else {
                        Log.e("FreeStar", "ArticleItemActivity→→→done:" + e.getMessage());
                    }
                }
            });

        }
        super.onDestroy();
    }

    @OnClick(R.id.ib_collect)
    public void setIbCollect() {
        Log.e("FreeStar", "ArticleItemActivity→→→onClick:" + ibCollect.isSelected());
        if (!ibCollect.isSelected()) {
            ibCollect.setSelected(true);
        } else {
            ibCollect.setSelected(false);
        }
    }

    @OnClick(R.id.ib_back)
    public void setIbBack() {
        finish();
    }

}
