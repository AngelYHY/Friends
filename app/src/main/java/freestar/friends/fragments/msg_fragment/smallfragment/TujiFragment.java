package freestar.friends.fragments.msg_fragment.smallfragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
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
import freestar.friends.activities.PicItemActivity;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.User;
import freestar.friends.util.profile.TypeActivity1;

/**
 * A simple {@link Fragment} subclass.
 */
public class TujiFragment extends Fragment {

    private ListView lv;
    private List<Atlas> mDatas = new ArrayList<>();
    private MyTYpetujiAdapter mAdapter;
    private RecyclerView recyclerViewv;

    public TujiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tuji, container, false);
        lv = (ListView) view.findViewById(R.id.type_tuji);
        initData();
        addListener();
        return view;
    }

    private void addListener() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Atlas atlas = mDatas.get(i);
                Intent intent = new Intent(getActivity(), PicItemActivity.class);
                Log.e("FreeStar", "PageFragment1→→→onItemClick:" + atlas.toString() + i + "--------" + mDatas.size());
                intent.putExtra("atlas", atlas);
                startActivity(intent);
            }
        });
    }

    public void initData() {
        User user = new User();
        String id = TypeActivity1.getId();
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
                    Log.e("FreeStar", "TypeshelunFragment→→→done:" + list.size());
                    mDatas.addAll(list);

                    if (mAdapter == null) {
                        mAdapter = new MyTYpetujiAdapter(mDatas, getActivity());
                        lv.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }
                } else {
                    Log.e("FreeStar", "TypeshelunFragment→→→done:" + e.getMessage());
                }
            }
        });
    }

}
