package com.example.androidgpt_pro;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class DatabaseSynchronization {

    private FirebaseFirestore db;
    private CollectionReference pColRef;
    private CollectionReference eColRef;
    private DatabaseTools dt;

    public DatabaseSynchronization() {
        db = FirebaseFirestore.getInstance();
        pColRef = db.collection("Profile");
        eColRef = db.collection("Event");
        dt = new DatabaseTools();
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

    public void addCheckInProfileEvent(String profileID,
                                       String eventID,
                                       String count,
                                       String nextCount) {
        String data = dt.constructIDCountString(eventID, count);
        pColRef.document(profileID)
                .update("pCheckInEvents", FieldValue.arrayRemove(data));
        String nextData = dt.constructIDCountString(eventID, nextCount);
        pColRef.document(profileID)
                .update("pCheckInEvents", FieldValue.arrayUnion(nextData));
    }
    public void addCheckInEventProfile(String eventID,
                                       String profileID,
                                       String count,
                                       String nextCount) {
        String data = dt.constructIDCountString(profileID, count);
        eColRef.document(eventID)
                .update("eCheckInProfiles", FieldValue.arrayRemove(data));
        String nextData = dt.constructIDCountString(profileID, nextCount);
        eColRef.document(eventID)
                .update("eCheckInProfiles", FieldValue.arrayUnion(nextData));
    }
}
