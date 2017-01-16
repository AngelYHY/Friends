package freestar.friends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import freestar.friends.adapter.AAdapter;
import freestar.friends.bean.Article;
import freestar.friends.bean.CollectArticle;
import freestar.friends.bean.ItemU;
import freestar.friends.bean.LikeArticle;
import freestar.friends.bean.User;
import freestar.friends.util.fly_heat.PeriscopeLayout;
import freestar.friends.util.status_bar.StatusBarUtil;

public class ArticleItemActivity1 extends AppCompatActivity implements View.OnClickListener {


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
    ListView lv;
    @Bind(R.id.periscope)
    PeriscopeLayout periscope;
    private Article article;
    private AAdapter mAdapter;
    private TextView name;
    private SimpleDraweeView head;
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
        Log.e("FreeStar", "ArticleItemActivity1→→→onCreate:" + fuser);
        urlList = article.getUrlList();
        this.user = new User();
        this.user.setObjectId(App.userId);
        Log.e("FreeStar", "ArticleItemActivity1→→→onCreate:用户id" + App.userId + "88888");
        View view = LayoutInflater.from(this).inflate(R.layout.head_view, null);
        lv.addHeaderView(view);
        initDate();
        //查询当前用户是否是已经收藏或者点过赞
        queryState();
        tvDisNum.setText(article.getDisNum() + "");
        name = (TextView) view.findViewById(R.id.tv_name);
        head = (SimpleDraweeView) view.findViewById(R.id.head_article);
        TextView title = (TextView) view.findViewById(R.id.title);
        Log.e("FreeStar", "ArticleItemActivity→→→setDate:" + article.toString());
        title.setText(article.getTitle());
        Log.e("FreeStar", "ArticleItemActivity1→→→setDate:" + article.getAuthor().getHeadUrl());

        if (fuser != null) {
            head.setImageURI(fuser.getHeadUrl());
            name.setText(fuser.getNiname());
            Log.e("FreeStar", "ArticleItemActivity1→→→onCreate:不为空");
        } else {
            name.setText(article.getAuthor().getNiname());
            Log.e("FreeStar", "ArticleItemActivity1→→→onCreate:为空");
            head.setImageURI(article.getAuthor().getHeadUrl());
        }
        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ArticleItemActivity1.this, UserDataActivity.class);
                Log.e("FreeStar", "ArticleItemActivity→→→onClick:" + article.getAuthor().toString());
                intent.putExtra("user", article.getAuthor());
                startActivity(intent);
            }
        });

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
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:" + collectA.getObjectId());
                            if (collectA.getSources().getObjectId().equals(article.getObjectId())) {
                                Log.e("FreeStar", "MainActivity→→→done:" + collectA.getObjectId());
                                Log.e("FreeStar", "ArticleItemActivity1→→→done:已点赞");
                                ibCollect.setSelected(true);
                                startColl = true;
                                collectArticle = collectA;
                                Log.e("FreeStar", "ArticleItemActivity1→→→done:" + collectA.getObjectId());
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
                Log.e("FreeStar", "ArticleItemActivity1→→→done:" + list.size() + "----1111");
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            LikeArticle likeA = list.get(i);
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:" + likeA.getObjectId());
                            if (likeA.getSource().getObjectId().equals(article.getObjectId())) {
                                Log.e("FreeStar", "MainActivity→→→done:" + likeA.getObjectId());
                                Log.e("FreeStar", "ArticleItemActivity1→→→done:已点赞");
                                ibLike.setSelected(true);
                                starLike = true;
                                likeArticle = likeA;
                                Log.e("FreeStar", "ArticleItemActivity1→→→done:" + likeA.getObjectId());
                            }
                        }
                    }

                } else {
                    Log.e("FreeStar", "ArticleItemActivity1→→→done:" + e.getMessage());
                }
            }
        });
    }

    private void initDate() {
        Log.e("FreeStar", "ArticleItemActivity1→→→queryL:开始查询");
        BmobQuery<ItemU> query = new BmobQuery<>();
        Article article1 = new Article();
        article1.setObjectId(article.getObjectId());
        Log.e("FreeStar", "ArticleItemActivity1→→→initDate:" + article.toString() + "---" + article.getObjectId());
        query.include("article.auther");
        query.order("order");
        query.addWhereEqualTo("article", new BmobPointer(article1));
        query.findObjects(new FindListener<ItemU>() {
            @Override
            public void done(List<ItemU> list, BmobException e) {
                Log.e("FreeStar", "ArticleItemActivity1→→→done:");
                if (e == null) {
                    Log.e("FreeStar", "ArticleItemActivity1→→→done:" + list.size());
                    if (list.size() > 0) {
                        for (ItemU itemU : list) {
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:" + itemU.toString());
                        }
                    } else {
                        Log.e("FreeStar", "ArticleItemActivity1→→→done:没有数据");
                    }
                    int j = 0;
                    for (ItemU itemU : list) {
                        if (itemU.isUrl()) {
                            itemU.setUrl(urlList.get(j++));
                        }
                    }
                    setCAdapter(list);

                } else {

                    Log.e("FreeStar", "ArticleItemActivity1→→→done:55555" + e.getMessage());
                }
            }
        });
    }

    private void setCAdapter(List<ItemU> list) {
        Log.e("FreeStar", "ArticleItemActivity1→→→setCAdapter:");
        mAdapter = new AAdapter(list, ArticleItemActivity1.this);
        lv.setAdapter(mAdapter);
    }

    @OnClick({R.id.line_com, R.id.line_com_num, R.id.ib_like})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.line_com:
                Intent intent = new Intent(this, DiscussActivity.class);
                intent.putExtra("object", article);
//                startActivity(intent);
                startActivityForResult(intent, RESC);
                break;
            case R.id.line_com_num:
                Intent intent1 = new Intent(this, SearchActivity2.class);
                intent1.putExtra("object", article);
//                startActivity(intent1);
                startActivityForResult(intent1, RESC);
                break;
            case R.id.ib_like:
                Log.e("FreeStar", "ArticleItemActivity1→→→onClick:" + ibLike.isSelected());
                if (!ibLike.isSelected()) {
                    periscope.addHeart();
                    periscope.addHeart();
                    periscope.addHeart();
                    ibLike.setSelected(true);
                    Log.e("FreeStar", "ArticleItemActivity1→→→onClick:" + ibLike.isSelected());
                } else {
                    ibLike.setSelected(false);
                    Log.e("FreeStar", "ArticleItemActivity1→→→onClick:" + ibLike.isSelected());
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        endColl = ibCollect.isSelected();
        endLike = ibLike.isSelected();
        Log.e("FreeStar", "ArticleItemActivity1→→→onDestroy:endLike" + endLike);
        Log.e("FreeStar", "ArticleItemActivity1→→→onDestroyend:Coll" + endColl);
        //开始状态和结束状态 不一致时 进行操作
        if (starLike != endLike) {
            if (endLike) {
                article.increment("likeNum"); // 点赞数增1
                article.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:点赞成功+1");
                        }
                    }
                });
                LikeArticle like = new LikeArticle(user, article);
                like.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:点赞记录生成" + s);
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:失败" + e.getMessage());
                        }
                    }
                });

            } else {
                article.increment("likeNum", -1); // 点赞数减1
                article.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:点赞-1");
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:失败" + e.getMessage());
                        }
                    }
                });

                LikeArticle la = new LikeArticle();
                la.setObjectId(likeArticle.getObjectId());
                la.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:ok+删除记录");
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:删除失败" + e.getMessage());
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
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:生成收藏记录" + s);
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:收藏失败");
                        }
                    }
                });

            } else {
                CollectArticle article1 = new CollectArticle();
                Log.e("FreeStar", "ArticleItemActivity1→→→onDestroy:记录id为" + collectArticle.getObjectId());
                article1.setObjectId(collectArticle.getObjectId());
                article1.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:删除记录成功");
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity1→→→done:收藏记录失败");
                        }
                    }
                });

            }
        }

        super.onDestroy();
    }

    @OnClick(R.id.ib_collect)
    public void setIbCollect() {
        Log.e("FreeStar", "ArticleItemActivity1→→→onClick:" + ibCollect.isSelected());
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
        Log.e("FreeStar", "ArticleItemActivity1→→→onActivityResult:");
        if (requestCode == RESC && resultCode == RESULT_OK) {
            String back = data.getStringExtra("OK");
            if (back.equals("YESA")) {
                Log.e("FreeStar", "ArticleItemActivity1→→→onActivityResult:进来了");
                int i = Integer.parseInt(tvDisNum.getText() + "") + 1;
                tvDisNum.setText(i + "");
            }
        }
    }
}
