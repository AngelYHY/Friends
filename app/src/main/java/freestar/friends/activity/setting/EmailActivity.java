package freestar.friends.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.util.status_bar.BaseActivity;

/**
 * 邮箱设置
 */
public class EmailActivity extends BaseActivity {
    EditText editText_email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);
        editText_email = (EditText) findViewById(R.id.editText_email);
    }

    //添加邮箱
    public void emailtrueClick(View view) {
        final String email = editText_email.getText().toString();
        User u = new User();
        u.setEmail(email);
        u.update(App.userId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(EmailActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                    startActivityForResult(new Intent(EmailActivity.this, SafeActivity.class), 1);
                    finish();
                } else {
                    Toast.makeText(EmailActivity.this, "修改失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void btnvbackclick(View view) {
        finish();
    }
}
