package freestar.friends.fragment.message;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.activity.ArticleItemActivity;
import freestar.friends.activity.DiscussActivity;
import freestar.friends.activity.PicItemActivity;
import freestar.friends.activity.UserDataActivity;
import freestar.friends.adapter.ComDisAdapter;
import freestar.friends.bean.Article;
import freestar.friends.bean.ArticleDis;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.AtlasDis;
import freestar.friends.bean.User;
import freestar.friends.util.TimeComparator;

import static freestar.friends.R.layout.comment_adapter1;

/**
 * 社区评论
 */
public class CommDisFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener {

    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;
    private User user;
    private List<Object> messageList;
    private int j;
    private boolean start;
    boolean end;
    private ComDisAdapter mAdapter;
    private int i;
    List<Object> mDate = new ArrayList<>();

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //刷新
            if (msg.what == 1) {
                if (start && end) {
                    if (j != 0) {
                        Collections.sort(messageList, new TimeComparator());
                        mSwipeLayout.post(new Runnable() {
                            @Override
                            public void run() {
                                mSwipeLayout.setRefreshing(false);
                            }
                        });
                        mAdapter.setNewData(messageList);
                        if (messageList.size() < 20) {
                            mAdapter.loadMoreEnd();
                        }
                        start = false;
                        end = false;
                        j = 0;
                    }
                }
            } else if (msg.what == 2) {  //加载
                if (start && end) {
                    if (j != 0) {
                        Collections.sort(mDate, new TimeComparator());
                        if (mDate.size() < 20) {
                            mAdapter.loadMoreEnd();
                        } else {
                            mAdapter.addData(mDate);
                            mAdapter.loadMoreComplete();
                        }
                        start = false;
                        end = false;
                        j = 0;
                    }
                }
            }
        }
    };

    public CommDisFragment() {
        messageList = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_com_dis, container, false);
        ButterKnife.bind(this, view);
        mSwipeLayout.setOnRefreshListener(this);
        user = new User();
        user.setObjectId(App.userId);

        onRefresh();
        initRV();

        return view;
    }

    private void initRV() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int i) {
                Object item = adapter.getItem(i);
                ArticleDis articleDis;
                AtlasDis atlasDis;
                Intent intent;

                switch (view.getId()) {
                    case R.id.btn_reply:
                        intent = new Intent(getActivity(), DiscussActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("bundle", (Serializable) item);
                        intent.putExtras(bundle);
//                        intent.putExtra("object", s);
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + item.toString());
                        startActivity(intent);
                        break;
                    case R.id.source:
                        if (item instanceof ArticleDis) {
                            Article article = ((ArticleDis) item).getArticle();
                            intent = new Intent(getActivity(), ArticleItemActivity.class);
//                            Log.e("FreeStar", "PageFragment1→→→onItemClick:" + article.toString() + i + "--------" + mDatas.size());
                            intent.putExtra("article", article);
                            intent.putExtra("user", ((ArticleDis) item).getArticle().getAuthor());
                            Log.e("FreeStar", "CommunityDisFragment→→→onClick:+++" + ((ArticleDis) item).getAuthor());
                            startActivity(intent);
                        } else {
                            Atlas atlas = ((AtlasDis) item).getAtlas();
                            intent = new Intent(getActivity(), PicItemActivity.class);
//                            Log.e("FreeStar", "PageFragment1→→→onItemClick:" + atlas.toString() + i + "--------" + mDatas.size());
                            intent.putExtra("atlas", atlas);
//                            Log.e("FreeStar", "CommunityDisFragment→→→onClick:6666" + ((AtlasDis) item).getAtlas().getAuthor().toString());
                            intent.putExtra("user", ((AtlasDis) item).getAtlas().getAuthor());
                            startActivity(intent);
                        }
                        break;
                    case R.id.img_user_comment:
                        User cuser = null;
                        if (item instanceof ArticleDis) {
                            articleDis = (ArticleDis) item;
                            cuser = articleDis.getCuser();

                        } else if (item instanceof AtlasDis) {
                            atlasDis = (AtlasDis) item;
                            cuser = atlasDis.getCuser();
                        }
                        intent = new Intent(getActivity(), UserDataActivity.class);
                        intent.putExtra("user", cuser);
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + cuser);
                        startActivity(intent);
                        break;
                }
            }
        });

        mAdapter = new ComDisAdapter(comment_adapter1);
        mAdapter.openLoadAnimation();
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
    }

    private void initData() {
        //  查询 图集和作者  的信息
        BmobQuery<AtlasDis> query = new BmobQuery<>();
        query.addWhereEqualTo("author", user);
        Log.e("FreeStar", "CommunityDisFragment→→→initData:" + "id" + user.getObjectId());
        query.include("cuser,atlas,author,atlas.author");
        query.setLimit(10);
        query.findObjects(new FindListener<AtlasDis>() {
            @Override
            public void done(List<AtlasDis> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (AtlasDis atlasDis : list) {
                            Log.e("FreeStar", "CommunityDisFragment→→→done:" + "id:" + atlasDis.getObjectId());
                        }
                        messageList.addAll(list);
                        j++;
                    }
                    start = true;
                    handler.sendEmptyMessage(1);
                } else {
                    Log.e("ccc", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });

        BmobQuery<ArticleDis> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("author", user);
        query1.include("cuser,article,author,article.author");
        query1.setLimit(10);
        query1.findObjects(new FindListener<ArticleDis>() {
            @Override
            public void done(List<ArticleDis> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        for (ArticleDis articleDis : list) {
                            Log.e("FreeStar", "CommunityDisFragment→→→done:id:" + articleDis.getObjectId());
                        }
                        messageList.addAll(list);
                        j++;
                    }
                    end = true;
                    handler.sendEmptyMessage(1);
                } else {
                    Log.e("ccc", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
        i++;
    }

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


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onLoadMoreRequested() {
        Log.e("bbb", "---进入查询集合的方法--");

        //  查询 图集和作者  的信息
        BmobQuery<AtlasDis> query = new BmobQuery<>();
        query.addWhereEqualTo("author", user);
        query.include("cuser,atlas,author");
//        query.order("-createdAt");
        query.setLimit(10);
        query.setSkip(10 * i);
        query.findObjects(new FindListener<AtlasDis>() {
            @Override
            public void done(List<AtlasDis> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        mDate.addAll(list);
                        j++;
                    }
                    start = true;
                    handler.sendEmptyMessage(2);
                } else {
                    Log.e("ccc", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });

        BmobQuery<ArticleDis> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("author", user);
        query1.include("cuser,article,author");
        query1.setLimit(10);
        query1.setSkip(10 * i);
        query1.findObjects(new FindListener<ArticleDis>() {
            @Override
            public void done(List<ArticleDis> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        messageList.addAll(list);
                        j++;
                    }
                    end = true;
                    handler.sendEmptyMessage(2);
                } else {
                    Log.e("ccc", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
        i++;
    }
}
