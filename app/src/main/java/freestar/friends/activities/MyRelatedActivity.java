package freestar.friends.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

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
import cn.bmob.v3.listener.SaveListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.adapter.DisSayAdapter;
import freestar.friends.bean.DisSay;
import freestar.friends.bean.User;
import freestar.friends.util.status_bar.BaseActivity;
import freestar.friends.widget.XRecyclerView;

/**
 * Created by Administrator on 2016/7/12.
 * 与我相关
 */

public class MyRelatedActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycler_view)
    XRecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    @Bind(R.id.kj_pl)
    EditText mKjPl;
    @Bind(R.id.kj_pl_fb)
    ImageView mKjPlFb;
    @Bind(R.id.kj_pl_slu)
    LinearLayout mKjPlSlu;

//    private List<DisSay> mDatas = new ArrayList<>();
//    private CommonAdapter<DisSay> mAdapter;

    int i;

    private User user;
    private DisSay disSay;
    private DisSayAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_related);
        ButterKnife.bind(this);

        user = new User();
        user.setObjectId(App.userId);
        mSwipeLayout.setOnRefreshListener(this);

        onRefresh();
        initRV();

    }

    private void initRV() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int i) {
                DisSay s = (DisSay) adapter.getItem(i);
                Intent intent = null;
                switch (view.getId()) {
                    case R.id.img_user_comment:
                        User cuser1 = s.getCuser();
                        intent = new Intent(MyRelatedActivity.this, UserDataActivity.class);
                        intent.putExtra("user", cuser1);
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + cuser1);
                        startActivity(intent);
                        break;
                    case R.id.btn_reply:
                        mKjPlSlu.setVisibility(View.VISIBLE);
                        //弹开虚拟键盘和输入框
                        InputMethodManagerup();
                        disSay = s;
                        break;
                    case R.id.img_message:
                        User user = s.getMessage().getUser();
                        intent = new Intent(MyRelatedActivity.this, UserDataActivity.class);
                        intent.putExtra("user", user);
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + user);
                        startActivity(intent);
                        break;
                    case R.id.source:
                        intent = new Intent(MyRelatedActivity.this, DynDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("message", s.getMessage());
                        intent.putExtras(bundle);
                        startActivity(intent);
                        break;
                }
            }
        });
        mAdapter = new DisSayAdapter();

        mAdapter.openLoadAnimation(5);
        mAdapter.setAutoLoadMoreSize(2);
        mAdapter.setOnLoadMoreListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initData() {
        BmobQuery<DisSay> query = new BmobQuery<>();
        query.addWhereEqualTo("author", new BmobPointer(user));
        query.include("cuser,message,message.user,author");
        query.setLimit(10);
        query.order("-createdAt");
        query.findObjects(new FindListener<DisSay>() {
            @Override
            public void done(List<DisSay> list, BmobException e) {
                mSwipeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                    }
                });
                if (e == null) {
                    mAdapter.setNewData(list);
                    if (list.size() < 10) {
                        Log.e("FreeStar", "MyRelatedActivity→→→done:");
                        mAdapter.loadMoreEnd();
                    }
                } else {
                    Log.e("FreeStar", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
        i++;
    }

    //弹开虚拟键盘和输入框
    private void InputMethodManagerup() {

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        mKjPl.setFocusable(true);
        mKjPl.setFocusableInTouchMode(true);
        mKjPl.requestFocus();
        mKjPl.requestFocusFromTouch();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        downKeyBoard();
        return super.onTouchEvent(event);
    }

    public void downKeyBoard() {
        if (mKjPlSlu.getVisibility() == View.VISIBLE) {
            mKjPlSlu.setVisibility(View.GONE);
            InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm2.hideSoftInputFromWindow(mKjPlFb.getWindowToken(), 0);
        }
    }

    private void startQuery() {
        BmobQuery<DisSay> query = new BmobQuery<>();
        query.addWhereEqualTo("author", new BmobPointer(user));
        query.include("cuser,message,message.user,author");
        query.setSkip(10 * i); // 忽略前10条数据（即第一页数据结果）
        query.setLimit(10);
        query.order("-createdAt");
        query.findObjects(new FindListener<DisSay>() {
            @Override
            public void done(List<DisSay> list, BmobException e) {
                if (e == null) {
                    if (list.size() < 10) {
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.addData(list);
                        mAdapter.loadMoreComplete();
                    }
                } else {
                    Log.d("FreeStar", "DynDetailActivity→→→done" + e.getMessage());
                }
            }
        });
        i++;
    }

//    private void setCAdapter() {
//        mAdapter = new CommonAdapter<DisSay>(this, R.layout.comment_adapter2, mDatas) {
//            @Override
//            protected void convert(ViewHolder holder, final DisSay s, int position) {
//                User cuser = s.getCuser();
//                holder.setSDV(R.id.img_user_comment, cuser.getHeadUrl()).setText(R.id.tv_name_comment, cuser.getNiname()).setText(R.id.tv_time_comment, s.getCreatedAt())
//                        .setText(R.id.tv_context, s.getComment()).setSDV(R.id.img_message, s.getAuthor().getHeadUrl()).setText(R.id.tv_user_message, s.getMessage().getM_message());
//
//                holder.setOnClickListener(R.id.img_user_comment, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        User cuser1 = s.getCuser();
//                        Intent intent = new Intent(MyRelatedActivity.this, UserDataActivity.class);
//                        intent.putExtra("user", cuser1);
//                        Log.e("FreeStar", "SearchActivity→→→onClick:" + cuser1);
//                        startActivity(intent);
//                    }
//                });
//
//                holder.setOnClickListener(R.id.btn_reply, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        mKjPlSlu.setVisibility(View.VISIBLE);
//                        //弹开虚拟键盘和输入框
//                        InputMethodManagerup();
//                        disSay = s;
//                    }
//                });
//
//                holder.setOnClickListener(R.id.img_message, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        User user = s.getMessage().getUser();
//                        Intent intent = new Intent(MyRelatedActivity.this, UserDataActivity.class);
//                        intent.putExtra("user", user);
//                        Log.e("FreeStar", "SearchActivity→→→onClick:" + user);
//                        startActivity(intent);
//                    }
//                });
//
//                holder.setOnClickListener(R.id.source, new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        Intent intent = new Intent(MyRelatedActivity.this, DynDetailActivity.class);
//                        Bundle bundle = new Bundle();
//                        bundle.putSerializable("message", s.getMessage());
//                        intent.putExtras(bundle);
//                        startActivity(intent);
//                    }
//                });
//
//            }
//        };
//        mListView.setAdapter(mAdapter);
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

    @OnClick({R.id.back_dongtai, R.id.kj_pl_fb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_dongtai:
                finish();
                break;
            case R.id.kj_pl_fb:
                Log.e("FreeStar", "DynDetailActivity→→→onClick:进入点击事件");
                String context = mKjPl.getText().toString().trim();
                if (context.equals("")) {
                    Toast.makeText(MyRelatedActivity.this, "写写东西吧", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    mKjPl.setText("");
                    downKeyBoard();
                }
                DisSay ds = new DisSay(user, disSay, context, disSay.getMessage(), disSay.getCuser(), disSay.getAuthor());
                ds.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        Log.e("FreeStar", "DynDetailActivity→→→done:人对人评论");
                        Toast.makeText(MyRelatedActivity.this, "评论成功", Toast.LENGTH_SHORT).show();
                    }
                });
                break;

        }
    }

    @Override
    public void onLoadMoreRequested() {
        startQuery();
    }
}
