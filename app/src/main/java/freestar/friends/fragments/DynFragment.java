package freestar.friends.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.activities.CommentActivity;
import freestar.friends.activities.DyFabuActivity;
import freestar.friends.activities.SearchActivity3;
import freestar.friends.adapter.DongtaiAdapter;
import freestar.friends.bean.Message;
import freestar.friends.bean.User;
import freestar.friends.util.profile.MyinfoActivity;
import freestar.friends.util.profile.TypeActivity1;
import freestar.friends.util.view.XListView;

/**
 * SwipeRefreshLayout.OnRefreshListener
 */
public class DynFragment extends Fragment implements XListView.IXListViewListener {
    View view, view1;
    List<Message> mlist = new ArrayList<>();
    DongtaiAdapter dongTaiAdapter;
    SimpleDraweeView circleImageView;
    XListView xListView;
    Handler handler;
    int i;
    //london
    ImageView headview;
    private String[] listId;
    TextView my_connection, my_talk, my_give;

    public DynFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dynamiu, container, false);
        Log.d("FreeStar", "DynFragment→→→onCreateView");
        initview();
        initSource();
        return view;
    }

    private void initview() {
        xListView = (XListView) view.findViewById(R.id.dy_listview);
        //头
        view1 = LayoutInflater.from(getActivity()).inflate(R.layout.imageview, null);
        headview = (ImageView) view1.findViewById(R.id.image_view);
        //头像跳转
        circleImageView = (SimpleDraweeView) view1.findViewById(R.id.touxiang);
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(App.userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    circleImageView.setImageURI(user.getHeadUrl());
                }
            }
        });
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), MyinfoActivity.class);
                startActivity(intent);
                Log.d("FreeStar", "跳到个人中心界面--------------------------");
            }
        });
        //与我有关
        my_connection = (TextView) view1.findViewById(R.id.my_connection);
        my_connection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), SearchActivity3.class);
                startActivity(intent);
                Log.d("FreeStar", "跳到与我有关界面--------------------------");
            }
        });
        //我要发布
        my_give = (TextView) view1.findViewById(R.id.my_give);
        my_give.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), DyFabuActivity.class);
                startActivity(intent);
                Log.d("FreeStar", "跳到发布动态界面--------------------------");
            }
        });
        //我的说说
        my_talk = (TextView) view1.findViewById(R.id.my_talk);
        my_talk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), TypeActivity1.class);
                startActivity(intent);
                Log.d("FreeStar", "跳到我之前发布动态界面--------------------------");
            }
        });
        xListView.addHeaderView(view1);
        xListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (i - 2 != -1) {
                    Intent intent = new Intent(getActivity(), CommentActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("message", mlist.get(i - 2));
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
        //xlistview的刷新（属性）
        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);
        handler = new Handler();
    }

    //定义适配器----------------解决异步问题-------------------》
    private void initAdapter() {
        dongTaiAdapter = new DongtaiAdapter(getActivity(), mlist);
        xListView.setAdapter(dongTaiAdapter);
    }

    //数据源----------------list<Message>---------------------》
    private void initSource() {
        final BmobQuery<User> query = new BmobQuery<>();
        final User user = new User();
        user.setObjectId(App.userId);
        query.addWhereRelatedTo("user", new BmobPointer(user));
        //这是先查找出你的全部好友
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                listId = new String[list.size() + 1];
                for (int i = 0; i < list.size(); i++) {
                    listId[i] = list.get(i).getObjectId();
                }
                listId[listId.length - 1] = App.userId;
                //这是从动态里面 查找属于你的好友发的动态
                BmobQuery<Message> query1 = new BmobQuery<>();
                //user是列名
                query1.include("user");
                //每次限制10条
                query1.setLimit(10);
                //根据时间来查询
                query1.order("-createdAt");
                query1.addWhereContainedIn("user", Arrays.asList(listId));
                query1.findObjects(new FindListener<Message>() {
                    @Override
                    public void done(List<Message> list, BmobException e) {
                        if (e == null) {
                            mlist.addAll(list);
                            if (dongTaiAdapter == null) {
                                initAdapter();
                            } else {
                                dongTaiAdapter.notifyDataSetChanged();
                                xListView.stopRefresh();
                            }
                        } else {
                            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                i++;
            }
        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /*
    **刷新and加载
     */
    @Override
    public void onRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mlist.clear();
                i = 0;
                initSource();
//                onLoad();
            }
        }, 2000);
    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startQuery();
//                onLoad();
            }

        }, 2000);

    }

    //加载
    private void startQuery() {
        //这是从动态里面 查找属于你的好友发的动态
        BmobQuery<Message> query1 = new BmobQuery<>();
        //user是列名
        query1.include("user");
        //每次限制10条
        query1.setLimit(10);
        query1.setSkip(10 * i); // 忽略前10条数据（即第一页数据结果）
        //根据时间来查询
        query1.order("-createdAt");
        query1.addWhereContainedIn("user", Arrays.asList(listId));
        Log.e("FreeStar", "MainActivity→→→done:111111");
        query1.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                if (e == null) {
                    if (list.size() == 0) {
                        Toast.makeText(getActivity(), "没有数据了", Toast.LENGTH_SHORT).show();
                    } else {
                        mlist.addAll(list);
                        dongTaiAdapter.notifyDataSetChanged();
                    }
                    xListView.stopLoadMore();
                } else {
                    Log.e("FreeStar", "DynFragment→→→done:" + e.getMessage());
                    Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        i++;
    }

    @Override
    public void onResume() {
        super.onResume();
        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(App.userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    circleImageView.setImageURI(user.getHeadUrl());
                }
            }
        });
    }
}


