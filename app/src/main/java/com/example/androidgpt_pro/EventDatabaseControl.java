package com.example.androidgpt_pro;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * This is a class that controls the interaction of event data with the database.
 */
public class EventDatabaseControl {

    private FirebaseFirestore db;
    private CollectionReference eColRef;
    private String eLastEventID;
    private String eID;
    private String eName;
    private String eLocation;
    private String eSpfLocation;
    private String eDescription;
    private String eTime;
    private ArrayList<String> eSignUpProfiles;
    private ArrayList<String> eCheckInProfiles;


    /**
     * This is the constructor of class EventDatabaseControl.
     */
    public EventDatabaseControl() {
        db = FirebaseFirestore.getInstance();
        eColRef = db.collection("Event");
    }


    public String createEvent() {
        eColRef.document("00000000").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                eLastEventID = documentSnapshot.getString("eLastEventID");
                eID = strAddOne(eLastEventID);
                HashMap<String, String> data = new HashMap<>();
                data.put("eLastEventID", eID);
                eColRef.document("00000000").set(data);
            }
        });
        return eID;
    }

    public void removeEvent(String eventID) {
        eColRef.document(eventID).delete();
    }


    public void initEvent(String eventID,
                          String eventName,
                          String eventLocation,
                          String eventSimplifiedLocation,
                          String eventDescription,
                          String eventTime) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("eName", eventName);
        data.put("eLocation", eventLocation);
        data.put("eSpfLocation", eventSimplifiedLocation);
        data.put("eDescription", eventDescription);
        data.put("eTime", eventTime);
        eColRef.document(eventID).set(data);
    }

    
    public String getEventName(String eventID) {
        eID = eventID;
        downloadData();
        return eName;
    }

    public String getEventLocation(String eventID) {
        eID = eventID;
        downloadData();
        return eLocation;
    }

    public String getEventSimplifiedLocation(String eventID) {
        eID = eventID;
        downloadData();
        return eSpfLocation;
    }

    public String getEventDescription(String eventID) {
        eID = eventID;
        downloadData();
        return eDescription;
    }

    public String getEventTime(String eventID) {
        eID = eventID;
        downloadData();
        return eTime;
    }


    public void setEventName(String eventID, String eventName) {
        eColRef.document(eventID).update("eName", eventName);
    }

    public void setEventLocation(String eventID, String eventLocation) {
        eColRef.document(eventID).update("eLocation", eventLocation);
    }

    public void setEventSimplifiedLocation(String eventID, String eventSimplifiedLocation) {
        eColRef.document(eventID).update("eSpfLocation", eventSimplifiedLocation);
    }

    public void setEventDescription(String eventID, String eventDescription) {
        eColRef.document(eventID).update("eDescription", eventDescription);
    }

    public void setEventTime(String eventID, String eventTime) {
        eColRef.document(eventID).update("eTime", eventTime);
    }


//    public void addEventSignUpProfile(String profileID) {
//
//    }
//
//    public void delEventSignUpProfile(String profileID) {
//
//    }
//
//    public void addEventCheckInProfile(String profileID) {
//
//    }
//
//    public void delEventCheckInProfile(String profileID) {
//
//    }


    private void downloadData() {
        eColRef.document(eID).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                eName = documentSnapshot.getString("eName");
                eLocation = documentSnapshot.getString("eLocation");
                eSpfLocation = documentSnapshot.getString("eSpfLocation");
                eDescription = documentSnapshot.getString("eDescription");
                eTime = documentSnapshot.getString("eTime");
            }
        });
    }

    private String strAddOne(String value) {
        int intValue = Integer.parseInt(value);
        intValue++;
        return String.format("%08d", intValue);
    }
}
