package freestar.friends.fragments.pages_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import freestar.friends.R;
import freestar.friends.activities.PublishPicture;
import freestar.friends.bean.Article;
import freestar.friends.util.Banner;
import freestar.friends.util.BannerConfig;
import freestar.friends.util.material_refresh_layout.MaterialHeaderView;
import freestar.friends.util.recycler_and_fab.recyclerview.CommonAdapter;
import freestar.friends.util.recycler_and_fab.recyclerview.DividerItemDecoration;
import freestar.friends.util.recycler_and_fab.recyclerview.base.ViewHolder;
import freestar.friends.util.recycler_and_fab.recyclerview.wrapper.HeaderAndFooterWrapper;
import freestar.friends.util.recycler_and_fab.recyclerview.wrapper.LoadMoreWrapper;


/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class PageFragment11 extends Fragment {
    private List<Article> mDatas = new ArrayList<>();
    private CommonAdapter<Article> mAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    int count;
    private FloatingActionButton fab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        fab = (FloatingActionButton) view.findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PublishPicture.class));
            }
        });
        initData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        mAdapter = new CommonAdapter<Article>(getContext(), R.layout.item_layout, mDatas) {
            @Override
            protected void convert(ViewHolder holder, Article s, int position) {
                position = position - 1;
//                holder.setImageResource(R.id.main_picture, mDatas.get(position).getIdPicture()).setText(R.id.date, mDatas.get(position).getDate())
//                        .setText(R.id.num, mDatas.get(position).getDisNum());
            }
        };
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        initHeaderAndFooter();
        MaterialHeaderView headerView = new MaterialHeaderView(getActivity());
        setHeaderView(headerView);
//        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
//        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
//        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
//            @Override
//            public void onLoadMoreRequested() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        for (int i = 0; i < 7; i++) {
//                            mDatas.add(new Article1("小邋遢，这有有个小邋遢，邋遢大王就是他." + count, R.mipmap.ic_test_0, "2016.02.0" + i, "" + count++));
//                        }
//                        mLoadMoreWrapper.notifyDataSetChanged();
//                    }
//                }, 3000);
//            }
//        });

        mRecyclerView.setAdapter(mLoadMoreWrapper);
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener<Article>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, Article o, int position) {
                Toast.makeText(getContext(), "pos = " + position, Toast.LENGTH_SHORT).show();
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Article o, int position) {
                return false;
            }
        });
        return view;
    }

    private void setHeaderView(MaterialHeaderView headerView) {
//        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, Util.dip2px(context, hIGHER_HEAD_HEIGHT));
//        layoutParams.gravity = Gravity.TOP;
//        headerView.setLayoutParams(layoutParams);
//        headerView.setWaveColor(isShowWave ? waveColor : Color.TRANSPARENT);
//        headerView.showProgressArrow(showArrow);
//        headerView.setProgressSize(progressSize);
//        headerView.setProgressColors(colorSchemeColors);
//        headerView.setProgressStokeWidth(PROGRESS_STOKE_WIDTH);
//        headerView.setTextType(textType);
//        headerView.setProgressTextColor(progressTextColor);
//        headerView.setProgressValue(progressValue);
//        headerView.setProgressValueMax(progressMax);
//        headerView.setIsProgressBg(showProgressBg);
//        headerView.setProgressBg(progressBg);
//        headerView.setVisibility(View.GONE);
    }

    private void initData() {
        for (int i = 0; i < 7; i++) {
//            mDatas.add(new Article1("小邋遢，这有有个小邋遢，邋遢大王就是他。" + count, R.mipmap.ic_launcher, "2016.02.0" + i, "" + count++));
        }
    }

    private void initHeaderAndFooter() {
        View header = LayoutInflater.from(getContext()).inflate(R.layout.banner_layout, null);
        initBanner(header);
//        initAutoHead(header);
        mHeaderAndFooterWrapper.addHeaderView(header);
    }

    private void initBanner(View header) {
        String[] images = getResources().getStringArray(R.array.url);
        String[] titles = getResources().getStringArray(R.array.title);
        Banner banner = (Banner) header.findViewById(R.id.banner1);
        //显示数字指示器和标题
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        //记得设置标题列表哦
        banner.setBannerTitle(titles);
        banner.setImages(images);//可以选择设置图片网址，或者资源文件，默认用Glide加载
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {//设置点击事件
            @Override
            public void OnBannerClick(View view, int position) {
                Toast.makeText(getActivity(), "你点击了：" + position, Toast.LENGTH_LONG).show();
            }
        });
    }

//    private void initAutoHead(View header) {
//        List<Integer> resIds = new ArrayList<>();
//        // 模拟几张图片
//        resIds.add(R.mipmap.ic_test_0);
//        resIds.add(R.mipmap.ic_test_1);
//        resIds.add(R.mipmap.ic_test_2);
//        resIds.add(R.mipmap.ic_test_3);
//        resIds.add(R.mipmap.ic_test_4);
//        resIds.add(R.mipmap.ic_test_5);
//        resIds.add(R.mipmap.ic_test_6);
//        resIds.add(R.mipmap.ic_test_0);
//        resIds.add(R.mipmap.ic_test_4);
//        resIds.add(R.mipmap.ic_test_5);
//        resIds.add(R.mipmap.ic_test_3);
//
//        BannerAdapter bannerAdapter = new BannerAdapter(getContext());
//        bannerAdapter.update(resIds);
//
//        AutoPlayViewPager autoPlayViewPage = (AutoPlayViewPager) header.findViewById(R.id.view_pager);
//        autoPlayViewPage.setAdapter(bannerAdapter);
//
//        autoPlayViewPage.setDirection(AutoPlayViewPager.Direction.LEFT);// 设置播放方向
//        autoPlayViewPage.setCurrentItem(200); // 设置每个Item展示的时间
//        autoPlayViewPage.start(); // 开始轮播
//    }
}