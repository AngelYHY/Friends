package freestar.friends.activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.util.Judge;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    int i = 30;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                btnRegist.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                Log.e("FreeStar", "RegisterActivity→→→handleMessage:获取验证码");
                btnRegist.setText("获取验证码");
                btnRegist.setClickable(true);
                i = 30;
            } else {
                Log.e("FreeStar", "RegisterActivity→→→handleMessage:执行了");
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;

                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        // 提交验证码成功
                        Toast.makeText(getApplicationContext(), "提交验证码成功", Toast.LENGTH_SHORT).show();
                        Log.e("FreeStar", "RegisterActivity→→→handleMessage:提交验证码成功");
//                        getToken();
                        add();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Log.e("FreeStar", "RegisterActivity→→→handleMessage:验证码已经发送");
                    }
                } else {
                    if (mProBar != null) {
                        mProBar.setVisibility(View.GONE);
                    }
                    Toast.makeText(RegisterActivity.this, "验证失败", Toast.LENGTH_SHORT).show();
                    ((Throwable) data).printStackTrace();
                    Log.e("FreeStar", "RegisterActivity→→→handleMessage:" + data.toString());
                }
            }
        }
    };

    private ProgressBar mProBar;
    private String phone;
    private String code1;
    private String psw;
    private EditText etIdentifycode;
    private Button btnRegist;
    private EditText etPassword;
    private EditText etRepeatpassword;
    private EditText etUsername;
    private FloatingActionButton fab;
    private CardView cvAdd;
    private Button btnGo;


    EventHandler eventHandler = new EventHandler() {
        @Override
        public void afterEvent(int event, int result, Object data) {
            Message msg = new Message();
            msg.arg1 = event;
            msg.arg2 = result;
            msg.obj = data;
            handler.sendMessage(msg);
        }
    };
    private String count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            ShowEnterAnimation();
        }
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateRevealClose();
            }
        });
        // 注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void initView() {
        etUsername = (EditText) findViewById(R.id.et_username);
        etIdentifycode = (EditText) findViewById(R.id.et_identifycode);
        btnRegist = (Button) findViewById(R.id.btn_regist);
        etPassword = (EditText) findViewById(R.id.et_password);
        etRepeatpassword = (EditText) findViewById(R.id.et_repeatpassword);
        fab = (FloatingActionButton) findViewById(R.id.fab);
        cvAdd = (CardView) findViewById(R.id.cv_add);
        btnGo = (Button) findViewById(R.id.bt_go);
        btnGo.setOnClickListener(this);
        btnRegist.setOnClickListener(this);
        fab.setOnClickListener(this);
    }

    private void ShowEnterAnimation() {
        Transition transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition);
        getWindow().setSharedElementEnterTransition(transition);

        transition.addListener(new Transition.TransitionListener() {
            @Override
            public void onTransitionStart(Transition transition) {
                cvAdd.setVisibility(View.GONE);
            }

            @Override
            public void onTransitionEnd(Transition transition) {
                transition.removeListener(this);
                animateRevealShow();
            }

            @Override
            public void onTransitionCancel(Transition transition) {

            }

            @Override
            public void onTransitionPause(Transition transition) {

            }

            @Override
            public void onTransitionResume(Transition transition) {

            }
        });
    }

    public void animateRevealShow() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, fab.getWidth() / 2, cvAdd.getHeight());
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
            }

            @Override
            public void onAnimationStart(Animator animation) {
                cvAdd.setVisibility(View.VISIBLE);
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    public void animateRevealClose() {
        Animator mAnimator = ViewAnimationUtils.createCircularReveal(cvAdd, cvAdd.getWidth() / 2, 0, cvAdd.getHeight(), fab.getWidth() / 2);
        mAnimator.setDuration(500);
        mAnimator.setInterpolator(new AccelerateInterpolator());
        mAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                cvAdd.setVisibility(View.INVISIBLE);
                super.onAnimationEnd(animation);
                fab.setImageResource(R.drawable.plus);
                RegisterActivity.super.onBackPressed();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
            }
        });
        mAnimator.start();
    }

    @Override
    public void onBackPressed() {
        animateRevealClose();
    }

    public void onClick(View view) {
        Log.e("FreeStar", "RegisterActivity→→→onClick:");
        switch (view.getId()) {
            case R.id.btn_regist:
                Log.e("FreeStar", "RegisterActivity→→→onClick:btn_regist");
                count = etUsername.getText().toString().trim();
                // 1. 通过规则判断手机号
                if (!Judge.judgePhoneNums(count)) {
                    Toast.makeText(RegisterActivity.this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                }
                queryData();

                break;
            case R.id.bt_go:
                phone = etUsername.getText().toString().trim();
                psw = etPassword.getText().toString().trim();
                String repsw = etRepeatpassword.getText().toString().trim();
                code1 = etIdentifycode.getText().toString().trim();

                if (phone.equals("")) {
                    Log.e("FreeStar", "RegisterActivity→→→onClick:手机号不能为空");
                    Toast.makeText(RegisterActivity.this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else if (psw.equals("")) {
                    Log.e("FreeStar", "RegisterActivity→→→onClick:密码不能为空");
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!psw.equals(repsw)) {
                    Log.e("FreeStar", "RegisterActivity→→→onClick:两次输入密码不同");
                    Toast.makeText(RegisterActivity.this, "两次输入密码不同", Toast.LENGTH_SHORT).show();
                } else if (!code1.equals("")) {
                    if (Judge.judgePhoneNums(phone)) {
                        Log.e("FreeStar", "RegisterActivity→→→onClick:验证码不为空");
                        createProgressBar();
                        SMSSDK.submitVerificationCode("86", phone, code1);
                    }
                } else if (code1.equals("")) {
                    Log.e("FreeStar", "RegisterActivity→→→onClick:验证码不能为空");
                    Toast.makeText(RegisterActivity.this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    /**
     * 查询数据
     */
    private void queryData() {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("phoneNum", count);
        Log.e("FreeStar", "RegisterActivity→→→queryData:");
        query.count(User.class, new CountListener() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "RegisterActivity→→→done:" + integer);
                    if (integer == 0) {
                        Log.e("FreeStar", "RegisterActivity→→→done:" + integer);
                        // 2. 通过sdk发送短信验证
                        SMSSDK.getVerificationCode("86", count);
                        Log.e("FreeStar", "RegisterActivity→→→onClick:btn_regist");
                        // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                        btnRegist.setClickable(false);
                        btnRegist.setText("重新发送(" + i + ")");
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                for (; i > 0; i--) {
                                    handler.sendEmptyMessage(-9);
                                    if (i <= 0) {
                                        break;
                                    }
                                    try {
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                handler.sendEmptyMessage(-8);
                            }
                        }).start();
                        Log.e("FreeStar", "RegisterActivity→→→onClick:2");
                    } else {
                        Toast.makeText(RegisterActivity.this, "手机号已被注册", Toast.LENGTH_SHORT).show();
                        Log.e("FreeStar", "RegisterActivity→→→done:手机号已被注册");
                    }
                } else {
                    Log.e("FreeStar", "RegisterActivity→→→done:失败" + e.getMessage() + ", " + e.getErrorCode());
                }
            }
        });
    }

    private void getToken() {
        final String url = App.getUrl();

        RequestParams mRequestParams = new RequestParams(url);
        mRequestParams.addQueryStringParameter("id", App.userId);
        mRequestParams.addQueryStringParameter("psw", psw);
        x.http().get(mRequestParams, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                if (result != null) {
                    mProBar.setVisibility(View.GONE);
                    User user = new User();
                    user.setObjectId(App.userId);
                    user.setToken(result);
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.e("FreeStar", "RegisterActivity→→→done:成功");
                            } else {
                                Log.e("FreeStar", "RegisterActivity→→→done:" + e.getMessage());
                            }
                        }
                    });

                    startActivity(new Intent(RegisterActivity.this, HomePage.class));
                    finish();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }

    private void add() {
        User user = new User();
        user.setPhoneNum(phone);
        user.setPassword(psw);
        user.setSex(true);
        user.setBirthday("2015-01-01");
//        user.setTypeswitch(true);
//        user.setCollectswitch(true);
        user.setNiname("你好！摄友");
//        user.setConcernswitch(true);
//        Log.i("token", "获得的token为：" + result);
//        user.setToken(result);
        user.setHeadUrl("https://ss1.baidu.com/6ONXsjip0QIZ8tyhnq/it/u=764544484,768159935&fm=58");
        //保存并监听服务端回馈信息
        user.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {

                if (e == null) {
                    App.userId = s;
                    Toast.makeText(RegisterActivity.this, "注册成功" + s, Toast.LENGTH_SHORT).show();
                    mProBar.setVisibility(View.GONE);
                    getToken();
                } else {
                    Log.i("FreeStar", "注册失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    /**
     * progressbar
     */
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

    @Override
    protected void onDestroy() {
        SMSSDK.unregisterAllEventHandler();
        super.onDestroy();
    }
}
