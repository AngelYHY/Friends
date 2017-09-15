package freestar.friends.fragment.dynamic;


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
import freestar.friends.activity.PicItemActivity;
import freestar.friends.adapter.ComAtlasAdapter;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.CollectAtlas;
import freestar.friends.bean.User;
import freestar.friends.activity.CollectActivity;

/**
 * 我的收藏 图集
 */
public class CollectAtlasFragment extends Fragment {


    @Bind(R.id.rv)
    RecyclerView mRv;

    private ComAtlasAdapter mAdapter;

    public CollectAtlasFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_collect_atlas, container, false);
        ButterKnife.bind(this, view);

        initRV();
        initData();

        return view;
    }

    private void initRV() {
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ComAtlasAdapter(R.layout.rv_collect);
        mRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), PicItemActivity.class);
                intent.putExtra("atlas", (Atlas) adapter.getItem(position));
                startActivity(intent);
            }
        });
        mRv.setAdapter(mAdapter);
    }

    public void initData() {
        User user = new User();
        String id = CollectActivity.getId();
        if (id == null) {
            user.setObjectId(App.userId);
        } else {
            user.setObjectId(id);
        }
        BmobQuery<CollectAtlas> query = new BmobQuery<>();
        query.include("sources");
        query.setLimit(15);
        query.order("-createdAt");
        query.addWhereEqualTo("user", user);
        query.findObjects(new FindListener<CollectAtlas>() {
            @Override
            public void done(List<CollectAtlas> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "CollectshelunFragment→→→done:" + list.size());
                    List<Atlas> atlases = new ArrayList<>();
                    for (CollectAtlas collectAtlas : list) {
                        atlases.add(collectAtlas.getSources());
                    }
                    mAdapter.setNewData(atlases);
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
