package freestar.friends.bean;

import java.util.Date;

/**
 * Created by Administrator on 2016/7/24 0024.
 */
//赞赏信息
public class Admire {
    private int id;
    private Date date;
    //给赞赏的人
    private User from;
    //收赞赏的人
    private User to;
    //对什么赞赏
    private String source;
    private int money;

    public Admire(int id, Date date, User from, User to, String source, int money) {
        this.id = id;
        this.date = date;
        this.from = from;
        this.to = to;
        this.source = source;
        this.money = money;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }
}
