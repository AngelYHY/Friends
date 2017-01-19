package freestar.friends.activities;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.util.Judge;

public class LoginA extends AppCompatActivity {

    @Bind(R.id.et_username)
    EditText etUsername;
    @Bind(R.id.et_password)
    EditText etPassword;
    @Bind(R.id.bt_go)
    Button btGo;
    @Bind(R.id.tv_login)
    TextView tvLogin;
    @Bind(R.id.tv_remember)
    TextView tvRemember;
    @Bind(R.id.iv_check)
    ImageView ivCheck;
    @Bind(R.id.cv)
    CardView cv;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    private ProgressBar mProBar;

//    private EditText etUsername;
//    private EditText etPassword;
//    private TextView tvLogin;
//    private TextView tvRemember;
//    private ImageView ivCheck;
//    private CardView cv;
//    private FloatingActionButton fab;

//    private void initView() {
//        etUsername = (EditText) findViewById(R.id.et_username);
//        etPassword = (EditText) findViewById(R.id.et_password);
//        tvLogin = (TextView) findViewById(R.id.tv_login);
//        tvRemember = (TextView) findViewById(R.id.tv_remember);
//        ivCheck = (ImageView) findViewById(R.id.iv_check);
//        cv = (CardView) findViewById(R.id.cv_add);
////        fab = (FloatingActionButton) findViewById(R.id.fab_btn);
//    }

    private String count;
    private String psw;
    private SharedPreferences.Editor edit;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
//        initView();
//        User user = BmobUser.getCurrentUser(User.class);
//        if(user != null){
//            // 允许用户使用应用
//            Intent intent = new Intent(LoginA.this,HomePageA.class);
//            intent.putExtra("user",user);
//            startActivity(intent);
//        }else{
//            //缓存用户对象为空时， 可打开用户注册界面…
//
//        }
    }

    //    保存数据到本地
    private void store(User user) {
        edit = preferences.edit();
        edit.putString("token", user.getToken());
        edit.putString("userId", user.getObjectId());
        edit.putString("headUrl", user.getHeadUrl());
        edit.putString("phoneNum", user.getPhoneNum());
        edit.putString("niname", user.getNiname());
        edit.putString("count", count);
        edit.putString("psw", psw);
        edit.putBoolean("check", true);
        edit.commit();
    }

    @OnClick({R.id.bt_go, R.id.fab})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(this, fab, fab.getTransitionName());
                    startActivity(new Intent(this, RegisterActivity.class), options.toBundle());
                } else {
                    startActivity(new Intent(this, RegisterActivity.class));
                }
                break;

            case R.id.bt_go:
                createProgressBar();
                count = etUsername.getText().toString().trim();
                psw = etPassword.getText().toString().trim();
                if (count.equals("")) {
                    mProBar.setVisibility(View.GONE);
                    Toast.makeText(LoginA.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                } else if (psw.equals("")) {
                    mProBar.setVisibility(View.GONE);
                    Toast.makeText(LoginA.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    if (Judge.judgePhoneNums(count)) {
                        queryNum();
                    } else {
                        mProBar.setVisibility(View.GONE);
                        Toast.makeText(LoginA.this, "手机号码不正确", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }
    }

    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    private void queryNum() {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("phoneNum", count);
        query.setMaxCacheAge(TimeUnit.DAYS.toMillis(7));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() == 1) {
                        User user = list.get(0);
                        //获取app的对象，调用setUser 把此对象加进去，增加复用
                        App app = (App) getApplication();
                        app.setUser(user);
                        app.setToken(user.getToken());
                        app.setUserId(user.getObjectId());

                        Log.e("user", "登录的对象为：" + user.toString());
                        if (user.getPassword().equals(psw)) {
                            preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                            if (ivCheck.getVisibility() == View.VISIBLE) {
                                store(user);
                            } else {
                                edit = preferences.edit();
                                edit.clear();
                                edit.commit();
                            }
                            App.user = user;
                            App.token = user.getToken();

                            Intent i2 = new Intent(LoginA.this, HomePageA.class);
                            startActivity(i2);

                            finish();
                        } else {
                            Toast.makeText(LoginA.this, "密码错误", Toast.LENGTH_SHORT).show();
                        }
                    } else if (list.size() == 0) {
                        Toast.makeText(LoginA.this, "号码并未注册", Toast.LENGTH_SHORT).show();
                        Log.e("FreeStar", "ForgetPasswordActivity→→→done:号码并未注册");
                    }
                } else {
                    Log.e("FreeStar", "RegisterActivity→→→done:失败" + e.getMessage() + ", " + e.getErrorCode());
                }
            }
        });
    }

    //忘记密码的单击事件
    @OnClick(R.id.tv_login)
    public void onClick() {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.tv_remember)
    public void setTvRemember() {
        Log.e("FreeStar", "LoginA→→→setTvRemember:");
        if (ivCheck.getVisibility() == View.GONE) {
            ivCheck.setVisibility(View.VISIBLE);
        } else {
            ivCheck.setVisibility(View.GONE);
        }
    }

}
