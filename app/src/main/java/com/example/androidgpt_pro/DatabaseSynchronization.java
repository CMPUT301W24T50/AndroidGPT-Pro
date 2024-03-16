package com.example.androidgpt_pro;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

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
        eColRef.document(eventID).update("eSignUpProfiles", FieldValue.arrayUnion(eventID));
    }
    public void delSignUpEventProfile(String eventID, String profileID) {
        eColRef.document(eventID).update("eSignUpProfiles", FieldValue.arrayRemove(eventID));
    }

//    public void addCheckInProfileEvent(String profileID, String eventID) {
//
//    }
//    public void addCheckInEventProfile(String eventID, String profileID) {
//
//    }
}
