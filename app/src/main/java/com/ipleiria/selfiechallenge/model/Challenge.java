package com.ipleiria.selfiechallenge.model;

import java.util.ArrayList;

/**
 * Created by Joel on 05/05/2017.
 */

public class Challenge {

    private String name; //Example: take a photo eating ice cream
    private String id;
    private String description;
    private User user;
    private ArrayList<String> photoURL;


    public Challenge(String name, String description, User user, ArrayList<String> URLphotos) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.photoURL = URLphotos;
    }

    public Challenge(String name, String description, User user) {
        this.name = name;
        this.description = description;
        this.user = user;
        this.photoURL = new ArrayList<>();
    }

    public Challenge(String id, String name, String description, User user, ArrayList<String> URLphotos) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.user = user;
        this.photoURL = URLphotos;
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

    public ArrayList<String> getPhotos() {
        return photoURL;
    }

    public void addPhoto(String photoURL){
       this.photoURL.add(photoURL);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
