package freestar.friends.fragment.dynamic.smallfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.activity.ArticleItemActivity;
import freestar.friends.adapter.ComArticleAdapter;
import freestar.friends.bean.Article;
import freestar.friends.bean.CollectArticle;
import freestar.friends.bean.User;
import freestar.friends.util.profile.CollectActivity;

/**
 * 我的收藏 摄论
 */
public class CollectArticleFragment extends Fragment {

    @Bind(R.id.rv)
    RecyclerView mRv;

    private ComArticleAdapter mAdapter;

    public CollectArticleFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collect_article, container, false);
        ButterKnife.bind(this, view);

        initRV();
        initData();
        return view;
    }

    private void initRV() {
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ComArticleAdapter(R.layout.rv_collect);
        mRv.setAdapter(mAdapter);
        mRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Article article = ((CollectArticle) adapter.getItem(position)).getSources();
                Intent intent = new Intent(getActivity(), ArticleItemActivity.class);
                intent.putExtra("article", article);
                startActivity(intent);
            }
        });
    }


    public void initData() {
        User user = new User();
        String id = CollectActivity.getId();
        if (id == null) {
            user.setObjectId(App.userId);
        } else {
            user.setObjectId(id);
        }
        BmobQuery<CollectArticle> query = new BmobQuery<>();
        query.include("sources");
        query.setLimit(15);
        query.order("-createdAt");
        query.addWhereEqualTo("user", user);
        query.findObjects(new FindListener<CollectArticle>() {
            @Override
            public void done(List<CollectArticle> list, BmobException e) {
                if (e == null) {
                    List<Article> articles = new ArrayList<>();
                    for (CollectArticle collectArticle : list) {
                        articles.add(collectArticle.getSources());
                    }
                    mAdapter.setNewData(articles);
                } else {
                    Log.e("FreeStar", "CollectshelunFragment→→→done:" + e.getMessage());
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
