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
import freestar.friends.activities.DynDetailActivity;
import freestar.friends.bean.Message;
import freestar.friends.bean.User;
import freestar.friends.util.profile.TypeActivity1;

/**
 * A simple {@link Fragment} subclass.
 */
public class ShuoshuoFragment extends Fragment {
    private ListView lv;
    private List<Message> mDatas = new ArrayList<>();
    private MyTypessAdapter mAdapter;

    public ShuoshuoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_shuoshuo, container, false);
        lv = (ListView) view.findViewById(R.id.type_shuoshuo);
        initData();
        addListener();
        return view;
    }

    private void addListener() {

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Message message = mDatas.get(i);
                Intent intent = new Intent(getActivity(), DynDetailActivity.class);
                Log.e("FreeStar", "PageFragment1→→→onItemClick:" + message.toString() + i + "--------" + mDatas.size());
                intent.putExtra("message", message);
                startActivity(intent);
            }
        });
    }

    public void initData() {
//        User user = new User();
//        user.setObjectId(App.userId);
//        //user.setObjectId("c8249e3753");
        User user = new User();
        String id = TypeActivity1.getId();
        if (id == null) {
            user.setObjectId(App.userId);
        } else {
            user.setObjectId(id);
        }
        BmobQuery<Message> query = new BmobQuery<>();
        query.include("user");
        query.setLimit(15);
        query.order("-createdAt");
        query.addWhereEqualTo("user", user);
        query.findObjects(new FindListener<Message>() {
            @Override
            public void done(List<Message> list, BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "TypessFragment→→→done:" + list.size());
                    mDatas.addAll(list);
                    for (Message mDate : mDatas) {
                        Log.e("TAG", "TypessFragment: " + mDate.toString());

                        if (mAdapter == null) {
                            mAdapter = new MyTypessAdapter(mDatas, getActivity());
                            lv.setAdapter(mAdapter);
                        } else {
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Log.e("FreeStar", "TypessFragment→→→done:" + e.getMessage());
                }
            }
        });
    }
}
