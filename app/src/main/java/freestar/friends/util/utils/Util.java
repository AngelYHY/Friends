package freestar.friends.util.utils;

/**
 * Created by Administrator on 2016/8/19 0019.
 */
public class Util {

    public static String getPicName(String s) {
        int i = s.lastIndexOf("/");
        return s.substring(i + 1, s.length());
    }

}
