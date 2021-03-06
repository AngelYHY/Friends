package freestar.friends.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.R;
import freestar.friends.adapter.DiscussAtlasDetailAdapter;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.AtlasDis;
import freestar.friends.util.status_bar.BaseActivity;
import freestar.friends.widget.XRecyclerView;

/**
 * 图集评论
 */

public class DiscussAtlasDetailActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    private static final int RESC = 9;

    @Bind(R.id.recycler_view)
    XRecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    private Atlas atlas;

    int i;
    private DiscussAtlasDetailAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar_with_padding1);
        ButterKnife.bind(this);

        atlas = (Atlas) getIntent().getSerializableExtra("object");

        initRV();

        mSwipeLayout.setOnRefreshListener(this);

        onRefresh();
    }

    private void initRV() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int i) {
                AtlasDis s = (AtlasDis) adapter.getItem(i);
                Intent intent;
                switch (view.getId()) {
                    case R.id.line_reply:
                        intent = new Intent(DiscussAtlasDetailActivity.this, DiscussActivity.class);
                        intent.putExtra("object", s);
                        intent.putExtra("user", s.getAuthor());
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + s.toString());
                        startActivityForResult(intent, RESC);
                        break;
                    case R.id.sdv_head:
                        intent = new Intent(DiscussAtlasDetailActivity.this, UserDataActivity.class);
                        intent.putExtra("user", s.getCuser());
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + s.getCuser());
                        startActivity(intent);
                        break;
                    case R.id.btn_name:
                        intent = new Intent(DiscussAtlasDetailActivity.this, UserDataActivity.class);
                        intent.putExtra("user", s.getFather_user());
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + s.getFather_user());
                        startActivity(intent);
                        break;
                }

            }
        });

        mAdapter = new DiscussAtlasDetailAdapter();
        mAdapter.openLoadAnimation(1);
        mAdapter.setAutoLoadMoreSize(4);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        BmobQuery<AtlasDis> query = new BmobQuery<>();
        query.addWhereEqualTo("atlas", new BmobPointer(atlas));
        query.include("cuser,comment_father_user,father_user,author");
        query.setLimit(10);
        query.order("-createdAt");
        query.findObjects(new FindListener<AtlasDis>() {
            @Override
            public void done(List<AtlasDis> list, BmobException e) {
                mSwipeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                    }
                });
                if (e == null) {
                    mAdapter.setNewData(list);
                    if (list.size() < 10) {
                        mAdapter.loadMoreEnd();
                    }
                } else {
                    Log.e("FreeStar", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
        i++;
    }

    private void startQuery() {

        Log.e("FreeStar", "SearchActivity→→→run:");
        BmobQuery<AtlasDis> query = new BmobQuery<>();
        query.addWhereEqualTo("atlas", new BmobPointer(atlas));
        query.include("cuser,comment_father_user,father_user");
        query.order("-createdAt");
        query.setSkip(10 * i); // 忽略前10条数据（即第一页数据结果）
        query.setLimit(10);
        query.findObjects(new FindListener<AtlasDis>() {
            @Override
            public void done(List<AtlasDis> list, BmobException e) {
                if (e == null) {
                    if (list.size() < 10) {
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.addData(list);
                        mAdapter.loadMoreComplete();
                    }
                } else {
                    Log.e("FreeStar", "SearchActivity→→→done:" + e.getMessage());
                }
            }
        });
        i++;
    }

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESC && resultCode == RESULT_OK) {
            String back = data.getStringExtra("OK");
            if (back.equals("YES")) {
                onRefresh();
                Intent intent = new Intent();
                intent.putExtra("OK", "YES");
                setResult(RESULT_OK, intent);
            } else if (back.equals("fresh")) {
                onRefresh();
            }
        }
    }

    @OnClick({R.id.line_com, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.line_com:
                Intent intent = new Intent(this, DiscussActivity.class);
                intent.putExtra("object", atlas);
                startActivityForResult(intent, RESC);
                break;
            case R.id.tv_search:
                finish();
                break;
        }
    }

    @OnClick(R.id.back_dongtai)
    public void onClick() {
        finish();
    }

    @Override
    public void onLoadMoreRequested() {
        startQuery();
    }
}
