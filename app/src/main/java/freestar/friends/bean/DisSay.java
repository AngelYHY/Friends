package freestar.friends.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * 评论列表类。。。。。一级评论 多级评论
 */
public class DisSay extends BmobObject implements Serializable, MultiItemEntity {
    //当前评论的用户
    private User cuser;
    //当前评论表中的评论
    private DisSay comment_father_user;
    //评论内容
    private String comment;
    //评论的源头  当前指的是 图集 Atlas
    private Message message;
    private User father_user;
    //说说作者
    private User author;

    public DisSay(User cuser, DisSay comment_father_user, String comment, Message message, User father_user) {
        this.father_user = father_user;
        this.cuser = cuser;
        this.comment_father_user = comment_father_user;
        this.comment = comment;
        this.message = message;
    }

    public User getCuser() {
        return cuser;
    }

    public void setCuser(User cuser) {
        this.cuser = cuser;
    }

    public DisSay getComment_father_user() {
        return comment_father_user;
    }

    public void setComment_father_user(DisSay comment_father_user) {
        this.comment_father_user = comment_father_user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
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

    public DisSay(User cuser, DisSay comment_father_user, String comment, Message message, User father_user, User author) {

        this.cuser = cuser;
        this.comment_father_user = comment_father_user;
        this.comment = comment;
        this.message = message;
        this.father_user = father_user;
        this.author = author;
    }

    public DisSay(String comment, User cuser, Message message, User author) {
        this.author = author;
        this.comment = comment;
        this.cuser = cuser;
        this.message = message;
    }

    @Override
    public int getItemType() {
        return getComment_father_user() == null ? 0 : 1;
    }
}
