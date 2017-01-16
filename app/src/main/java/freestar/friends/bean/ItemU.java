package freestar.friends.bean;

import java.io.Serializable;
import java.util.List;

import cn.bmob.v3.BmobObject;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class ItemU extends BmobObject implements Serializable {
    private String url;
    private String context;
    private boolean isUrl;
    private Article article;
    private List<String> list;
    private Integer order;

    public ItemU(String url, Boolean isUrl, Integer order) {
        this.url = url;
        this.isUrl = isUrl;
        this.order = order;
    }

    public ItemU(Boolean isUrl, String context, Integer order) {
        this.isUrl = isUrl;
        this.context = context;
        this.order = order;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public List<String> getList() {
        return list;
    }

    public ItemU() {
    }


    public ItemU(Boolean isUrl, String context) {
        this.isUrl = isUrl;
        this.context = context;
    }

    public void setUrl(Boolean url) {
        isUrl = url;
    }

    public void setList(List<String> list) {
        this.list = list;
    }

    public Article getArticle() {
        return article;
    }

    public void setArticle(Article article) {
        this.article = article;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public boolean isUrl() {
        return isUrl;
    }

    public void setUrl(boolean url) {
        isUrl = url;
    }

    public ItemU(String context) {
        this.context = context;
    }

    @Override
    public String toString() {
        return "ItemU{" +
                "url='" + url + '\'' +
                ", context='" + context + '\'' +
                ", isUrl=" + isUrl +
                ", article=" + article +
                ", list=" + list +
                '}';
    }
}
