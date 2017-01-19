package freestar.friends.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import freestar.friends.R;
import freestar.friends.activities.QurryFriendIdActivity;
import freestar.friends.fragments.msg_fragment.ChatFragment;
import freestar.friends.fragments.msg_fragment.CommunityDisFragment_test;
import freestar.friends.fragments.msg_fragment.SystemFragment;


public class MsgFragment extends Fragment {
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View msgView = inflater.inflate(R.layout.message_view, null);
        toolbar = (Toolbar) msgView.findViewById(R.id.toolbar_msgFragment);
        addMenuOnClick();

        ViewPager vp = (ViewPager) msgView.findViewById(R.id.viewpager_msgFragment);
        //与viewpager相结合的标签栏
        TabLayout tabLayout = (TabLayout) msgView.findViewById(R.id.tabs_msgFragment);
        tabLayout.setupWithViewPager(vp);

        Adapter adapter = new Adapter(getFragmentManager());
        adapter.addFragment(new ChatFragment(), "聊天消息 ");
        adapter.addFragment(new CommunityDisFragment_test(), "社区评论");
        adapter.addFragment(new SystemFragment(), "我的关注");
        vp.setAdapter(adapter);
        vp.setOffscreenPageLimit(2);
//        vp.setOffscreenPageLimit(3);
        return msgView;
    }

    private void addMenuOnClick() {

        toolbar.inflateMenu(R.menu.msgfrangment_menu);//设置右上角的填充菜单

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.msgfragment_lianxiren) {
                    Intent intent = new Intent(getActivity(), QurryFriendIdActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

    //Fragment适配器
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
