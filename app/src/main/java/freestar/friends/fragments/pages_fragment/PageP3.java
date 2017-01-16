package freestar.friends.fragments.pages_fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.R;
import freestar.friends.activities.PicItemActivity;
import freestar.friends.activities.PublishPicture;
import freestar.friends.bean.Atlas;
import freestar.friends.util.Banner;
import freestar.friends.util.BannerConfig;
import freestar.friends.util.abslistview.CommonAdapter;
import freestar.friends.util.abslistview.ViewHolder;
import freestar.friends.util.view.XListView;

public class PageP3 extends Fragment implements XListView.IXListViewListener, AdapterView.OnItemClickListener {
    private List<Atlas> mDatas = new ArrayList<>();
    private XListView xListView;
    private FloatingActionButton fab;
    private Handler mHandler;
    private CommonAdapter<Atlas> adapter;
    private int i;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.one, container, false);
        xListView = (XListView) view.findViewById(R.id.xlv);
        fab = (FloatingActionButton) view.findViewById(R.id.fab_btn);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(), PublishPicture.class));
            }
        });
        fab.attachToListView(xListView);
        initData();

        xListView.setPullLoadEnable(true);
        xListView.setXListViewListener(this);
        xListView.setOnItemClickListener(this);
        mHandler = new Handler();
        return view;
    }

    private void initData() {
        BmobQuery<Atlas> query = new BmobQuery<>();
        query.include("author");
        query.setLimit(15);
        query.addWhereEqualTo("kind", "动物");
//        query.order("likeNum");
        query.findObjects(new FindListener<Atlas>() {
            @Override
            public void done(List<Atlas> list, BmobException e) {
                if (e == null) {
//                    Log.e("FreeStar", "PageFragment1→→→done:" + list.size());
//                    mDatas.addAll(list);
//                    for (Atlas mData : mDatas) {
//                        Log.e("FreeStar", "PageFragment1→→→done:" + mData.toString());
//                    }
                    mDatas.addAll(list);
                    if (adapter == null) {
                        setCAdapter();
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    Log.e("FreeStar", "PageFragment1→→→done:数据源改变");
                } else {
                    Log.e("FreeStar", "PageFragment1→→→done:+++++++++" + e.getMessage());
                }
            }
        });
        i++;
    }

    private void setCAdapter() {
        adapter = new CommonAdapter<Atlas>(getActivity(), R.layout.atlas_layout, mDatas) {
            @Override
            protected void convert(ViewHolder holder, Atlas s, int position) {
                Log.e("FreeStar", "PageFragment1→→→convert:" + s.toString());
                holder.setText(R.id.tv_time, s.getCreatedAt()).setSDV(R.id.iv_main, s.getMainPic()).setText(R.id.tv_disNum, s.getLikeNum() + "赞").setText(R.id.tv_title, s.getTitle() + "(" + s.getNum() + " 图)");
            }
        };
        xListView.setAdapter(adapter);
    }

    private void initBanner(View header) {
        String[] images = new String[5];
        String[] titles = new String[5];
        for (int i = 0; i < 5; i++) {
            images[i] = mDatas.get(i).getMainPic();
            titles[i] = mDatas.get(i).getTitle();
        }

        Banner banner = (Banner) header.findViewById(R.id.banner1);
        //显示数字指示器和标题
        banner.setBannerStyle(BannerConfig.NUM_INDICATOR_TITLE);
        //记得设置标题列表哦
        banner.setBannerTitle(titles);
        banner.setImages(images);//可以选择设置图片网址，或者资源文件，默认用Glide加载
        banner.setOnBannerClickListener(new Banner.OnBannerClickListener() {//设置点击事件
            @Override
            public void OnBannerClick(View view, int position) {
                Toast.makeText(getActivity(), "你点击了：" + position, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onRefresh() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mDatas.clear();
                i = 0;
                initData();

                onLoad();
            }
        }, 3000);
    }

    @Override
    public void onLoadMore() {
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startQuery();

                onLoad();
            }
        },3000);


//        if (flag) {
//            mHandler.sendEmptyMessage(1);
//        }
    }

    private void onLoad() {
        xListView.stopRefresh();
        xListView.stopLoadMore();
        xListView.setRefreshTime(new Date().toLocaleString());
    }

    private void startQuery() {
        BmobQuery<Atlas> query = new BmobQuery<>();
        query.addWhereEqualTo("kind", "动物");
        query.setSkip(15 * i); // 忽略前15条数据（即第一页数据结果）
        query.setLimit(15);
        query.order("-createdAt");
//        query.order("likeNum");
        query.findObjects(new FindListener<Atlas>() {
            @Override
            public void done(List<Atlas> list, BmobException e) {
                if (e == null) {
//                    Log.e("FreeStar", "PageFragment1→→→done:" + list.size());
//                    mDatas.addAll(list);
//                    for (Atlas mData : mDatas) {
//                        Log.e("FreeStar", "PageFragment1→→→done:" + mData.toString());
//                    }
//                    adapter.notifyDataSetChanged();
//                    if (list.size() == 0) {
//                        Toast.makeText(getActivity(), "没有数据了", Toast.LENGTH_SHORT).show();
//                        flag = false;
////                        mLoadMoreWrapper.setViewGone();
//                    }
                    if (list.size() == 0) {
                        Toast.makeText(getActivity(), "没有数据了", Toast.LENGTH_SHORT).show();
                    } else {
                        mDatas.addAll(list);
                        adapter.notifyDataSetChanged();
                    }

                } else {
                    Log.e("FreeStar", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
        i++;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Atlas atlas = mDatas.get(i-1);
        Intent intent = new Intent(getActivity(), PicItemActivity.class);
        Log.e("FreeStar", "PageFragment1→→→onItemClick:" + atlas.toString() + i + "--------" + mDatas.size());
        intent.putExtra("atlas", atlas);
        startActivity(intent);
    }
}