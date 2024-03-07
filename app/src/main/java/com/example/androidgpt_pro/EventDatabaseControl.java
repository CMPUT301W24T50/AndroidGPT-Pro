package com.example.androidgpt_pro;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is a class that controls the interaction of event data with the database.
 */
public class EventDatabaseControl {

    private FirebaseFirestore db;
    private CollectionReference eColRef;

    private String eID;
    private String eName;
    private String eLocation;
    private String eSpfLocation;
    private String eDescription;
    private String eTime;


    public EventDatabaseControl() {
        db = FirebaseFirestore.getInstance();
        eColRef = db.collection("Event");
    }


    public String createNewEvent() {
        return "";
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
}
