package freestar.friends.util.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.fragments.msg_fragment.smallfragment.ShelunFragment;
import freestar.friends.fragments.msg_fragment.smallfragment.ShuoshuoFragment;
import freestar.friends.fragments.msg_fragment.smallfragment.TujiFragment;
import freestar.friends.util.status_bar.BaseActivity;

public class TypeActivity1 extends BaseActivity {

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
    @Bind(R.id.tv_publish)
    TextView tvPublish;
    private User user;
    static String id;

    public static String getId() {
        return id;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_publish_);
        ButterKnife.bind(this);
        tabLayout.setupWithViewPager(vp);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        Log.e("FreeStar", "TypeActivity1→→→onCreate:" + user);
        if (user != null) {
            id = user.getObjectId();
            tvPublish.setText("主人的发布");
        }
        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(new ShuoshuoFragment(), "我的说说");
        adapter.addFragment(new TujiFragment(), "我的图集");
        adapter.addFragment(new ShelunFragment(), "我的摄论");
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(2);
    }

    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        //将fragment和title放入集合
        public void addFragment(Fragment fragment, String title) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
        }

        //设置展示的fragment
        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

}
