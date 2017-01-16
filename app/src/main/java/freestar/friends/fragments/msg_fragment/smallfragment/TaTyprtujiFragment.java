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

import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.activities.PicItemActivity;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class TaTyprtujiFragment extends Fragment {
    private ListView lv;
    private List<Atlas> mDatas  = new ArrayList<>();
    private MyTYpetujiAdapter mAdapter;
    private User user;

    public TaTyprtujiFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ta_typrtuji, container, false);
        lv = (ListView) view.findViewById(R.id.ta_type);
        Intent intent = getActivity().getIntent();
        user = (User) intent.getSerializableExtra("user");
        //Log.e("FreeStar", "UserDataActivity→→→onCreate:" + user.toString());
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

    public void initData(){
//        User user = new User();
//        user.setObjectId(App.userId);
        //user.setObjectId("c8249e3753");
        User friend = new User();
        friend.setObjectId(user.getObjectId());

        BmobQuery<Atlas> query = new BmobQuery<>();
        query.include("author");
        query.setLimit(15);
        query.order("-createdAt");
        query.addWhereEqualTo("author", user);
        query.findObjects(new FindListener<Atlas>() {
            @Override
            public void done(List<Atlas> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "taTypeshelunFragment→→→done:" + list.size());
                    mDatas.addAll(list);
                    for (Atlas mDate : mDatas) {
                        Log.e("TAG", "taTypeshelunFragment: "+mDate.toString());

                        if (mAdapter == null){
                            mAdapter = new MyTYpetujiAdapter(mDatas, getActivity() );
                            lv.setAdapter(mAdapter);
                        }else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Log.e("FreeStar", "taTypeshelunFragment→→→done:" + e.getMessage());
                }
            }
        });
    }

}
