package com.ipleiria.selfiechallenge.model;

/**
 * Created by Joel on 04/05/2017.
 */

public class POI {

    private String name;
    private String address;
    private String urlPhoto;

    public POI(String name, String address, String urlPhoto) {
        this.name = name;
        this.address = address;
        this.urlPhoto = urlPhoto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }
}
