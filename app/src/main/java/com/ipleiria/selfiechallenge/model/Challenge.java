package com.ipleiria.selfiechallenge.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Joel on 05/05/2017.
 */

public class Challenge {

    private String name; //Example: take a photo eating ice cream
    private String description;
    private User user;
    private ArrayList<Bitmap> photos;


    public Challenge(String name, String description, User user, ArrayList<Bitmap> allPhotos) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.photos = allPhotos;
    }

    public Challenge(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Bitmap> getPhotos() {
        return photos;
    }

    public void setPhotos(ArrayList<Bitmap> photos) {
        this.photos = photos;
    }

    public void addPhoto(Bitmap photo){
       this.photos.add(photo);

    }
}
