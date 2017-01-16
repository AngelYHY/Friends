package freestar.friends.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/8/27 0027.
 */
public class LikeAtlas extends BmobObject {
    private User user;
    private Atlas source;

    public LikeAtlas() {
    }

    public LikeAtlas(User user, Atlas source) {
        this.user = user;
        this.source = source;

    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Atlas getSource() {
        return source;
    }

    public void setSource(Atlas source) {
        this.source = source;
    }
}
