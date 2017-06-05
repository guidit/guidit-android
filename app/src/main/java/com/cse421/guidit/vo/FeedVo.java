package com.cse421.guidit.vo;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class FeedVo {
    private int id;
    private String content;
    private String city;
    private String date;
    private int userId;
    private String userName;
    private String profile;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getContent() {
        return content;
    }
    
    public void setContent(String content) {
        this.content = content;
    }
    
    public String getCity() {
        return city;
    }
    
    public void setCity(String city) {
        this.city = city;
    }
    
    public String getDate() {
        return date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }
    
    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public String getProfile() {
        return profile;
    }
    
    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "FeedVo{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", city='" + city + '\'' +
                ", date='" + date + '\'' +
                ", userId='" + userId + '\'' +
                ", userName='" + userName + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }
}
