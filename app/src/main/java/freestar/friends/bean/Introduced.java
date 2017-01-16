package freestar.friends.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by 28655 on 2016/8/19.
 */
public class Introduced  extends BmobObject{
    private String title;//标题
    private String describe;//描述
    private String type;
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getDescribe() {
        return describe;
    }
    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
