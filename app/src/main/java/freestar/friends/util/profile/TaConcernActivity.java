package freestar.friends.util.profile;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.fragments.msg_fragment.MyconcernAdapter;
import io.rong.imkit.RongIM;

public class TaConcernActivity extends AppCompatActivity {
    ListView lv;
    List<User> friendList;
    MyconcernAdapter adapter;
    public User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ta_concern);
        lv = (ListView) findViewById(R.id.list_concern_ta);
        friendList = new ArrayList<>();
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        query();
        addListener();
    }

    private void addListener() {
        //  这是单击联系人的框可以聊天
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String name = friendList.get(i).getNiname();
                String id = friendList.get(i).getPhoneNum();
                RongIM.getInstance().startPrivateChat(TaConcernActivity.this, id, name);
            }
        });
    }
    private void query() {
        //查询出联系人 显示出来
        BmobQuery<User> query = new BmobQuery<>();
        User friend = new User();
        friend.setObjectId(user.getObjectId());   //当前账号id

        query.order("-acreatedAt");//时间降序
        query.addWhereRelatedTo("follow", new BmobPointer(user));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    for (User user : list) {
                        Log.e("FreeStar", "followMainActivity→→→done:" + user.toString());

                        friendList.add(user);
                        Log.i("aaa", "followMainActivity--------------" + friendList.toString());
                        //  现在需要的是把这个对象传到适配器中
                        if (adapter == null) {
                            adapter = new MyconcernAdapter(friendList, TaConcernActivity.this);
                            lv.setAdapter(adapter);
                        } else {
                            adapter.notifyDataSetChanged();
                        }

                    }
                    Log.e("FreeStar", "followMainActivity→→→done:" + list.size());
                } else {
                    Log.e("FreeStar", "MainActivity→→→done:" + e.getMessage());
                }
            }
        });

    }

    public void concernbackclick(View view){
        finish();
    }
}
