package freestar.friends.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGANinePhotoLayout;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import de.hdodenhof.circleimageview.CircleImageView;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.User;
import freestar.friends.fragments.CommFragment;
import freestar.friends.fragments.DynFragment;
import freestar.friends.fragments.MsgFragment;
import freestar.friends.util.profile.CollectActivity1;
import freestar.friends.util.profile.ConcernActivity1;
import freestar.friends.util.profile.MyinfoActivity;
import freestar.friends.util.profile.SetupActivity;
import freestar.friends.util.profile.TypeActivity1;
import freestar.friends.util.status_bar.BaseActivity;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class HomePageA extends BaseActivity implements EasyPermissions.PermissionCallbacks{
    RelativeLayout layout;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;

    //四个碎片
    CommFragment communityFragment;
    MsgFragment messageFragment;
    DynFragment dynamiuFragment;
    RadioGroup radioGroup;

    //声明临时变量，用来指向当前页面显示的碎片
    Fragment fragment;
    @Bind(R.id.main_main)
    RelativeLayout mainMain;
    @Bind(R.id.community)
    RadioButton community;
    @Bind(R.id.message)
    RadioButton message;
    @Bind(R.id.dynamic)
    RadioButton dynamic;

    @Bind(R.id.bottom_radio)
    RadioGroup bottomRadio;
    @Bind(R.id.slide_imag)
    CircleImageView slideImag;
    @Bind(R.id.slide_tv)
    TextView slideTv;
    @Bind(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    Intent intent;
//    ArrayList<User> friendList;


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                findFriends();
            }
        }


    };
    private int RESC = 6;

    private void findFriends() {


//   查询出数据库的人   将信息给消息提供者
//        friendList = new ArrayList<>();
        BmobQuery<User> query = new BmobQuery<>();
//        query.order("-createdAt");
        query.findObjects(new FindListener<User>() {
            @Override
            public void done(final List<User> list, BmobException e) {
                if (e == null) {
                    for (User user : list) {
                        Log.e("friendList", "friendList的信息为 ：" + user.toString());
                    }
                    RongIM.setUserInfoProvider(new RongIM.UserInfoProvider() {
                        @Override
                        public UserInfo getUserInfo(String s) {
                            Log.e("FreeStar", "HomePageA→→→getUserInfo:" + s);
                            for (User i : list) {
                                if (i.getObjectId().equals(s)) {
                                    return new UserInfo(i.getObjectId(), i.getNiname(), Uri.parse(i.getHeadUrl()));
                                }
                            }
                            return null;
                        }
                    }, true);
                } else {
                    Log.e("FreeStar", "HomePageA→→→done:" + e.getMessage());
                    Toast.makeText(HomePageA.this, "网络异常", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//       Support getActionBar().hide();
        setContentView(R.layout.activity_community);
        ButterKnife.bind(this);
        initData();
        radioGroup = (RadioGroup) findViewById(R.id.bottom_radio);
        //监听事件groupdadio
        addlistner();
        //获取默认碎片
        defaultFragment();
        perssign();
        getAndset();
    }

    private void initData() {
        Log.e("FreeStar", "HomePageA→→→initData:" + App.token);
        RongIM.connect(App.token, new RongIMClient.ConnectCallback() {
            @Override
            public void onTokenIncorrect() {
            }

            @Override
            public void onSuccess(String s) {
                Log.e("FreeStar", "HomePageA→→→onSuccess:登录成功");
                handler.sendEmptyMessage(1);
            }

            @Override
            public void onError(RongIMClient.ErrorCode errorCode) {
                Log.e("FreeStar", "HomePageA→→→onError:" + errorCode.getMessage());
                Toast.makeText(HomePageA.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void defaultFragment() {
        communityFragment = new CommFragment();
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main_main, communityFragment);
        fragmentTransaction.commit();//提交事务.commit();
        fragment = communityFragment;
    }

    private void addlistner() {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                fragmentTransaction = fragmentManager.beginTransaction();
                switch (i) {
                    case R.id.community:
                        //首先隐藏当前已经显示出来的碎片
                        fragmentTransaction.hide(fragment);
                        //需要界面显示当前碎片为消息碎片
                        if (communityFragment != null && communityFragment.isAdded()) {
                            //已经添加，隐藏已经显示的碎片，显示当前的碎片
                            fragmentTransaction.show(communityFragment);
                        } else {
                            //没有添加，属于第一次添加,
                            communityFragment = new CommFragment();
                            fragmentTransaction.add(R.id.main_main, communityFragment);
                        }
                        fragment = communityFragment;
                        break;
                    case R.id.message:
                        //首先隐藏当前已经显示出来的碎片
                        fragmentTransaction.hide(fragment);
                        //需要界面显示当前碎片为圈子碎片
                        /*mGroupFragment = new GroupFragment();
                        mFragmentTransaction.replace(RenWen.id.middle,mGroupFragment);*/
                        if (messageFragment != null && messageFragment.isAdded()) {
                            //已经添加，隐藏已经显示的碎片，显示当前的碎片
                            fragmentTransaction.show(messageFragment);
                        } else {
                            //没有添加，属于第一次添加
                            messageFragment = new MsgFragment();
                            fragmentTransaction.add(R.id.main_main, messageFragment);
                        }
                        fragment = messageFragment;
                        break;

                    case R.id.dynamic:
                        //首先隐藏当前已经显示出来的碎片
                        fragmentTransaction.hide(fragment);
                        //需要界面显示当前碎片为学校碎片
                        /*mSchoolFragment = new SchoolFragment();
                        mFragmentTransaction.replace(RenWen.id.middle,mSchoolFragment);*/
                        if (dynamiuFragment != null && dynamiuFragment.isAdded()) {
                            //已经添加，隐藏已经显示的碎片，显示当前的碎片
                            fragmentTransaction.show(dynamiuFragment);
                        } else {
                            //没有添加，属于第一次添加
                            dynamiuFragment = new DynFragment();
                            fragmentTransaction.add(R.id.main_main, dynamiuFragment);
                        }
                        fragment = dynamiuFragment;
                        break;
                }
                fragmentTransaction.commit();
            }
        });
    }

    //头像点击
    public void slideimgClick(View view) {
        startActivityForResult(new Intent(this, MyinfoActivity.class), RESC);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESC && resultCode == RESULT_OK) {
            String back = data.getStringExtra("url");
            Log.e("FreeStar", "HomePageA→→→onActivityResult:" + back);
            if (back != null) {
                Picasso.with(getBaseContext()).load(back).placeholder(R.mipmap.ic_launcher).into(slideImag);
            }
            String name = data.getStringExtra("name");
            if (name != null) {
                slideTv.setText(name);
            }
        }
    }

    //设置
    public void szClick(View view) {
        startActivity(new Intent(this, SetupActivity.class));
    }

    //主页
    public void godfriendsClick(View view) {
        // startActivity(new Intent(this, ContactActivity.class));
//        Intent intent = new Intent(this, HomePageA.class);
//        startActivity(intent);
//        finish();
        drawerLayout.closeDrawers();
    }

    //发布
    public void typesClick(View view) {
        Intent intent = new Intent(this, TypeActivity1.class);
        startActivity(intent);
    }

    //收藏
    public void collecClick(View view) {
        Intent intent = new Intent(this, CollectActivity1.class);
        startActivity(intent);
    }

    //关注
    public void concernClick(View view) {
        Intent intent = new Intent(this, ConcernActivity1.class);
        startActivity(intent);
    }

//    public interface MyTouchListener {
//        public void onTouchEvent(MotionEvent event);
//    }
//
//    // 保存MyTouchListener接口的列表
//    private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<>();
//
//    /**
//     * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
//     *
//     * @param listener
//     */
//    public void registerMyTouchListener(MyTouchListener listener) {
//        myTouchListeners.add(listener);
//    }
//
//    /**
//     * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
//     *
//     * @param listener
//     */
//    public void unRegisterMyTouchListener(MyTouchListener listener) {
//        myTouchListeners.remove(listener);
//    }
//
//    /**
//     * 分发触摸事件给所有注册了MyTouchListener的接口
//     */
//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        for (MyTouchListener listener : myTouchListeners) {
//            listener.onTouchEvent(ev);
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    private void getAndset() {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(App.userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    if (user.getHeadUrl() == null) {
                        slideImag.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Picasso.with(getBaseContext()).load(user.getHeadUrl()).placeholder(R.mipmap.ic_launcher).into(slideImag);
                    }
                } else {
                    Toast.makeText(HomePageA.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void perssign() {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.setMaxCacheAge(TimeUnit.DAYS.toMillis(15));
        bmobQuery.getObject(App.userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    slideTv.setText(user.getNiname());
                }
            }
        });
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {

            if ((System.currentTimeMillis() - exitTime) > 2000) //System.currentTimeMillis()无论何时调用，肯定大于2000
            {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private BGANinePhotoLayout mCurrentClickNpl;

    public void setCurrentClickNpl(BGANinePhotoLayout currentClickNpl) {
        mCurrentClickNpl = currentClickNpl;
    }

    private static final int REQUEST_CODE_PERMISSION_PHOTO_PREVIEW = 1;

    /**
     * 图片预览，兼容6.0动态权限
     */
    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PREVIEW)
    public void photoPreviewWrapper() {
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

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }
}
