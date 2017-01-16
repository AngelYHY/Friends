package freestar.friends.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/8/27 0027.
 */
public class LikeArticle extends BmobObject {
    private User user;
    private Article source;

    public LikeArticle(User user, Article source) {
        this.user = user;
        this.source = source;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Article getSource() {
        return source;
    }

    public void setSource(Article source) {
        this.source = source;
    }

    public LikeArticle() {
    }
}
