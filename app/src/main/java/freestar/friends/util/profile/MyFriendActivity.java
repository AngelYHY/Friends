package freestar.friends.util.profile;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.fragments.msg_fragment.MyBaseAdapter;
import freestar.friends.util.status_bar.BaseActivity;

public class MyFriendActivity extends BaseActivity {
    ListView lv;
    List<User> friendList;
    MyBaseAdapter adapter;
    String id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_friend);
    }

    public void query() {
        BmobQuery<User> query = new BmobQuery<>();
        User user = new User();
        user.setObjectId(App.userId);   //当前账号id

        query.addWhereRelatedTo("user", new BmobPointer(user));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    for (User user : list) {
                        Log.e("FreeStar", "MainActivity→→→done:" + user.toString());

                        friendList.add(user);
                        Log.i("aaa", "friendList--------------" + friendList.toString());
                        //  现在需要的是把这个对象传到适配器中
                        if (adapter == null) {
                            adapter = new MyBaseAdapter(friendList, MyFriendActivity.this);
                            lv.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }

                    }
                    Log.e("FreeStar", "MainActivity→→→done:" + list.size());
                } else {
                    Log.e("FreeStar", "MainActivity→→→done:" + e.getMessage());
                }
            }
        });
    }
}
