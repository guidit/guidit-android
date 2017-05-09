package com.cse421.guidit.vo;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class PlanVo {
    int id;
    String name;
    boolean isPublic;
    int viewCount;
    String picture;
    
    public int getId() {
        return id;
    }
    
    public void setId(int id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public boolean isPublic() {
        return isPublic;
    }
    
    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }
    
    public int getViewCount() {
        return viewCount;
    }
    
    public void setViewCount(int viewCount) {
        this.viewCount = viewCount;
    }
    
    public String getPicture() {
        return picture;
    }
    
    public void setPicture(String picture) {
        this.picture = picture;
    }
    
    @Override
    public String toString() {
        return "PlanVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isPublic=" + isPublic +
                ", viewCount=" + viewCount +
                ", picture='" + picture + '\'' +
                '}';
    }
}
