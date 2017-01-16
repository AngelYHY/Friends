package freestar.friends.fragments.pages_fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.R;
import freestar.friends.bean.Atlas;
import freestar.friends.util.abslistview.CommonAdapter;
import freestar.friends.util.abslistview.ViewHolder;
import freestar.friends.util.material_refresh_layout.MaterialRefreshLayout;
import freestar.friends.util.material_refresh_layout.MaterialRefreshListener;


/**
 * Created by Administrator on 2016/8/20 0020.
 */
public class PageFragment9 extends Fragment {
    private List<Atlas> mDatas = new ArrayList<>();
    private CommonAdapter<Atlas> commonAdapter = new CommonAdapter<Atlas>(getActivity(), R.layout.item_layout, mDatas) {
        @Override
        protected void convert(ViewHolder holder, Atlas s, int position) {
//            holder.setText(R.id.tv_time, s.getCreatedAt()).setSDV(R.id.iv_main, s.getMainPic()).setText(R.id.tv_disNum, s.getDisNum() + "com_icon").setText(R.id.tv_title, s.getTitle() + "(" + s.getNum() + " 图)");
            holder.setText(R.id.tv_time, s.getCreatedAt()).setSDV(R.id.iv_main, s.getMainPic()).setText(R.id.tv_disNum, s.getLikeNum() + "赞").setText(R.id.tv_title, s.getTitle() + "(" + s.getNum() + " 图)");

        }
    };

    private MaterialRefreshLayout materialRefreshLayout;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                listView.setAdapter(commonAdapter);
            } else if (msg.what == 2) {
                commonAdapter.notifyDataSetChanged();
            }
        }
    };
    private ListView listView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_listview, container, false);

        listView = (ListView) view.findViewById(R.id.lv);

        initData();

        materialRefreshLayout = (MaterialRefreshLayout) view.findViewById(R.id.refresh);
        materialRefreshLayout.setLoadMore(true);
        materialRefreshLayout.finishRefreshLoadMore();
        materialRefreshLayout.setMaterialRefreshListener(new MaterialRefreshListener() {
            @Override
            public void onRefresh(final MaterialRefreshLayout materialRefreshLayout) {
                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "refresh ", Toast.LENGTH_LONG).show();
                        Log.e("FreeStar", "PageFragment9→→→run:refresh");
                        BmobQuery<Atlas> query = new BmobQuery<>();
                        query.addWhereEqualTo("kind", "风景");
                        query.findObjects(new FindListener<Atlas>() {
                            @Override
                            public void done(List<Atlas> list, BmobException e) {
                                if (e == null) {
                                    Log.e("FreeStar", "PageFragment2→→→done:" + list.size());
                                    mDatas.addAll(list);
                                    for (Atlas mData : mDatas) {
                                        Log.e("FreeStar", "PageFragment2→→→done:" + mData.toString());
                                    }
                                    handler.sendEmptyMessage(1);
                                } else {
                                    Log.e("FreeStar", "PageFragment2→→→done:" + e.getMessage());
                                }
                            }
                        });
                        materialRefreshLayout.finishRefresh();
                    }
                }, 3000);

            }

            @Override
            public void onfinish() {
                Toast.makeText(getActivity(), "finish", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRefreshLoadMore(final MaterialRefreshLayout materialRefreshLayout) {

                materialRefreshLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "load ", Toast.LENGTH_LONG).show();
                        Log.e("FreeStar", "PageFragment9→→→run:load");
                        BmobQuery<Atlas> query = new BmobQuery<>();
                        query.addWhereEqualTo("kind", "其他");
                        query.findObjects(new FindListener<Atlas>() {
                            @Override
                            public void done(List<Atlas> list, BmobException e) {
                                if (e == null) {
                                    Log.e("FreeStar", "PageFragment2→→→done:" + list.size());
                                    mDatas.addAll(list);
                                    for (Atlas mData : mDatas) {
                                        Log.e("FreeStar", "PageFragment2→→→done:" + mData.toString());
                                    }
                                    handler.sendEmptyMessage(1);
                                } else {
                                    Log.e("FreeStar", "PageFragment2→→→done:" + e.getMessage());
                                }
                            }
                        });
                        materialRefreshLayout.finishRefreshLoadMore();
                    }
                }, 3000);
            }
        });
        return view;
    }

    private void initData() {
        BmobQuery<Atlas> query = new BmobQuery<>();
        query.addWhereEqualTo("kind", "其他");
        query.findObjects(new FindListener<Atlas>() {
            @Override
            public void done(List<Atlas> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "PageFragment2→→→done:" + list.size());
                    mDatas.addAll(list);
                    for (Atlas mData : mDatas) {
                        Log.e("FreeStar", "PageFragment2→→→done:" + mData.toString());
                    }
                    handler.sendEmptyMessage(1);
                } else {
                    Log.e("FreeStar", "PageFragment2→→→done:" + e.getMessage());
                }
            }
        });
    }
}
