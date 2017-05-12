package com.ipleiria.selfiechallenge.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by marciofernandescalil on 5/7/17.
 */

public final class Firebase {

    // Firebase
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference dbUserChallenges = database.getReference("userChallenges");
    public static DatabaseReference dbUsers = database.getReference("users");
    public static FirebaseStorage storage = FirebaseStorage.getInstance();
    public static String StorageURL = "gs://selfie-challenge-f7b92.appspot.com";

}
