package freestar.friends.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import cn.bmob.v3.listener.SaveListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.DisSay;
import freestar.friends.bean.User;
import freestar.friends.util.abslistview.CommonAdapter;
import freestar.friends.util.abslistview.ViewHolder;
import freestar.friends.util.status_bar.BaseActivity;
import freestar.friends.util.view.XListView;

/**
 * Created by Administrator on 2016/7/12.
 */

public class SearchActivity3 extends BaseActivity implements XListView.IXListViewListener, View.OnClickListener {

    @Bind(R.id.back_dongtai)
    ImageView backDongtai;
    private List<DisSay> mDatas = new ArrayList<>();
    private CommonAdapter<DisSay> mAdapter;
    private Handler mHandler;
    int i;
    private XListView mListView;
    private User user;
    ImageView sumbit_conments;
    LinearLayout contain_layout;
    EditText conments;
    private DisSay disSay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_bar_with_padding2);
        ButterKnife.bind(this);
        mListView = (XListView) findViewById(R.id.xListView);
        contain_layout = (LinearLayout) findViewById(R.id.kj_pl_slu);
        sumbit_conments = (ImageView) findViewById(R.id.kj_pl_fb);
        conments = (EditText) findViewById(R.id.kj_pl);
        user = new User();
        user.setObjectId(App.userId);
        initData();
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mHandler = new Handler();
        sumbit_conments.setOnClickListener(this);
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
                if (e == null) {
                    Log.e("FreeStar", "SearchActivity→→→done:" + list.size());
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

    //弹开虚拟键盘和输入框
    private void InputMethodManagerup() {

        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        conments.setFocusable(true);
        conments.setFocusableInTouchMode(true);
        conments.requestFocus();
        conments.requestFocusFromTouch();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Downkeyboard();
        return super.onTouchEvent(event);
    }

    public void Downkeyboard() {
        if (contain_layout.getVisibility() == View.VISIBLE) {
            contain_layout.setVisibility(View.GONE);
            InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm2.hideSoftInputFromWindow(sumbit_conments.getWindowToken(), 0);
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
                    Log.e("FreeStar", "SearchActivity→→→done:" + list.size());
                    if (list.size() == 0) {
                        Toast.makeText(SearchActivity3.this, "没有数据了", Toast.LENGTH_SHORT).show();
                    } else {
                        mDatas.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
//                    mListView.stopLoadMore();
                    Log.e("FreeStar", "SearchActivity→→→done:数据源改变");
                } else {
                    Log.e("FreeStar", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
        i++;
    }

    private void setCAdapter() {
        mAdapter = new CommonAdapter<DisSay>(this, R.layout.comment_adapter2, mDatas) {
            @Override
            protected void convert(ViewHolder holder, final DisSay s, int position) {
                User cuser = s.getCuser();
                holder.setSDV(R.id.img_user_comment, cuser.getHeadUrl()).setText(R.id.tv_name_comment, cuser.getNiname()).setText(R.id.tv_time_comment, s.getCreatedAt())
                        .setText(R.id.tv_context, s.getComment()).setSDV(R.id.img_message, s.getAuthor().getHeadUrl()).setText(R.id.tv_user_message, s.getMessage().getM_message());

                holder.setOnClickListener(R.id.img_user_comment, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        User cuser1 = s.getCuser();
                        Intent intent = new Intent(SearchActivity3.this, UserDataActivity.class);
                        intent.putExtra("user", cuser1);
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + cuser1);
                        startActivity(intent);
                    }
                });
                holder.setOnClickListener(R.id.btn_reply, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        contain_layout.setVisibility(View.VISIBLE);
                        //弹开虚拟键盘和输入框
                        InputMethodManagerup();
                        disSay = s;
                    }
                });

                holder.setOnClickListener(R.id.img_message, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        User user = s.getMessage().getUser();
                        Intent intent = new Intent(SearchActivity3.this, UserDataActivity.class);
                        intent.putExtra("user", user);
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + user);
                        startActivity(intent);
                    }
                });
                holder.setOnClickListener(R.id.source, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SearchActivity3.this, DynDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("message", s.getMessage());
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });


            }
        };
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
    public void onClick(View view) {
        Log.e("FreeStar", "DynDetailActivity→→→onClick:进入点击事件");
        String context = conments.getText().toString().trim();
        if (context.equals("")) {
            Toast.makeText(SearchActivity3.this, "写写东西吧", Toast.LENGTH_SHORT).show();
            return;
        } else {
            conments.setText("");
            Downkeyboard();
        }
        DisSay ds = new DisSay(user, disSay, context, disSay.getMessage(), disSay.getCuser(), disSay.getAuthor());
        ds.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                Log.e("FreeStar", "DynDetailActivity→→→done:人对人评论");
//                initSourceAdapter();
                Toast.makeText(SearchActivity3.this, "评论成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @OnClick(R.id.back_dongtai)
    public void onClick() {
        finish();
    }
}
