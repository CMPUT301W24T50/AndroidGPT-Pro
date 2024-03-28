package com.example.androidgpt_pro;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is a class to synchronize the profile database and the event database.
 */
public class DatabaseSynchronization {

    private FirebaseFirestore db;
    private CollectionReference pColRef;
    private CollectionReference eColRef;
    private DatabaseTools dt;


    /**
     * This is the constructor of class DatabaseSynchronization.
     */
    public DatabaseSynchronization() {
        db = FirebaseFirestore.getInstance();
        pColRef = db.collection("Profile");
        eColRef = db.collection("Event");
        dt = new DatabaseTools();
    }


    /**
     * This is an adder to synchronize the profile database for organized records.
     * @param profileID
     * profileID: A profile's ID.
     * @param eventID
     * eventID: An event's ID.
     */
    public void addOrganizedProfileEvent(String profileID, String eventID) {
        pColRef.document(profileID)
                .update("pOrganizedEvents", FieldValue.arrayUnion(eventID));
    }

    /**
     * This is a deleter to synchronize the profile database for organized records.
     * @param profileID
     * profileID: A profile's ID.
     * @param eventID
     * eventID: An event's ID.
     */
    public void delOrganizedProfileEvent(String profileID, String eventID) {
        pColRef.document(profileID)
                .update("pOrganizedEvents", FieldValue.arrayRemove(eventID));
    }

    /**
     * This is a deleter to synchronize the event database for organized records.
     * @param eventID
     * eventID: An event's ID.
     */
    public void delOrganizedEvent(String eventID) {
        eColRef.document(eventID).delete();
    }


    /**
     * This is an adder to synchronize the profile database for sign up records.
     * @param profileID
     * profileID: A profile's ID.
     * @param eventID
     * eventID: An event's ID.
     */
    public void addSignUpProfileEvent(String profileID, String eventID) {
        pColRef.document(profileID).update("pSignUpEvents", FieldValue.arrayUnion(eventID));
    }

    /**
     * This is a deleter to synchronize the profile database for sign up records.
     * @param profileID
     * profileID: A profile's ID.
     * @param eventID
     * eventID: An event's ID.
     */
    public void delSignUpProfileEvent(String profileID, String eventID) {
        pColRef.document(profileID).update("pSignUpEvents", FieldValue.arrayRemove(eventID));
    }

    /**
     * This is an adder to synchronize the event database for sign up records.
     * @param eventID
     * eventID: An event's ID.
     * @param profileID
     * profileID: A profile's ID.
     */
    public void addSignUpEventProfile(String eventID, String profileID) {
        eColRef.document(eventID).update("eSignUpProfiles", FieldValue.arrayUnion(profileID));
    }

    /**
     * This is a deleter to synchronize the event database for sign up records.
     * @param eventID
     * eventID: An event's ID.
     * @param profileID
     * profileID: A profile's ID.
     */
    public void delSignUpEventProfile(String eventID, String profileID) {
        eColRef.document(eventID).update("eSignUpProfiles", FieldValue.arrayRemove(profileID));
    }


    /**
     * This is a creator to synchronize the profile database for check in records.
     * @param profileID
     * profileID: A profile's ID.
     * @param eventID
     * eventID: An event's ID.
     * @param count
     * count: A string number represent the number of times.
     */
    public void newCheckInProfileEvent(String profileID, String eventID, String count) {
        String data = dt.constructIDCountString(eventID, count);
        pColRef.document(profileID)
                .update("pCheckInEvents", FieldValue.arrayUnion(data));
    }

    /**
     * This is an adder to synchronize the profile database for check in records.
     * @param profileID
     * profileID: A profile's ID.
     * @param eventID
     * eventID: An event's ID.
     * @param count
     * count: A string number represent the number of times.
     * @param nextCount
     * nextCount: A string number represent the number of the next time.
     */
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

    /**
     * This is a creator to synchronize the event database for check in records.
     * @param eventID
     * eventID: An event's ID.
     * @param profileID
     * profileID: A profile's ID.
     * @param count
     * count: A string number represent the number of times.
     */
    public void newCheckInEventProfile(String eventID, String profileID, String count) {
        String data = dt.constructIDCountString(profileID, count);
        pColRef.document(eventID)
                .update("eCheckInProfiles", FieldValue.arrayUnion(data));
    }

    /**
     * This is an adder to synchronize the event database for check in records.
     * @param eventID
     * eventID: An event's ID.
     * @param profileID
     * profileID: A profile's ID.
     * @param count
     * count: A string number represent the number of times.
     * @param nextCount
     * nextCount: A string number represent the number of the next time.
     */
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
