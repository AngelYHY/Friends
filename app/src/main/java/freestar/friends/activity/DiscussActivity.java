package freestar.friends.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.Article;
import freestar.friends.bean.ArticleDis;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.AtlasDis;
import freestar.friends.bean.User;
import freestar.friends.util.status_bar.BaseActivity;

/**
 * Created by Administrator on 2016/8/21 0021.
 */
public class DiscussActivity extends BaseActivity {
    @Bind(R.id.btn_back)
    Button btnBack;
    @Bind(R.id.btn_publish)
    Button btnPublish;
    @Bind(R.id.et_reply)
    EditText etReply;
    private Object object;
    private User user;
    private String text;
    public String objectId;
    public Atlas atlas;
    private Article article;
    private User author;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {

            } else if (msg.what == 2) {

            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.discuss_layout);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        object = intent.getSerializableExtra("object");
        author = (User) intent.getSerializableExtra("user");
        Bundle extra = intent.getExtras();
        Object bundle = extra.getSerializable("bundle");
        if (bundle != null) {
            Log.e("FreeStar", "DiscussActivity→→→onCreate:执行了");
            object = bundle;
        }
    }

    @OnClick({R.id.btn_back, R.id.btn_publish})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                finish();
                break;
            case R.id.btn_publish:
                text = etReply.getText().toString().trim();
                if (TextUtils.isEmpty(text)) {
                    Toast.makeText(DiscussActivity.this, "写点东西吧", Toast.LENGTH_SHORT).show();
                } else {
                    user = new User();
                    user.setObjectId(App.userId);
                    if (object instanceof Atlas) {
                        atlas = (Atlas) object;
                        Log.e("FreeStar", "DiscussActivity→→→onClick:" + atlas.toString());
                        //添加一条对图集的评论的记录
                        addInfo(1);
                    } else if (object instanceof Article) {
                        Log.e("FreeStar", "DiscussActivity→→→onClick:" + ((Article) object).getObjectId());
                        article = (Article) object;
                        Log.e("FreeStar", "DiscussActivity→→→onClick:" + article.toString());
                        //添加一条对摄论的评论的记录
                        addInfoA(1);
                    } else if (object instanceof AtlasDis) {
                        Log.e("FreeStar", "DiscussActivity→→→onClick:对图集中的回复进行回复");
                        //先把这条记录添加到表中 对图集中的回复进行回复
                        addInfo(2);
                    } else if (object instanceof ArticleDis) {
                        addInfoA(2);
                    }
                }
                break;
        }
    }

    private void addInfoA(int i) {
        ArticleDis articleDis = null;
        Intent intent = null;
        if (i == 1) {
            updateDisNumA();
            articleDis = new ArticleDis(user, text, article, article.getAuthor());
            intent = new Intent();
            intent.putExtra("OK", "YESA");
            //评论数+1
        } else if (i == 2) {
//            Log.e("FreeStar", "DiscussActivity→→→addInfo:" + ((AtlasDis) object).getAtlas().toString());
            articleDis = new ArticleDis(user, (ArticleDis) object, text, ((ArticleDis) object).getArticle(), ((ArticleDis) object).getCuser(), ((ArticleDis) object).getCuser());
            intent = new Intent();
            intent.putExtra("OK", "fresh");
        }
        assert articleDis != null;
        articleDis.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "DiscussActivity→→→done:成功记录 id 为 " + s);
                    objectId = s;
                } else {
                    Log.e("FreeStar", "DiscussActivity→→→done:" + e.getMessage());
                }
            }
        });
        setResult(RESULT_OK, intent);
        DiscussActivity.this.finish();
    }

    private void updateDisNumA() {
        article.increment("disNum"); // 评论数递增1
//        Log.e("FreeStar", "DiscussActivity→→→updateDisNum:" + atlas.toString());
        article.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "PicItemActivity→→→done:成功咯");
                } else {
                    Log.e("FreeStar", "DiscussActivity→→→done:" + e.getMessage());
                }
            }
        });
    }

    public void addInfo(final int i) {
        AtlasDis atlasDis = null;
        Intent intent = null;
        if (i == 1) {
            updateDisNum();
            atlasDis = new AtlasDis(user, text, atlas, atlas.getAuthor());
            intent = new Intent();
            intent.putExtra("OK", "YES");
            //评论数+1
        } else if (i == 2) {
            Log.e("FreeStar", "DiscussActivity→→→addInfo:" + ((AtlasDis) object).getAtlas().toString());
            atlasDis = new AtlasDis(user, (AtlasDis) object, text, ((AtlasDis) object).getAtlas(), ((AtlasDis) object).getCuser(), ((AtlasDis) object).getCuser());
            intent = new Intent();
            intent.putExtra("OK", "fresh");
        }
        assert atlasDis != null;
        atlasDis.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "DiscussActivity→→→done:成功 记录id为  " + s);
                    objectId = s;
                } else {
                    Log.e("FreeStar", "DiscussActivity→→→done:" + e.getMessage());
                }
            }
        });
        setResult(RESULT_OK, intent);
        DiscussActivity.this.finish();
    }

    private void updateDisNum() {
        atlas.increment("disNum"); // 评论数递增1
        Log.e("FreeStar", "DiscussActivity→→→updateDisNum:" + atlas.toString());
        atlas.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "PicItemActivity→→→done:成功咯");
                } else {
                    Log.e("FreeStar", "DiscussActivity→→→done:" + e.getMessage());
                }
            }
        });
    }
}
