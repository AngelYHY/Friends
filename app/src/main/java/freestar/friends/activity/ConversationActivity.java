package freestar.friends.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.util.status_bar.BaseActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;
import io.rong.imlib.model.UserInfo;

/**
 * 会话窗口
 */
public class ConversationActivity extends BaseActivity implements RongIM.ConversationBehaviorListener {
    //目标 ID
    private String mTargetId;
    private String title;
    Toolbar mToolbar;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);

        mToolbar = (Toolbar) findViewById(R.id.conversation_toolbar);
        RongIM.setConversationBehaviorListener(this);//设置会话界面操作的监听器
        initData();
    }

    private void initData() {
        mTargetId = getIntent().getData().getQueryParameter("targetId");
        title = getIntent().getData().getQueryParameter("title");
        if (title != null && !title.isEmpty()) {
            mToolbar.setTitle(title);
        } else {
            mToolbar.setTitle("null");
        }
        mToolbar.setTitleTextColor(Color.WHITE);
        mToolbar.setNavigationIcon(R.mipmap.back);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    //   单击  显示 好友信息
    @Override
    public boolean onUserPortraitClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        BmobQuery<User> query = new BmobQuery<>();
        query.addWhereContains("objectId", userInfo.getUserId());   //模糊查询
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(List<User> list, BmobException e) {
                if (e == null) {
                    User user = list.get(0);
                    Intent intent = new Intent(ConversationActivity.this, UserDataActivity.class);
                    intent.putExtra("user", user);
                    startActivity(intent);

                }

            }
        });

        return false;
    }


    //长按显示出大图
    @Override
    public boolean onUserPortraitLongClick(Context context, Conversation.ConversationType conversationType, UserInfo userInfo) {
        startActivity(BGAPhotoPreviewActivity.newIntent(context, null, String.valueOf(userInfo.getPortraitUri())));
        return false;
    }

    @Override
    public boolean onMessageClick(Context context, View view, Message message) {
        return false;
    }

    @Override
    public boolean onMessageLinkClick(Context context, String s) {
        return false;
    }

    @Override
    public boolean onMessageLongClick(Context context, View view, Message message) {
        return false;
    }

}