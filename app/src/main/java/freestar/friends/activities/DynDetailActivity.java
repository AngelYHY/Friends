package freestar.friends.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
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
import freestar.friends.bean.DisSay;
import freestar.friends.bean.Message;
import freestar.friends.bean.MessageLike;
import freestar.friends.bean.User;
import freestar.friends.util.abslistview.CommonAdapter;
import freestar.friends.util.abslistview.ViewHolder;
import freestar.friends.util.fly_heat.PeriscopeLayout;
import freestar.friends.util.status_bar.BaseActivity;
import freestar.friends.util.view.XListView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * 詳情界面+listview（評論）
 */
public class DynDetailActivity extends BaseActivity implements XListView.IXListViewListener, BGANinePhotoLayout.Delegate {

    Message message;

    SimpleDraweeView simpleDraweeView;

    TextView name, time, comtent;

    BGANinePhotoLayout ninegridview;

    ImageView zanbiao, pinglunbiao, comeback, sumbit_conments;
    LinearLayout contain_layout;
    EditText conments;

    CommonAdapter<DisSay> commentAdapter;//評論的適配器
    List<DisSay> mList = new ArrayList<>();//数据源
    boolean flag;
    DisSay disSay;

    @Bind(R.id.back_dongtai)
    ImageView mBackDongtai;

    @Bind(R.id.kj_pl)
    EditText mKjPl;
    @Bind(R.id.kj_pl_fb)
    ImageButton mKjPlFb;
    @Bind(R.id.kj_pl_slu)
    LinearLayout mKjPlSlu;
    @Bind(R.id.periscope)
    PeriscopeLayout mPeriscope;

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

        initView();
        queryState();
        initSource();
        initSourceAdapter();
        initClick();
    }

    private void initView() {
        //加头布局
        headView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.detail_layout, null);
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
        //返回
        comeback = (ImageView) findViewById(R.id.back_dongtai);
        //心
        periscope = (PeriscopeLayout) findViewById(R.id.periscope);
        //弹出框
        contain_layout = (LinearLayout) findViewById(R.id.kj_pl_slu);
        //提交按钮
        sumbit_conments = (ImageButton) findViewById(R.id.kj_pl_fb);
        //文本框
        conments = (EditText) findViewById(R.id.kj_pl);

//        conment_listview = (XListView) findViewById(R.id.commemt_listview);
//        conment_listview.addHeaderView(headView);
//
//        conment_listview.setPullLoadEnable(true);
//        conment_listview.setXListViewListener(this);
//        handler = new Handler();
    }

    private void initSource() {
        simpleDraweeView.setImageURI(message.getUser().getHeadUrl());
        name.setText(message.getUser().getNiname());
        time.setText(message.getCreatedAt());
        comtent.setText(message.getM_message());
        //九宮格
        ArrayList<String> urls = (ArrayList<String>) message.getUrls();

        if (urls != null) {
//            List<ImageInfo> imgs = new ArrayList<>();
//            for (int j = 0; j < urls.size(); j++) {
//                ImageInfo img = new ImageInfo();
//                img.setBigImageUrl(urls.get(j));
//                img.setThumbnailUrl(urls.get(j));
//                imgs.add(img);
//            }
            ninegridview.init(this);
            ninegridview.setVisibility(View.VISIBLE);
            ninegridview.setData(urls);
            ninegridview.setDelegate(this);
//            viewHolder.nineGridView.setAdapter(new NineGridViewClickAdapter(context, imgs));
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

    private void initSourceAdapter() {
        BmobQuery<DisSay> query = new BmobQuery<>();
        //根据message来找到评论
        query.addWhereEqualTo("message", new BmobPointer(message));
        query.order("-createdAt");
        query.setLimit(10);
        query.include("cuser,comment_father_user,father_user,author");
        query.findObjects(new FindListener<DisSay>() {
            @Override
            public void done(List<DisSay> list, BmobException e) {
                if (e == null) {
                    mList.addAll(list);
                    if (commentAdapter == null) {
                        initAdapter();
                    } else {
                        commentAdapter.notifyDataSetChanged();
//                        conment_listview.stopRefresh();
                    }
                } else {
                    Log.d("FreeStar", "DynDetailActivity→→→done" + e.getMessage());
                }
            }
        });
        i++;
    }

    private void initAdapter() {
        commentAdapter = new CommonAdapter<DisSay>(this, R.layout.dis_item_layout, mList) {
            @Override
            protected void convert(ViewHolder holder, final DisSay s, int position) {
                User cuser = s.getCuser();
                Log.e("FreeStar", "DynDetailActivity→→→convert:" + cuser.toString());
                //赋值
                holder.setSDV(R.id.sdv_head, cuser.getHeadUrl()).setText(R.id.tv_user_name, cuser.getNiname()).setText(R.id.tv_data, s.getCreatedAt()).setText(R.id.tv_disNum, s.getComment());
                if (s.getComment_father_user() != null) {
                    //评论的人
                    DisSay atlasDis = s.getComment_father_user();
                    //写说说的人
                    User father_user = s.getFather_user();
                    //是否显示
                    holder.setVisible(R.id.line_father_comment, true).setText(R.id.btn_name, father_user.getNiname()).setText(R.id.tv_father_comment, "    " + atlasDis.getComment()).setText(R.id.tv_father_data, atlasDis.getCreatedAt());
                } else {
                    holder.setVisible(R.id.line_father_comment, false);
                }
                holder.setOnClickListener(R.id.line_reply, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        contain_layout.setVisibility(View.VISIBLE);
                        //弹开虚拟键盘和输入框
                        InputMethodManagerup();
                        flag = true;
                        disSay = s;
                    }
                });
                holder.setOnClickListener(R.id.sdv_head, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DynDetailActivity.this, UserDataActivity.class);
                        intent.putExtra("user", s.getCuser());
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + s.getCuser());
                        startActivity(intent);
                    }
                });
                holder.setOnClickListener(R.id.btn_name, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(DynDetailActivity.this, UserDataActivity.class);
                        intent.putExtra("user", s.getFather_user());
                        Log.e("FreeStar", "SearchActivity→→→onClick:" + s.getFather_user());
                        startActivity(intent);
                    }
                });
            }
        };
        conment_listview.setAdapter(commentAdapter);
    }

    private void initClick() {
        zanbiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        pinglunbiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("FreeStar", "DynDetailActivity→→→onClick:pinglunbiao");
                contain_layout.setVisibility(View.VISIBLE);
                //弹开虚拟键盘和输入框
                InputMethodManagerup();
            }
        });

        simpleDraweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DynDetailActivity.this, UserDataActivity.class);
                intent.putExtra("user", message.getUser());
                startActivity(intent);
            }
        });

        sumbit_conments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("FreeStar", "DynDetailActivity→→→onClick:进入点击事件");
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
                    Downkeyboard();
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
            }
        });
        comeback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Downkeyboard();
        return super.onTouchEvent(event);
    }

    public void Downkeyboard() {
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
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mList.clear();
                i = 0;
                initSourceAdapter();
                onLoad();

            }
        }, 2000);

    }

    @Override
    public void onLoadMore() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startQuery();
                onLoad();
            }
        }, 2000);
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
                    if (list.size() == 0) {
                        Toast.makeText(DynDetailActivity.this, "没有数据了", Toast.LENGTH_SHORT).show();
                    } else {
                        mList.addAll(list);
                        commentAdapter.notifyDataSetChanged();
                    }
//                    conment_listview.stopLoadMore();
                } else {
                    Log.d("FreeStar", "DynDetailActivity→→→done" + e.getMessage());
                }
            }
        });
        i++;
    }

    private void onLoad() {
        conment_listview.stopRefresh();
        conment_listview.stopLoadMore();
        conment_listview.setRefreshTime(new Date().toLocaleString());
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
}
