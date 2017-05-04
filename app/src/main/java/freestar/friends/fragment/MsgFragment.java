package freestar.friends.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import freestar.friends.R;
import freestar.friends.activity.SearchFriendActivity;
import freestar.friends.adapter.MyPagerAdapter;
import freestar.friends.fragment.message.ChatFragment;
import freestar.friends.fragment.message.CommDisFragment;
import freestar.friends.fragment.message.MyConcernFragment;


public class MsgFragment extends Fragment {
    Toolbar toolbar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View msgView = inflater.inflate(R.layout.message_view, container, false);
        toolbar = (Toolbar) msgView.findViewById(R.id.toolbar_msgFragment);
        addMenuOnClick();

        ViewPager vp = (ViewPager) msgView.findViewById(R.id.viewpager_msgFragment);
        //与viewpager相结合的标签栏
        TabLayout tabLayout = (TabLayout) msgView.findViewById(R.id.tabs_msgFragment);
        tabLayout.setupWithViewPager(vp);

        List<Fragment> fragments = new ArrayList<>();
        List<String> titles = new ArrayList<>();
        fragments.add(new ChatFragment());
        fragments.add(new CommDisFragment());
        fragments.add(new MyConcernFragment());
        titles.add("聊天消息");
        titles.add("社区评论");
        titles.add("我的关注");

        vp.setAdapter(new MyPagerAdapter(getChildFragmentManager(), fragments, titles));
        vp.setOffscreenPageLimit(2);
        return msgView;
    }

    private void addMenuOnClick() {
        toolbar.inflateMenu(R.menu.msgfrangment_menu);//设置右上角的填充菜单

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuItemId = item.getItemId();
                if (menuItemId == R.id.msgfragment_lianxiren) {
                    Intent intent = new Intent(getActivity(), SearchFriendActivity.class);
                    startActivity(intent);
                }
                return true;
            }
        });
    }

}
