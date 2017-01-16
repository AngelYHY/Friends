package freestar.friends.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
//用户
public class User extends BmobObject implements Serializable {
    private String phoneNum;
    private String password;
    private String token;
    private String niname;
    BmobRelation user;
    private BmobRelation collect_atlas;
    private Boolean typeswitch;
    private Boolean collectswitch;
    private Boolean concernswitch;

    @Override
    public String toString() {
        return "User{" +
                "phoneNum='" + phoneNum + '\'' +
                ", password='" + password + '\'' +
                ", token='" + token + '\'' +
                ", niname='" + niname + '\'' +
                ", user=" + user +
                ", collect_atlas=" + collect_atlas +
                ", typeswitch=" + typeswitch +
                ", collectswitch=" + collectswitch +
                ", concernswitch=" + concernswitch +
                ", payPassword='" + payPassword + '\'' +
                ", headUrl='" + headUrl + '\'' +
                ", sex=" + sex +
                ", birthday='" + birthday + '\'' +
                ", email='" + email + '\'' +
                ", persSign='" + persSign + '\'' +
                ", follow=" + follow +
                ", collectAtlas=" + collectAtlas +
                ", collectArticle=" + collectArticle +
                '}';
    }

    public boolean isConcernswitch() {
        return concernswitch;
    }

    public void setConcernswitch(boolean concernswitch) {
        this.concernswitch = concernswitch;
    }

    public boolean isCollectswitch() {
        return collectswitch;
    }

    public void setCollectswitch(boolean collectswitch) {
        this.collectswitch = collectswitch;
    }

    public boolean isTypeswitch() {
        return typeswitch;
    }

    public void setTypeswitch(boolean typeswitch) {
        this.typeswitch = typeswitch;
    }

    public BmobRelation getCollect_atlas() {
        return collect_atlas;
    }

    public BmobRelation getUser() {
        return user;
    }

    public void setUser(BmobRelation user) {
        this.user = user;
    }

    public void setCollect_atlas(BmobRelation collect_atlas) {
        this.collect_atlas = collect_atlas;
    }

    //支付密码
    private String payPassword;
    //头像地址
    private String headUrl;
    private boolean sex;

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    //出生日期
    private String birthday;

    //邮箱
    private String email;
    //个性签名

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    private String persSign;


    public void setFollow(BmobRelation follow) {
        this.follow = follow;
    }

    private BmobRelation follow;
    private BmobRelation collectAtlas;
    private BmobRelation collectArticle;

    public BmobRelation getCollectArticle() {
        return collectArticle;
    }

    public void setCollectArticle(BmobRelation collectArticle) {
        this.collectArticle = collectArticle;
    }

    public BmobRelation getCollectAtlas() {
        return collectAtlas;
    }

    public void setCollectAtlas(BmobRelation collectAtlas) {
        this.collectAtlas = collectAtlas;
    }


    public boolean getSex() {
        return sex;
    }

    public void setSex(boolean sex) {

        this.sex = sex;
    }

    public String getPersSign() {
        return persSign;
    }

    public void setPersSign(String persSign) {
        this.persSign = persSign;
    }


    public User(String tableName, String phoneNum, String password, String token) {
        super(tableName);
        this.phoneNum = phoneNum;
        this.password = password;
        this.token = token;
    }


    public String getHeadUrl() {
        return headUrl;
    }

    public void setHeadUrl(String headUrl) {
        this.headUrl = headUrl;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getNiname() {
        return niname;
    }

    public void setNiname(String niname) {
        this.niname = niname;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public User(String niname, String headUrl) {

        this.niname = niname;
        this.headUrl = headUrl;
    }


    public User() {
    }


}

