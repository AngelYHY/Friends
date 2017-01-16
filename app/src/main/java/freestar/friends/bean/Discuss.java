package freestar.friends.bean;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
//一级评论
public class Discuss {
    private int id;
    private Date date;
    //对什么评论
    private String source;
    private String content;
    private User user;
}
