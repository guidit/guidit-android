package com.cse421.guidit.vo;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class CommentVo {
    private int id;
    private String userProfile;
    private String userId;
    private String comment;
    private String date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
    }

    public void setDate(String date) {
        this.date = date;

    }
}
