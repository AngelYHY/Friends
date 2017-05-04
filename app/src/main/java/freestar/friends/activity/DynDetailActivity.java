package freestar.friends.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemChildClickListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.orhanobut.logger.Logger;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.adapter.SayDisAdapter;
import freestar.friends.bean.DisSay;
import freestar.friends.bean.Message;
import freestar.friends.bean.MessageLike;
import freestar.friends.bean.User;
import freestar.friends.util.fly_heat.PeriscopeLayout;
import freestar.friends.util.status_bar.BaseActivity;
import freestar.friends.widget.SoftKeyBoardListener;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 动态详情
 */
public class DynDetailActivity extends BaseActivity implements BGANinePhotoLayout.Delegate, SwipeRefreshLayout.OnRefreshListener, BaseQuickAdapter.RequestLoadMoreListener, View.OnClickListener {

    Message message;

    SimpleDraweeView simpleDraweeView;

    TextView name, time, comtent;

    BGANinePhotoLayout ninegridview;

    ImageView zanbiao, pinglunbiao;

    boolean flag;
    DisSay disSay;

    @Bind(R.id.kj_pl)
    EditText conments;
    @Bind(R.id.kj_pl_fb)
    ImageButton sumbit_conments;
    @Bind(R.id.kj_pl_slu)
    LinearLayout contain_layout;
    @Bind(R.id.periscope)
    PeriscopeLayout mPeriscope;
    @Bind(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.swipeLayout)
    SwipeRefreshLayout mSwipeLayout;

    private User user;
    private PeriscopeLayout periscope;
    private boolean starLike;
    private boolean endLike = false;
    private MessageLike likeMsg;
    Handler handler;
    View headView;
    int i;
    private BGANinePhotoLayout mCurrentClickNpl;
    private static final int REQUEST_CODE_PERMISSION_PHOTO_PREVIEW = 1;
    private SayDisAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        ButterKnife.bind(this);
        //传值过来更新界面Bundle
        Bundle bundle = getIntent().getExtras();
        message = (Message) bundle.getSerializable("message");
        Log.e("FreeStar", "DynDetailActivity→→→onCreate:" + message);

        user = new User();
        user.setObjectId(App.userId);

        //加头布局
        headView = LayoutInflater.from(this).inflate(R.layout.detail_layout, null);
        mSwipeLayout.setOnRefreshListener(this);

        queryState();
        onRefresh();

        initRV();

        initView();
        initSource();
    }

    private void initRV() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.addOnItemTouchListener(new OnItemChildClickListener() {
            @Override
            public void onSimpleItemChildClick(BaseQuickAdapter adapter, View view, int i) {
                DisSay s = (DisSay) adapter.getItem(i);
                Intent intent;
                switch (view.getId()) {
                    case R.id.line_reply:
                        contain_layout.setVisibility(View.VISIBLE);
                        //弹开虚拟键盘和输入框
                        InputMethodManagerup();
                        flag = true;
                        disSay = s;
                        break;
                    case R.id.sdv_head:
                        intent = new Intent(DynDetailActivity.this, UserDataActivity.class);
                        intent.putExtra("user", s.getCuser());
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + s.getCuser());
                        startActivity(intent);
                        break;
                    case R.id.btn_name:
                        intent = new Intent(DynDetailActivity.this, UserDataActivity.class);
                        intent.putExtra("user", s.getFather_user());
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + s.getFather_user());
                        startActivity(intent);
                        break;
                }

            }
        });

        mAdapter = new SayDisAdapter();
        mAdapter.addHeaderView(headView);

        mAdapter.openLoadAnimation(2);
        mAdapter.setAutoLoadMoreSize(2);
        mAdapter.setOnLoadMoreListener(this, mRecyclerView);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void initView() {
        //头像
        simpleDraweeView = (SimpleDraweeView) headView.findViewById(R.id.dongtai_touxiang);
        //昵称
        name = (TextView) headView.findViewById(R.id.dongtai_name);
        //时间
        time = (TextView) headView.findViewById(R.id.dongtai_time);
        //内容
        comtent = (TextView) headView.findViewById(R.id.daongtai_content);
        //点赞图标
        zanbiao = (ImageButton) headView.findViewById(R.id.dongtai_zan);
        //评论图标
        pinglunbiao = (ImageView) headView.findViewById(R.id.dongtai_pinglun);
//        //九宫格
        ninegridview = (BGANinePhotoLayout) headView.findViewById(R.id.ninegridview);

        //心
        periscope = (PeriscopeLayout) findViewById(R.id.periscope);

        RelativeLayout rl = (RelativeLayout) findViewById(R.id.rl);
        rl.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Logger.e("touch 事件");
                if (contain_layout.getVisibility() == View.VISIBLE) {
                    contain_layout.setVisibility(View.GONE);
                }
                return false;
            }
        });

        zanbiao.setOnClickListener(this);
        pinglunbiao.setOnClickListener(this);
        simpleDraweeView.setOnClickListener(this);

        SoftKeyBoardListener.setListener(this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
//                Toast.makeText(this, "键盘显示 高度" + height, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void keyBoardHide(int height) {
//                Toast.makeText(this, "键盘隐藏 高度" + height, Toast.LENGTH_SHORT).show();
                Logger.e("gone");
                contain_layout.setVisibility(View.GONE);
            }
        });
    }

    private void initSource() {
        simpleDraweeView.setImageURI(message.getUser().getHeadUrl());
        name.setText(message.getUser().getNiname());
        time.setText(message.getCreatedAt());
        comtent.setText(message.getM_message());
        //九宮格
        ArrayList<String> urls = (ArrayList<String>) message.getUrls();

        if (urls != null) {
            ninegridview.init(this);
            ninegridview.setVisibility(View.VISIBLE);
            ninegridview.setData(urls);
            ninegridview.setDelegate(this);
        } else {
            ninegridview.setVisibility(View.GONE);
        }
    }

    private void queryState() {
        BmobQuery<MessageLike> query1 = new BmobQuery<>();
        query1.addWhereEqualTo("user", user);
        query1.include("source");
        query1.findObjects(new FindListener<MessageLike>() {
            @Override
            public void done(List<MessageLike> list, BmobException e) {
                Log.e("FreeStar", "ArticleItemActivity→→→done:" + list.size() + "----1111");
                if (e == null) {
                    if (list.size() > 0) {
                        for (int i = 0; i < list.size(); i++) {
                            MessageLike likeM = list.get(i);
                            Log.e("FreeStar", "ArticleItemActivity→→→done:" + likeM.getObjectId());
                            if (likeM.getSource().getObjectId().equals(message.getObjectId())) {
                                Log.e("FreeStar", "MainActivity→→→done:" + message.getObjectId());
                                Log.e("FreeStar", "ArticleItemActivity→→→done:已点赞");
                                zanbiao.setSelected(true);
                                starLike = true;
                                likeMsg = likeM;
                                Log.e("FreeStar", "ArticleItemActivity→→→done:" + likeM.getObjectId());
                            }
                        }
                    }

                } else {
                    Log.e("FreeStar", "ArticleItemActivity→→→done:" + e.getMessage());
                }
            }
        });
    }

    private void initData() {
        BmobQuery<DisSay> query = new BmobQuery<>();
        //根据message来找到评论
        query.addWhereEqualTo("message", new BmobPointer(message));
        query.order("-createdAt");
        query.setLimit(10);
        query.include("cuser,comment_father_user,father_user,author");
        query.findObjects(new FindListener<DisSay>() {
            @Override
            public void done(List<DisSay> list, BmobException e) {
                mSwipeLayout.post(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeLayout.setRefreshing(false);
                    }
                });
                if (e == null) {
                    mAdapter.setNewData(list);
                    if (list.size() < 10) {
                        mAdapter.loadMoreEnd();
                    }
                } else {
                    Log.e("FreeStar", "PageFragment1→→→done:" + e.getMessage());
                }
            }
        });
        i++;
    }

    //弹开虚拟键盘和输入框
    private void InputMethodManagerup() {
        InputMethodManager imm = (InputMethodManager) getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        conments.setFocusable(true);
        conments.setFocusableInTouchMode(true);
        conments.requestFocus();
        conments.requestFocusFromTouch();
    }

//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        downKeyBoard();
//        return super.onTouchEvent(event);
//    }

    public void downKeyBoard() {
        Logger.e("event");
        if (contain_layout.getVisibility() == View.VISIBLE) {
            contain_layout.setVisibility(View.GONE);
            InputMethodManager imm2 = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm2.hideSoftInputFromWindow(sumbit_conments.getWindowToken(), 0);
        }
    }

    @Override
    protected void onDestroy() {
        endLike = zanbiao.isSelected();
        if (starLike != endLike) {
            if (endLike) {
                message.increment("likeNum"); // 点赞数增1
                message.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:点赞成功+1");
                        }
                    }
                });
                MessageLike like = new MessageLike(user, message);
                like.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:点赞记录生成" + s);
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:失败" + e.getMessage());
                        }
                    }
                });
            } else {
                message.increment("likeNum", -1); // 点赞数减1
                message.update(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:点赞-1");
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:失败" + e.getMessage());
                        }
                    }
                });

                MessageLike la = new MessageLike();
                la.setObjectId(message.getObjectId());
                la.delete(new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e == null) {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:ok+删除记录");
                        } else {
                            Log.e("FreeStar", "ArticleItemActivity→→→done:删除失败" + e.getMessage());
                        }
                    }
                });

            }
        }
        super.onDestroy();
    }

    @Override
    public void onRefresh() {
        mSwipeLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeLayout.setRefreshing(true);
            }
        });
        initData();
    }

    private void startQuery() {
        BmobQuery<DisSay> query = new BmobQuery<>();
        //根据message来找到评论
        query.addWhereEqualTo("message", new BmobPointer(message));
        query.order("-createdAt");
        query.setLimit(10);
        query.setSkip(10 * i); // 忽略前10条数据（即第一页数据结果）
        query.include("cuser,comment_father_user,father_user,author");
        query.findObjects(new FindListener<DisSay>() {
            @Override
            public void done(List<DisSay> list, BmobException e) {
                if (e == null) {
                    if (list.size() < 10) {
                        mAdapter.loadMoreEnd();
                    } else {
                        mAdapter.addData(list);
                        mAdapter.loadMoreComplete();
                    }
                } else {
                    Log.d("FreeStar", "DynDetailActivity→→→done" + e.getMessage());
                }
            }
        });
        i++;
    }

    @Override
    public void onClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        mCurrentClickNpl = ninePhotoLayout;
        photoPreviewWrapper();
    }

    @Override
    public boolean onLongClickNinePhotoItem(BGANinePhotoLayout ninePhotoLayout, View view, int position, String model, List<String> models) {
        return false;
    }

    /**
     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PREVIEW)
    private void photoPreviewWrapper() {
        if (mCurrentClickNpl == null) {
            return;
        }

        // 保存图片的目录，改成你自己要保存图片的目录。如果不传递该参数的话就不会显示右上角的保存按钮
        File downloadDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerDownload");

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            if (mCurrentClickNpl.getItemCount() == 1) {
                // 预览单张图片
                startActivity(BGAPhotoPreviewActivity.newIntent(this, downloadDir, mCurrentClickNpl.getCurrentClickItem()));
            } else if (mCurrentClickNpl.getItemCount() > 1) {
                // 预览多张图片
                startActivity(BGAPhotoPreviewActivity.newIntent(this, downloadDir, mCurrentClickNpl.getData(), mCurrentClickNpl.getCurrentClickItemPosition()));
            }
        } else {
            EasyPermissions.requestPermissions(this, "图片预览需要以下权限:\n\n1.访问设备上的照片", REQUEST_CODE_PERMISSION_PHOTO_PREVIEW, perms);
        }
    }

    //    , R.id.dongtai_zan, R.id.dongtai_pinglun, R.id.dongtai_touxiang
    @OnClick({R.id.back_dongtai, R.id.kj_pl_fb})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_dongtai:
                finish();
                break;
            case R.id.kj_pl_fb:
                if (!sumbit_conments.isSelected()) {
                    sumbit_conments.setSelected(true);
                } else {
                    sumbit_conments.setSelected(false);
                }
                String context = conments.getText().toString().trim();
                if (context.equals("")) {
                    Toast.makeText(DynDetailActivity.this, "写写东西吧", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    conments.setText("");
                    downKeyBoard();
                }
                if (flag) {
                    //人对人的评论
                    DisSay ds = new DisSay(user, disSay, context, message, disSay.getCuser(), disSay.getCuser());
//                    User cuser, DisSay comment_father_user, String comment, Message message,User father_user
                    ds.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            Log.e("FreeStar", "DynDetailActivity→→→done:人对人评论");
                            onRefresh();
                        }
                    });  //人对动态的评论
                    flag = false;
                } else {
                    Log.e("FreeStar", "DynDetailActivity→→→onClick:人对动态的评论");
                    DisSay dsf = new DisSay(context, user, message, message.getUser());
                    message.increment("comNum");
                    message.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.e("FreeStar", "DynDetailActivity→→→done:评论数成功+1");
                            } else {
                                Log.e("FreeStar", "DynDetailActivity→→→done:评论数更新失败");
                            }
                        }
                    });
                    dsf.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            Log.e("FreeStar", "DynDetailActivity→→→done:+保存成功");
                            onRefresh();
                        }
                    });
                }
                break;
            case R.id.dongtai_zan:
                //如果已经点过赞 再点一次就取消 减一
                Log.e("FreeStar", "PicItemActivity→→→onClick:" + zanbiao.isSelected());
                if (!zanbiao.isSelected()) {
                    periscope.addHeart();
                    periscope.addHeart();
                    periscope.addHeart();
                    zanbiao.setSelected(true);
                    Log.e("FreeStar", "PicItemActivity→→→onClick:" + zanbiao.isSelected());
                } else {
                    zanbiao.setSelected(false);
                    Log.e("FreeStar", "PicItemActivity→→→onClick:" + zanbiao.isSelected());
                }
                break;
            case R.id.dongtai_pinglun:
                Log.e("FreeStar", "DynDetailActivity→→→onClick:pinglunbiao");
                contain_layout.setVisibility(View.VISIBLE);
                //弹开虚拟键盘和输入框
                InputMethodManagerup();
                break;
            case R.id.dongtai_touxiang:
                Intent intent = new Intent(DynDetailActivity.this, UserDataActivity.class);
                intent.putExtra("user", message.getUser());
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onLoadMoreRequested() {
        startQuery();
    }
}
