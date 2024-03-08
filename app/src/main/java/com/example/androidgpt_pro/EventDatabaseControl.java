package com.example.androidgpt_pro;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * This is a class that controls the interaction between event data and the database.
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


    /**
     * This is the event creator.
     * @return eventID
     * eventID: A unique ID of the new event.
     */
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

    /**
     * This is the event deleter.
     * @param eventID
     * eventID: An ID of an event.
     */
    public void removeEvent(String eventID) {
        eColRef.document(eventID).delete();
    }


    /**
     * This is the initializer of an event.
     * @param eventID
     * eventID: An ID of an event.
     * @param eventName
     * eventName: An event's name.
     * @param eventLocation
     * eventLocation: An event's location.
     * @param eventSimplifiedLocation
     * eventSimplifiedLocation: An event's simplified location.
     * @param eventDescription
     * eventDescription: An event's description.
     * @param eventTime
     * eventTime: An event's time.
     */
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
        data.put("eSignUpProfiles", eSignUpProfiles);
        data.put("eCheckInProfiles", eCheckInProfiles);
        eColRef.document(eventID).set(data);
    }


    /**
     * This is a setter for Event Name.
     * @param eventID
     * eventID: An ID of an event.
     * @param eventName
     * eventName: An event's name.
     */
    public void setEventName(String eventID, String eventName) {
        eColRef.document(eventID).update("eName", eventName);
    }

    /**
     * This is a setter for Event Location.
     * @param eventID
     * eventID: An ID of an event.
     * @param eventLocation
     * eventLocation: An event's location.
     */
    public void setEventLocation(String eventID, String eventLocation) {
        eColRef.document(eventID).update("eLocation", eventLocation);
    }

    /**
     * This is a setter for Event Simplified Location.
     * @param eventID
     * eventID: An ID of an event.
     * @param eventSimplifiedLocation
     * eventSimplifiedLocation: An event's simplified location.
     */
    public void setEventSimplifiedLocation(String eventID, String eventSimplifiedLocation) {
        eColRef.document(eventID).update("eSpfLocation", eventSimplifiedLocation);
    }

    /**
     * This is a setter for Event Description.
     * @param eventID
     * eventID: An ID of an event.
     * @param eventDescription
     * eventDescription: An event's description.
     */
    public void setEventDescription(String eventID, String eventDescription) {
        eColRef.document(eventID).update("eDescription", eventDescription);
    }

    /**
     * This is a setter for Event Time.
     * @param eventID
     * eventID: An ID of an event.
     * @param eventTime
     * eventTime: An event's time.
     */
    public void setEventTime(String eventID, String eventTime) {
        eColRef.document(eventID).update("eTime", eventTime);
    }


    /**
     * This is an adder used to add the given profile ID to the event sign up list.
     * @param eventID
     * eventID: An ID of an event.
     * @param profileID
     * profileID: An ID of a profile.
     */
    public void addEventSignUpProfile(String eventID, String profileID) {
        eColRef.document(eventID).update("eSignUpProfiles", FieldValue.arrayUnion(eventID));
    }

    /**
     * This is a deleter that can remove profile that want to cancel sign up.
     * @param eventID
     * eventID: An ID of an event.
     * @param profileID
     * profileID: An ID of a profile.
     */
    public void delEventSignUpProfile(String eventID, String profileID) {
        eColRef.document(eventID).update("eSignUpProfiles", FieldValue.arrayRemove(eventID));
    }

    /**
     * This is an adder used to add the given profile ID to the event check in list.
     * @param eventID
     * eventID: An ID of an event.
     * @param profileID
     * profileID: An ID of a profile.
     */
    public void addEventCheckInProfile(String eventID, String profileID) {
        eColRef.document(eventID).update("eCheckInProfiles", FieldValue.arrayUnion(eventID));
    }


    /**
     * This is a tool to add 1 to a string.
     * @param value
     * value: An 8 bits string number.
     * @return "value + 1"
     * "value + 1": 8-digit number after adding 1.
     */
    public String strAddOne(String value) {
        int intValue = Integer.parseInt(value);
        intValue++;
        return String.format("%08d", intValue);
    }
}
