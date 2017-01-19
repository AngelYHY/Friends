package freestar.friends.fragments.pages_fragment;

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
import freestar.friends.activities.ArticleItemActivity1;
import freestar.friends.activities.PublishArticle;
import freestar.friends.adapter.ArticleAdapter;
import freestar.friends.bean.Article;

public class ArticleListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    private int i;
    private ArticleAdapter mAdapter;
    private String mType;

    public ArticleListFragment() {
    }

    public static ArticleListFragment newInstance(String type) {
        ArticleListFragment fragment = new ArticleListFragment();
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
        mAdapter = new ArticleAdapter();
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Article article = (Article) adapter.getItem(position);
                Intent intent = new Intent(getActivity(), ArticleItemActivity1.class);
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });
        mAdapter.openLoadAnimation(1);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setAutoLoadMoreSize(2);
        mAdapter.setOnLoadMoreListener(this);
    }

    private void initData() {
        BmobQuery<Article> query = new BmobQuery<>();
        switch (mType) {
            case "推荐":
                query.order("-likeNum");
                break;
            case "技巧":
                query.addWhereEqualTo("kind", mType);
                query.order("-createdAt");
                break;
            case "器材":
                query.addWhereEqualTo("kind", mType);
                query.order("-createdAt");
                break;
            case "游记":
                query.addWhereEqualTo("kind", mType);
                query.order("-createdAt");
                break;
        }
        query.include("author");
        query.setLimit(15);
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                mSwipeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                    }
                });

                if (e == null) {
                    mAdapter.setNewData(list);
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

    @OnClick(R.id.fab)
    public void onClick() {
        startActivity(new Intent(getActivity(), PublishArticle.class));
    }


    private void startQuery() {
        BmobQuery<Article> query = new BmobQuery<>();
        switch (mType) {
            case "推荐":
                query.order("-likeNum");
                break;
            case "技巧":
                query.addWhereEqualTo("kind", mType);
                query.order("-createdAt");
                break;
            case "器材":
                query.addWhereEqualTo("kind", mType);
                query.order("-createdAt");
                break;
            case "游记":
                query.addWhereEqualTo("kind", mType);
                query.order("-createdAt");
                break;
        }
        query.setSkip(15 * i); // 忽略前15条数据（即第一页数据结果）
        query.setLimit(15);

        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "PageFragment1→→→done:" + list.size());
                    if (list.size() == 0) {
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
    public void onLoadMoreRequested() {
        startQuery();
    }
}