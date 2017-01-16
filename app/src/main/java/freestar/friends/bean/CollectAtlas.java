package freestar.friends.bean;

import java.io.Serializable;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/8/24 0024.
 */
public class CollectAtlas extends BmobObject implements Serializable {

    //收藏的用户
    private User user;
    private Atlas sources;
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setSources(Atlas sources) {
        this.sources = sources;
    }

    public CollectAtlas() {
    }

    public Atlas getSources() {
        return sources;

    }

    public CollectAtlas(User user, Atlas sources) {
        this.user = user;
        this.sources = sources;
    }

}
