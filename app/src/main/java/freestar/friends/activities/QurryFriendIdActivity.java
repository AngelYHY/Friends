package freestar.friends.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.fragments.msg_fragment.MyBaseAdapter;
import freestar.friends.util.status_bar.BaseActivity;

public class QurryFriendIdActivity extends BaseActivity {


    @Bind(R.id.lv_qurryFriendId)
    ListView lv;
    List<User> friendList;
    MyBaseAdapter adapter;
    ImageView ivDeleteText;
    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qurry_friend_id);
        ButterKnife.bind(this);

        ivDeleteText = (ImageView) findViewById(R.id.ivDeleteText);
        etSearch = (EditText) findViewById(R.id.etSearch);

        ivDeleteText.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                etSearch.setText("");
            }
        });

        etSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    ivDeleteText.setVisibility(View.GONE);
                } else {
                    ivDeleteText.setVisibility(View.VISIBLE);
                }
            }
        });



        Button tv = (Button) findViewById(R.id.btnSearch);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("friendList", "执行了tv的单机方法");
                String id = etSearch.getText().toString().trim();
                Log.e("friendList", "得到的id为：" + id);
                qurryUserId(id);     //查到了 这个模糊id的用户信息  返还给friendList


            }
        });
        addListener();
    }


    private void addListener() {
        //  这是单击  显示   用户信息
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(QurryFriendIdActivity.this, UserDataActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", friendList.get(i));
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            }
        });
    }


    private void qurryUserId(String id) {
        BmobQuery<User> query = new BmobQuery<User>();
        query.addWhereNotEqualTo("phoneNum", App.phoneNum);
        query.addWhereContains("phoneNum", id);   //模糊查询
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {

                    friendList = list;
                    Log.e("friendList", "-----friendList:" + friendList.toString());
                    adapter = new MyBaseAdapter(friendList, QurryFriendIdActivity.this);
                    lv.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }

}
