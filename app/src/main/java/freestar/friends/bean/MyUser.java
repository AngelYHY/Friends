package freestar.friends.bean;

import java.util.Set;

import cn.bmob.v3.BmobUser;

/**
 * Created by 28655 on 2016/8/19.
 */
public class MyUser extends BmobUser {
    private String token;
    //支付密码
    private String payPassword;
    //头像地址
    private String urlHeadPhoto;




    //出生日期
    private String dayOfDate;
    //邮箱
    //个性签名
    private String persSign;
    //我的好友
    private Set<User> friends;
    //我的收藏
    private String brithday;

    public String getBrithday() {
        return brithday;
    }

    public void setBrithday(String brithday) {
        this.brithday = brithday;
    }

    public String getPersSign() {
        return persSign;
    }

    public void setPersSign(String persSign) {
        this.persSign = persSign;
    }

    //private Set<Collect> myCollect;
    //我的关注
    private Set<User> myFollow;
    //我的发布
    private Set<Object> myPublish;



    public MyUser( String token) {

        this.token = token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }


    public String getUrlHeadPhoto() {
        return urlHeadPhoto;
    }

    public void setUrlHeadPhoto(String urlHeadPhoto) {
        this.urlHeadPhoto = urlHeadPhoto;
    }

}
