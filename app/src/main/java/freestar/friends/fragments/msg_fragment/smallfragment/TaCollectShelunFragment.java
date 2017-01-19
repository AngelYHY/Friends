package freestar.friends.fragments.msg_fragment.smallfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.R;
import freestar.friends.activities.ArticleItemActivity;
import freestar.friends.bean.Article;
import freestar.friends.bean.CollectArticle;
import freestar.friends.bean.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaCollectShelunFragment extends Fragment {
    private ListView lv;
    private List<CollectArticle> mDatas  = new ArrayList<>();
    private MyCollect1Adapter mAdapter;
    private User user;

    public TaCollectShelunFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_ta_collect_shelun, container, false);
        lv = (ListView) view.findViewById(R.id.ta_collectshelun);
        Intent intent = getActivity().getIntent();
        user = (User) intent.getSerializableExtra("user");
        initData();
        addListener();
        return view;
    }
    private void addListener() {
        //  这是单击联系人的框可以聊天
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Article article = mDatas.get(i).getSources();
                Log.e("TAG", "onItemClick: "+article.getUrlList());
                Intent intent = new Intent(getActivity(),ArticleItemActivity.class);
                Log.e("FreeStar", "PageFragment1→→→onItemClick:" + article.toString() + i + "--------" + mDatas.size());
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });
    }


    public void initData(){
        User friend = new User();
        friend.setObjectId(user.getObjectId());
        //user.setObjectId("c8249e3753");
        BmobQuery<CollectArticle> query = new BmobQuery<>();
        query.include("sources");
        query.setLimit(15);
        query.order("-createdAt");
        query.addWhereEqualTo("user", user);
        query.findObjects(new FindListener<CollectArticle>() {
            @Override
            public void done(List<CollectArticle> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "CollectshelunFragment→→→done:" + list.size());
                    mDatas.addAll(list);
                    for (CollectArticle mDate : mDatas) {
                        Log.e("TAG", "shelundone: "+mDate.toString());

                        if (mAdapter == null){
                            mAdapter = new MyCollect1Adapter(mDatas, getActivity() );
                            lv.setAdapter(mAdapter);
                        }else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Log.e("FreeStar", "CollectshelunFragment→→→done:" + e.getMessage());
                }
            }
        });
    }
}
