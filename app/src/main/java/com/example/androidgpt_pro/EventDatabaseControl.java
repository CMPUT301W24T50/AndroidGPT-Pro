package com.example.androidgpt_pro;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

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

    public String getEventName() {
        return "";
    }

    public String getEventLocation() {
        return "";
    }

    public String getEventSimplifiedLocation() {
        return "";
    }

    public String getEventDescription() {
        return "";
    }

    public String getEventTime() {
        return "";
    }


    public void setEventName(String eventName) {

    }

    public void setEventLocation(String eventLocation) {

    }

    public void setEventSimplifiedLocation(String eventSimplifiedLocation) {

    }

    public void setEventDescription(String eventDescription) {

    }

    public void setEventTime(String eventTime) {

    }


    public void addEventProfile(String profileID) {

    }

    public void delEventProfile(String profileID) {

    }


    private String strAddOne(String value) {
        int intValue = Integer.parseInt(value);
        intValue++;
        return String.format("%08d", intValue);
    }
}
