package freestar.friends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.R;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.AtlasDis;
import freestar.friends.bean.User;
import freestar.friends.util.recycler_and_fab.recyclerview.CommonAdapter;
import freestar.friends.util.recycler_and_fab.recyclerview.DividerItemDecoration;
import freestar.friends.util.recycler_and_fab.recyclerview.base.ViewHolder;
import freestar.friends.util.recycler_and_fab.recyclerview.wrapper.EmptyWrapper;
import freestar.friends.util.recycler_and_fab.recyclerview.wrapper.HeaderAndFooterWrapper;
import freestar.friends.util.recycler_and_fab.recyclerview.wrapper.LoadMoreWrapper;

/**
 * Created by Administrator on 2016/7/12.
 */

public class SearchActivity extends AppCompatActivity {

    private static final int RESC = 9;
    @Bind(R.id.line_com)
    LinearLayout lineCom;
    @Bind(R.id.tv_search)
    Button tvSearch;
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.tv_head_bar)
    TextView tvHeadBar;
    @Bind(R.id.rv_comment)
    RecyclerView rvComment;
    private List<AtlasDis> mDatas = new ArrayList<>();
    private CommonAdapter<AtlasDis> mAdapter;
    private LoadMoreWrapper mLoadMoreWrapper;
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private Atlas atlas;
    int i = 0;
    boolean flag;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                startQuery();
            }
        }
    };

    private void startQuery() {

        Log.e("FreeStar", "SearchActivity→→→run:");
        BmobQuery<AtlasDis> query = new BmobQuery<>();
        query.addWhereEqualTo("atlas", new BmobPointer(atlas));
        query.include("cuser");
        query.order("-createdAt");
        query.setSkip(10 * i); // 忽略前10条数据（即第一页数据结果）
        query.setLimit(10);
        query.findObjects(new FindListener<AtlasDis>() {
            @Override
            public void done(List<AtlasDis> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "SearchActivity→→→done:" + list.size());
                    mDatas.addAll(list);
                    for (AtlasDis mData : mDatas) {
                        Log.e("FreeStar", "SearchActivity→→→done:" + mData.toString());
                    }
                    if (list.size() == 0) {
                        Toast.makeText(SearchActivity.this, "没有更多数据了", Toast.LENGTH_SHORT).show();
                        flag = true;
                    }
                    mLoadMoreWrapper.notifyDataSetChanged();
                } else {
                    Log.e("FreeStar", "SearchActivity→→→done:" + e.getMessage());
                }
            }
        });
        i++;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar_with_padding);
        ButterKnife.bind(this);
        atlas = (Atlas) getIntent().getSerializableExtra("object");
        Log.e("FreeStar", "SearchActivity→→→onCreate:" + atlas.toString());

        rvComment.setLayoutManager(new LinearLayoutManager(this));
        rvComment.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL_LIST));
        initData();

        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener<AtlasDis>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, final AtlasDis o, int position) {
                view.findViewById(R.id.line_reply).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchActivity.this, DiscussActivity.class);
                        intent.putExtra("object", o);
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + o.toString());
                        startActivity(intent);
                    }
                });
                view.findViewById(R.id.sdv_head).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchActivity.this, UserDataActivity.class);
                        intent.putExtra("user", o.getCuser());
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + o.getCuser());
                        startActivity(intent);
                    }
                });
                view.findViewById(R.id.sdv_father_head).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchActivity.this, UserDataActivity.class);
                        intent.putExtra("user", o.getFather_user());
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + o.getFather_user());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, AtlasDis o, int position) {
                return false;
            }
        });
    }

    //设置rv的适配器
    private void setRVAdapter() {
        mAdapter = new CommonAdapter<AtlasDis>(this, R.layout.dis_item_layout, mDatas) {
            @Override
            protected void convert(ViewHolder holder, AtlasDis s, int position) {
                User cuser = s.getCuser();
                holder.setSDV(R.id.sdv_head, cuser.getHeadUrl()).setText(R.id.tv_user_name, cuser.getNiname()).setText(R.id.tv_data, s.getCreatedAt()).setText(R.id.tv_disNum, s.getComment());
                Log.e("FreeStar", "SearchActivity→→→convert:" + s.toString());
                if (s.getComment_father_user() != null) {
                    AtlasDis atlasDis = s.getComment_father_user();
                    User father_user = s.getFather_user();
                    Log.e("FreeStar", "SearchActivity→→→convert:" + atlasDis.toString());
                    holder.setVis(R.id.line_father_comment).setSDV(R.id.sdv_father_head, father_user.getHeadUrl()).setText(R.id.tv_father_name, father_user.getNiname()).setText(R.id.tv_father_comment, atlasDis.getComment()).setText(R.id.tv_father_data, atlasDis.getCreatedAt());
                }
            }
        };
    }

    private void initData() {
        BmobQuery<AtlasDis> query = new BmobQuery<>();
        query.addWhereEqualTo("atlas", new BmobPointer(atlas));
        query.include("cuser,comment_father_user,father_user");
        query.setLimit(10);
        query.order("-createdAt");
        query.findObjects(new FindListener<AtlasDis>() {
            @Override
            public void done(List<AtlasDis> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "SearchActivity→→→done:" + list.size());
                    mDatas.addAll(list);
                    for (AtlasDis mData : mDatas) {
                        Log.e("FreeStar", "SearchActivity→→→done:" + mData.toString());
                    }
                    if (list.size() == 0) {
                        initEmptyView();
                    }
//                    else if (list.size() > 14) {
//                        initMore();
//                    }
                    else {
                        setCAdapter();
//                        rvComment.setAdapter(mAdapter);
                    }
                    Log.e("FreeStar", "SearchActivity→→→done:数据源改变");
                } else {
                    Log.e("FreeStar", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
    }

    private void setCAdapter() {
        mAdapter = new CommonAdapter<AtlasDis>(this, R.layout.dis_item_layout, mDatas) {
            @Override
            protected void convert(ViewHolder holder, AtlasDis s, int position) {
                User cuser = s.getCuser();
                holder.setSDV(R.id.sdv_head, cuser.getHeadUrl()).setText(R.id.tv_user_name, cuser.getNiname()).setText(R.id.tv_data, s.getCreatedAt()).setText(R.id.tv_disNum, s.getComment());
                Log.e("FreeStar", "SearchActivity→→→convert:" + s.toString());
                if (s.getComment_father_user() != null) {
                    AtlasDis atlasDis = s.getComment_father_user();
                    User father_user = s.getFather_user();
                    Log.e("FreeStar", "SearchActivity→→→convert:" + atlasDis.toString());
                    holder.setVis(R.id.line_father_comment).setSDV(R.id.sdv_father_head, father_user.getHeadUrl()).setText(R.id.tv_father_name, father_user.getNiname()).setText(R.id.tv_father_comment, atlasDis.getComment()).setText(R.id.tv_father_data, atlasDis.getCreatedAt());
                }
            }
        };
        initHeaderAndFooter();
        //初始化加载更多
        initMore();
        rvComment.setAdapter(mLoadMoreWrapper);
        mAdapter.setOnItemClickListener(new CommonAdapter.OnItemClickListener<AtlasDis>() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, final AtlasDis o, int position) {
                view.findViewById(R.id.line_reply).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchActivity.this, DiscussActivity.class);
                        intent.putExtra("object", o);
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + o.toString());
                        startActivity(intent);
                    }
                });
                view.findViewById(R.id.sdv_head).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchActivity.this, UserDataActivity.class);
                        intent.putExtra("user", o.getCuser());
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + o.getCuser());
                        startActivity(intent);
                    }
                });
                view.findViewById(R.id.sdv_father_head).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchActivity.this, UserDataActivity.class);
                        intent.putExtra("user", o.getFather_user());
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + o.getFather_user());
                        startActivity(intent);
                    }
                });
            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, AtlasDis o, int position) {
                return false;
            }
        });
    }

    private void initHeaderAndFooter() {
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        View header = LayoutInflater.from(this).inflate(R.layout.iv_layout_new, null);
        mHeaderAndFooterWrapper.addHeaderView(header);
    }

    private void initMore() {
        mLoadMoreWrapper = new LoadMoreWrapper(mHeaderAndFooterWrapper);
        mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        rvComment.setAdapter(mLoadMoreWrapper);

        mLoadMoreWrapper.setOnLoadMoreListener(new LoadMoreWrapper.OnLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                if (flag) {
                    handler.sendEmptyMessage(1);
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESC && resultCode == RESULT_OK) {
            String back = data.getStringExtra("OK");
            if (back.equals("YES")) {
                Intent intent = new Intent();
                intent.putExtra("OK", "YES");
                setResult(RESULT_OK, intent);
            }
        }
    }

    @OnClick({R.id.line_com, R.id.tv_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.line_com:
                Intent intent = new Intent(this, DiscussActivity.class);
                Log.e("FreeStar", "SearchActivity→→→onClick:" + atlas);
                intent.putExtra("object", atlas);
//                startActivity(intent);
                startActivityForResult(intent, RESC);
                break;
            case R.id.tv_search:
                finish();
                break;
        }
    }

    private void initEmptyView() {
        EmptyWrapper mEmptyWrapper = new EmptyWrapper(mAdapter);
        mEmptyWrapper.setEmptyView(LayoutInflater.from(this).inflate(R.layout.empty_view, rvComment, false));
        rvComment.setAdapter(mEmptyWrapper);
    }
}
