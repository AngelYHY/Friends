package freestar.friends.bean;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
//钱包操作
public class PurseOpera {
    private int id;
    private Date date;
    //充值:true或者提现:false
    private boolean opera;
    private User user;
    private int money;
}
