package freestar.friends.util.status_bar;

import android.support.v7.app.AppCompatActivity;

/**
 * Created by Jaeger on 16/2/14.
 * <p/>
 * Email: chjie.jaeger@gamil.com
 * GitHub: https://github.com/laobie
 */

//为状态栏配色  需要继承这个activity
public class BaseActivity extends AppCompatActivity {

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        setStatusBar();
    }

    protected void setStatusBar() {
        //必须用0xDE051B 这种形式  否则没有效果
        StatusBarUtil.setColor(this, 0xDE051B, 0);
    }
}
