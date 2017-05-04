package freestar.friends.activity.publish;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerActivity;
import cn.bingoogolapple.photopicker.activity.BGAPhotoPickerPreviewActivity;
import cn.bingoogolapple.photopicker.widget.BGASortableNinePhotoLayout;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.bean.Message;
import freestar.friends.bean.User;
import freestar.friends.util.status_bar.BaseActivity;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/*
发表说说
 */
public class DynamicPublishActivity extends BaseActivity
        implements EasyPermissions.PermissionCallbacks,
        BGASortableNinePhotoLayout.Delegate {

    private static final int REQUEST_CODE_PERMISSION_PHOTO_PICKER = 1;
    private static final int REQUEST_CODE_CHOOSE_PHOTO = 1;
    private static final int REQUEST_CODE_PHOTO_PREVIEW = 2;
    private static final String EXTRA_MOMENT = "EXTRA_MOMENT";
    private static final int MAX_PHOTO_COUNT = 9;

    private CheckBox mSortableCb;
    private BGASortableNinePhotoLayout mPhotosSnpl;
    private EditText mContentEt;
    private TextView mchoice, mpublish;
    private ImageView back_dy;
    private ImageButton fabiao;
    private ArrayList<String> data1;//本地图片ID
    private String content;//内容

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dy_fabu);
        mContentEt = (EditText) findViewById(R.id.et_moment_add_content);
        mPhotosSnpl = (BGASortableNinePhotoLayout) findViewById(R.id.snpl_moment_add_photos);
        fabiao = (ImageButton) findViewById(R.id.fabiao);
        back_dy = (ImageView) findViewById(R.id.back_dy);
        initclick();
    }

    private void initclick() {
        mPhotosSnpl.init(this);
        //是否显示加号图片
        mPhotosSnpl.setIsPlusSwitchOpened(true);
        //图片是否可拖拽
        mPhotosSnpl.setIsSortable(false);
        // 设置拖拽排序控件的代理
        mPhotosSnpl.setDelegate(this);
        fabiao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fabiao.isSelected()){
                    fabiao.setSelected(true);
                }else {
                    fabiao.setSelected(false);
                }
                content = mContentEt.getText().toString().trim();
                if (content.length() == 0) {
                    Toast.makeText(DynamicPublishActivity.this, "写点东西吧！", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    //选了本地照片
                    if (data1 != null) {
                        upPic();
                    } else {
                        addNoPoInfo();
                    }
                    createProgressBar();
                }
            }
        });
        back_dy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        ProgressBar mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    //开始执行发布
    private void addNoPoInfo() {
        User user = new User();
        user.setObjectId(App.userId);
        Message message = new Message(0, 0, user, content);
        message.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(DynamicPublishActivity.this, "成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DynamicPublishActivity.this, "失败", Toast.LENGTH_SHORT).show();
                }
                DynamicPublishActivity.this.finish();
            }

        });
    }

    //-------------------------------------------------  private ArrayList<String> data1;
    private void upPic() {
        //图片数组 集合转换成数组
        final String[] strPath = new String[data1.size()];
        for (int i = 0; i < data1.size(); i++) {
            strPath[i] = data1.get(i);
        }
        Log.e("FreeStar", "MainActivity→→→dealEditData:" + strPath.length);

        for (String s : strPath) {
            Log.e("FreeStar", "MainActivity→→→upPic:" + s);
        }

        BmobFile.uploadBatch(strPath, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == strPath.length) {
                    //如果数量相等，则代表文件全部上传完成
                    //do something
                    Log.e("FreeStar", "MainActivity→→→onSuccess:成功咯" + strPath.length);
                    for (String url : urls) {
                        Log.e("FreeStar", "MainActivity→→→onSuccess:" + url);
                    }
//                    mainPic = files.get(0).getUrl();
                    addInfo(urls);
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                Log.e("FreeStar", "MainActivity→→→onError:" + "错误码" + statuscode + ",错误描述：" + errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
    }

    //发表-------------------------》上传数据到数据库
    private void addInfo(final List<String> urls) {
        User user = new User();
        user.setObjectId(App.userId);
        Message message = new Message(0, 0, urls, user, content);
        message.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(DynamicPublishActivity.this, "成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(DynamicPublishActivity.this, "失败", Toast.LENGTH_SHORT).show();
                }
                finish();
            }

        });

    }


//    public void onClick(View v) {
////         if (v.getId()==R.id.tv_moment_add_choice_photo){
////             choicePhotoWrapper();
////         }else if (v.getId()==R.id.tv_moment_add_publish){
////             String content = mContentEt.getText().toString().trim();
////             if (content.length() == 0 && mPhotosSnpl.getItemCount() == 0) {
////                 Toast.makeText(this, "必须填写这一刻的想法或选择照片！", Toast.LENGTH_SHORT).show();
////                 return;
////             }
////         }
//
//
//    }

    //获取权限时
    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        //不做处理
    }

    //未获取权限时
    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Toast.makeText(this, "您拒绝了「图片选择」所需要的相关权限!", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClickAddNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, ArrayList<String> models) {
        choicePhotoWrapper();
    }

    @Override
    public void onClickDeleteNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        mPhotosSnpl.removeItem(position);
    }

    @Override
    public void onClickNinePhotoItem(BGASortableNinePhotoLayout sortableNinePhotoLayout, View view, int position, String model, ArrayList<String> models) {
        startActivityForResult(BGAPhotoPickerPreviewActivity.newIntent(DynamicPublishActivity.this, MAX_PHOTO_COUNT, models, models, position, false), REQUEST_CODE_PHOTO_PREVIEW);
    }

    @AfterPermissionGranted(REQUEST_CODE_PERMISSION_PHOTO_PICKER)
    private void choicePhotoWrapper() {
        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // 拍照后照片的存放目录，改成你自己拍照后要存放照片的目录。如果不传递该参数的话就没有拍照功能
            File takePhotoDir = new File(Environment.getExternalStorageDirectory(), "BGAPhotoPickerTakePhoto");

            startActivityForResult(BGAPhotoPickerActivity.newIntent(this, takePhotoDir, MAX_PHOTO_COUNT, mPhotosSnpl.getData()), REQUEST_CODE_CHOOSE_PHOTO);
        } else {
            EasyPermissions.requestPermissions(this, "图片选择需要以下权限:\n\n1.访问设备上的照片", REQUEST_CODE_PERMISSION_PHOTO_PICKER, perms);
        }
    }
//    onRequestPermissionsResult

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

//onActivityResult

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE_CHOOSE_PHOTO) {
            mPhotosSnpl.setData(BGAPhotoPickerActivity.getSelectedImages(data));
            data1 = mPhotosSnpl.getData();
        } else if (requestCode == REQUEST_CODE_PHOTO_PREVIEW) {
            mPhotosSnpl.setData(BGAPhotoPickerPreviewActivity.getSelectedImages(data));
            data1 = mPhotosSnpl.getData();
        }
    }

}

