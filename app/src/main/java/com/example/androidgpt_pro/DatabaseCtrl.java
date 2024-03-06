package com.example.androidgpt_pro;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public abstract class DatabaseCtrl {
    private FirebaseFirestore db;
    private CollectionReference eventRef;
    private CollectionReference profileRef;

    public DatabaseCtrl() {
        db = FirebaseFirestore.getInstance();
        eventRef = db.collection("Event");
        profileRef = db.collection("Profile");
    }

    public int genNewProfile() {
        profileRef.count();
        return 0;
    }
}
