package com.cse421.guidit.vo;

/**
 * Created by jeongyi on 2017. 5. 17..
 * test 용도 입니다 나중에 삭제할거임!!!!
 */

public class SightListVo {

    public int image;
    public String title;
    public String subtitle;

    public SightListVo(int image, String title, String subtitle) {
        this.image = image;
        this.title = title;
        this.subtitle = subtitle;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
}
