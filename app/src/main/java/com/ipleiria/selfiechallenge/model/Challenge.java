package com.ipleiria.selfiechallenge.model;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by Joel on 05/05/2017.
 */

public class Challenge {

    private String name; //Example: take a photo eating ice cream
    private User user;
    private ArrayList<Bitmap> photos;


    public Challenge(String name, User user, ArrayList<Bitmap> allPhotos) {
        this.name = name;
        this.user = user;
        this.photos = allPhotos;
    }

    public Challenge(String name, User user) {
        this.name = name;
        this.user = user;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
