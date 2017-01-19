package freestar.friends.fragments.msg_fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.activities.ArticleItemActivity;
import freestar.friends.activities.DiscussActivity;
import freestar.friends.activities.PicItemActivity;
import freestar.friends.activities.UserDataActivity;
import freestar.friends.bean.Article;
import freestar.friends.bean.ArticleDis;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.AtlasDis;
import freestar.friends.bean.User;
import freestar.friends.util.TimeComparator;
import freestar.friends.util.abslistview.CommonAdapter;
import freestar.friends.util.abslistview.ViewHolder;
import freestar.friends.util.view.XListView;

/**
 * 社区评论
 */
public class CommunityDisFragment extends Fragment implements XListView.IXListViewListener {
    XListView lv;

    ArrayList<Object> messageList = new ArrayList<>();
    EditText conments;
    private User user;
    private CommonAdapter<Object> mAdapter;
    private Button sumbit_conments;
    private LinearLayout contain_layout;
    private Object object;
    private Handler mHandler;
    ArrayList<Object> mDate = new ArrayList<>();
    int j;

    public CommunityDisFragment() {

    }

    int i;

    boolean start;
    boolean end;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (start && end) {
                    if (j != 0) {
                        Collections.sort(messageList, new TimeComparator());
                        if (mAdapter == null) {
                            setAdapter();
                        } else {
                            mAdapter.notifyDataSetChanged();
                            lv.stopRefresh();
                        }
                        start = false;
                        end = false;
                        j = 0;
                    }
                }
            } else if (msg.what == 2) {
                if (start && end) {
                    if (j != 0) {
                        Collections.sort(mDate, new TimeComparator());
                        messageList.addAll(mDate);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(getActivity(), "没有数据咯", Toast.LENGTH_SHORT).show();
                    }
                    lv.stopLoadMore();
                }
            }
        }
    };

    private void setAdapter() {
        mAdapter = new CommonAdapter<Object>(getActivity(), R.layout.comment_adapter1, messageList) {
            @Override
            protected void convert(ViewHolder holder, final Object s, int position) {
                ArticleDis articleDis;
                AtlasDis atlasDis;
                if (s instanceof ArticleDis) {
                    articleDis = (ArticleDis) s;
                    User cuser = articleDis.getCuser();
                    holder.setSDV(R.id.img_user_comment, cuser.getHeadUrl()).setText(R.id.tv_name_comment, cuser.getNiname()).setText(R.id.tv_time_comment, articleDis.getCreatedAt())
                            .setText(R.id.tv_context, articleDis.getComment()).setSDV(R.id.img_message, articleDis.getArticle().getMainPic()).setText(R.id.tv_user_message, articleDis.getArticle().getTitle());
                } else if (s instanceof AtlasDis) {
                    atlasDis = (AtlasDis) s;
                    User cuser = atlasDis.getCuser();
                    holder.setSDV(R.id.img_user_comment, cuser.getHeadUrl()).setText(R.id.tv_name_comment, cuser.getNiname()).setText(R.id.tv_time_comment, atlasDis.getCreatedAt())
                            .setText(R.id.tv_context, atlasDis.getComment()).setSDV(R.id.img_message, atlasDis.getAtlas().getMainPic()).setText(R.id.tv_user_message, atlasDis.getAtlas().getTitle());
                }
                holder.setOnClickListener(R.id.btn_reply, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
//                        contain_layout.setVisibility(View.VISIBLE);
//                        InputMethodManagerup();
//                        object = s;
                        Intent intent = new Intent(getActivity(), DiscussActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("bundle", (Serializable) s);
                        intent.putExtras(bundle);
//                        intent.putExtra("object", s);
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + s.toString());
                        startActivity(intent);
//                        if (s instanceof ArticleDis) {
//                            articleDis = (ArticleDis) s;
//                        }
                    }
                });
                holder.setOnClickListener(R.id.source, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (s instanceof ArticleDis) {
                            Article article = ((ArticleDis) s).getArticle();
                            Intent intent = new Intent(getActivity(), ArticleItemActivity.class);
                            Log.e("FreeStar", "PageFragment1→→→onItemClick:" + article.toString() + i + "--------" + mDatas.size());
                            intent.putExtra("article", article);
                            intent.putExtra("user", ((ArticleDis) s).getArticle().getAuthor());
                            Log.e("FreeStar", "CommunityDisFragment→→→onClick:+++" + ((ArticleDis) s).getAuthor());
                            startActivity(intent);
                        } else if (s instanceof AtlasDis) {
                            Atlas atlas = ((AtlasDis) s).getAtlas();
                            Intent intent = new Intent(getActivity(), PicItemActivity.class);
                            Log.e("FreeStar", "PageFragment1→→→onItemClick:" + atlas.toString() + i + "--------" + mDatas.size());
                            intent.putExtra("atlas", atlas);
                            Log.e("FreeStar", "CommunityDisFragment→→→onClick:6666" + ((AtlasDis) s).getAtlas().getAuthor().toString());
                            intent.putExtra("user", ((AtlasDis) s).getAtlas().getAuthor());
                            startActivity(intent);
                        }
                    }
                });
                holder.setOnClickListener(R.id.img_user_comment, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        User cuser = null;
                        if (s instanceof ArticleDis) {
                            ArticleDis articleDis = (ArticleDis) s;
                            cuser = articleDis.getCuser();

                        } else if (s instanceof AtlasDis) {
                            AtlasDis atlasDis = (AtlasDis) s;
                            cuser = atlasDis.getCuser();
                        }
                        Intent intent = new Intent(getActivity(), UserDataActivity.class);
                        intent.putExtra("user", cuser);
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + cuser);
                        startActivity(intent);
                    }
                });
            }
        };
        lv.setAdapter(mAdapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment, container, false);
//        View view = inflater.inflate(R.layout.refresh_recycler_view, container, false);
        user = new User();
        user.setObjectId(App.userId);

        initData();

        lv = (XListView) view.findViewById(R.id.lv_comment);
        lv.setPullLoadEnable(true);
        lv.setPullRefreshEnable(true);
        lv.setXListViewListener(this);
        mHandler = new Handler();
        return view;
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
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                messageList.clear();
                i = 0;
                initData();

            }
        }, 2000);
    }

    private void onLoad() {
        lv.stopRefresh();
        lv.stopLoadMore();
        lv.setRefreshTime(new Date().toLocaleString());
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startQuery();
            }
        }, 2000);
    }

    private void startQuery() {

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
