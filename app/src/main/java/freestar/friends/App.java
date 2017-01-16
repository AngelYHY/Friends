package freestar.friends;

import android.app.Application;
import android.content.Context;
import android.util.Log;
import android.widget.ImageView;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.xutils.common.util.DensityUtil;
import org.xutils.image.ImageOptions;
import org.xutils.x;

import cn.bmob.v3.Bmob;
import cn.smssdk.SMSSDK;
import freestar.friends.bean.User;
import io.rong.imkit.RongIM;

/**
 * Created by Administrator on 2016/8/10 0010.
 */
public class App extends Application {
//    private static String url = "http://10.50.7.43:8080/RongYun/";
        private static String url = "http://10.50.7.42:8080/FreeStar/Login_user";

    public static User user;
    public static String userId = "";
    public static String token = "";
    public static String  headUrl="";
    public static String  phoneNum="";
    public static String  niname="";
    public static ImageOptions imageOptions;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        App.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        App.userId = userId;
    }

    public static String getUrl() {
        return url;
    }

    public User getUser() {
        return user;
    }

    public static  void setUser(User user) {
        App.user = user;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        // 启动短信验证sdk
        SMSSDK.initSDK(this, "159c78ded2234", "ba38b324303f8e16c164731f131d9780");

//        LeakCanary.install(this);
        Bmob.initialize(this, "e229bd613a9202af72e4b5263d92736a");
        x.Ext.init(this);//初始化xUtils
        RongIM.init(this);
        Fresco.initialize(this);
        Log.d("FreeStar", "App→→→onCreate");
        imageOptions = new ImageOptions.Builder()
                .setSize(DensityUtil.dip2px(120), DensityUtil.dip2px(120))//图片大小
                .setRadius(DensityUtil.dip2px(5))//ImageView圆角半径
                .setCrop(true)// 如果ImageView的大小不是定义为wrap_content, 不要crop.
                .setImageScaleType(ImageView.ScaleType.CENTER_CROP)
                .setLoadingDrawableId(R.mipmap.ic_launcher)//加载中默认显示图片
                .setFailureDrawableId(R.mipmap.ic_launcher)//加载失败后默认显示图片
                .build();
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
// MultiDex.install(this);
    }

}
