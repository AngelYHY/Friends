package freestar.friends.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/8/23 0023.
 */
public class AtlasDis extends BmobObject {
    //当前评论的用户
    private User cuser;
    //当前评论表中的评论
    private AtlasDis comment_father_user;
    //评论内容
    private String comment;
    //评论的源头  当前指的是 图集 Atlas
    private Atlas atlas;
    private User father_user;
    private User author;

    public User getAuthor() {
        return author;
    }

    public void setAuthor(User author) {
        this.author = author;
    }

    public User getFather_user() {
        return father_user;
    }

    public void setFather_user(User father_user) {
        this.father_user = father_user;
    }

    public AtlasDis(User cuser, AtlasDis comment_father_user, String comment, Atlas atlas, User father_user) {
        this.father_user = father_user;
        this.cuser = cuser;
        this.comment_father_user = comment_father_user;
        this.comment = comment;
        this.atlas = atlas;
    }

    public AtlasDis(User cuser, AtlasDis comment_father_user, String comment, Atlas atlas, User father_user, User author) {
        this.father_user = father_user;
        this.cuser = cuser;
        this.comment_father_user = comment_father_user;
        this.comment = comment;
        this.atlas = atlas;
        this.author = author;
    }


//    public AtlasDis(User cuser, String comment, Atlas atlas) {
//        this.cuser = cuser;
//        this.comment = comment;
//        this.atlas = atlas;
//    }

//    public AtlasDis(User cuser, AtlasDis comment_father_user, Atlas atlas, User author) {
//        this.cuser = cuser;
//        this.comment_father_user = comment_father_user;
//        this.atlas = atlas;
//        this.author = author;
//    }

    //
    public AtlasDis(User cuser, String comment, Atlas atlas, User author) {
        this.cuser = cuser;
        this.comment = comment;
        this.atlas = atlas;
        this.author = author;
    }

    public AtlasDis(Atlas atlas, User author) {

        this.atlas = atlas;
        this.author = author;
    }

    //    public AtlasDis(User cuser, String comment, AtlasDis atlas, User father_user) {
//        this.father_user = father_user;
//        this.cuser = cuser;
//        this.comment = comment;
//        this.comment_father_user = atlas;
//    }
    public User getCuser() {
        return cuser;
    }

    public void setCuser(User cuser) {
        this.cuser = cuser;
    }

    public AtlasDis getComment_father_user() {
        return comment_father_user;
    }

    public void setComment_father_user(AtlasDis comment_father_user) {
        this.comment_father_user = comment_father_user;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Atlas getAtlas() {
        return atlas;
    }

    public void setAtlas(Atlas atlas) {
        this.atlas = atlas;
    }

    public AtlasDis() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AtlasDis atlasDis = (AtlasDis) o;

        if (!atlas.equals(atlasDis.atlas)) return false;
        return author.equals(atlasDis.author);

    }

    @Override
    public int hashCode() {
        int result = atlas.hashCode();
        result = 31 * result + author.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "AtlasDis{" +
                "cuser=" + cuser +
                ", comment_father_user=" + comment_father_user +
                ", comment='" + comment + '\'' +
                ", atlas=" + atlas +
                ", father_user=" + father_user +
                '}';
    }
}
