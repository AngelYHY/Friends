package freestar.friends.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/8/27 0027.
 */
public class MessageLike extends BmobObject {
    private User user;
    private Message source;

    public MessageLike(User user, Message source) {
        this.user = user;
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Message getSource() {
        return source;
    }

    public void setSource(Message source) {
        this.source = source;
    }

    public MessageLike() {
    }
}
