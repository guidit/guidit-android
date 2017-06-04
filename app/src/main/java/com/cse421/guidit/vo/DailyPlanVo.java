package com.cse421.guidit.vo;

import java.util.ArrayList;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class DailyPlanVo {
    private ArrayList<SightVo> sightList;
    private String review;
    private ArrayList<String> pictureList;

    public DailyPlanVo() {
        sightList = new ArrayList<>();
        review = "";
        pictureList = new ArrayList<>();
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

    public ArrayList<String> getPictureList() {
        return pictureList;
    }

    public void setPictureList(ArrayList<String> pictureList) {
        this.pictureList = pictureList;
    }

    @Override
    public String toString() {
        return "DailyPlanVo{" +
                "sightList=" + sightList +
                ", review='" + review + '\'' +
                ", pictureList=" + pictureList +
                '}';
    }
}
