package freestar.friends.fragments.pages_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.R;
import freestar.friends.activities.PicItemActivity;
import freestar.friends.activities.PublishPicture;
import freestar.friends.bean.Atlas;
import freestar.friends.util.Banner;
import freestar.friends.util.BannerConfig;
import freestar.friends.util.recycler_and_fab.recyclerview.CommonAdapter;
import freestar.friends.util.recycler_and_fab.recyclerview.DividerItemDecoration;
import freestar.friends.util.recycler_and_fab.recyclerview.base.ViewHolder;
import freestar.friends.util.recycler_and_fab.recyclerview.wrapper.EmptyWrapper;
import freestar.friends.util.recycler_and_fab.recyclerview.wrapper.HeaderAndFooterWrapper;
import freestar.friends.util.recycler_and_fab.recyclerview.wrapper.LoadMoreWrapper;


/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class PageFragment333 extends Fragment {
    private List<Atlas> mDatas = new ArrayList<>();
    private CommonAdapter<Atlas> mAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private FloatingActionButton fab;
    int i = 0;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                startQuery();
            }
        }
    };
    private RecyclerView mRecyclerView;

    private void startQuery() {
        BmobQuery<Atlas> query = new BmobQuery<>();
        query.addWhereEqualTo("kind", "动物");
        query.setSkip(10 * i); // 忽略前10条数据（即第一页数据结果）
        query.setLimit(10);
        query.order("-createdAt");
        query.findObjects(new FindListener<Atlas>() {
            @Override
            public void done(List<Atlas> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "PageFragment1→→→done:" + list.size());
                    mDatas.addAll(list);
                    for (Atlas mData : mDatas) {
                        Log.e("FreeStar", "PageFragment1→→→done:" + mData.toString());
                    }
                    if (list.size() == 0) {
                        Toast.makeText(getActivity(), "没有数据了", Toast.LENGTH_SHORT).show();
                        mLoadMoreWrapper.setViewGone();
                    } else {
                        mLoadMoreWrapper.notifyDataSetChanged();
                    }
                } else {
                    Log.e("FreeStar", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
        i++;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one, container, false);
        initData();

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);

        fab = (FloatingActionButton) view.findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PublishPicture.class));
            }
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        mAdapter = new CommonAdapter<Atlas>(getContext(), R.layout.atlas_layout, mDatas) {
            @Override
            protected void convert(ViewHolder holder, Atlas s, int position) {
                holder.setText(R.id.tv_time, s.getCreatedAt()).setSDV(R.id.iv_main, s.getMainPic()).setText(R.id.tv_disNum, s.getLikeNum() + "赞").setText(R.id.tv_title, s.getTitle() + "(" + s.getNum() + " 图)");
            }
        };

//        initHeaderAndFooter();
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);

        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener<Atlas>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, Atlas o, int position) {
                Intent intent = new Intent(getActivity(), PicItemActivity.class);
                Log.e("FreeStar", "PageFragment1→→→onItemClick:" + o.toString());
                intent.putExtra("atlas", o);
                startActivity(intent);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Atlas o, int position) {
                return false;
            }
        });
        return view;
    }

    private void initHeaderAndFooter() {
//        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
//        View header = LayoutInflater.from(getContext()).inflate(R.layout.banner_layout1, null);
//        initBanner(header);
////        initAutoHead(header);
//        mHeaderAndFooterWrapper.addHeaderView(header);
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

    private void initData() {
        BmobQuery<Atlas> query = new BmobQuery<>();
        query.addWhereEqualTo("kind", "动物");
        query.include("author");
        query.setLimit(15);
        query.order("-createdAt");
        query.findObjects(new FindListener<Atlas>() {
            @Override
            public void done(List<Atlas> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "PageFragment1→→→done:" + list.size());
                    mDatas.addAll(list);
                    for (Atlas mData : mDatas) {
                        Log.e("FreeStar", "PageFragment1→→→done:" + mData.toString());
                    }
                    if (list.size() == 0) {
                        initEmptyView();
                    } else if (list.size() > 14) {
                        initMore();
                    } else {
                        mRecyclerView.setAdapter(mAdapter);
                    }
                    Log.e("FreeStar", "PageFragment1→→→done:数据源改变");
                } else {
                    Log.e("FreeStar", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
    }

    private void initMore() {
        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mRecyclerView.setAdapter(mLoadMoreWrapper);

        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        Log.e("FreeStar", "PageFragment1→→→run:");
//
//                    }
//                }, 3000);
                handler.sendEmptyMessage(1);
            }
        });

    }

    private void initEmptyView() {
        EmptyWrapper mEmptyWrapper = new EmptyWrapper(mAdapter);
        mEmptyWrapper.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.empty_view, mRecyclerView, false));
        mRecyclerView.setAdapter(mEmptyWrapper);
    }

}