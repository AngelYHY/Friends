package freestar.friends.fragments.msg_fragment;


import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.activities.UserDataActivity;
import freestar.friends.bean.User;
import freestar.friends.util.abslistview.CommonAdapter;
import freestar.friends.util.abslistview.ViewHolder;
import io.rong.imkit.RongIM;

/**
 * 项目 联系人界面
 */
public class SystemFragment extends Fragment {


    ListView mListView;

    public List<User> getFriendList() {
        return friendList;
    }

    List<User> friendList;
    private CommonAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_system, container, false);
        initListData();

        mListView = (ListView) view.findViewById(R.id.lv_lianxiren);
        addListener();

        return view;
    }

    private void initListData() {
        //初始化数据
        friendList = new ArrayList<>();
        query();
    }

    private void addListener() {
        //  这是单击联系人的框可以聊天
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = friendList.get(i).getNiname();
                String id = friendList.get(i).getObjectId();
                RongIM.getInstance().startPrivateChat(getActivity(), id, name);

            }
        });


        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setIcon(R.drawable.trash);
                builder.setTitle("删除").setMessage("确定删除此好友？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User u = friendList.get(position);

                        friendList.remove(position);

                        User me = new User();
                        me.setObjectId(App.userId);

//                        User user = new User();
//                        user.setObjectId(u.getObjectId());
                        Log.e("删除的id", "删除的id" + u.toString() + "   " + u.getObjectId());

                        BmobRelation relation = new BmobRelation();
                        relation.remove(u);

                        me.setUser(relation);
                        me.update(new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
//                                    BmobQuery<User> query = new BmobQuery<>();
//                                    User user = new User();
//                                    user.setObjectId(App.userId);   //当前账号id
//
//                                    query.addWhereRelatedTo("user", new BmobPointer(user));//多对多
//                                    query.findObjects(new FindListener<User>() {
//                                        @Override
//                                        public void done(List<User> list, BmobException e) {
//                                            if (e == null) {
//                                                friendList.clear();
//                                                friendList.addAll(list);
//                                                //  现在需要的是把这个对象传到适配器中
//                                                if (mAdapter == null) {
//                                                    mAdapter =
//                                                            mListView.setAdapter(mAdapter);
//                                                } else {
//                                                    mAdapter.notifyDataSetChanged();
//                                                }
//
//                                            } else {
//                                                Log.e("FreeStar", "MainActivity→→→done:" + e.getMessage());
//                                            }
//                                        }
//                                    });

                                    mAdapter.notifyDataSetChanged();
                                    Toast.makeText(getActivity(), "删除好友成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getActivity(), "删除好友失败", Toast.LENGTH_SHORT).show();
                                    Log.e("删除好友失败", e.getMessage() + "删除好友失败:" + e.getErrorCode());
                                }
                            }

                        });


                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(), "点击了取消", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
                return true;
            }
        });
        mListView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                Log.e("ListView", "onScrollStateChanged");
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });

        mListView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.e("ListView", "onItemSelected:" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.e("ListView", "onNothingSelected:");
            }
        });


    }


    private void query() {
        //查询出联系人 显示出来
        BmobQuery<User> query = new BmobQuery<>();
        User user = new User();
        user.setObjectId(App.userId);   //当前账号id

        query.addWhereRelatedTo("user", new BmobPointer(user));//多对多
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    if (mAdapter == null) {
                        friendList.addAll(list);
                        setAdapter();
                    } else {
                        friendList.clear();
                        friendList.addAll(list);
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("FreeStar", "MainActivity→→→done:++++" + e.getMessage());
                }
            }
        });

    }

    private void setAdapter() {
        mAdapter = new CommonAdapter<User>(getActivity(), R.layout.listview_item1, friendList) {
            @Override
            protected void convert(ViewHolder viewHolder, final User item, int position) {
                viewHolder.setSDV(R.id.img_chat_friend, item.getHeadUrl()).setText(R.id.text_data, item.getNiname());
                viewHolder.setOnClickListener(R.id.img_chat_friend, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, UserDataActivity.class);
                        intent.putExtra("user", item);
                        startActivity(intent);
                    }
                });
            }
        };
        mListView.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onResume() {
        super.onResume();

        query();


//        BmobQuery<User> query = new BmobQuery<>();
//        User user = new User();
//        user.setObjectId(App.userId);   //当前账号id
//
//        query.addWhereRelatedTo("user", new BmobPointer(user));//多对多
//        query.findObjects(new FindListener<User>() {
//            @Override
//            public void done(List<User> list, BmobException e) {
//                if (e == null) {
//
//                    friendList.clear();
//                    friendList.addAll(list);
//                    mAdapter.notifyDataSetChanged();
//
//                    //  现在需要的是把这个对象传到适配器中
////                    if (mAdapter == null) {
////                        mAdapter = new MyBaseAdapter(friendList, getActivity());
////                        mListView.setAdapter(adapter);
////                    } else {
////                        adapter.notifyDataSetChanged();
////                    }
//                } else {
//                    Log.e("FreeStar", "MainActivity→→→done:" + e.getMessage());
//                }
//            }
//        });


    }
}
