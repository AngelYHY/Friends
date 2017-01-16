package freestar.friends.util.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import java.util.ArrayList;
import java.util.List;

import freestar.friends.R;
import freestar.friends.fragments.msg_fragment.smallfragment.ShelunFragment;
import freestar.friends.fragments.msg_fragment.smallfragment.ShuoshuoFragment;
import freestar.friends.fragments.msg_fragment.smallfragment.TujiFragment;
import freestar.friends.util.status_bar.BaseActivity;

public class TypeActivity extends BaseActivity implements RadioGroup.OnCheckedChangeListener, ViewPager.OnPageChangeListener {
    ViewPager vp;
    private ArrayList<Fragment> fragmentList;
    RadioGroup rg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_type);
        //实例化集合
        fragmentList = new ArrayList<>();
        //初始化fragment集合
        fragmentList.add(new TujiFragment());
        fragmentList.add(new ShelunFragment());
        fragmentList.add(new ShuoshuoFragment());

        //找到viewpager控件
        vp = (ViewPager) findViewById(R.id.viewpager_type);

        rg = (RadioGroup) findViewById(R.id.group);

        //rb选中事件
        rg.setOnCheckedChangeListener(this);
        //vp:页面切换事件
        vp.addOnPageChangeListener(this);
        //适配器找到 fragment
        vp.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), fragmentList));
        vp.setOffscreenPageLimit(3);

    }


    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int i) {
        int index = -1;
        switch (i) {
            case R.id.tuji:
                index = 0;
                break;
            case R.id.shelun:
                index = 1;
                break;
            case R.id.shuoshuo:
                index = 2;
                break;
        }
        //设置vp的显示项
        vp.setCurrentItem(index);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

        //根据位置找到radiobutton
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

        //返回特定位置的Fragment
        @Override
        public Fragment getItem(int position) {
            //根据位置找到对应的Fragment
            return fragmentList.get(position);
        }

        //vp显示的fragment个数
        @Override
        public int getCount() {
            return (fragmentList == null) ? 0 : fragmentList.size();
        }
    }
}
