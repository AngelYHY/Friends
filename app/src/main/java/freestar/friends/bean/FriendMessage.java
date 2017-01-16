package freestar.friends.bean;

public class FriendMessage {
    private String userId;
    private String name;
    private String portraitUri;

    public FriendMessage() {
    }

    public FriendMessage(String userId) {
        this.userId = userId;
    }

    public FriendMessage(String userId, String name, String portraitUri) {
        this.userId = userId;
        this.name = name;
        this.portraitUri = portraitUri;

    }

    /** Not-null value. */
    public String getUserId() {
        return userId;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPortraitUri() {
        return portraitUri;
    }

    public void setPortraitUri(String portraitUri) {
        this.portraitUri = portraitUri;
    }

}
