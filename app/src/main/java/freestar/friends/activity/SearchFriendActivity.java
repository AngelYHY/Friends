package freestar.friends.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.adapter.SearchFriendAdapter;
import freestar.friends.bean.User;
import freestar.friends.util.status_bar.BaseActivity;

import static freestar.friends.R.id.ivDeleteText;

/**
 * 查找好友
 */
public class SearchFriendActivity extends BaseActivity {

    @Bind(R.id.etSearch)
    EditText mEtSearch;
    @Bind(ivDeleteText)
    ImageView mIvDeleteText;
    @Bind(R.id.rv)
    RecyclerView mRv;
    private SearchFriendAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_friend);
        ButterKnife.bind(this);

        initRV();
        mEtSearch.addTextChangedListener(new TextWatcher() {

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // TODO Auto-generated method stub

            }

            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                // TODO Auto-generated method stub

            }

            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    mIvDeleteText.setVisibility(View.GONE);
                } else {
                    mIvDeleteText.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void initRV() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchFriendAdapter(R.layout.chat_lv_friend);
        mRv.setAdapter(mAdapter);
        mRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(SearchFriendActivity.this, UserDataActivity.class);
                intent.putExtra("user", (User) adapter.getItem(position));
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
                    mAdapter.setNewData(list);
                }

            }
        });
    }

    @OnClick({R.id.btnSearch, R.id.ivDeleteText})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btnSearch:
                Log.e("friendList", "执行了tv的单机方法");
                String id = mEtSearch.getText().toString().trim();
                Log.e("friendList", "得到的id为：" + id);
                qurryUserId(id);     //查到了 这个模糊id的用户信息  返还给friendList
                break;
            case R.id.ivDeleteText:
                mEtSearch.setText("");
                break;
        }
    }
}
