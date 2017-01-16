package freestar.friends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.R;
import freestar.friends.adapter.AAdapterAtlasDis;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.AtlasDis;
import freestar.friends.util.status_bar.BaseActivity;
import freestar.friends.util.view.XListView;

/**
 * Created by Administrator on 2016/7/12.
 */

public class SearchActivity44 extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener {

    private static final int RESC = 9;
    @Bind(R.id.line_com)
    LinearLayout lineCom;
    @Bind(R.id.tv_search)
    Button tvSearch;
    @Bind(R.id.linearLayout)
    LinearLayout linearLayout;
    @Bind(R.id.tv_head_bar)
    TextView tvHeadBar;
    @Bind(R.id.back_dongtai)
    ImageView backDongtai;

    private Atlas atlas;
    private List<AtlasDis> mDatas = new ArrayList<>();
    //    private CommonAdapter<AtlasDis> mAdapter;
    private AAdapterAtlasDis mAdapter;
    private Handler mHandler;
    int i;
    private XListView mListView;
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar_with_padding1);
        ButterKnife.bind(this);
        mListView = (XListView) findViewById(R.id.xListView);
        atlas = (Atlas) getIntent().getSerializableExtra("object");
        Log.e("FreeStar", "SearchActivity→→→onCreate:" + atlas.toString());
        initData();
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mHandler = new Handler();
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
                if (e == null) {
                    mDatas.addAll(list);
                    if (mAdapter == null) {
                        setCAdapter();
                    } else {
                        mAdapter.notifyDataSetChanged();
//                        mListView.stopRefresh();
                    }
                    Log.e("FreeStar", "SearchActivity→→→done:数据源改变");
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
                    Log.e("FreeStar", "SearchActivity→→→done:" + list.size());
                    if (list.size() == 0) {
                        Toast.makeText(SearchActivity44.this, "没有数据了", Toast.LENGTH_SHORT).show();
                    } else {
                        mDatas.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
//                    mListView.stopLoadMore();
                } else {
                    Log.e("FreeStar", "SearchActivity→→→done:" + e.getMessage());
                }
            }
        });
        i++;
    }

    private void setCAdapter() {
//        mAdapter = new CommonAdapter<AtlasDis>(this, R.layout.dis_item_layout, mDatas) {
//            @Override
//            protected void convert(ViewHolder holder, final AtlasDis s, int position) {
//                User cuser = s.getCuser();
//                holder.setSDV(R.id.sdv_head, cuser.getHeadUrl()).setText(R.id.tv_user_name, cuser.getNiname()).setText(R.id.tv_data, s.getCreatedAt()).setText(R.id.tv_disNum, s.getComment());
//                Log.e("FreeStar", "SearchActivity→→→convert:" + s.toString());
//                if (s.getComment_father_user() != null) {
//                    AtlasDis atlasDis = s.getComment_father_user();
//                    User father_user = s.getFather_user();
//                    Log.e("FreeStar", "SearchActivity→→→convert:" + atlasDis.toString());
//                    holder.setVis(R.id.line_father_comment).setSDV(R.id.sdv_father_head, father_user.getHeadUrl()).setText(R.id.tv_father_name, father_user.getNiname()).setText(R.id.tv_father_comment, atlasDis.getComment()).setText(R.id.tv_father_data, atlasDis.getCreatedAt());
//                }
//                holder.setOnClickListener(R.id.line_reply, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(SearchActivity44.this, DiscussActivity.class);
//                        intent.putExtra("object", s);
//                        intent.putExtra("user", s.getAuthor());
//                        Log.e("FreeStar", "SearchActivity→→→onClick:" + s.toString());
////                        startActivity(intent);
//                        startActivityForResult(intent, RESC);
//                    }
//                });
//                holder.setOnClickListener(R.id.sdv_head, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(SearchActivity44.this, UserDataActivity.class);
//                        intent.putExtra("user", s.getCuser());
//                        Log.e("FreeStar", "SearchActivity→→→onClick:" + s.getCuser());
//                        startActivity(intent);
//                    }
//                });
//                holder.setOnClickListener(R.id.sdv_father_head, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(SearchActivity44.this, UserDataActivity.class);
//                        intent.putExtra("user", s.getFather_user());
//                        Log.e("FreeStar", "SearchActivity→→→onClick:" + s.getFather_user());
//                        startActivity(intent);
//                    }
//                });
//            }
//        };


        mAdapter = new AAdapterAtlasDis(mDatas, SearchActivity44.this);
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatas.clear();
                i = 0;
                initData();
                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startQuery();
                onLoad();
            }
        }, 2000);
    }

    private void onLoad() {
        mListView.stopRefresh();
        mListView.stopLoadMore();
        mListView.setRefreshTime(new Date().toLocaleString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESC && resultCode == RESULT_OK) {
            String back = data.getStringExtra("OK");
            Log.e("FreeStar", "SearchActivity1→→→onActivityResult:88888" + back);
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
                Log.e("FreeStar", "SearchActivity→→→onClick:" + atlas);
                intent.putExtra("object", atlas);
//                startActivity(intent);
                startActivityForResult(intent, RESC);
                break;
            case R.id.tv_search:
                finish();
                break;
            case R.id.back_dongtai:
                finish();
                break;
        }
    }

    @OnClick(R.id.back_dongtai)
    public void onClick() {
        finish();
    }
}
