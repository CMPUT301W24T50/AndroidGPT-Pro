package com.example.androidgpt_pro;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is a class that controls the interaction of event data with the database.
 */
public class EventDbCtrl {

    private FirebaseFirestore db;
    private CollectionReference eRef;

    private String eID;
    private String eName;
    private String eDescription;
    private String eTime;

    public EventDbCtrl() {
        db = FirebaseFirestore.getInstance();
        eRef = db.collection("UserProfile");
    }
}
