package freestar.friends.util.profile;

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

public class NinameActivity extends BaseActivity {
    private EditText editText_niname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_niname);
        editText_niname = (EditText) findViewById(R.id.editText_niname);
    }

    public void trueClick(View view) {
        final String niname = editText_niname.getText().toString();
        if (niname.equals("")) {
            Toast.makeText(this, "写点东西吧", Toast.LENGTH_SHORT).show();
            return;
        }
        User u = new User();
        u.setNiname(niname);
        u.update(App.userId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(NinameActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
//                    startActivityForResult(new Intent(NinameActivity.this,MyinfoActivity.class),1);
                    Intent intent = new Intent();
                    intent.putExtra("name", niname);
                    setResult(RESULT_OK, intent);
                    finish();
                } else {
                    Toast.makeText(NinameActivity.this, "修改失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void btnvbackclick(View view) {
        finish();
    }
}
