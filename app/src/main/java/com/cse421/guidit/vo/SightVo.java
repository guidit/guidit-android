package com.cse421.guidit.vo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hokyung on 2017. 5. 1..
 */

public class SightVo implements Parcelable{
    int id;
    String name;
    String location;
    String type;
    String information;
    String picture;
    double score;
    double mapX;
    double mapY;
    boolean favorite;


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


    public double getMapX() {
        return mapX;
    }

    public void setMapX(double mapX) {
        this.mapX = mapX;
    }

    public double getMapY() {
        return mapY;
    }

    public void setMapY(double mapY) {
        this.mapY = mapY;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
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
                ", x=" + mapX +
                ", y=" + mapY +
                '}';
    }


    //parcelable

    public SightVo(int id, String name, String type, String picture, double score, double mapX, double mapY) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.picture = picture;
        this.score = score;
        this.mapX = mapX;
        this.mapY = mapY;
    }

    public SightVo() {
        super();
    }

    public SightVo(Parcel in) {
        super();
        readFromParcel(in);
    }

    public static final Parcelable.Creator<SightVo> CREATOR = new Parcelable.Creator<SightVo>() {
        public SightVo createFromParcel(Parcel in) {
            return new SightVo(in);
        }

        public SightVo[] newArray(int size) {

            return new SightVo[size];
        }

    };

    public void readFromParcel(Parcel in) {

        id = in.readInt();
        name = in.readString();
        location = in.readString();
        type = in.readString();
        information = in.readString();
        picture = in.readString();
        score = in.readDouble();
        mapX = in.readDouble();
        mapY = in.readDouble();

    }
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(location);
        dest.writeString(type);
        dest.writeString(information);
        dest.writeString(picture);
        dest.writeDouble(score);
        dest.writeDouble(mapX);
        dest.writeDouble(mapY);
    }
}
