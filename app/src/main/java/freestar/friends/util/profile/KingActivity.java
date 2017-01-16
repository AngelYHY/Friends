package freestar.friends.util.profile;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.util.status_bar.BaseActivity;

public class KingActivity extends BaseActivity {
    @Bind(R.id.button)
    Button button;
    @Bind(R.id.tv_add)
    TextView tvAdd;
    @Bind(R.id.layout_action)
    RelativeLayout layoutAction;
    //    @Bind(R.id.switch1)
//    Switch switch1;
//    @Bind(R.id.switch2)
//    Switch switch2;
//    @Bind(R.id.switch3)
//    Switch switch3;
    private boolean aSwitchStart;
    private boolean aswitch_collectStart;
    private boolean aswitch_concernStart;

    private Switch aSwitch, aswitch_collect, aswitch_concern;
    private User user;
    private SharedPreferences preferences;
    private SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_king);
        ButterKnife.bind(this);
        aSwitch = (Switch) findViewById(R.id.switch1);
        aswitch_collect = (Switch) findViewById(R.id.switch3);
        aswitch_concern = (Switch) findViewById(R.id.switch2);
        user = new User();
        user.setObjectId(App.userId);
        //获得按钮的状态
        getState();
//        queryState();

    }

    private void getState() {
        preferences = getSharedPreferences(App.userId, Context.MODE_PRIVATE);
        aSwitchStart = preferences.getBoolean("aSwitch", false);
        aswitch_collectStart = preferences.getBoolean("aswitch_collect", false);
        aswitch_concernStart = preferences.getBoolean("aswitch_concern", false);

        aSwitch.setChecked(aSwitchStart);
        aswitch_collect.setChecked(aswitch_collectStart);
        aswitch_concern.setChecked(aswitch_concernStart);
    }

    private void queryState() {
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(App.userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    if (user.isCollectswitch()) {
                        aswitch_collect.setChecked(true);
                        aswitch_collectStart = true;
                    }
                    if (user.isTypeswitch()) {
                        aSwitch.setChecked(true);
                        aSwitchStart = true;
                    }
                    if (user.isConcernswitch()) {
                        aswitch_concern.setChecked(true);
                        aswitch_concernStart = true;
                    }
                }
            }
        });
    }

    public void btnvbackclick(View view) {
        finish();
    }

                        @Override
    protected void onDestroy() {
        edit = preferences.edit();
        if (aSwitchStart != aSwitch.isChecked()) {
            if (aSwitch.isChecked()) {
                user.setTypeswitch(true);
                user.update(App.userId, new UpdateListener() {
                    @Override
                        public void done(BmobException e) {
                            if (e == null) {
//                            Toast.makeText(KingActivity.this, "公开发布", Toast.LENGTH_SHORT).show();
                            Log.e("FreeStar", "KingActivity→→→done:公开我的发布");
                            } else {

                            }
                        }
                    });
                edit.putBoolean("aSwitch", true);
                } else {
                user.setTypeswitch(false);
                user.update(App.userId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
//                            Toast.makeText(KingActivity.this, "关闭发布", Toast.LENGTH_SHORT).show();
                            Log.e("FreeStar", "KingActivity→→→done:关闭我的发布");
                            } else {

                            }
                        }
                    });
                edit.putBoolean("aSwitch", false);
                }
        }
        if (aswitch_collectStart != aswitch_collect.isChecked()) {
            if (aswitch_collect.isChecked()) {
                user.setCollectswitch(true);
                user.update(App.userId, new UpdateListener() {
                    @Override
                        public void done(BmobException e) {
                            if (e == null) {
//                            Toast.makeText(KingActivity.this, "公开发布", Toast.LENGTH_SHORT).show();
                            Log.e("FreeStar", "KingActivity→→→done:公开我的收藏");
                            } else {

                            }
                        }
                    });
                edit.putBoolean("aswitch_collect", true);
                } else {
                user.setCollectswitch(false);
                user.update(App.userId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
//                            Toast.makeText(KingActivity.this, "关闭发布", Toast.LENGTH_SHORT).show();
                            Log.e("FreeStar", "KingActivity→→→done:关闭我的收藏");
                            } else {

                            }
                        }
                    });
                edit.putBoolean("aswitch_collect", false);
                }
        }


        if (aswitch_concernStart != aswitch_concern.isChecked()) {
            if (aswitch_concern.isChecked()) {
                user.setConcernswitch(true);
                user.update(App.userId, new UpdateListener() {
                    @Override
                        public void done(BmobException e) {
                            if (e == null) {
//                            Toast.makeText(KingActivity.this, "公开发布", Toast.LENGTH_SHORT).show();
                            Log.e("FreeStar", "KingActivity→→→done:公开关注成功");
                            } else {

                            }
                        }
                    });
                edit.putBoolean("aswitch_concern", true);
                } else {
                user.setConcernswitch(false);
                user.update(App.userId, new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
//                            Toast.makeText(KingActivity.this, "关闭发布", Toast.LENGTH_SHORT).show();
                            Log.e("FreeStar", "KingActivity→→→done:关闭关注成功");
                            } else {

                            }
                        }
                    });
                edit.putBoolean("aswitch_concern", false);
                }
        }
        edit.commit();
        super.onDestroy();
    }
}
