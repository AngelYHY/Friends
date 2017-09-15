package freestar.friends.activity.setting;

import android.os.Bundle;
import android.view.View;

import freestar.friends.R;
import freestar.friends.util.status_bar.BaseActivity;

/**
 * 帮助
 */
public class HelpActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    public void btnvbackclick(View view) {
        finish();
    }

}
