package freestar.friends.fragments.msg_fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.activities.ArticleItemActivity1;
import freestar.friends.activities.PicItemActivity;
import freestar.friends.bean.Article;
import freestar.friends.bean.ArticleDis;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.AtlasDis;
import freestar.friends.bean.User;
import freestar.friends.util.TimeComparator;
import freestar.friends.util.abslistview.CommonAdapter;
import freestar.friends.util.abslistview.ViewHolder;

/**
 * 暂时定为  与我相关
 */
public class CommentFragment2 extends Fragment implements View.OnClickListener {
    ListView lv;

    ArrayList<Object> messageList = new ArrayList<>();
    EditText conments;
    private User user;
    private CommonAdapter<Object> mAdapter;
    private Button sumbit_conments;
    private LinearLayout contain_layout;
    private Object object;
    private Handler mHandler;
    ArrayList<Object> mDate = new ArrayList<>();

    public CommentFragment2() {

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
                    Collections.sort(messageList, new TimeComparator());
                    if (mAdapter == null) {
                        setAdapter();
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                    start = false;
                    end = false;
                }
            } else if (msg.what == 2) {
                if (start || end) {
                    Collections.sort(mDate, new TimeComparator());
                    messageList.addAll(mDate);
                    mAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getActivity(), "没有数据咯", Toast.LENGTH_SHORT).show();
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
                        contain_layout.setVisibility(View.VISIBLE);
                        InputMethodManagerup();
                        object = s;
//                        Intent intent = new Intent(getActivity(), DiscussActivity.class);
//                        intent.putExtra("object", s);
//                        Log.e("FreeStar", "SearchActivity→→→onClick:" + s.toString());
//                        startActivity(intent);
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
                            Intent intent = new Intent(getActivity(), ArticleItemActivity1.class);
                            Log.e("FreeStar", "PageFragment1→→→onItemClick:" + article.toString() + i + "--------" + mDatas.size());
                            intent.putExtra("article", article);
                            startActivity(intent);
                        } else if (s instanceof AtlasDis) {
                            Atlas atlas = ((AtlasDis) s).getAtlas();
                            Intent intent = new Intent(getActivity(), PicItemActivity.class);
                            Log.e("FreeStar", "PageFragment1→→→onItemClick:" + atlas.toString() + i + "--------" + mDatas.size());
                            intent.putExtra("atlas", atlas);
                            startActivity(intent);
                        }
                    }
                });

            }
        };
        lv.setAdapter(mAdapter);
    }

    //弹开虚拟键盘和输入框
    private void InputMethodManagerup() {
        Log.e("FreeStar", "CommentFragment1→→→InputMethodManagerup:执行弹出键盘");
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        conments.setFocusable(true);
        conments.setFocusableInTouchMode(true);
        conments.requestFocus();
        conments.requestFocusFromTouch();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_comment1, container, false);
        conments = (EditText) view.findViewById(R.id.kj_pl);
        contain_layout = (LinearLayout) view.findViewById(R.id.kj_pl_slu);

        sumbit_conments = (Button) view.findViewById(R.id.kj_pl_fb);

        sumbit_conments.setOnClickListener(this);
        user = new User();
        user.setObjectId(App.userId);
        Log.e("comment", "-------进入comment碎片:");
        lv = (ListView) view.findViewById(R.id.lv_comment);
//        lv = (XListView) view.findViewById(R.id.lv_comment);
//        lv.setPullLoadEnable(true);
//        lv.setXListViewListener(this);
        mHandler = new Handler();
        initData();
//        initListData();
//        * Fragment中，注册
//                * 接收MainActivity的Touch回调的对象
//                * 重写其中的onTouchEvent函数，并进行该Fragment的逻辑处理
//                */
//        HomePageA.MyTouchListener myTouchListener = new HomePageA.MyTouchListener() {
//            @Override
//            public void onTouchEvent(MotionEvent event) {
//                // 处理手势事件
//                Downkeyboard();
//            }
//        };
//
//        // 将myTouchListener注册到分发列表
//        ((HomePageA) this.getActivity()).registerMyTouchListener(myTouchListener);
        return view;
    }

    private void initData() {

        //  查询 图集和作者  的信息
        BmobQuery<AtlasDis> query = new BmobQuery<>();
        query.addWhereEqualTo("author", user);
        query.include("cuser,atlas,author");
//        query.order("-createdAt");
        query.setLimit(10);
        query.findObjects(new FindListener<AtlasDis>() {
            @Override
            public void done(List<AtlasDis> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        messageList.addAll(list);
                        start = true;
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    Log.e("ccc", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });

        BmobQuery<ArticleDis> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("author", user);
        query1.include("cuser,article,author");
        query1.setLimit(10);
        query1.findObjects(new FindListener<ArticleDis>() {
            @Override
            public void done(List<ArticleDis> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        messageList.addAll(list);
                        end = true;
                        handler.sendEmptyMessage(1);
                    }
                } else {
                    Log.e("ccc", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    public void Downkeyboard() {
//        if (conments.getVisibility() == v.VISIBLE && sumbit_conments.getVisibility() == v.VISIBLE) {
        if (contain_layout.getVisibility() == View.VISIBLE) {
            Log.e("FreeStar", "CommentFragment1→→→Downkeyboard:lalallala ");
            contain_layout.setVisibility(View.GONE);
            InputMethodManager imm2 = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm2.hideSoftInputFromWindow(sumbit_conments.getWindowToken(), 0);
        }
    }

    @Override
    public void onClick(View view) {
        Log.e("FreeStar", "CommentActivity→→→onClick:进入点击事件");
        String context = conments.getText().toString().trim();
        if (context.equals("")) {
            Toast.makeText(getActivity(), "写写东西吧", Toast.LENGTH_SHORT).show();
            return;
        } else {
            conments.setText("");
            Downkeyboard();
        }
        if (object instanceof ArticleDis) {
            ArticleDis articleDis = new ArticleDis(user, (ArticleDis) object, context, ((ArticleDis) object).getArticle(), ((AtlasDis) object).getCuser());
            articleDis.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.e("FreeStar", "DiscussActivity→→→done:成功" + s);
                    } else {
                        Log.e("FreeStar", "DiscussActivity→→→done:" + e.getMessage());
                    }
                }
            });
        } else if (object instanceof AtlasDis) {
            AtlasDis atlasDis = new AtlasDis(user, (AtlasDis) object, context, ((AtlasDis) object).getAtlas(), ((AtlasDis) object).getCuser());
            atlasDis.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        Log.e("FreeStar", "DiscussActivity→→→done:成功" + s);
                    } else {
                        Log.e("FreeStar", "DiscussActivity→→→done:" + e.getMessage());
                    }
                }
            });
        }
    }
//
//    @Override
//    public void onRefresh() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                messageList.clear();
//                initData();
////                mAdapter.notifyDataSetChanged();
//                onLoad();
//            }
//        }, 2000);
//    }

//    private void onLoad() {
//        lv.stopRefresh();
//        lv.stopLoadMore();
//        lv.setRefreshTime(new Date().toLocaleString());
//    }

//    @Override
//    public void onLoadMore() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                startQuery();
////                mAdapter.notifyDataSetChanged();
//                onLoad();
//            }
//        }, 2000);
//    }

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
                        handler.sendEmptyMessage(2);
                    }
                } else {
                    Log.e("ccc", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });

        BmobQuery<ArticleDis> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("author", user);
        query1.include("cuser,article,author");
        query1.setLimit(10);
        query.setSkip(10 * i);
        query1.findObjects(new FindListener<ArticleDis>() {
            @Override
            public void done(List<ArticleDis> list, BmobException e) {
                if (e == null) {
                    if (list.size() > 0) {
                        mDate.addAll(list);
                        handler.sendEmptyMessage(2);
                    }
                } else {
                    Log.e("ccc", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
        i++;
    }


}
