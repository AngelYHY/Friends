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
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.activities.ArticleItemActivity1;
import freestar.friends.bean.Article;
import freestar.friends.bean.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaTypeshelunFragment extends Fragment {

    private ListView lv;
    private List<Article> mDatas  = new ArrayList<>();
    private MyTyprAdapter mAdapter;
    private User user;
    public TaTypeshelunFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ta_typeshelun, container, false);
        lv = (ListView) view.findViewById(R.id.ta_shelun);
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
                Article article = mDatas.get(i);
                Log.e("TAG", "onItemClick: "+article.getUrlList());
                Intent intent = new Intent(getActivity(),ArticleItemActivity1.class);
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
        BmobQuery<Article> query = new BmobQuery<>();
        query.include("author");
        query.setLimit(15);
        query.order("-createdAt");
        query.addWhereEqualTo("author", user);
        query.findObjects(new FindListener<Article>() {
            @Override
            public void done(List<Article> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "TypeshelunFragment→→→done:" + list.size());
                    mDatas.addAll(list);
                    for (Article mDate : mDatas) {
                        Log.e("TAG", "TypeshelunFragment: "+mDate.toString());

                        if (mAdapter == null){
                            mAdapter = new MyTyprAdapter(mDatas, getActivity() );
                            lv.setAdapter(mAdapter);
                        }else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Log.e("FreeStar", "TypeshelunFragment→→→done:" + e.getMessage());
                }
            }
        });
    }

}
