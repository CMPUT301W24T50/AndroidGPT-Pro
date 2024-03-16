package com.example.androidgpt_pro;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a class that controls the interaction between event data and the database.
 */
public class EventDatabaseControl {

    private FirebaseFirestore db;
    private CollectionReference eColRef;
    private DatabaseSynchronization ds;

    private String eLastEventID;
    private String eID;
    private String eName;
    private String eLocation;
    private String eSpfLocation;
    private String eDescription;
    private String eTime;
    private String eDate;
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
     * @param eventDate
     * eventDate: An event's date.
     */
    public void initEvent(String eventID,
                          String eventName,
                          String eventLocation,
                          String eventSimplifiedLocation,
                          String eventDescription,
                          String eventTime,
                          String eventDate) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("eName", eventName);
        data.put("eLocation", eventLocation);
        data.put("eSpfLocation", eventSimplifiedLocation);
        data.put("eDescription", eventDescription);
        data.put("eTime", eventTime);
        data.put("eDate", eventDate);
        data.put("eSignUpProfiles", eSignUpProfiles);
        data.put("eCheckInProfiles", eCheckInProfiles);
        eColRef.document(eventID).set(data);
    }


    /**
     * This is a getter for a snapshot of the EventStat.
     * @return eventStatSnapshotGetTask
     * eventStatSnapshotGetTask: A task for get the DocumentSnapshot.
     */
    public Task<DocumentSnapshot> getEventStat() {
        return eColRef.document("00000000").get();
    }

    /**
     * This is a setter for the EventStat.
     * @param eventLastEventID
     * eventLastEventID: The ID of the last event.
     */
    public void setEventStat(String eventLastEventID) {
        eColRef.document("00000000").update("eLastEventID", eventLastEventID);
    }


    /**
     * This is a getter for a event snapshot.
     * @param eventID
     * eventID: An event's ID.
     * @return eventSnapshotGetTask
     * eventSnapshotGetTask: A task for getting eventDocumentSnapshot.
     */
    public Task<DocumentSnapshot> getEventSnapshot(String eventID) {
        return eColRef.document(eventID).get();
    }

    /**
     * This is a getter for a event.
     * @param eventID
     * eventID: An event's ID.
     * @return event
     * event: An event document.
     */
    public DocumentReference getEvent(String eventID) {
        return eColRef.document(eventID);
    }


    /**
     * This is a getter for Last Event ID.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventLastEventID
     * eventLastEventID: The ID of the last event.
     */
    public String getLastEventID(DocumentSnapshot eventDocumentSnapshot) {
        return eventDocumentSnapshot.getString("eLastEventID");
    }

    /**
     * This is a getter for Event Name.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventName
     * eventName: The name of the event.
     */
    public String getEventName(DocumentSnapshot eventDocumentSnapshot) {
        return eventDocumentSnapshot.getString("eName");
    }

    /**
     * This is a getter for Event Location.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventLocation
     * eventLocation: The location of the event.
     */
    public String getEventLocation(DocumentSnapshot eventDocumentSnapshot) {
        return eventDocumentSnapshot.getString("eLocation");
    }

    /**
     * This is a getter for Event Simplified Location.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventSimplifiedLocation
     * eventSimplifiedLocation: The simplified location of the event.
     */
    public String getEventSimplifiedLocation(DocumentSnapshot eventDocumentSnapshot) {
        return eventDocumentSnapshot.getString("eSpfLocation");
    }

    /**
     * This is a getter for Event Description.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventDescription
     * eventDescription: The description of the event.
     */
    public String getEventDescription(DocumentSnapshot eventDocumentSnapshot) {
        return eventDocumentSnapshot.getString("eDescription");
    }

    /**
     * This is a getter for Event Time.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventTime
     * eventTime: The time of the event.
     */
    public String getEventTime(DocumentSnapshot eventDocumentSnapshot) {
        return eventDocumentSnapshot.getString("eTime");
    }

    /**
     * This is a getter for Event Date.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventDate
     * eventDate: The date of the event.
     */
    public String getEventDate(DocumentSnapshot eventDocumentSnapshot) {
        return eventDocumentSnapshot.getString("eDate");
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
     * This is a setter for Event Date.
     * @param eventID
     * eventID: An ID of an event.
     * @param eventDate
     * eventTime: An event's date.
     */
    public void setEventDate(String eventID, String eventDate) {
        eColRef.document(eventID).update("eDate", eventDate);
    }

    /**
     * This is an adder used to add the given profile ID to the event sign up list.
     * @param eventID
     * eventID: An ID of an event.
     * @param profileID
     * profileID: An ID of a profile.
     */
    public void addEventSignUpProfile(String eventID, String profileID) {
        eColRef.document(eventID).update("eSignUpProfiles", FieldValue.arrayUnion(profileID));
        ds.addSignUpProfileEvent(profileID, eventID);
    }

    /**
     * This is a deleter that can remove profile that want to cancel sign up.
     * @param eventID
     * eventID: An ID of an event.
     * @param profileID
     * profileID: An ID of a profile.
     */
    public void delEventSignUpProfile(String eventID, String profileID) {
        eColRef.document(eventID).update("eSignUpProfiles", FieldValue.arrayRemove(profileID));
        ds.delSignUpProfileEvent(profileID, eventID);
    }

    /**
     * This is an adder used to add the given profile ID to the event check in list.
     * @param eventID
     * eventID: An ID of an event.
     * @param profileID
     * profileID: An ID of a profile.
     */
    public void addEventCheckInProfile(String eventID, String profileID) {
        eColRef.document(eventID).update("eCheckInProfiles", FieldValue.arrayUnion(profileID));
    }


    /**
     * This is a calculator to get the ID of the next event.
     * @param value
     * value: An 8 bits string number.
     * @return valueAddOne
     * valueAddOne: 8-digit number after adding 1.
     */
    public String calculateNextEventID(String value) {
        int intValue = Integer.parseInt(value);
        intValue++;
        return String.format("%08d", intValue);
    }
}
