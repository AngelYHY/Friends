package freestar.friends.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.adapter.MyConcernAdapter;
import freestar.friends.bean.User;
import freestar.friends.util.status_bar.BaseActivity;
import io.rong.imkit.RongIM;

/**
 * 我的关注
 */
public class ConcernActivity extends BaseActivity {

    static String id;
    @Bind(R.id.rv)
    RecyclerView mRv;
    private MyConcernAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concern);
        ButterKnife.bind(this);

        initRV();
        query();
    }

    private void initRV() {
        mRv.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new MyConcernAdapter(R.layout.listview_item1);
        mRv.setAdapter(mAdapter);
        mRv.addOnItemTouchListener(new OnItemClickListener() {
            @Override
            public void onSimpleItemClick(BaseQuickAdapter adapter, View view, int position) {
                User u = (User) adapter.getItem(position);
                RongIM.getInstance().startPrivateChat(ConcernActivity.this, u.getObjectId(), u.getNiname());
            }

            @Override
            public void onItemLongClick(final BaseQuickAdapter adapter, View view, final int position) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ConcernActivity.this);
                builder.setIcon(R.drawable.trash);
                builder.setTitle("删除").setMessage("确定删除此好友？");
                builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        User u = mAdapter.getItem(position);
                        adapter.remove(position);

                        User me = new User();
                        me.setObjectId(App.userId);

                        BmobRelation relation = new BmobRelation();
                        relation.remove(u);

                        me.setUser(relation);
                        me.update(new UpdateListener() {

                            @Override
                            public void done(BmobException e) {
                                if (e == null) {
                                    mAdapter.notifyDataSetChanged();
                                    Toast.makeText(ConcernActivity.this, "删除好友成功", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(ConcernActivity.this, "删除好友失败", Toast.LENGTH_SHORT).show();
                                    Log.e("删除好友失败", e.getMessage() + "删除好友失败:" + e.getErrorCode());
                                }
                            }

                        });
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(ConcernActivity.this, "点击了取消", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.show();
            }

            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                Intent intent = new Intent(ConcernActivity.this, UserDataActivity.class);
                intent.putExtra("user", (User) adapter.getItem(position));
                startActivity(intent);
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
        query.addWhereRelatedTo("user", new BmobPointer(user));
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    mAdapter.setNewData(list);
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
