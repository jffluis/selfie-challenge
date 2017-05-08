package com.ipleiria.selfiechallenge.model;

import android.widget.ImageView;

/**
 * Created by Joel on 05/05/2017.
 */

public class User {

    private String name;
    private ImageView photo;
    private int points;


    public User(String name, ImageView photo, int points) {
        this.name = name;
        this.photo = photo;
        this.points = points;
    }

    public User(String name, int points) {
        this.name = name;
        this.points = points;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ImageView getPhoto() {
        return photo;
    }

    public void setPhoto(ImageView photo) {
        this.photo = photo;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

}
