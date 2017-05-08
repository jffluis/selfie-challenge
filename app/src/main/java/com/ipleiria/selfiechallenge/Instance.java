package com.ipleiria.selfiechallenge;

import com.ipleiria.selfiechallenge.model.Challenge;
import com.ipleiria.selfiechallenge.model.User;

import java.util.ArrayList;

/**
 * Created by Joel on 03/05/2017.
 */

public class Instance {

    private static final Instance ourInstance = new Instance();
    private User currentUser;
    private ArrayList<Challenge> challengesList;

    public static Instance getInstance() {
        return ourInstance;
    }

    private Instance() {
        currentUser = new User(getFullName(), 1000);
        challengesList = new ArrayList<>();
    }

    private String fullName;

    private String urlPhoto;

    public String setFullName(String fullName) {
        this.fullName = fullName;
        return this.fullName;
    }

    public String setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
        return this.urlPhoto;
    }

    public String getFullName() {
        return fullName;
    }

    public String getUrlPhoto() {
        return urlPhoto;
    }

    public User getCurrentUser(){
        return currentUser;
    }

    public void addChallenge(Challenge challenge){
        challengesList.add(challenge);
    }

    public ArrayList<Challenge> getChallengesList() {
        return challengesList;
    }

    public void setChallengesList(ArrayList<Challenge> challengesList) {
        this.challengesList = challengesList;
    }
}
