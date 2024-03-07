package com.example.androidgpt_pro;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DatabaseCtrl {
    private FirebaseFirestore db;
    private CollectionReference eventRef;
    private CollectionReference profileRef;

    public DatabaseCtrl() {
        db = FirebaseFirestore.getInstance();
        eventRef = db.collection("Event");
        profileRef = db.collection("Profile");
    }
}
