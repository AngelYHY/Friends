package freestar.friends.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2016/8/21 0021.
 */
public class Praisebean implements Serializable {
    private static final long serialVersionUID = 2L;
    private int p_id;//主键 点赞的主键
    private int f_t_id;//外键：帖子主键   说说主键
    private int f_u_id;//外键：用户主键   用户主键

    public Praisebean(int p_id, int f_t_id, int f_u_id) {
        this.p_id = p_id;
        this.f_t_id = f_t_id;
        this.f_u_id = f_u_id;
    }

    public int getP_id() {
        return p_id;
    }

    public void setP_id(int p_id) {
        this.p_id = p_id;
    }

    public int getF_t_id() {
        return f_t_id;
    }

    public void setF_t_id(int f_t_id) {
        this.f_t_id = f_t_id;
    }

    public int getF_u_id() {
        return f_u_id;
    }

    public void setF_u_id(int f_u_id) {
        this.f_u_id = f_u_id;
    }

    @Override
    public String toString() {
        return "Praisebean{" +
                "p_id=" + p_id +
                ", f_t_id=" + f_t_id +
                ", f_u_id=" + f_u_id +
                '}';
    }
}
