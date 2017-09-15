package freestar.friends.activity.setting;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import freestar.friends.R;
import freestar.friends.util.status_bar.BaseActivity;
import io.rong.imkit.RongIM;

/**
 * 设置中心
 */
public class SettingActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
    }


    public void fankuiClick(View view) {

        String name = "官方客服";
        String id = "00001";
        RongIM.getInstance().startPrivateChat(SettingActivity.this, id, name);

    }


    //图片清理
    public void qktpClick(View view) {
        Toast.makeText(SettingActivity.this, "图片清理处理完毕", Toast.LENGTH_SHORT).show();
    }

    //搜索清理
    public void qkssClick(View view) {
        Toast.makeText(SettingActivity.this, "处理完毕xiuxiu", Toast.LENGTH_SHORT).show();
    }

    //帮助
    public void bzClick(View view) {
        Intent intent = new Intent(this, HelpActivity.class);
        startActivity(intent);
    }

    //unsubmitmessage
    public void messageClick(View view) {
        Intent intent = new Intent(this, SetupMessage.class);
        startActivity(intent);
    }


    public void btnvbackclick(View view) {
        finish();
    }
}
