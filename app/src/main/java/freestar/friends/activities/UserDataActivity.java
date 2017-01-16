package freestar.friends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.util.profile.CollectActivity1;
import freestar.friends.util.profile.ConcernActivity1;
import freestar.friends.util.profile.TypeActivity1;
import freestar.friends.util.status_bar.StatusBarUtil;
import io.rong.imkit.RongIM;

public class UserDataActivity extends AppCompatActivity {
    @Bind(R.id.img_back_userData)
    ImageView img_back;
    @Bind(R.id.toolbar_userData)
    Toolbar toolbarUserData;
    @Bind(R.id.img_userData)
    SimpleDraweeView imgUser;
    @Bind(R.id.tv_niname_userData)
    TextView tvNiname;
    @Bind(R.id.tv_user_userData)
    TextView tvUser;
    @Bind(R.id.tv_sign_userData)
    TextView tvSign;
    @Bind(R.id.tv_kongjian_userData)
    TextView tvKongjian;
    @Bind(R.id.btn_add_userData)
    Button btnAdd;
    @Bind(R.id.textView10)
    TextView textView10;
    @Bind(R.id.textView11)
    TextView textView11;
    @Bind(R.id.textView12)
    TextView textView12;
    @Bind(R.id.textView13)
    TextView textView13;
    @Bind(R.id.relativeLayout2)
    RelativeLayout relativeLayout2;
    @Bind(R.id.relativeLayout3)
    RelativeLayout relativeLayout3;
    @Bind(R.id.linearLayout2)
    LinearLayout linearLayout2;
    @Bind(R.id.btn_message_userData)
    Button btnMessageUserData;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_data);
        StatusBarUtil.setColor(this, 0xca104b, 0);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        Log.e("FreeStar", "UserDataActivity→→→onCreate:" + user.toString());
        initData();
    }

    private void initData() {

        if (user.getHeadUrl() == null) {

            imgUser.setBackgroundResource(R.mipmap.ic_launcher);
        } else {
            imgUser.setImageURI(user.getHeadUrl());
        }

        if (user.getPhoneNum() == null) {
            tvUser.setText("账号异常");
        } else {
            tvUser.setText(user.getPhoneNum());
        }

        if (user.getPersSign() == null) {
            tvSign.setText("主人很任性，不喜欢写字");
        } else {
            tvSign.setText(user.getPersSign());
        }


        if (user.getNiname() == null) {
            tvNiname.setText("昵称");
        } else {
            tvNiname.setText(user.getNiname());
        }


    }


    private void add() {
        //当前登录App的用户
        User user1 = new User();
        user1.setObjectId(App.userId);  //这个是我的id
        Log.e("登录的id", "登录的id" + App.userId);

        //这是好友 列表中这个人的好友将会多一个
        User friend = new User();
        friend.setObjectId(user.getObjectId());   //这个是佳兴的id
        Log.e("点击对应的id", "点击对应的id" + user.getObjectId());

        //将当前用户添加到Friend表中的friend字段值中，表明当前用户和用户之间是好友关系
        BmobRelation relation = new BmobRelation();
        //将   这个用户   添加到多对多关联中
        relation.add(friend);
        user1.setUser(relation);
//        user1.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//                if (e == null) {
//                    Toast.makeText(UserDataActivity.this, "关注好友成功", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(UserDataActivity.this, "已经关注过好友", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        user1.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(UserDataActivity.this, "关注好友成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(UserDataActivity.this, "已经关注过好友", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @OnClick({R.id.img_back_userData, R.id.btn_add_userData, R.id.btn_message_userData})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back_userData:
                finish();
                break;
            //添加好友
            case R.id.btn_add_userData:
                if (user.getObjectId().equals(App.userId)) {
                    Toast.makeText(UserDataActivity.this, "您不能关注自己哦", Toast.LENGTH_SHORT).show();
                } else {
                    add();
                }
                break;
            case R.id.btn_message_userData:
                String name = user.getNiname();
                String id = user.getPhoneNum();
                RongIM.getInstance().startPrivateChat(UserDataActivity.this, id, name);

        }
    }

    public void tatypeClick(View view) {
        if (user.isTypeswitch()) {
            Intent intent = new Intent(UserDataActivity.this, TypeActivity1.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("user", user);
//            intent.putExtras(bundle);
            intent.putExtra("user", user);
            startActivity(intent);
        } else {
            Toast.makeText(UserDataActivity.this, "主人未公开他的发布哦", Toast.LENGTH_SHORT).show();
        }
//        Follow friend = new Follow();
//        //friend.setObjectId(user.getObjectId());
//        final BmobQuery<User> bmobQuery = new BmobQuery<User>();
//        bmobQuery.getObject(user.getObjectId(), new QueryListener<User>() {
//            @Override
//            public void done(User user, BmobException e) {
//                if (e == null) {
//                    if (user.isTypeswitch()) {
//                        Intent intent = new Intent(UserDataActivity.this, TaTypeActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("user", user);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(UserDataActivity.this, "未公开", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//
//                }
//            }
//        });

    }
    public void tacollectClick(View view) {


        if (user.isCollectswitch()) {
            Intent intent = new Intent(UserDataActivity.this, CollectActivity1.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("user", user);
//            intent.putExtras(bundle);
            intent.putExtra("user", user);
            startActivity(intent);
        } else {
            Toast.makeText(UserDataActivity.this, "主人未公开他的收藏哦", Toast.LENGTH_SHORT).show();
        }
//
//        Follow friend = new Follow();
//        //friend.setObjectId(user.getObjectId());
//        final BmobQuery<User> bmobQuery = new BmobQuery<User>();
//        bmobQuery.getObject(user.getObjectId(), new QueryListener<User>() {
//            @Override
//            public void done(User user, BmobException e) {
//                if (e == null) {
//                    if (user.isTypeswitch()) {
//                        Intent intent = new Intent(UserDataActivity.this, TaTypeActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("user", user);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(UserDataActivity.this, "未公开", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//
//                }
//            }
//        });
    }

    public void concernClick(View view) {
        if (user.isConcernswitch()) {
            Intent intent = new Intent(UserDataActivity.this, ConcernActivity1.class);
//            Bundle bundle = new Bundle();
//            bundle.putSerializable("user", user);
//            intent.putExtras(bundle);
            intent.putExtra("user", user);
            startActivity(intent);
        } else {
            Toast.makeText(UserDataActivity.this, "主人未公开他的关注哦", Toast.LENGTH_SHORT).show();
        }
//        Follow friend = new Follow();
//        //friend.setObjectId(user.getObjectId());
//        final BmobQuery<User> bmobQuery = new BmobQuery<>();
//        bmobQuery.getObject(user.getObjectId(), new QueryListener<User>() {
//            @Override
//            public void done(User user, BmobException e) {
//                if (e == null) {
//                    if (user.isTypeswitch()) {
//                        Intent intent = new Intent(UserDataActivity.this, TaConcernActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("user", user);
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    } else {
//                        Toast.makeText(UserDataActivity.this, "未公开", Toast.LENGTH_SHORT).show();
//                    }
//                } else {
//
//                }
//
//            }
//        });
    }
}
