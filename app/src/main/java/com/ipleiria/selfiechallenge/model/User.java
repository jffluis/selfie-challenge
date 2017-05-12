package com.ipleiria.selfiechallenge.model;

import android.widget.ImageView;

/**
 * Created by Joel on 05/05/2017.
 */

public class User {

    private String name;
    private String photoURL;
    private int points;
    private String id;


    public User(String name, String photoURL, int points) {
        this.name = name;
        this.photoURL = photoURL;
        this.points = points;
    }

    public User(String id, String name, String photoURL, int points) {
        this.id = id;
        this.name = name;
        this.photoURL = photoURL;
        this.points = points;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }
}
