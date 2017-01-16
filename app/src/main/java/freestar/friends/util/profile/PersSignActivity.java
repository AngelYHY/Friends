package freestar.friends.util.profile;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.util.status_bar.BaseActivity;

public class PersSignActivity extends BaseActivity {
    private EditText editText_perssign;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pers_sign);

        editText_perssign = (EditText) findViewById(R.id.editText_perssign);
        //设置EditText的显示方式为多行文本输入
        editText_perssign.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        //文本显示的位置在EditText的最上方
        editText_perssign.setGravity(Gravity.TOP);
        //改变默认的单行模式
        editText_perssign.setSingleLine(false);
        //水平滚动设置为False
        editText_perssign.setHorizontallyScrolling(false);
        editText_perssign.setFilters(new InputFilter[]{new InputFilter.LengthFilter(50)});

    }

    public void perssignClick(View view) {
        final String perssign = editText_perssign.getText().toString();
        if (perssign.equals("")) {
            Toast.makeText(PersSignActivity.this, "写点东西吧", Toast.LENGTH_SHORT).show();
            return;
        }
        User u = new User();
        u.setPersSign(perssign);
        u.update(App.userId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast.makeText(PersSignActivity.this, "修改成功", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent();
                    intent.putExtra("OK", perssign);
                    setResult(RESULT_OK, intent);
                    Log.e("FreeStar", "PersSignActivity→→→done:修改了");
//                    startActivity(intent);
//                    startActivityForResult(new Intent(PersSignActivity.this,MyinfoActivity.class),1);
                    finish();

                } else {
                    Toast.makeText(PersSignActivity.this, "修改失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void btnvbackclick(View view) {

        finish();
    }
}
