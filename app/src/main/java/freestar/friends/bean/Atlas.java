package freestar.friends.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
//图集信息
public class Atlas extends BmobObject{
    private String kind;
    private User author;
    //点赞数
    private Integer likeNum;
    //评论数
    private Integer disNum;
    //标题
    private String title;

    private String mainPic;

    private String picName;//--------------------------->7
    private BmobRelation collect;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public BmobRelation getCollect() {
        return collect;
    }

    public void setCollect(BmobRelation collect) {
        this.collect = collect;
    }

    public String getMainPic() {
        return mainPic;
    }

    public Atlas(String kind, String picName, User author, Integer likeNum, Integer disNum, String title, String mainPic, String num) {
        this.kind = kind;
        this.author = author;
        this.likeNum = likeNum;
        this.disNum = disNum;
        this.title = title;
        this.mainPic = mainPic;
        this.num = num;
        this.picName = picName;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
    }

    //图集中的图片数量
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

    public Atlas() {
    }

    @Override
    public String toString() {
        return "Atlas{" +
                "kind='" + kind + '\'' +
                ", author=" + author +
                ", likeNum='" + likeNum + '\'' +
                ", disNum='" + disNum + '\'' +
                ", title='" + title + '\'' +
                ", mainPic='" + mainPic + '\'' +
                ", picName='" + picName + '\'' +
                ", num='" + num + '\'' +
                '}';
    }
}
