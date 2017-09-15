package freestar.friends.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import freestar.friends.R;
import freestar.friends.adapter.MyPagerAdapter;
import freestar.friends.bean.User;
import freestar.friends.fragment.dynamic.CollectArticleFragment;
import freestar.friends.fragment.dynamic.CollectAtlasFragment;
import freestar.friends.util.status_bar.BaseActivity;

/**
 * 我的收藏
 */
public class CollectActivity extends BaseActivity {

    @Bind(R.id.tv_publish)
    TextView tvPublish;
    @Bind(R.id.toolbar_msgFragment)
    Toolbar toolbarMsgFragment;
    @Bind(R.id.tabs_msgFragment)
    TabLayout tabLayout;
    @Bind(R.id.appbar_msgFragment)
    AppBarLayout appbarMsgFragment;
    @Bind(R.id.viewpager_msgFragment)
    ViewPager vp;
    @Bind(R.id.main_content)
    CoordinatorLayout mainContent;
    private User user;
    static String id;

    public static String getId() {
        return id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_collect_);
        ButterKnife.bind(this);
        tabLayout.setupWithViewPager(vp);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        Log.e("FreeStar", "TypeActivity1→→→onCreate:" + user);
        if (user != null) {
            id = user.getObjectId();
            tvPublish.setText("主人的收藏");
        }

        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        fragments.add(new CollectArticleFragment());
        fragments.add(new CollectAtlasFragment());

        titles.add("收藏的摄论");
        titles.add("收藏的图集");

        vp.setAdapter(new MyPagerAdapter(getSupportFragmentManager(), fragments, titles));
    }

}
