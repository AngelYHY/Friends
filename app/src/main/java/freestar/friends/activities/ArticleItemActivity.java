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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.adapter.ArticleDetailAdapter;
import freestar.friends.bean.Article;
import freestar.friends.bean.CollectArticle;
import freestar.friends.bean.ItemU;
import freestar.friends.bean.LikeArticle;
import freestar.friends.bean.User;
import freestar.friends.util.fly_heat.PeriscopeLayout;
import freestar.friends.util.status_bar.StatusBarUtil;

public class ArticleItemActivity extends AppCompatActivity implements View.OnClickListener {

    @Bind(R.id.ib_back)
    ImageButton ibBack;
    @Bind(R.id.ib_collect)
    ImageButton ibCollect;
    @Bind(R.id.rela_head)
    RelativeLayout relaHead;
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
    @Bind(R.id.rv)
    RecyclerView mRecyclerView;
    @Bind(R.id.periscope)
    PeriscopeLayout periscope;
    private Article article;
    private ArticleDetailAdapter mAdapter;
    private List<String> urlList;
    private static final int RESC = 6;
    boolean starLike = false;
    boolean endLike = false;
    boolean startColl = false;
    boolean endColl = false;
    private User user;
    private CollectArticle collectArticle;
    private LikeArticle likeArticle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);
        ButterKnife.bind(this);
        StatusBarUtil.setColor(this, 0x000000, 0);

        Intent intent = getIntent();
        article = (Article) intent.getSerializableExtra("article");
        User fuser = (User) intent.getSerializableExtra("user");
        Log.e("FreeStar", "ArticleItemActivity→→→onCreate:" + fuser);
        urlList = article.getUrlList();
        Log.e("FreeStar", "ArticleItemActivity55555555→→→onCreate:" + urlList.toString());

        this.user = new User();
        this.user.setObjectId(App.userId);
        Log.e("FreeStar", "ArticleItemActivity→→→onCreate:用户id" + App.userId + "88888");

        View headView = LayoutInflater.from(this).inflate(R.layout.head_view, null);
//        mRecyclerView.addHeaderView(headView);

        initRV(headView);

        initDate();

        //查询当前用户是否是已经收藏或者点过赞
        queryState();
        tvDisNum.setText(String.valueOf(article.getDisNum()));
        TextView name = (TextView) headView.findViewById(R.id.tv_name);
        SimpleDraweeView head = (SimpleDraweeView) headView.findViewById(R.id.head_article);
        TextView title = (TextView) headView.findViewById(R.id.title);
        Log.e("FreeStar", "ArticleItemActivity→→→setDate:" + article.toString());
        title.setText(article.getTitle());
        Log.e("FreeStar", "ArticleItemActivity→→→setDate:" + article.getAuthor().getHeadUrl());

        if (fuser != null) {
            head.setImageURI(fuser.getHeadUrl());
            name.setText(fuser.getNiname());
            Log.e("FreeStar", "ArticleItemActivity→→→onCreate:不为空");
        } else {
            name.setText(article.getAuthor().getNiname());
            Log.e("FreeStar", "ArticleItemActivity→→→onCreate:为空");
            head.setImageURI(article.getAuthor().getHeadUrl());
        }
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArticleItemActivity.this, UserDataActivity.class);
                Log.e("FreeStar", "ArticleItemActivity→→→onClick:" + article.getAuthor().toString());
                intent.putExtra("user", article.getAuthor());
                startActivity(intent);
            }
        });

    }

    private void initRV(View headView) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ArticleDetailAdapter();
        mAdapter.addHeaderView(headView);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void queryState() {

        BmobQuery<CollectArticle> query = new BmobQuery<>();
        query.include("sources");
        query.addWhereEqualTo("user", user);
        query.findObjects(new FindListener<CollectArticle>() {
            @Override
            public void done(List<CollectArticle> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "PicItemActivity→→→done:收藏记录为" + list.size());
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            CollectArticle collectA = list.get(i);
                            Log.e("FreeStar", "ArticleItemActivity→→→done:" + collectA.getObjectId());
                            if (collectA.getSources().getObjectId().equals(article.getObjectId())) {
                                Log.e("FreeStar", "MainActivity→→→done:" + collectA.getObjectId());
                                Log.e("FreeStar", "ArticleItemActivity→→→done:已点赞");
                                ibCollect.setSelected(true);
                                startColl = true;
                                collectArticle = collectA;
                                Log.e("FreeStar", "ArticleItemActivity→→→done:" + collectA.getObjectId());
                            }
                        }
                    }

                }


            }
        });

        BmobQuery<LikeArticle> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user", user);
        query1.include("source");
        query1.findObjects(new FindListener<LikeArticle>() {
            @Override
            public void done(List<LikeArticle> list, BmobException e) {
                Log.e("FreeStar", "ArticleItemActivity→→→done:" + list.size() + "----1111");
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            LikeArticle likeA = list.get(i);
                            Log.e("FreeStar", "ArticleItemActivity→→→done:" + likeA.getObjectId());
                            if (likeA.getSource().getObjectId().equals(article.getObjectId())) {
                                Log.e("FreeStar", "MainActivity→→→done:" + likeA.getObjectId());
                                Log.e("FreeStar", "ArticleItemActivity→→→done:已点赞");
                                ibLike.setSelected(true);
                                starLike = true;
                                likeArticle = likeA;
                                Log.e("FreeStar", "ArticleItemActivity→→→done:" + likeA.getObjectId());
                            }
                        }
                    }

                } else {
                    Log.e("FreeStar", "ArticleItemActivity→→→done:" + e.getMessage());
                }
            }
        });
    }

    private void initDate() {
        Log.e("FreeStar", "ArticleItemActivity→→→queryL:开始查询");
        BmobQuery<ItemU> query = new BmobQuery<>();
        Article article1 = new Article();
        article1.setObjectId(article.getObjectId());
        Log.e("FreeStar", "ArticleItemActivity→→→initDate:" + article.toString() + "---" + article.getObjectId());
        query.include("article.auther");
        query.order("order");
        query.addWhereEqualTo("article", new BmobPointer(article1));
        query.findObjects(new FindListener<ItemU>() {
            @Override
            public void done(List<ItemU> list, BmobException e) {
                Log.e("FreeStar", "ArticleItemActivity→→→done:");
                if (e == null) {
                    int j = 0;
                    //原先存储的是手机地址，改为网络地址
                    for (ItemU itemU : list) {
                        if (itemU.isUrl()) {
                            itemU.setUrl(urlList.get(j++));
                        }
                    }
                    mAdapter.setNewData(list);
                } else {
                    Log.e("FreeStar", "ArticleItemActivity→→→done:55555" + e.getMessage());
                }
            }
        });
    }

    @OnClick({R.id.line_com, R.id.line_com_num, R.id.ib_like})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.line_com:
                intent = new Intent(this, DiscussActivity.class);
                intent.putExtra("object", article);
                startActivityForResult(intent, RESC);
                break;
            case R.id.line_com_num:
                intent = new Intent(this, DiscussArticleDetailActivity.class);
                intent.putExtra("object", article);
                startActivityForResult(intent, RESC);
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

    @Override
    protected void onDestroy() {
        endColl = ibCollect.isSelected();
        endLike = ibLike.isSelected();
        Log.e("FreeStar", "ArticleItemActivity→→→onDestroy:endLike" + endLike);
        Log.e("FreeStar", "ArticleItemActivity→→→onDestroyend:Coll" + endColl);
        //开始状态和结束状态 不一致时 进行操作
        if (starLike != endLike) {
            if (endLike) {
                article.increment("likeNum"); // 点赞数增1
                article.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:点赞成功+1");
                        }
                    }
                });
                LikeArticle like = new LikeArticle(user, article);
                like.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:点赞记录生成" + s);
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:失败" + e.getMessage());
                        }
                    }
                });

            } else {
                article.increment("likeNum", -1); // 点赞数减1
                article.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:点赞-1");
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:失败" + e.getMessage());
                        }
                    }
                });

                LikeArticle la = new LikeArticle();
                la.setObjectId(likeArticle.getObjectId());
                la.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:ok+删除记录");
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:删除失败" + e.getMessage());
                        }
                    }
                });

            }
        }

        //开始收藏状态和结束状态不一致时改变
        if (startColl != endColl) {
            if (endColl) {
                CollectArticle collectAtlas = new CollectArticle(user, article);
                collectAtlas.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:生成收藏记录" + s);
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:收藏失败");
                        }
                    }
                });

            } else {
                CollectArticle article1 = new CollectArticle();
                Log.e("FreeStar", "ArticleItemActivity→→→onDestroy:记录id为" + collectArticle.getObjectId());
                article1.setObjectId(collectArticle.getObjectId());
                article1.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:删除记录成功");
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:收藏记录失败");
                        }
                    }
                });

            }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("FreeStar", "ArticleItemActivity→→→onActivityResult:");
        if (requestCode == RESC && resultCode == RESULT_OK) {
            String back = data.getStringExtra("OK");
            if (back.equals("YESA")) {
                Log.e("FreeStar", "ArticleItemActivity→→→onActivityResult:进来了");
                int i = Integer.parseInt(tvDisNum.getText().toString()) + 1;
                tvDisNum.setText(String.valueOf(i));
            }
        }
    }
}
