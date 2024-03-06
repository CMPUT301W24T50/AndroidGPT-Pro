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

    public int createNewProfile() {
        HashMap<String, String> data = new HashMap<>();
        data.put("profileID", "00000000");
        data.put("profileName", "STAT");
        data.put("profilePassword", "");
        data.put("profileImage", "");
        data.put("profilePhoneNumber", "");
        data.put("profileEmail", "");
        profileRef.document("00000000").set(data);
        return 0;
    }

    public void createNewEvent() {
        HashMap<String, String> data = new HashMap<>();
        data.put("EventID", "00000000");
        data.put("EventName", "STAT");
        data.put("EventDescription", "");
        data.put("EventImage", "");
        eventRef.document("00000000").set(data);
    }
}
