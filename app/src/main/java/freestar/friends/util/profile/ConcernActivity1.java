package freestar.friends.util.profile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.fragments.msg_fragment.MyconcernAdapter;
import freestar.friends.util.status_bar.BaseActivity;
import io.rong.imkit.RongIM;

public class ConcernActivity1 extends BaseActivity {

    ListView lv;
    List<User> friendList;
    MyconcernAdapter adapter;
    @Bind(R.id.tv_add)
    TextView tvAdd;
    @Bind(R.id.layout_all)
    LinearLayout layoutAll;
    @Bind(R.id.list_concern)
    ListView listConcern;
    @Bind(R.id.tv_no)
    TextView tvNo;
    @Bind(R.id.layout_no)
    LinearLayout layoutNo;
    private User user;
    static String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concern);
        ButterKnife.bind(this);
        Intent intent = getIntent();
        user = (User) intent.getSerializableExtra("user");
        Log.e("FreeStar", "TypeActivity1→→→onCreate:" + user);
        if (user != null) {
            id = user.getObjectId();
            tvAdd.setText("主人的关注");
        }
        lv = (ListView) findViewById(R.id.list_concern);
        friendList = new ArrayList<>();
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
                RongIM.getInstance().startPrivateChat(ConcernActivity1.this, id, name);
            }
        });
    }

    private void query() {
        //查询出联系人 显示出来
        User user = new User();
        if (id != null) {
            user.setObjectId(id);
        } else {
            user.setObjectId(App.userId);   //当前账号id
        }
        BmobQuery<User> query = new BmobQuery<>();

//        query.order("-acreatedAt");//时间降序
        query.addWhereRelatedTo("user", new BmobPointer(user));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
//                    for (User user : list) {
//                        friendList.add(user);
//                    }
                    friendList.addAll(list);
                    //  现在需要的是把这个对象传到适配器中
                    if (adapter == null) {
                        adapter = new MyconcernAdapter(friendList, ConcernActivity1.this);
                        lv.setAdapter(adapter);
                    } else {
                        adapter.notifyDataSetChanged();
                    }
                    Log.e("FreeStar", "followMainActivity→→→done:" + list.size());
                } else {
                    Log.e("FreeStar", "MainActivity→→→done:" + e.getMessage());
                }
            }
        });

    }

    public void concernbackclick(View view) {
        finish();
    }

}
