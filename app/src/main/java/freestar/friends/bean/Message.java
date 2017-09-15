package freestar.friends.bean;


import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class Message extends BmobObject implements MultiItemEntity {
    private String m_message;
    private User user;
    List<String> urls;
    private Integer likeNum;
    private Integer comNum;
    private boolean iszan;
    private BmobRelation likeUser;

    public BmobRelation getLikeUser() {
        return likeUser;
    }

    public void setLikeUser(BmobRelation likeUser) {
        this.likeUser = likeUser;
    }

    public Message() {
    }

    public Message(Integer likeNum, Integer comNum, User user) {
        this.likeNum = likeNum;
        this.comNum = comNum;
        this.user = user;
    }

    public Message(Integer likeNum, Integer comNum, User user, String m_message) {
        this.likeNum = likeNum;
        this.comNum = comNum;
        this.user = user;
        this.m_message = m_message;
    }

    public Message(Integer likeNum, Integer comNum, List<String> urls, User user) {
        this.likeNum = likeNum;
        this.comNum = comNum;
        this.urls = urls;
        this.user = user;
    }

    public Message(Integer likeNum, Integer comNum, List<String> urls, User user, String m_message) {
        this.likeNum = likeNum;
        this.comNum = comNum;
        this.urls = urls;
        this.user = user;
        this.m_message = m_message;
    }

    //
    public boolean iszan() {
        return iszan;
    }

    public void setIszan(boolean iszan) {
        this.iszan = iszan;
    }

    //
    public Integer getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(Integer likeNum) {
        this.likeNum = likeNum;
    }

    //
    public Integer getComNum() {
        return comNum;
    }

    public void setComNum(Integer comNum) {
        this.comNum = comNum;
    }

    //
    public List<String> getUrls() {
        return urls;
    }

    public String getM_message() {
        return m_message;
    }

    public void setM_message(String m_message) {
        this.m_message = m_message;
    }

    //
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Message{" +
                "m_message='" + m_message + '\'' +
                ", user=" + user +
                ", urls=" + urls +
                ", likeNum=" + likeNum +
                ", comNum=" + comNum +
                ", iszan=" + iszan +
                '}';
    }

    @Override
    public int getItemType() {
        return getUrls() == null ? 0 : 1;
    }
}
