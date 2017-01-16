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
import freestar.friends.activities.PicItemActivity;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.CollectAtlas;
import freestar.friends.bean.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaCollectTujiFragment extends Fragment {
    private ListView lv;
    private List<CollectAtlas> mDatas  = new ArrayList<>();
    private MyCollectAdapter mAdapter;
    private String id;
    private User user;

    public TaCollectTujiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_ta_collect_tuji, container, false);
        lv = (ListView) view.findViewById(R.id.tacollect_tuji);
        Intent intent = getActivity().getIntent();
        user = (User) intent.getSerializableExtra("user");
        initData();
        addListener();
        return view;
    }

    private void addListener() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Atlas atlas = mDatas.get(i).getSources();
                Intent intent = new Intent(getActivity(), PicItemActivity.class);
                Log.e("FreeStar", "PageFragment1→→→onItemClick:" + atlas.toString() + i + "--------" + mDatas.size());
                intent.putExtra("atlas", atlas);
                startActivity(intent);
            }
        });
    }


    public void initData(){
        User friend = new User();
        friend.setObjectId(user.getObjectId());
        //user.setObjectId("c8249e3753");
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
                    mDatas.addAll(list);
                    for (CollectAtlas mDate : mDatas) {
                        Log.e("TAG", "shelundone: "+mDate.toString());

                        if (mAdapter == null){
                            mAdapter = new MyCollectAdapter(mDatas, getActivity() );
                            lv.setAdapter(mAdapter);
                        }else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Log.e("FreeStar", "CollectshelunFragment→→→done:" + e.getMessage());
                }
            }
        });
    }

}
