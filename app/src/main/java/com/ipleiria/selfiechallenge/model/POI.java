package com.ipleiria.selfiechallenge.model;

import android.location.Location;

/**
 * Created by Joel on 04/05/2017.
 */

public class POI {

    private String name;
    private String address;
    private String urlPhoto;
    private Location location;

    public POI(String name, String address, String urlPhoto, Location location) {
        this.name = name;
        this.address = address;
        this.urlPhoto = urlPhoto;
        this.location = location;
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

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
