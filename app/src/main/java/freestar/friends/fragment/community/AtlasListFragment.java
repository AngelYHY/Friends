package freestar.friends.fragment.community;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.R;
import freestar.friends.activity.PicItemActivity;
import freestar.friends.activity.publish.PicturePublish;
import freestar.friends.adapter.AtlasAdapter;
import freestar.friends.bean.Atlas;

public class AtlasListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    private int i;
    private AtlasAdapter mAdapter;
    private String mType;

    public AtlasListFragment() {
    }

    public static AtlasListFragment newInstance(String type) {
        AtlasListFragment fragment = new AtlasListFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        mType = bundle.getString("type");
        View view = inflater.inflate(R.layout.modify_one, container, false);
        ButterKnife.bind(this, view);
        mSwipeLayout.setOnRefreshListener(this);
        initRV();
        onRefresh();
        return view;
    }

    private void initRV() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new AtlasAdapter();
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Atlas atlas = (Atlas) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), PicItemActivity.class);
                intent.putExtra("atlas", atlas);
                startActivity(intent);
            }
        });
        mAdapter.openLoadAnimation(1);
        mAdapter.setAutoLoadMoreSize(2);
        mAdapter.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        BmobQuery<Atlas> query = new BmobQuery<>();
        switch (mType) {
            case "推荐":
                query.order("-likeNum");
                break;
            case "风景":
                query.addWhereEqualTo("kind", mType);
                query.order("-createdAt");
                break;
            case "动物":
                query.addWhereEqualTo("kind", mType);
                query.order("-createdAt");
                break;
            case "其他":
                query.addWhereEqualTo("kind", mType);
                query.order("-createdAt");
                break;
        }
        query.include("author");
        query.setLimit(15);
        query.findObjects(new FindListener<Atlas>() {
            @Override
            public void done(List<Atlas> list, BmobException e) {
                mSwipeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                    }
                });

                if (e == null) {
                    mAdapter.setNewData(list);
                    if (list.size() < 15) {
                        mAdapter.loadMoreEnd();
                    }
                    Log.e("FreeStar", "PageFragment1→→→done:数据源改变");
                } else {
                    Log.e("FreeStar", "PageFragment1→→→done:+++++++++" + e.getMessage());
                }
            }
        });
        i++;
    }

//    private void initBanner(View header) {
//        String[] images = new String[5];
//        String[] titles = new String[5];
//        for (int i = 0; i < 5; i++) {
//            images[i] = mDatas.get(i).getMainPic();
//            titles[i] = mDatas.get(i).getTitle();
//        }
//
//        Banner banner = (Banner) header.findViewById(R.id.banner1);
//        //显示数字指示器和标题
//        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
//        //记得设置标题列表哦
//        banner.setBannerTitle(titles);
//        banner.setImages(images);//可以选择设置图片网址，或者资源文件，默认用Glide加载
//        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {//设置点击事件
//            @Override
//            public void OnBannerClick(View view, int position) {
//                Toast.makeText(getActivity(), "你点击了：" + position, Toast.LENGTH_LONG).show();
//            }
//        });
//    }

    @Override
    public void onRefresh() {
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
        initData();
    }

    private void startQuery() {
        BmobQuery<Atlas> query = new BmobQuery<>();
        switch (mType) {
            case "推荐":
                query.order("-likeNum");
                break;
            case "风景":
                query.addWhereEqualTo("kind", mType);
                query.order("-createdAt");
                break;
            case "动物":
                query.addWhereEqualTo("kind", mType);
                query.order("-createdAt");
                break;
            case "其他":
                query.addWhereEqualTo("kind", mType);
                query.order("-createdAt");
                break;
        }
        query.setSkip(15 * i); // 忽略前15条数据（即第一页数据结果）
        query.setLimit(15);

        query.findObjects(new FindListener<Atlas>() {
            @Override
            public void done(List<Atlas> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "PageFragment1→→→done:" + list.size());
                    if (list.size() < 15) {
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.addData(list);
                        mAdapter.loadMoreComplete();
                    }
                } else {
                    Log.e("FreeStar", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
        i++;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.fab)
    public void onClick() {
        startActivity(new Intent(getActivity(), PicturePublish.class));
    }

    @Override
    public void onLoadMoreRequested() {
        startQuery();
    }

}