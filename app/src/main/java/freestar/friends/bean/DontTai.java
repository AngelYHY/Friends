package freestar.friends.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/8/8 0008.
 */
public class DontTai extends BmobObject implements Serializable {

    String touxiang;//头像
    String name;//姓名
    String time;//时间
    String content;//内容
    String countzan;//点赞的个数
    String countpinglun;//评论的个数
    boolean iszan;//是否赞
    ArrayList<String> zanname;//点赞人数
    ArrayList<DisSay> commentList;//当前帖子下的所有评
//    String pic;

    public DontTai(){

    }
    public DontTai(String touxiang, String name, String time, String content, String countzan, String countpinglun, boolean iszan, ArrayList<String> zanname, ArrayList<DisSay> commentList) {
        this.touxiang = touxiang;
        this.name = name;
        this.time = time;
        this.content = content;
        this.countzan = countzan;
        this.countpinglun = countpinglun;
        this.iszan = iszan;
        this.zanname = zanname;
        this.commentList = commentList;
    }
    public DontTai(String touxiang, String name, String time, String content, String countzan, String countpinglun, boolean iszan){
        this.touxiang = touxiang;
        this.name = name;
        this.time = time;
        this.content = content;
        this.countzan = countzan;
        this.countpinglun = countpinglun;
        this.iszan = iszan;
//        this.pic=pic;

    }
    public ArrayList<String> getZanname() {
        return zanname;
    }

//    public String getPic() {
//        return pic;
//    }
//
//    public void setPic(String pic) {
//        this.pic = pic;
//    }

    public void setZanname(ArrayList<String> zanname) {
        this.zanname = zanname;
    }

    public boolean iszan() {
        return iszan;
    }

    public void setIszan(boolean iszan) {
        this.iszan = iszan;
    }

    public String getTouxiang() {
        return touxiang;
    }

    public void setTouxiang(String touxiang) {
        this.touxiang = touxiang;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCountpinglun() {
        return countpinglun;
    }

    public void setCountpinglun(String countpinglun) {
        this.countpinglun = countpinglun;
    }

    public String getCountzan() {
        return countzan;
    }

    public void setCountzan(String countzan) {
        this.countzan = countzan;
    }

    public List<DisSay> getCommentList() {
        return commentList;
    }

    public void setCommentList(ArrayList<DisSay> commentList) {
        this.commentList = commentList;
    }

    @Override
    public String toString() {
        return "DontTai{" +
                "touxiang='" + touxiang + '\'' +
                ", name='" + name + '\'' +
                ", time='" + time + '\'' +
                ", content='" + content + '\'' +
                ", countzan='" + countzan + '\'' +
                ", countpinglun='" + countpinglun + '\'' +
                ", iszan=" + iszan +
                ", zanname=" + zanname +
                ", commentList=" + commentList +
                '}';
    }
}
