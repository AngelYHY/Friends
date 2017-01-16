package freestar.friends.fragments.pages_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import freestar.friends.activities.PublishPicture;
import freestar.friends.bean.Atlas;
import freestar.friends.util.recycler_and_fab.recyclerview.CommonAdapter;
import freestar.friends.util.recycler_and_fab.recyclerview.DividerItemDecoration;
import freestar.friends.util.recycler_and_fab.recyclerview.base.ViewHolder;
import freestar.friends.util.recycler_and_fab.recyclerview.wrapper.HeaderAndFooterWrapper;
import freestar.friends.util.recycler_and_fab.recyclerview.wrapper.LoadMoreWrapper;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class PageFragment3 extends Fragment {
    private List<Atlas> mDatas = new ArrayList<>();
    private CommonAdapter<Atlas> mAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    int i = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        initData();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PublishPicture.class));
            }
        });
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        mAdapter = new CommonAdapter<Atlas>(getContext(), R.layout.item_layout, mDatas) {
            @Override
            protected void convert(ViewHolder holder, Atlas s, int position) {
                holder.setText(R.id.tv_time, s.getCreatedAt()).setSDV(R.id.iv_main, s.getMainPic()).setText(R.id.tv_disNum, s.getLikeNum() + "赞").setText(R.id.tv_title, s.getTitle() + "(" + s.getNum() + " 图)");

            }
        };

        HeaderAndFooterWrapper mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.e("FreeStar", "PageFragment1→→→run:");
                        BmobQuery<Atlas> query = new BmobQuery<>();
                        query.addWhereEqualTo("kind", "动物");
                        query.setSkip(10 * i); // 忽略前10*i条数据（即第一页数据结果）
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
                                } else {
                                    Log.e("FreeStar", "PageFragment1→→→done:" + e.getMessage());
                                }
                            }
                        });
                        mLoadMoreWrapper.notifyDataSetChanged();
                        i++;
                    }
                }, 3000);
            }
        });

        mRecyclerView.setAdapter(mLoadMoreWrapper);
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener<Atlas>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, Atlas o, int position) {
                Toast.makeText(getContext(), "pos = " + position, Toast.LENGTH_SHORT).show();
                mAdapter.notifyItemRemoved(position);
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, Atlas o, int position) {
                return false;
            }
        });
        return view;
    }

    private void initData() {
        BmobQuery<Atlas> query = new BmobQuery<>();
        query.addWhereEqualTo("kind", "动物");
        query.include("author");
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
                    Log.e("FreeStar", "PageFragment1→→→done:数据源改变");
                    mAdapter.notifyDataSetChanged();
                } else {
                    Log.e("FreeStar", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
    }
}
