package com.cse421.guidit.vo;

import java.util.ArrayList;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class DailyPlanVo {
    private int id;
    private int dayNum;
    private ArrayList<SightVo> sightList;
    private String review;
    private String picture;

    public DailyPlanVo() {
        id = dayNum = 0;
        sightList = new ArrayList<>();
        review = picture = "";
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getDayNum() {
        return dayNum;
    }

    public void setDayNum(int dayNum) {
        this.dayNum = dayNum;
    }

    public ArrayList<SightVo> getSightList() {
        return sightList;
    }

    public void setSightList(ArrayList<SightVo> sightList) {
        this.sightList = sightList;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return "DailyPlanVo{" +
                "id=" + id +
                ", dayNum=" + dayNum +
                ", sightList=" + sightList +
                ", review='" + review + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }
}
