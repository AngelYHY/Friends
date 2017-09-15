package freestar.friends.activity.setting;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.view.SimpleDraweeView;

import java.io.File;
import java.util.concurrent.TimeUnit;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;
import freestar.friends.App;
import freestar.friends.R;
import freestar.friends.activity.LoginActivity;
import freestar.friends.bean.User;
import freestar.friends.util.status_bar.StatusBarUtil;
import freestar.friends.util.takephoto.app.TakePhotoFragmentActivity;
import freestar.friends.util.takephoto.compress.CompressConfig;
import freestar.friends.util.takephoto.model.CropOptions;

public class MyInfoActivity extends TakePhotoFragmentActivity {

    public static final int NAME = 1;
    private TextView textView_niname, textView_sex, textView_br, textView_gex;
    DatePickerDialog dialog;
    private SimpleDraweeView set_avator;
    private String kind;
    String[] sexs = new String[]{"男", "女"};
    public static User user;
    private int RESC = 8;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        StatusBarUtil.setColor(this, 0xDE051B, 0);
        //昵称
        textView_niname = (TextView) findViewById(R.id.textView2);
        //出生日期
        textView_br = (TextView) findViewById(R.id.textView_bir);
        //性别
        textView_sex = (TextView) findViewById(R.id.textView_sex);
        //个性签名
        textView_gex = (TextView) findViewById(R.id.textView_gex);
        //头像
        set_avator = (SimpleDraweeView) findViewById(R.id.sdv_head);

        user = new User();
        user.setObjectId(App.userId);

        getAndset();
        perssign();
    }


    public void sexClick(View view) {
        new AlertDialog.Builder(this)
                .setTitle("别选错了哦！")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setSingleChoiceItems(sexs, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        textView_sex.setText(sexs[which]);
                        updateInfo(which);
                        dialog.dismiss();
                    }
                }).setNegativeButton("取消", null).show();
    }

    //修改性别
    private void updateInfo(int which) {
        if (which == 0) {
            user.setSex(true);
        } else {
            user.setSex(false);
        }
        user.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "MyinfoActivity→→→done:性别更新成功");
                } else {
                    Toast.makeText(MyInfoActivity.this, "性别更新失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void birClick(View view) {
        final TextView text = (TextView) findViewById(R.id.textView_bir);
        final java.util.Calendar c = java.util.Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(MyInfoActivity.this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                java.util.Calendar c = java.util.Calendar.getInstance();
                c.set(year, monthOfYear, dayOfMonth);
                CharSequence format = DateFormat.format("yyyy-MM-dd", c);
                text.setText(format);
                Log.e("FreeStar", "MyinfoActivity→→→onDateSet:" + format + user);

                if (user.getBirthday() == null || !user.getBirthday().equals(format)) {
                    user.setBirthday(format.toString());
                    user.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Log.e("FreeStar", "MyinfoActivity→→→done:出生日期修改成功");
                            } else {
                                Toast.makeText(MyInfoActivity.this, "出生日期修改失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        }, c.get(java.util.Calendar.YEAR), c.get(java.util.Calendar.MONTH), c.get(java.util.Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    public void nameClick(View view) {
        Intent intent = new Intent(this, NinameActivity.class);
        startActivityForResult(intent, NAME);
//        finish();
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RESC && resultCode == RESULT_OK) {
//            String back = data.getStringExtra("name");
//            if (back.equals("OK")) {
//                Intent intent1 = new Intent();
//                intent1.putExtra("name", "OK");
//                setResult(RESULT_OK, intent1);
//            }
//
//        }
//    }


    //个性签名
    public void gexClick(View view) {
        Intent intent = new Intent(this, PersSignActivity.class);
        startActivityForResult(intent, RESC);
//        finish();
    }

    //空间隐私
    public void kongClick(View view) {
        Intent intent = new Intent(this, KingActivity.class);
        startActivity(intent);
    }

    //安全设置
    public void anqClick(View view) {
        Intent intent = new Intent(this, SafeActivity.class);
        startActivity(intent);
    }

    //退出登入
    public void leaveClick(View view) {
        SharedPreferences preferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        preferences.edit().clear().commit();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RESC && resultCode == RESULT_OK) {
            //个性签名的修改
            String back = data.getStringExtra("OK");
            if (back != null) {
                textView_gex.setText(back);
            }
        } else if (requestCode == NAME && resultCode == RESULT_OK) {
            String name = data.getStringExtra("name");
            if (name != null) {
                Log.e("FreeStar", "MyinfoActivity→→→onActivityResult:" + name);
                textView_niname.setText(name);
                Intent intent1 = new Intent();
                intent1.putExtra("name", name);
                setResult(RESULT_OK, intent1);
            }
        }
    }


    //返回
    public void btnvbackclick(View view) {
        finish();
    }

    //获得用户信息
    private void getAndset() {
        BmobQuery<User> bmobQuery = new BmobQuery<>();
        bmobQuery.getObject(App.userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
//                    MyinfoActivity.user = user;
                    //设置性别
                    if (user.getSex()) {
                        textView_sex.setText("男");
                    } else {
                        textView_sex.setText("女");
                    }
                    //设置昵称

                    App.niname = user.getNiname();
                    textView_niname.setText(user.getNiname());
                    //设置出生日期
                    textView_br.setText(user.getBirthday());
                    //设置个性签名
                    textView_gex.setText(user.getPersSign());
                    if (user.getHeadUrl() == null) {
                        Log.i("touxiang ", "done: kong" + user.getHeadUrl());
                        set_avator.setImageResource(R.mipmap.ic_launcher);
                    } else {
                        Log.i("touxiang ", "done: you" + user.getHeadUrl());
                        App.headUrl = user.getHeadUrl();
                        set_avator.setImageURI(user.getHeadUrl());
//                        Picasso.with(getBaseContext()).load(user.getHeadUrl()).placeholder(R.mipmap.ic_launcher).into(set_avator);
//                        x.image().bind(set_avator, user.getHeadUrl(), App.imageOptions);
                    }
                } else {
                    Toast.makeText(MyInfoActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.e("FreeStar", "MyinfoActivity→→→done:失败");
                }
            }
        });
    }

    //获得生日
//    private void getbrithday() {
//        BmobQuery<User> bmobQuery = new BmobQuery<>();
//        bmobQuery.getObject(App.userId, new QueryListener<User>() {
//            @Override
//            public void done(User user, BmobException e) {
//                if (e == null) {
//                    textView_br.setText(user.getBrithday());
//                } else {
//
//                }
//            }
//        });
//    }

    //获得个性签名
    public void perssign() {
        BmobQuery<User> bmobQuery = new BmobQuery<User>();
        bmobQuery.setMaxCacheAge(TimeUnit.DAYS.toMillis(15));
        bmobQuery.getObject(App.userId, new QueryListener<User>() {
            @Override
            public void done(User user, BmobException e) {
                if (e == null) {
                    textView_gex.setText(user.getPersSign());
                } else {

                }
            }
        });
    }

    private void startTake() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            //申请WRITE_EXTERNAL_STORAGE权限
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    1);
        }
        File file = new File(Environment.getExternalStorageDirectory(), "/temp/" + System.currentTimeMillis() + ".jpg");
        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
        Uri imageUri = Uri.fromFile(file);
        CompressConfig compressConfig = new CompressConfig.Builder().setMaxSize(50 * 1024).setMaxPixel(800).create();
        CropOptions cropOptions = new CropOptions.Builder().setAspectX(1).setAspectY(1).setWithOwnCrop(false).create();
        switch (kind) {
            case "相册":
                getTakePhoto().onEnableCompress(compressConfig, true).onPickFromGalleryWithCrop(imageUri, cropOptions);
                break;
            case "文件":
                getTakePhoto().onEnableCompress(compressConfig, true).onPickFromDocumentsWithCrop(imageUri, cropOptions);
                break;
            case "相机":
                getTakePhoto().onEnableCompress(compressConfig, true).onPickFromCaptureWithCrop(imageUri, cropOptions);
                break;
        }
    }

    private void showDL() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setTitle("请选择路径");
        final String[] path = {"相册", "文件", "相机"};
        builder.setSingleChoiceItems(path, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //这的which表示数组的下标
                kind = path[which];
                Toast.makeText(MyInfoActivity.this, "类别为：" + path[which], Toast.LENGTH_SHORT).show();
            }
        });

        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (kind == null) {
                    kind = "相册";
                }
                startTake();
            }
        });

        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(String msg) {
        super.takeFail(msg);
    }

    @Override
    public void takeSuccess(String imagePath) {
        super.takeSuccess(imagePath);
        Log.e("FreeStar", "MyinfoActivity→→→takeSuccess:" + imagePath);
//        showImg(imagePath);
        final BmobFile bmobFile = new BmobFile(new File(imagePath));
        bmobFile.uploadblock(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    //bmobFile.getFileUrl()--返回的上传文件的完整地址
//                    toast("上传文件成功:" + bmobFile.getFileUrl());
                    Log.e("FreeStar", "MyinfoActivity→→→done:成功");
                    Log.e("FreeStar", "MyinfoActivity→→→done:" + bmobFile.getFileUrl());
                    set_avator.setImageURI(bmobFile.getFileUrl());
                    updateHead(bmobFile.getFileUrl());

                    Intent intent = new Intent();
                    intent.putExtra("url", bmobFile.getFileUrl());
                    setResult(RESULT_OK, intent);
                } else {
//                    toast("上传文件失败：" + e.getMessage());
                    Log.e("FreeStar", "MyinfoActivity→→→done:失败" + e.getMessage());
                }

            }

            @Override
            public void onProgress(Integer value) {
                // 返回的上传进度（百分比）
            }
        });
    }

    private void updateHead(String fileUrl) {
        final User user = new User();
        user.setHeadUrl(fileUrl);
        user.update(App.userId, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.e("FreeStar", "MyinfoActivity→→→done:ok");
                } else {
                    Toast.makeText(MyInfoActivity.this, "修改失败" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void showImg(String imagePath) {
        BitmapFactory.Options option = new BitmapFactory.Options();
        option.inSampleSize = 2;
        Bitmap bitmap = BitmapFactory.decodeFile(imagePath, option);
        set_avator.setImageBitmap(bitmap);
    }

    public void iconClick(View view) {
        showDL();
    }
}
