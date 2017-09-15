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

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.activity.MyPublishActivity;
import freestar.friends.activity.PicItemActivity;
import freestar.friends.adapter.ComAtlasAdapter;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.User;

/**
 * 我的发布 图集
 */
public class PublishAtlasFragment extends Fragment {

    @Bind(R.id.rv)
    RecyclerView mRv;
    private ComAtlasAdapter mAdapter;


    public PublishAtlasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_atlas, container, false);
        ButterKnife.bind(this, view);

        initRV();
        initData();

        return view;
    }

    private void initRV() {
        mRv.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new ComAtlasAdapter(R.layout.rv_collect);
        mRv.setAdapter(mAdapter);
        mRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(getActivity(), PicItemActivity.class);
                intent.putExtra("atlas", (Atlas) adapter.getItem(position));
                startActivity(intent);
            }
        });
    }


    public void initData() {
        User user = new User();
        String id = MyPublishActivity.getId();
        if (id == null) {
            user.setObjectId(App.userId);
        } else {
            user.setObjectId(id);
        }
        //user.setObjectId("c8249e3753");
        BmobQuery<Atlas> query = new BmobQuery<>();
        query.include("author");
        query.setLimit(15);
        query.order("-createdAt");
        query.addWhereEqualTo("author", user);
        query.findObjects(new FindListener<Atlas>() {
            @Override
            public void done(List<Atlas> list, BmobException e) {
                if (e == null) {
                    mAdapter.setNewData(list);
                } else {
                    Log.e("FreeStar", "TypeshelunFragment→→→done:" + e.getMessage());
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
