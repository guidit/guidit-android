package com.cse421.guidit.vo;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class SightVo {
    int id;
    String name;
    String location;
    String type;
    String information;
    String picture;
    double score;
    
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
    
    public String getLocation() {
        return location;
    }
    
    public void setLocation(String location) {
        this.location = location;
    }
    
    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }
    
    public String getInformation() {
        return information;
    }
    
    public void setInformation(String information) {
        this.information = information;
    }
    
    public String getPicture() {
        return picture;
    }
    
    public void setPicture(String picture) {
        this.picture = picture;
    }
    
    public double getScore() {
        return score;
    }
    
    public void setScore(double score) {
        this.score = score;
    }
    
    @Override
    public String toString() {
        return "SightVo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", location='" + location + '\'' +
                ", type='" + type + '\'' +
                ", information='" + information + '\'' +
                ", picture='" + picture + '\'' +
                ", score=" + score +
                '}';
    }
}
