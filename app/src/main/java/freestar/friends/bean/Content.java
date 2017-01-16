package freestar.friends.bean;

import java.io.Serializable;

import cn.bmob.v3.Bmob;

/**
 * 评论列表类。。。。。一级评论 多级评论
 */
public class Content extends Bmob implements Serializable {
    private User mainUser;     //发动态的某某
    private Message message;//动态消息
    private User pldeuser;//评论的某某
    private String conment;//评论内容--------集合 一条说说有多条
    public Content(){

    }
    //message mainUser pldeuser conment
    public Content(Message message, User mainUser, User pldeuser, String conment) {
        this.message = message;
        this.mainUser = mainUser;
        this.pldeuser = pldeuser;
        this.conment = conment;
    }public User getMainUser() {
        return mainUser;
    }
    //發說說的人
    //为什么没有   public User getMainUser(){ return mainUser
    public void setMainUser(User mainUser) {
        this.mainUser = mainUser;
    }
    //获取动态消息
    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
    //获取评论的某某
    public User getPldeuser() {
        return pldeuser;
    }

    public void setPldeuser(User pldeuser) {
        this.pldeuser = pldeuser;
    }
    //获取评论内容
    public String getConment() {
        return conment;
    }

    public void setConment(String conment) {
        this.conment = conment;
    }

    @Override
    public String toString() {
        return "Content{" +
                "mainUser=" + mainUser +
                ", message=" + message +
                ", pldeuser=" + pldeuser +
                ", conment='" + conment + '\'' +
                '}';
    }
}
