package com.ipleiria.selfiechallenge;

/**
 * Created by Joel on 03/05/2017.
 */

class Instance {
    private static final Instance ourInstance = new Instance();

    static Instance getInstance() {
        return ourInstance;
    }

    private Instance() {
    }

    private String fullName;
    private String urlPhoto;

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }
}
