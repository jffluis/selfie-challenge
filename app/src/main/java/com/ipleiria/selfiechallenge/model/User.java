package com.ipleiria.selfiechallenge.model;

import android.widget.ImageView;

import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Joel on 05/05/2017.
 */

public class User  implements Comparator{

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

    @Override
    public int compare(Object o1, Object o2) {

        User u1 = (User) o1;
        User u2 = (User) o2;

        if (u1.getPoints() > u2.getPoints()) {
            return 1;
        }
        else if (u1.getPoints() < u2.getPoints()) {
            return -1;
        }
        else {
            return 0;
        }
    }
}
