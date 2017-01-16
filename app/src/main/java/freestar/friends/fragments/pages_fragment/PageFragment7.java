package freestar.friends.fragments.pages_fragment;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import freestar.friends.bean.Article;
import freestar.friends.util.recycler_and_fab.recyclerview.CommonAdapter;
import freestar.friends.util.recycler_and_fab.recyclerview.DividerItemDecoration;
import freestar.friends.util.recycler_and_fab.recyclerview.base.ViewHolder;
import freestar.friends.util.recycler_and_fab.recyclerview.wrapper.HeaderAndFooterWrapper;
import freestar.friends.util.recycler_and_fab.recyclerview.wrapper.LoadMoreWrapper;

/**
 * Created by Administrator on 2016/7/13 0013.
 */
public class PageFragment7 extends Fragment {
    private List<Article> mDatas = new ArrayList<>();
    private CommonAdapter<Article> mAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    int count;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one, container, false);
        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        initData();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL_LIST));

        mAdapter = new CommonAdapter<Article>(getContext(), R.layout.item_layout, mDatas) {
            @Override
            protected void convert(ViewHolder holder, Article s, int position) {
//                holder.setImageResource(R.id.main_picture, mDatas.get(position).getIdPicture()).setText(R.id.date, mDatas.get(position).getDate())
//                        .setText(R.id.num, mDatas.get(position).getDisNum());
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
                        for (int i = 0; i < 7; i++) {
//                            mDatas.add(new Article1("小邋遢，这有有个小邋遢，邋遢大王就是他." + count, R.mipmap.ic_test_0, "2016.02.0" + i, "" + count++));
                        }
                        mLoadMoreWrapper.notifyDataSetChanged();
                    }
                }, 3000);
            }
        });

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

    private void initData() {
        BmobQuery<Article> query = new BmobQuery<>();
        query.addWhereEqualTo("kind", "奇彩");
        query.setLimit(10);
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "PageFragment2→→→done:" + list.size());
                    mDatas.addAll(list);
                    for (Article mData : mDatas) {
                        Log.e("FreeStar", "PageFragment2→→→done:" + mData.toString());
                    }
                } else {
                    Log.e("FreeStar", "PageFragment2→→→done:" + e.getMessage());
                }
            }
        });
    }
}
