package freestar.friends.activity.publish;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.BmobBatch;
import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BatchResult;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.activity.PhotoPickerActivity;
import freestar.friends.bean.Atlas;
import freestar.friends.bean.Photoes;
import freestar.friends.bean.User;
import freestar.friends.util.sort_rich_editor.view.editor.SEditorData;
import freestar.friends.util.sort_rich_editor.view.editor.SortRichEditor;
import freestar.friends.util.status_bar.BaseActivity;
import freestar.friends.util.utils.Util;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;
import top.zibin.luban.Luban;


@RuntimePermissions
public class PicturePublish extends BaseActivity {
    public static final int REQUEST_CODE_PICK_IMAGE = 1023;
    public static final int REQUEST_CODE_CAPTURE_CAMEIA = 1022;

    private static final File PHOTO_DIR = new File(
            Environment.getExternalStorageDirectory() + "/DCIM/Camera");
    @Bind(R.id.btn_cancel)
    Button btnCancel;
    @Bind(R.id.btn_publish)
    Button btnPublish;
    @Bind(R.id.iv_gallery)
    ImageButton ivGallery;
    @Bind(R.id.iv_camera)
    ImageButton ivCamera;
    @Bind(R.id.richEditor)
    SortRichEditor editor;
    private String mainPic;

    private TextView tvSort;
    private File mCurrentPhotoFile;
    // 照相机拍照得到的图片
    private Intent intent;
    String kind = "风景";
    private String titleData;
    private List<String> context = new ArrayList<>();
    int index;
    private ArrayList<String> path;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                upPic();
            }
        }
    };
    private ProgressBar mProBar;

    public Intent getTakePickIntent(File f) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE, null);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_picture);
        ButterKnife.bind(this);

    }

    @OnClick({R.id.btn_cancel, R.id.btn_publish, R.id.iv_gallery, R.id.iv_camera})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_publish:
                titleData = editor.getTitleData();
                if (titleData == null || titleData.equals("")) {
                    Toast.makeText(PicturePublish.this, "标题还没输入呢", Toast.LENGTH_SHORT).show();
                } else {
                    showDL();
                }
                break;
            //tupian
            case R.id.iv_gallery:
                startActivityForResult(new Intent(this, PhotoPickerActivity.class), REQUEST_CODE_PICK_IMAGE);
                break;
            //xiangji
            case R.id.iv_camera:
                PicturePublishPermissionsDispatcher.showCameraWithCheck(this);
                break;
        }
    }

    //点击发布
    private void showDL() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("请选择类别");
        final String[] sex = {"风景", "动物", "其他"};
        builder.setSingleChoiceItems(sex, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这的which表示数组的下标
                kind = sex[which];
                Toast.makeText(PicturePublish.this, "类别为：" + sex[which], Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                textView_sex.setText(sex[which]);
                Log.e("FreeStar", "MainActivity→→→onClick:" + which);
                titleData = editor.getTitleData();
                List<SEditorData> editList = editor.buildEditData();
                // 下面的代码可以上传、或者保存，请自行实现
                dealEditData(editList);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    private void dealEditData(List<SEditorData> editList) {
        path = new ArrayList<>();
        String inputStr;
        String imagePath;
        for (int i = 0; i < editList.size(); i++) {
            SEditorData data = editList.get(i);
            inputStr = data.getInputStr();
            imagePath = data.getImagePath();
            if (i == 0) {
                if (inputStr != null) {
                    if (imagePath == null) {
                        Toast.makeText(PicturePublish.this, "第一行只能放图片哦", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
            }
            if (!(inputStr == null && imagePath == null)) {
                if (imagePath != null) {
                    path.add(data.getImagePath());
                    if (inputStr != null && !inputStr.trim().equals("")) {
                        context.add(inputStr);
                    } else {
                        context.add("");
                    }
                } else {
                    String s = context.get(path.size() - 1);
                    s += inputStr;
                    context.set(path.size() - 1, s);
                }
            }
        }
        if (path.size() == 0) {
            Toast.makeText(PicturePublish.this, "请至少添加一张图片", Toast.LENGTH_SHORT).show();
            return;
        }
        createProgressBar();
        index = path.size();
        for (int i = 0; i < path.size(); i++) {
            compressWithRx(new File(path.get(i)), i);
        }
    }

    /**
     * 压缩单张图片 RxJava 方式
     */
    private void compressWithRx(File file, final int i) {
        Luban.get(this)
                .load(file)
                .putGear(Luban.THIRD_GEAR)
                .asObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnError(new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends File>>() {
                    @Override
                    public Observable<? extends File> call(Throwable throwable) {
                        return Observable.empty();
                    }
                })
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        path.set(i, file.getAbsolutePath());
                        if (i == index - 1) {
                            handler.sendEmptyMessage(1);
                        }
                    }
                });
    }

    private void createProgressBar() {
        FrameLayout layout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;

        mProBar = new ProgressBar(this);
        mProBar.setLayoutParams(layoutParams);
        mProBar.setVisibility(View.VISIBLE);
        layout.addView(mProBar);
    }

    private void upPic() {
        //图片数组
        final String[] strPath = new String[path.size()];
        for (int i = 0; i < path.size(); i++) {
            strPath[i] = path.get(i);
        }
        BmobFile.uploadBatch(strPath, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == strPath.length) {
                    //如果数量相等，则代表文件全部上传完成
                    mainPic = files.get(0).getUrl();
                    addInfo(urls);
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                if (statuscode == 9016) {
                    Toast.makeText(PicturePublish.this, "网络异常，发布失败", Toast.LENGTH_SHORT).show();
                    return;
                }
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

    private void addInfo(final List<String> urls) {
        User user = new User();
        user.setObjectId(App.userId);

        Atlas atlas = new Atlas(kind, Util.getPicName(mainPic), user, 0, 0, titleData, mainPic, urls.size() + "");
        atlas.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(PicturePublish.this, "成功", Toast.LENGTH_SHORT).show();
                    //添加关联
                    addAss(urls, s);
                }
            }

        });
    }

    private void addAss(List<String> urls, String s) {

        ArrayList<BmobObject> picList = new ArrayList<>();
        Atlas atlas = new Atlas();
        atlas.setObjectId(s);
        if (context.size() > urls.size()) {
            context.set(1, context.get(0) + context.get(1));

            for (int i = 0; i < urls.size(); i++) {
                picList.add(new Photoes(atlas, urls.get(i), context.get(i + 1), Util.getPicName(urls.get(i))));
            }
        } else {
            for (int i = 0; i < urls.size(); i++) {
                picList.add(new Photoes(atlas, urls.get(i), context.get(i), Util.getPicName(urls.get(i))));
            }
        }

        new BmobBatch().insertBatch(picList).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        BatchResult result = list.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            Log.e("FreeStar", "PublishPicture→→→done:" + "第" + i + "个数据批量添加成功：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt());
                        } else {
                            Log.e("FreeStar", "PublishPicture→→→done:" + "第" + i + "个数据批量添加失败：" + ex.getMessage() + "," + ex.getErrorCode());
                        }
                    }
                    finish();
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }

            }
        });
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        // NOTE: Perform action that requires the permission. If this is run by PermissionsDispatcher, the permission will have been granted
        PHOTO_DIR.mkdirs();// 创建照片的存储目录
        mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
        intent = getTakePickIntent(mCurrentPhotoFile);
        Log.e("FreeStar", "MainActivity→→→openCamera:");

        startActivityForResult(intent, REQUEST_CODE_CAPTURE_CAMEIA);
    }

    /**
     * 用当前时间给取得的图片命名
     */
    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "'IMG'_yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(date) + ".jpg";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (editor.isSort()) {
            tvSort.setText("排序");
        }
        if (requestCode == REQUEST_CODE_PICK_IMAGE) {
            String[] photoPaths = data.getStringArrayExtra(PhotoPickerActivity.INTENT_PHOTO_PATHS);
            editor.addImageArray(photoPaths);
        } else if (requestCode == REQUEST_CODE_CAPTURE_CAMEIA) {
            editor.addImage(mCurrentPhotoFile.getAbsolutePath());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // NOTE: delegate the permission handling to generated method
        PicturePublishPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnPermissionDenied(Manifest.permission.CAMERA)
    void showDeniedForCamera() {
        //获取权限失败
//        Toast.makeText(this, R.string.permission_camera_denied, Toast.LENGTH_SHORT).show();
        Log.e("FreeStar", "PublishPicture→→→showDeniedForCamera:");
    }

    @OnNeverAskAgain(Manifest.permission.CAMERA)
    void showNeverAskForCamera() {
        //这是永久拒绝授权
        Toast.makeText(this, "好的知道了", Toast.LENGTH_SHORT).show();
        Log.e("FreeStar", "MainActivity→→→showNeverAskForCamera:好的知道了");
    }

//    显示选择的弹窗

    private void showRationaleDialog(@StringRes int messageResId, final PermissionRequest request) {
        new AlertDialog.Builder(this)
                .setPositiveButton(R.string.button_allow, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.proceed();
                    }
                })
                .setNegativeButton(R.string.button_deny, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(@NonNull DialogInterface dialog, int which) {
                        request.cancel();
                    }
                })
                .setCancelable(false)
                .setMessage(messageResId)
                .show();
    }
}
