package com.cse421.guidit.vo;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class UserVo {

    public static UserVo userVo;

    public static UserVo getInstance () {
        if (userVo == null) {
            userVo = new UserVo();
        }
        return userVo;
    }

    public static void destroy () {
        userVo = null;
    }

    private int id;
    private String user_id;
    private String password;
    private String name;
    private String profile;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfile() {
        return profile;
    }

    public void setProfile(String profile) {
        this.profile = profile;
    }

    @Override
    public String toString() {
        return "UserVo{" +
                "id=" + id +
                ", user_id='" + user_id + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", profile='" + profile + '\'' +
                '}';
    }
}
