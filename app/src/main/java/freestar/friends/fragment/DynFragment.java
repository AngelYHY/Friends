package freestar.friends.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.activity.DynDetailActivity;
import freestar.friends.activity.publish.DynamicPublishActivity;
import freestar.friends.activity.MyRelatedActivity;
import freestar.friends.activity.UserDataActivity;
import freestar.friends.adapter.DynamicAdapter;
import freestar.friends.bean.Message;
import freestar.friends.bean.User;
import freestar.friends.activity.MyPublishActivity;
import freestar.friends.activity.setting.MyInfoActivity;

/**
 * SwipeRefreshLayout.OnRefreshListener
 */
public class DynFragment extends Fragment implements BaseQuickAdapter.RequestLoadMoreListener, SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {

    int i;

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    private String[] listId;
    private DynamicAdapter mAdapter;
    private SimpleDraweeView mTouxiang;

    public DynFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dynamiu, container, false);
        ButterKnife.bind(this, view);
        Log.d("FreeStar", "DynFragment→→→onCreateView");
        mSwipeLayout.setOnRefreshListener(this);
        initView();
        onRefresh();
        return view;
    }

    private void initView() {
        //头
        View headView = LayoutInflater.from(getActivity()).inflate(R.layout.imageview, null);

        mTouxiang = (SimpleDraweeView) headView.findViewById(R.id.touxiang);
        TextView connection = (TextView) headView.findViewById(R.id.my_connection);
        TextView talk = (TextView) headView.findViewById(R.id.my_talk);
        TextView give = (TextView) headView.findViewById(R.id.my_give);

        connection.setOnClickListener(this);
        talk.setOnClickListener(this);
        give.setOnClickListener(this);

        BmobQuery<User> query = new BmobQuery<>();
        query.getObject(App.userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    mTouxiang.setImageURI(user.getHeadUrl());
                }
            }
        });

        initRV(headView);
    }

    private void initRV(View headView) {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int i) {
                Message message = (Message) adapter.getItem(i);
                Intent intent = new Intent(getActivity(), DynDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("message", message);
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                if (view.getId() == R.id.dongtai_touxiang) {
                    Intent intent = new Intent(getActivity(), UserDataActivity.class);
                    intent.putExtra("user", ((Message) adapter.getItem(position)).getUser());
                    startActivity(intent);
                }
            }
        });

        mAdapter = new DynamicAdapter(getActivity());

        mAdapter.addHeaderView(headView);

        mAdapter.openLoadAnimation(2);
        mAdapter.setAutoLoadMoreSize(2);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
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
        ButterKnife.unbind(this);
    }

    /*
    **刷新and加载
     */
    @Override
    public void onRefresh() {
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
        initSource();
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
        query1.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                if (e == null) {
                    if (list.size() < 15) {
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.addData(list);
                        mAdapter.loadMoreComplete();
                    }
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
                    mTouxiang.setImageURI(user.getHeadUrl());
                }
            }
        });
    }

    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.touxiang:
                intent = new Intent(getActivity(), MyInfoActivity.class);
                break;
            //与我有关
            case R.id.my_connection:
                intent = new Intent(getActivity(), MyRelatedActivity.class);
                break;
            //我的说说
            case R.id.my_talk:
                intent = new Intent(getActivity(), MyPublishActivity.class);
                break;
            //我要发布
            case R.id.my_give:
                intent = new Intent(getActivity(), DynamicPublishActivity.class);
                break;
        }
        startActivity(intent);
    }

    @Override
    public void onLoadMoreRequested() {
        startQuery();
    }

}


