package freestar.friends.bean;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;


public class Ninepic extends BmobObject {
    /*
    图片表中有图片的id和关联表Message
     */
    String url;
    List<String> pics;
   Message message;

    @Override
    public String toString() {
        return "Ninepic{" +
                "url='" + url + '\'' +
                ", pics=" + pics +
                ", message=" + message +
                '}';
    }

    public Ninepic() {
    }

    public Ninepic(String url) {
        this.url = url;
    }

    public Ninepic(String url, Message message) {
        this.url = url;
        this.message = message;
    }

    public Ninepic(Message message, String url, List<String> pics) {
        this.message = message;
        this.url = url;
        this.pics = pics;
    }

    public List<String> getPics() {
        return pics;
    }

    public void setPics(List<String> pics) {
        this.pics = pics;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }
}
