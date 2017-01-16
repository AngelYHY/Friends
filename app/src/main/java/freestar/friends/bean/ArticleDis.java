package freestar.friends.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class ArticleDis extends BmobObject {
    //当前评论的用户
    private User cuser;
    //当前评论表中的评论
    private ArticleDis comment_father_user;
    //评论内容
    private String comment;
    //评论的源头  当前指的是 图集 Atlas
    private Article article;
    private User father_user;

    public ArticleDis(User cuser, ArticleDis comment_father_user, String comment, Article article, User father_user) {
        this.father_user = father_user;
        this.cuser = cuser;
        this.comment_father_user = comment_father_user;
        this.comment = comment;
        this.article = article;
    }

    public ArticleDis(User cuser, ArticleDis comment_father_user, String comment, Article article, User father_user, User author) {
        this.father_user = father_user;
        this.cuser = cuser;
        this.comment_father_user = comment_father_user;
        this.comment = comment;
        this.article = article;
        this.author = author;
    }

    public ArticleDis(User cuser, String comment, Article article, User author) {
        this.cuser = cuser;
        this.comment = comment;
        this.article = article;
        this.author = author;
    }

    private User author;

    @Override
    public String toString() {
        return "ArticleDis{" +
                "cuser=" + cuser +
                ", comment_father_user=" + comment_father_user +
                ", comment='" + comment + '\'' +
                ", article=" + article +
                ", father_user=" + father_user +
                ", author=" + author +
                '}';
    }

    public User getCuser() {
        return cuser;
    }

    public void setCuser(User cuser) {
        this.cuser = cuser;
    }

    public ArticleDis getComment_father_user() {
        return comment_father_user;
    }

    public void setComment_father_user(ArticleDis comment_father_user) {
        this.comment_father_user = comment_father_user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public User getFather_user() {
        return father_user;
    }

    public void setFather_user(User father_user) {
        this.father_user = father_user;
    }

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }
}
