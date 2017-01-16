package freestar.friends.bean;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/8/18 0018.
 */
public class Photoes extends BmobObject {
    private String url;
    private Atlas atlas;

    private String picName;

    private String context;


    public String getUrl() {
        return url;
    }

    public Atlas getAtlas() {
        return atlas;
    }

    public String getPicName() {
        return picName;
    }

    public String getContext() {
        return context;
    }

    public void setUrl(String url) {
        this.url = url;

    }

    public Photoes(Atlas atlas, String url, String context, String picName) {
        this.atlas = atlas;
        this.picName = picName;
        this.url = url;
        this.context = context;
    }

    public Photoes() {
    }
}
