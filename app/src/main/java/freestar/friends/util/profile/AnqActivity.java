package freestar.friends.util.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.activities.ForgetPasswordActivity;
import freestar.friends.bean.User;
import freestar.friends.util.status_bar.BaseActivity;

public class AnqActivity extends BaseActivity {
    TextView textView_pht, textView_myemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_anq);
        textView_pht = (TextView) findViewById(R.id.textView_pht);
        textView_myemail = (TextView) findViewById(R.id.textView_myemail);
        phoupdate();
        getemail();

    }

    private void phoupdate() {
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.getObject(App.userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    textView_pht.setText(user.getPhoneNum());
                } else {

                }
            }
        });
    }

    public void passClick(View view) {
        Intent intent = new Intent(this, ForgetPasswordActivity.class);
        startActivity(intent);
    }

    public void myemailClick(View view) {
        Intent intent = new Intent(this, EmailActivity.class);
        startActivity(intent);
    }

    //返回
    public void btnvbackclick(View view) {
        finish();
    }


    //邮箱
    private void getemail() {
        BmobQuery<User> bmobQuery = new BmobQuery<User>();

//        boolean isCache  = bmobQuery.hasCachedResult(User.class);
//        if (isCache){
//            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.CACHE_ELSE_NETWORK);
//        }else {
//            bmobQuery.setCachePolicy(BmobQuery.CachePolicy.NETWORK_ELSE_CACHE);
//        }
        bmobQuery.getObject(App.userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    textView_myemail.setText(user.getEmail());
                } else {
                    Toast.makeText(AnqActivity.this, "获得失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
