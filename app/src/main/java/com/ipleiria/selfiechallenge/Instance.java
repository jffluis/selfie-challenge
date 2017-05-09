package com.ipleiria.selfiechallenge;

import com.ipleiria.selfiechallenge.model.Challenge;
import com.ipleiria.selfiechallenge.model.User;

import java.util.ArrayList;

/**
 * Created by Joel on 03/05/2017.
 */

public class Instance {

    private Challenge currentChallenge;

    private static final Instance ourInstance = new Instance();
    private User currentUser;
    private ArrayList<Challenge> challengesList;
    public Challenge challengeToSee;
    public int selectedPhotoPos;

    public static Instance getInstance() {
        return ourInstance;
    }

    private Instance() {
        challengesList = new ArrayList<>();
    }

    private String fullName;

    private String urlPhoto;

    public void setFullName(String fullName) {
        this.fullName = fullName;
        if(currentUser == null) {
            currentUser = new User(this.getFullName(), 1000);
        }else{
            currentUser.setName(fullName);
        }
    }

    public void setUrlPhoto(String urlPhoto) {
        this.urlPhoto = urlPhoto;
    }

    public String getFullName() {
        System.out.println(fullName);
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

    public Challenge getCurrentChallenge() {
        return currentChallenge;
    }

    public void setCurrentChallenge(Challenge currentChallenge) {
        this.currentChallenge = currentChallenge;
    }
}
