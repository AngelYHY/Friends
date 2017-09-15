package freestar.friends.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.bgabanner.BGABanner;
import cn.bingoogolapple.bgabanner.BGABannerUtil;
import freestar.friends.App;
import freestar.friends.R;

/**
 * 引导页
 */
//依赖 compile 'com.nineoldandroids:library:2.4.0'
public class GuideActivity extends Activity implements View.OnClickListener {
    private static final String TAG = GuideActivity.class.getSimpleName();
    private TextView mSkipTv;
    private Button mEnterBtn;
    private BGABanner mContentBanner;
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initView();
        setListener();
        processLogic();
    }

    private void isRememberStatus() {
        preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
//        Explode explode = new Explode();
//        explode.setDuration(500);
//
//        getWindow().setExitTransition(explode);
//        getWindow().setEnterTransition(explode);
//        ActivityOptionsCompat oc2 = ActivityOptionsCompat.makeSceneTransitionAnimation(this);
        //判断记住密码多选框状态
        if (preferences.getBoolean("check", false)) {
            App.token = preferences.getString("token", "");
            App.headUrl = preferences.getString("headUrl", "");
            App.phoneNum = preferences.getString("phoneNum", "");
            App.niname = preferences.getString("niname", "");
            App.userId = preferences.getString("userId", "");
            Intent i2 = new Intent(this, HomePage.class);
            startActivity(i2);
        } else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }
    }

    private void initView() {
        setContentView(R.layout.activity_guide);
        mSkipTv = (TextView) findViewById(R.id.tv_guide_skip);
        mEnterBtn = (Button) findViewById(R.id.btn_guide_enter);
        mContentBanner = (BGABanner) findViewById(R.id.banner_guide_content);
    }

    private void setListener() {
        mSkipTv.setOnClickListener(this);
        mEnterBtn.setOnClickListener(this);
        // 监听广告 item 的单击事件
        mContentBanner.setOnItemClickListener(new BGABanner.OnItemClickListener() {
            @Override
            public void onBannerItemClick(BGABanner banner, View view, Object model, int position) {
                Log.i(TAG, "点击了第" + (position + 1) + "页");
            }
        });
        mContentBanner.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == mContentBanner.getItemCount() - 2) {
                    ViewCompat.setAlpha(mEnterBtn, positionOffset);
                    ViewCompat.setAlpha(mSkipTv, 1.0f - positionOffset);

                    if (positionOffset > 0.5f) {
                        mEnterBtn.setVisibility(View.VISIBLE);
                        mSkipTv.setVisibility(View.GONE);
                    } else {
                        mEnterBtn.setVisibility(View.GONE);
                        mSkipTv.setVisibility(View.VISIBLE);
                    }
                } else if (position == mContentBanner.getItemCount() - 1) {
                    mSkipTv.setVisibility(View.GONE);
                    mEnterBtn.setVisibility(View.VISIBLE);
                    ViewCompat.setAlpha(mEnterBtn, 1.0f);
                } else {
                    mSkipTv.setVisibility(View.VISIBLE);
                    ViewCompat.setAlpha(mSkipTv, 1.0f);
                    mEnterBtn.setVisibility(View.GONE);
                }
            }
        });
    }

    private void processLogic() {
        mContentBanner.setOverScrollMode(View.OVER_SCROLL_NEVER);

        // 初始化方式1：通过传入数据模型并结合Adapter的方式初始化
//        mContentBanner.setAdapter(new BGABanner.Adapter() {
//            @Override
//            public void fillBannerItem(BGABanner banner, View headView, Object model, int position) {
//                ((ImageView) headView).setImageResource((int) model);
//            }
//        });
//        mContentBanner.setData(Arrays.asList(R.drawable.ic_guide_1, R.drawable.ic_guide_2, R.drawable.ic_guide_3), null);


        // 初始化方式2：通过直接传入视图集合的方式初始化
        List<View> views = new ArrayList<>();
        views.add(BGABannerUtil.getItemImageView(this, R.mipmap.one));
        views.add(BGABannerUtil.getItemImageView(this, R.mipmap.two));
        views.add(BGABannerUtil.getItemImageView(this, R.mipmap.three
        ));
        mContentBanner.setData(views);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_guide_enter || view.getId() == R.id.tv_guide_skip) {
            isRememberStatus();
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mContentBanner.setBackgroundResource(android.R.color.white);
    }
}