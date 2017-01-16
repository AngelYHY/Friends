package freestar.friends.activities;

import android.content.Intent;
import android.os.Bundle;

import com.facebook.drawee.view.SimpleDraweeView;

import freestar.friends.R;
import freestar.friends.util.status_bar.BaseActivity;

public class MyIconActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_icon);
        SimpleDraweeView img = (SimpleDraweeView) findViewById(R.id.img_myIcon);

        Intent intent = getIntent();
        String url = intent.getStringExtra("user");
        img.setImageURI(url);

    }


}

