package freestar.friends.bean;

/**
 * Created by Administrator on 2016/8/25 0025.
 */
public class Item {
    private String url;
    private String context;
    public Boolean isUrl;

    public String getUrl() {
        return url;
    }

    public void setUrl(Boolean url) {
        isUrl = url;
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

    public Item(String url, String context, Boolean isUrl) {

        this.url = url;
        this.context = context;
        this.isUrl = isUrl;
    }
}
