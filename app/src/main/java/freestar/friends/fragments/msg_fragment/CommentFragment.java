package freestar.friends.fragments.msg_fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.AtlasDis;
import freestar.friends.bean.Message;
import freestar.friends.bean.Ninepic;
import freestar.friends.bean.User;

/**
 * 暂时定为  与我相关
 */
public class CommentFragment extends Fragment {
    ListView lv;

    ArrayList<AtlasDis> messageList;

    CommentAdapter adapter;


    public CommentFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
        lv = (ListView) view.findViewById(R.id.lv_comment);
        Log.e("comment", "-------进入comment碎片:");
        initData();

        initListData();
        return view;
    }


    private void initData() {
        //初始化数据
        messageList = new ArrayList<>();
    }


    public void initListData() {
        Log.e("bbb", "---进入查询集合的方法--");

        //  查询 图集和作者  的信息
        BmobQuery<AtlasDis> query = new BmobQuery<AtlasDis>();
        query.addWhereEqualTo("atlas", "413a855bc1");

      //  query.addQueryKeys("atlas,author");
        //   query.addWhereExists("author");
        query.include("cuser,father_user,comment,atlas,author,comment_father_user");
        //  query.include("atlas,cuser,author,father_user");

        //   query.setLimit(20);
        query.order("-createdAt");
        query.findObjects(new FindListener<AtlasDis>() {
            @Override
            public void done(List<AtlasDis> l, BmobException e) {
                if (e == null) {

                    Log.e("qqq", " l-------" + l.toString());
                    ArrayList<AtlasDis> list = new ArrayList<AtlasDis>();
                    for (int i = 0; i < l.size(); i++) {
                        list.add(new AtlasDis(l.get(i).getAtlas(), l.get(i).getAuthor()));
                    }
                    //  查询的是  动态的信息  和动态的作者信息
                    Log.e("qqq", "list--------" + list.toString());

                    //  去除集合中的重复值的
                    Set<AtlasDis> set = new HashSet<AtlasDis>(list);

                    list.clear();
                    list.addAll(set);
//                    for (int i = 0; i < list.size(); i++) {  //外循环是循环的次数
//                        for (int j = list.size() - 1; j > i; j--) { //内循环是 外循环一次比较的次数
//                            if (list.get(i).equals(list.get(j))) {
//                                list.remove(j);
//                            }
//                        }
//                    }

               //     messageList.addAll(list)
                    //   Log.e("qqq", "messageList--------" + messageList.toString());


                    //  把 图集和作者的信息  在适配器中显示
                    if (adapter == null) {
                        adapter = new CommentAdapter(list, getActivity(),l);
                        lv.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    Log.e("ccc", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });


    }

//    private void addListener() {
//        //下拉刷新
//        lv.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                //刷新数据,先清空集合中以后数据
//                messageList.clear();
//
//                getDataFromWeb(startIndex,pageCount);
//            }
//        });
//        //上拉加载
//        mListView.setOnLoadMoreListener(new PullAndLoadListView.OnLoadMoreListener() {
//            @Override
//            public void onLoadMore() {
//                //如果已经是最后一条数据，通知用户不必加载
//                if (mList.size() == newsCount){
//                    showToast("已加载全部数据");
//                    mListView.onLoadMoreComplete();
//                    return;
//                }
//                //加载更多，从以后的数据向后加载5条数据
//                startIndex = mList.size();
//                getDataFromWeb(startIndex,pageCount);
//            }
//        });
//    }


    void add() {
        //代码实现 当前用户 发表动态
        Message m = new Message();
        App app = (App) getActivity().getApplication();
        m.setUser(app.getUser());
        m.setM_message("hahahahahahaha");
        m.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(getActivity(), "成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "失败", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


}