package freestar.friends.bean;

import java.util.Date;
import java.util.Set;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
//说说
public class Mood {
    private int id;
    private User author;
    private Date date;
    private String content;
    private int likeNum;
    private Set<Object> discuss;

}
