package com.ipleiria.selfiechallenge.model;

/**
 * Created by Joel on 04/05/2017.
 */

public class POI {

    private String name;
    private String address;


    public POI(String name, String address) {
        this.name = name;
        this.address = address;
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
}
