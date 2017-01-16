package freestar.friends.bean;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
//聊天
public class Chat {
    private long id;
    private Date date;
    private String content;
    private User from;
    private User to;
    private String picture;
    //是否阅读过
    private boolean isRead;
}
