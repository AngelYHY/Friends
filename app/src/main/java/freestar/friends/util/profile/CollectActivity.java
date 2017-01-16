package freestar.friends.util.profile;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import freestar.friends.R;
import freestar.friends.fragments.msg_fragment.smallfragment.CollectShelunFragment;
import freestar.friends.fragments.msg_fragment.smallfragment.CollectTujiFragment;
import freestar.friends.util.status_bar.BaseActivity;

public class CollectActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    private ListView listView_collec;

    ViewPager vp;
    private ArrayList<Fragment> fragmentList;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collect);
        fragmentList = new ArrayList<>();
        fragmentList.add(new CollectShelunFragment());
        fragmentList.add(new CollectTujiFragment());

        vp = (ViewPager) findViewById(R.id.viewpager_collect);

        rg = (RadioGroup) findViewById(R.id.group);

        rg.setOnCheckedChangeListener(this);
        //vp:页面切换事件
        vp.addOnPageChangeListener(this);
//适配器找到 fragment
        vp.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));


    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        RadioButton radioButton = (RadioButton) rg.getChildAt(position);

        //radiobutton状态：checked
        radioButton.setChecked(true);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    class MyFragmentPagerAdapter extends FragmentPagerAdapter {
        List<Fragment> fragmentList;

        public MyFragmentPagerAdapter(FragmentManager fragmentManager, List<Fragment> fragmentList) {
            super(fragmentManager);
            this.fragmentList = fragmentList;

        }


        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getCount() {
            return (fragmentList == null) ? 0 : fragmentList.size();
        }
    }
}
