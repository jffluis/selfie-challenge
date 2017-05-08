package com.ipleiria.selfiechallenge.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by marciofernandescalil on 5/7/17.
 */

public final class Firebase {

    // Firebase
    public static FirebaseDatabase database = FirebaseDatabase.getInstance();
    public static DatabaseReference dbUserChallenges = database.getReference("userChallenges");
    public static DatabaseReference dbUser = database.getReference("user");

}
