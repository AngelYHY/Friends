package freestar.friends.activity;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.util.Judge;
import freestar.friends.util.status_bar.BaseActivity;

/**
 * 忘记密码
 */
public class ForgetPasswordActivity extends BaseActivity {

    @Bind(R.id.et_phone)
    EditText etPhone;
    @Bind(R.id.btn_yan)
    Button btnYan;
    @Bind(R.id.et_yan)
    EditText etYan;
    @Bind(R.id.et_mima1)
    EditText etMima1;
    @Bind(R.id.et_mima2)
    EditText etMima2;
    @Bind(R.id.btn_forgot)
    Button btnForgot;

    int i = 30;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                btnYan.setText("重新发送(" + i + ")");
            } else if (msg.what == -8) {
                Log.e("FreeStar", "RegisterActivity→→→handleMessage:获取验证码");
                btnYan.setText("获取验证码");
                btnYan.setClickable(true);
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
                        mProBar.setVisibility(View.GONE);
                        update();
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Log.e("FreeStar", "RegisterActivity→→→handleMessage:验证码已经发送");
                    }
                } else {
                    if (mProBar != null) {
                        mProBar.setVisibility(View.GONE);
                    }
                    ((Throwable) data).printStackTrace();
                    Log.e("FreeStar", "RegisterActivity→→→handleMessage:" + data.toString());
                }
            }
        }
    };
    private String id;

    private void update() {
        final User user = new User();
        user.setPassword(psw);
        user.update(id, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(ForgetPasswordActivity.this, "修改密码成功" + user.getUpdatedAt(), Toast.LENGTH_SHORT).show();
                    ForgetPasswordActivity.this.finish();
                } else {
                    Toast.makeText(ForgetPasswordActivity.this, "修改失败", Toast.LENGTH_SHORT).show();
                    Log.e("FreeStar", "ForgetPasswordActivity→→→done:" + e.getMessage());
                }
            }
        });
    }

    private String phone;
    private String psw;
    private String code1;
    private ProgressBar mProBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
        ButterKnife.bind(this);

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
        SMSSDK.registerEventHandler(eventHandler);
    }

    @OnClick({R.id.btn_yan, R.id.btn_forgot})
    public void onClick(View view) {
        String phoneNums = etPhone.getText().toString();
        switch (view.getId()) {
            case R.id.btn_yan:

                // 1. 通过规则判断手机号
                if (!Judge.judgePhoneNums(phoneNums)) {
                    Toast.makeText(ForgetPasswordActivity.this, "手机号格式不正确", Toast.LENGTH_SHORT).show();
                    return;
                } // 2. 通过sdk发送短信验证
                SMSSDK.getVerificationCode("86", phoneNums);

                // 3. 把按钮变成不可点击，并且显示倒计时（正在获取）
                btnYan.setClickable(false);
                btnYan.setText("重新发送(" + i + ")");
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
                break;

            case R.id.btn_forgot:
                phone = etPhone.getText().toString().trim();
                psw = etMima1.getText().toString().trim();
                String repsw = etMima2.getText().toString().trim();
                code1 = etYan.getText().toString().trim();
                if (phone.equals("")) {
                    Log.e("FreeStar", "RegisterActivity→→→onClick:手机号不能为空");
                    Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
                } else if (psw.equals("")) {
                    Log.e("FreeStar", "RegisterActivity→→→onClick:密码不能为空");
                    Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                } else if (!psw.equals(repsw)) {
                    Log.e("FreeStar", "RegisterActivity→→→onClick:两次输入密码不同");
                    Toast.makeText(this, "两次输入密码不同", Toast.LENGTH_SHORT).show();
                } else if (!code1.equals("")) {
                    if (Judge.judgePhoneNums(phone)) {
                        Log.e("FreeStar", "RegisterActivity→→→onClick:验证码不为空");
                        createProgressBar();
                        queryData();
                    }
                } else if (code1.equals("")) {
                    Log.e("FreeStar", "RegisterActivity→→→onClick:验证码不能为空");
                    Toast.makeText(this, "验证码不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
        }
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

    private void queryData() {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereEqualTo("phoneNum", phone);
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (list != null && list.size() == 1) {
                        id = list.get(0).getObjectId();
                        SMSSDK.submitVerificationCode("86", phone, code1);
                    } else {
                        Log.e("FreeStar", "ForgetPasswordActivity→→→done:号码并未注册");
                    }
                } else {
                    Log.e("FreeStar", "RegisterActivity→→→done:失败" + e.getMessage() + ", " + e.getErrorCode());
                }
            }
        });
    }
}
