package freestar.friends.util;

import java.util.Comparator;

import freestar.friends.bean.ArticleDis;
import freestar.friends.bean.AtlasDis;

/**
 * Created by Administrator on 2016/8/29 0029.
 */
public class TimeComparator implements Comparator<Object> {

    @Override
    public int compare(Object o, Object t1) {

        String start = null;
        String end = null;
        if (o instanceof AtlasDis && t1 instanceof AtlasDis) {
            start = ((AtlasDis) o).getCreatedAt();
            end = ((AtlasDis) t1).getCreatedAt();
        } else if (o instanceof ArticleDis && t1 instanceof ArticleDis) {
            start = ((ArticleDis) o).getCreatedAt();
            end = ((ArticleDis) t1).getCreatedAt();
        } else if (o instanceof AtlasDis && t1 instanceof ArticleDis) {
            start = ((AtlasDis) o).getCreatedAt();
            end = ((ArticleDis) t1).getCreatedAt();
        } else if (o instanceof ArticleDis && t1 instanceof AtlasDis) {
            start = ((ArticleDis) o).getCreatedAt();
            end = ((AtlasDis) t1).getCreatedAt();
        }
        return end.compareTo(start);
    }
}
