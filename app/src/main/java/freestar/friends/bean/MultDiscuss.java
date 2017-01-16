package freestar.friends.bean;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
//多级评论
public class MultDiscuss {
    private int id;
    private Date date;
    private String source;
    private String content;
    private User user;
    //父级评论
    private String superDiscuss;
    //是否有下级评论
    private boolean isNextDis;

}
