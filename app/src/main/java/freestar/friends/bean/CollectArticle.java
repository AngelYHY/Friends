package freestar.friends.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class CollectArticle extends BmobObject {
    private User user;
    private Article sources;

    private String kind;

    public CollectArticle(User user, Article sources) {
        this.user = user;
        this.sources = sources;
    }

    public Article getSources() {
        return sources;
    }

    public void setSources(Article sources) {
        this.sources = sources;
    }

    private User author;

    private Integer likeNum;

    private Integer disNum;

    private String title;

    private String mainPic;

    private String picName;
    public User getAuthor() {
        return author;
    }

    public CollectArticle(String kind, String picName, User author, Integer likeNum, Integer disNum, String title, String mainPic, String num) {
        this.kind = kind;
        this.author = author;
        this.likeNum = likeNum;
        this.disNum = disNum;
        this.title = title;
        this.mainPic = mainPic;
        this.num = num;
        this.picName = picName;
    }

    public String getMainPic() {
        return mainPic;
    }
    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    private String num;

    public String getPicName() {
        return picName;
    }

    public void setPicName(String picName) {
        this.picName = picName;
    }

    public String getNum() {
        return num;
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    public Integer getDisNum() {
        return disNum;
    }

    public void setDisNum(Integer disNum) {
        this.disNum = disNum;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CollectArticle() {
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }


}
