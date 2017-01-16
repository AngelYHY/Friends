package freestar.friends.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
//图集信息
public class Article extends BmobObject{
    private String kind;

    public User getAuthor() {
        return author;
    }

    private User author;
    //点赞数
    private Integer likeNum;
    //评论数
    private Integer disNum;
    //标题
    private String title;

    private String mainPic;

    public String getMainPic() {
        return mainPic;
    }

    public List<String> getUrlList() {
        return urlList;
    }


    public void setUrlList(List<String> urlList) {
        this.urlList = urlList;
    }

    private List<String> urlList;

    @Override
    public String toString() {
        return "Article{" +
                "kind='" + kind + '\'' +
                ", author=" + author +
                ", likeNum=" + likeNum +
                ", disNum=" + disNum +
                ", title='" + title + '\'' +
                ", mainPic='" + mainPic + '\'' +
                ", urlList=" + urlList +
                '}';
    }

    public Article(String kind, User author, Integer likeNum, Integer disNum, String title, String mainPic, List<String> urlList) {
        this.kind = kind;
        this.author = author;
        this.likeNum = likeNum;
        this.disNum = disNum;
        this.title = title;
        this.mainPic = mainPic;
        this.urlList = urlList;
    }

    public void setMainPic(String mainPic) {
        this.mainPic = mainPic;
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

    public Article() {
    }

}
