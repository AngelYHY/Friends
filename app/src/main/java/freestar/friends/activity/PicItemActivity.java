package freestar.friends.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
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
import freestar.friends.adapter.PicAdapter;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.CollectAtlas;
import freestar.friends.bean.LikeAtlas;
import freestar.friends.bean.Photoes;
import freestar.friends.bean.User;
import freestar.friends.util.fly_heat.PeriscopeLayout;
import freestar.friends.util.status_bar.StatusBarUtil;

/**
 * Created by Administrator on 2016/8/21 0021.
 */
public class PicItemActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private static final int RESC = 5;
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
    @Bind(R.id.ib_collect)
    ImageButton ibCollect;
    @Bind(R.id.rela_head)
    RelativeLayout relaHead;
    @Bind(R.id.context)
    TextView context;
    @Bind(R.id.scrollView)
    ScrollView scrollView;
    @Bind(R.id.tv_picNum)
    TextView tvPicNum;
    @Bind(R.id.vp_pic)
    ViewPager vpPic;
    @Bind(R.id.ib_back)
    ImageButton ibBack;
    @Bind(R.id.sdv_head)
    SimpleDraweeView sdvHead;
    @Bind(R.id.tv_name)
    TextView tvName;
    private ArrayList<String> list1;
    private PicAdapter picAdapter;
    private Atlas atlas;
    private User user;
    private CollectAtlas collectAtlas;
    private LikeAtlas likeAtlas;
    private ArrayList<Photoes> mDate = new ArrayList<>();
    boolean starLike = false;
    boolean endLike = false;
    boolean startColl = false;
    boolean endColl = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pic_layout);
        StatusBarUtil.setColor(this, 0x000000, 0);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        atlas = (Atlas) intent.getSerializableExtra("atlas");
        User fuser = (User) intent.getSerializableExtra("user");
        list1 = new ArrayList<>();
        user = new User();
        user.setObjectId(App.userId);
        queryState();
        queryPic();
        if (fuser != null) {
            sdvHead.setImageURI(fuser.getHeadUrl());
            tvName.setText(fuser.getNiname());
        } else {
            tvName.setText(atlas.getAuthor().getNiname());
            sdvHead.setImageURI(atlas.getAuthor().getHeadUrl());
        }
        if (atlas.getDisNum() == null) {
            tvDisNum.setText("0");
        } else {
            tvDisNum.setText("" + atlas.getDisNum());
        }
        vpPic.addOnPageChangeListener(this);
    }

    private void queryPic() {
        BmobQuery<Photoes> query = new BmobQuery<>();
        query.include("atlas.author");
        query.addWhereEqualTo("atlas", new BmobPointer(atlas));
        query.findObjects(new FindListener<Photoes>() {
            @Override
            public void done(List<Photoes> list, BmobException e) {
                mDate.addAll(list);
                if (e == null) {
                    if (list.size() > 0) {
                        for (Photoes photoes : list) {
                            list1.add(photoes.getUrl());
                        }
                    } else {
                        Log.e("FreeStar", "MainActivity→→→done:没有数据");
                    }
                    picAdapter = new PicAdapter(PicItemActivity.this, list1);
                    vpPic.setAdapter(picAdapter);
                    tvPicNum.setText("1/" + list1.size());

                } else {
                    Log.e("FreeStar", "MainActivity→→→done:" + e.getMessage());
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
                intent.putExtra("object", atlas);
                startActivityForResult(intent, RESC);
                break;
            case R.id.line_com_num:
                intent = new Intent(this, DiscussAtlasDetailActivity.class);
                intent.putExtra("object", atlas);
                startActivityForResult(intent, RESC);
                break;
            case R.id.ib_like:
                if (!ibLike.isSelected()) {
                    periscope.addHeart();
                    periscope.addHeart();
                    periscope.addHeart();
                    ibLike.setSelected(true);
                } else {
                    ibLike.setSelected(false);
                }
                break;
        }
    }

    @Override
    protected void onDestroy() {
        endColl = ibCollect.isSelected();
        endLike = ibLike.isSelected();
        //开始状态和结束状态 不一致时 进行操作
        if (starLike != endLike) {
            if (endLike) {
                atlas.increment("likeNum"); // 点赞数增1
                atlas.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:点赞成功+1");
                        }
                    }
                });
                LikeAtlas like = new LikeAtlas(user, atlas);
                like.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:点赞记录生成");
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:失败" + e.getMessage());
                        }
                    }
                });

            } else {
                atlas.increment("likeNum", -1); // 点赞数减1
                atlas.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:点赞-1");
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:失败" + e.getMessage());
                        }
                    }
                });

                LikeAtlas la = new LikeAtlas();
                la.setObjectId(likeAtlas.getObjectId());
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
                CollectAtlas collectAtlas = new CollectAtlas(user, atlas);
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
                CollectAtlas article1 = new CollectAtlas();
                article1.setObjectId(collectAtlas.getObjectId());
                article1.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:删除记录成功");
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:删除收藏记录失败");
                        }
                    }
                });

            }
        }

        super.onDestroy();
    }

    @OnClick(R.id.ib_collect)
    public void setIbCollect() {
        if (!ibCollect.isSelected()) {
            ibCollect.setSelected(true);
        } else {
            ibCollect.setSelected(false);
        }
    }

    private void queryState() {
        BmobQuery<CollectAtlas> query = new BmobQuery<>();
        query.addWhereEqualTo("user", user);
        query.include("sources");
        query.findObjects(new FindListener<CollectAtlas>() {
            @Override
            public void done(List<CollectAtlas> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            CollectAtlas collectA = list.get(i);
                            if (collectA.getSources().getObjectId().equals(atlas.getObjectId())) {
                                ibCollect.setSelected(true);
                                startColl = true;
                                collectAtlas = collectA;
                            }
                        }
                    }

                }

            }
        });

        BmobQuery<LikeAtlas> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user", user);
        query.include("source");
        query1.findObjects(new FindListener<LikeAtlas>() {
            @Override
            public void done(List<LikeAtlas> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            LikeAtlas likeA = list.get(i);
                            Log.e("FreeStar", "ArticleItemActivity→→→done:" + likeA.getObjectId());
                            if (likeA.getSource().getObjectId().equals(atlas.getObjectId())) {
                                Log.e("FreeStar", "MainActivity→→→done:" + likeA.getObjectId());
                                Log.e("FreeStar", "ArticleItemActivity→→→done:已点赞");
                                ibLike.setSelected(true);
                                starLike = true;
                                likeAtlas = likeA;
                                Log.e("FreeStar", "ArticleItemActivity→→→done:" + likeA.getObjectId());
                            }
                        }
                    }
                }
            }
        });
    }

    @OnClick(R.id.ib_back)
    public void setIbBack() {
        finish();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        tvPicNum.setText(position + 1 + "/" + list1.size());
        context.setText(mDate.get(position).getContext());
    }

    @Override
    public void onPageScrollStateChanged(int state) {
    }

    @OnClick(R.id.sdv_head)
    public void onClick() {
        Intent intent = new Intent(this, UserDataActivity.class);
        intent.putExtra("user", atlas.getAuthor());
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESC && resultCode == RESULT_OK) {
            String back = data.getStringExtra("OK");
            if (back.equals("YES")) {
                Log.e("FreeStar", "PicItemActivity→→→onActivityResult:pic 成功");
                int i = Integer.parseInt(tvDisNum.getText() + "") + 1;
                tvDisNum.setText(i + "");
            }
        }
    }
}
