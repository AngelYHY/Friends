package freestar.friends.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by freestar on 2017/1/18 0018.
 */

public class Dis extends BmobObject{
    //当前评论的用户
    private User cuser;

    //评论内容
    private String comment;

    private User father_user;

    private User author;

    public Dis(User cuser, String comment, User father_user, User author) {
        this.cuser = cuser;
        this.comment = comment;
        this.father_user = father_user;
        this.author = author;
    }
    

    public User getCuser() {
        return cuser;
    }

    public void setCuser(User cuser) {
        this.cuser = cuser;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
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
