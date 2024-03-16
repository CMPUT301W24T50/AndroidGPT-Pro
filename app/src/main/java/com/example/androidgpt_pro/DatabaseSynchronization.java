package com.example.androidgpt_pro;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DatabaseSynchronization {

    private FirebaseFirestore db;
    private CollectionReference pColRef;
    private CollectionReference eColRef;

    public DatabaseSynchronization() {
        db = FirebaseFirestore.getInstance();
        pColRef = db.collection("Profile");
        eColRef = db.collection("Event");
    }

    public void addSignUpProfileEvent(String profileID, String eventID) {
        pColRef.document(profileID).update("pSignUpEvents", FieldValue.arrayUnion(eventID));
    }
    public void delSignUpProfileEvent(String profileID, String eventID) {
        pColRef.document(profileID).update("pSignUpEvents", FieldValue.arrayRemove(eventID));
    }
    public void addSignUpEventProfile(String eventID, String profileID) {
        eColRef.document(eventID).update("eSignUpProfiles", FieldValue.arrayUnion(profileID));
    }
    public void delSignUpEventProfile(String eventID, String profileID) {
        eColRef.document(eventID).update("eSignUpProfiles", FieldValue.arrayRemove(profileID));
    }

    public void addCheckInProfileEvent(String profileID, String eventID, String count) {
        HashMap<String, String> data = new HashMap<>();
        data.put(eventID, count);
        pColRef.document(profileID).update("pCheckInEvents", data);
    }
    public void addCheckInEventProfile(String eventID, String profileID, String count) {
        HashMap<String, String> data = new HashMap<>();
        data.put(profileID, count);
        eColRef.document(eventID).update("eCheckInProfiles", data);
    }
}
