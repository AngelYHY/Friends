package freestar.friends.activities;

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
import freestar.friends.bean.Article;
import freestar.friends.bean.ItemU;
import freestar.friends.bean.User;
import freestar.friends.util.sort_rich_editor.view.editor.SEditorData;
import freestar.friends.util.sort_rich_editor.view.editor.SortRichEditor;
import freestar.friends.util.status_bar.BaseActivity;
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
public class PublishArticle extends BaseActivity {
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
    String kind = "技巧";
    private String titleData;
    private List<ItemU> itemUs;

    private List<String> path;
    private ProgressBar mProBar;
    private int index;

    private int flag;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                upPic();
            } else if (msg.what == 0) {

            }
        }
    };

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

    @OnClick({R.id.btn_cancel, R.id.btn_publish, R.id.iv_gallery, R.id.iv_camera})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cancel:
                finish();
                break;
            case R.id.btn_publish:
                titleData = editor.getTitleData();
                if (titleData == null || titleData.equals("")) {
                    Toast.makeText(this, "标题还没输入呢", Toast.LENGTH_SHORT).show();
                } else {
                    showDL();
                }
                break;
            case R.id.iv_gallery:
                startActivityForResult(new Intent(this, PhotoPickerActivity.class), REQUEST_CODE_PICK_IMAGE);
                break;
            case R.id.iv_camera:
                PublishArticlePermissionsDispatcher.showCameraWithCheck(this);
                break;
        }
    }

    private void showDL() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("请选择类别");
        final String[] sex = {"技巧", "器材", "游记"};
        builder.setSingleChoiceItems(sex, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这的which表示数组的下标
                kind = sex[which];
                Toast.makeText(PublishArticle.this, "类别为：" + sex[which], Toast.LENGTH_SHORT).show();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
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
        itemUs = new ArrayList<>();
        path = new ArrayList<>();
        String inputStr;
        String imagePath;
        int ii = 0;
        for (int i = 0; i < editList.size(); i++) {
            SEditorData data = editList.get(i);
            inputStr = data.getInputStr();
            imagePath = data.getImagePath();
            if (!(inputStr == null && imagePath == null)) {
                if (imagePath != null) {
                    itemUs.add(new ItemU(imagePath, true, ii++));
                    if (inputStr != null && !inputStr.trim().equals("")) {
                        itemUs.add(new ItemU(false, inputStr, ii++));
                    }
                } else {
                    if (!inputStr.trim().equals("")) {
                        itemUs.add(new ItemU(false, inputStr, ii++));
                    }
                }
            }
        }
        for (ItemU itemU : itemUs) {
            boolean flag = itemU.isUrl();
            if (flag) {
                path.add(itemU.getUrl());
            }
        }
        if (path.size() == 0) {
            Toast.makeText(this, "至少需要一张图片哦", Toast.LENGTH_SHORT).show();
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
                        Log.e("FreeStar", "PublishPicture→→→call:报错了呀");
                        throwable.printStackTrace();
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends File>>() {
                    @Override
                    public Observable<? extends File> call(Throwable throwable) {
                        Log.e("FreeStar", "PublishPicture→→→call:为什么错呢");
                        return Observable.empty();
                    }
                })
                .subscribe(new Action1<File>() {
                    @Override
                    public void call(File file) {
                        path.set(i, file.getAbsolutePath());
                        Log.e("FreeStar", "PublishPicture→→→call:" + i);
                        if (i == index - 1) {
                            handler.sendEmptyMessage(1);
                            Log.e("FreeStar", "PublishPicture→→→call:执行上传操作");
                        }
                        Log.e("FreeStar", "PublishPicture→→→call:" + file.getAbsolutePath());
                    }
                });
    }

    private void upPic() {
        //图片数组
        final String[] strPath = new String[path.size()];
        for (int i = 0; i < path.size(); i++) {
            strPath[i] = path.get(i);
        }
        Log.e("FreeStar", "PublishArticle→→→dealEditData:" + strPath.length);

        for (String s : strPath) {
            Log.e("FreeStar", "PublishArticle→→→upPic:" + s);
        }

        BmobFile.uploadBatch(strPath, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == strPath.length) {
                    //如果数量相等，则代表文件全部上传完成
                    //do something
                    mainPic = files.get(0).getUrl();
                    addAss(urls, mainPic);
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                Log.e("FreeStar", "PublishArticle→→→onError:" + "错误码" + statuscode + ",错误描述：" + errormsg);
                if (statuscode == 9016) {
                    Log.e("FreeStar", "PublishArticle→→→onError:有没执行呀");
                    mProBar.setVisibility(View.GONE);
                    Toast.makeText(PublishArticle.this, "网络异常，发布失败", Toast.LENGTH_SHORT).show();
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

    private void addInfo(String s) {

        ArrayList<BmobObject> items = new ArrayList<>();

//        ItemU item = new ItemU();
//        item.setObjectId(s);
        Article article = new Article();
        article.setObjectId(s);

        for (ItemU itemU : itemUs) {
            itemU.setArticle(article);
        }
        items.addAll(itemUs);

        new BmobBatch().insertBatch(items).doBatch(new QueryListListener<BatchResult>() {
            @Override
            public void done(List<BatchResult> list, BmobException e) {
                if (e == null) {
                    for (int i = 0; i < list.size(); i++) {
                        BatchResult result = list.get(i);
                        BmobException ex = result.getError();
                        if (ex == null) {
                            Log.e("FreeStar", "PublishArticle→→→done:" + "第" + i + "个数据批量添加成功：" + result.getCreatedAt() + "," + result.getObjectId() + "," + result.getUpdatedAt());

                        } else {
                            Log.e("FreeStar", "PublishArticle→→→done:" + "第" + i + "个数据批量添加失败：" + ex.getMessage() + "," + ex.getErrorCode());
                        }
                    }
                    finish();
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }

            }
        });
    }

    private void addAss(List<String> urls, String mainPic) {

//        ArrayList<BmobObject> picList = new ArrayList<>();
        User user = new User();
        user.setObjectId(App.userId);

        Article article = new Article(kind, user, 0, 0, titleData, mainPic, urls);

        article.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(PublishArticle.this, "成功", Toast.LENGTH_SHORT).show();
                    //添加关联
                    addInfo(s);
                }
            }

        });
    }

    @NeedsPermission(Manifest.permission.CAMERA)
    void showCamera() {
        // NOTE: Perform action that requires the permission. If this is run by PermissionsDispatcher, the permission will have been granted
        PHOTO_DIR.mkdirs();// 创建照片的存储目录
        mCurrentPhotoFile = new File(PHOTO_DIR, getPhotoFileName());// 给新照的照片文件命名
        Intent intent = getTakePickIntent(mCurrentPhotoFile);
        Log.e("FreeStar", "PublishArticle→→→openCamera:");

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
        PublishArticlePermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
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
        Log.e("FreeStar", "PublishArticle→→→showNeverAskForCamera:好的知道了");
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
