package com.example.androidgpt_pro;

import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * This is a class that controls the interaction between event data and the database.
 */
public class EventDatabaseControl {

    private FirebaseFirestore db;
    private CollectionReference eColRef;
    private DatabaseSynchronization ds;
    private DatabaseTools dt;

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
        ds = new DatabaseSynchronization();
        dt = new DatabaseTools();
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
     * This is the event deleter.
     * @param eventID
     * eventID: An ID of an event.
     */
    public void removeEvent(String eventID) {
        eColRef.document(eventID).delete();
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
     * This is a updater for the EventStat.
     * @param eventLastEventID
     * eventLastEventID: The ID of the last event.
     * @return eventID
     * eventID: The ID of the current event.
     */
    public String updateEventStat(String eventLastEventID) {
        String eventID = dt.calculateAddOne(eventLastEventID);
        eColRef.document("00000000").update("eLastEventID", eventID);
        return eventID;
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
     * This is a getter for Event SignUp Profiles.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventSignUpProfiles
     * eventSignUpProfiles: A list of profileID, null if no profile.
     */
    public ArrayList<String> getEventAllSignUpProfiles(DocumentSnapshot eventDocumentSnapshot) {
        return (ArrayList<String>) eventDocumentSnapshot.get("eSignUpProfiles");
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
     * This is a getter for Event CheckIn Profiles.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventCheckInProfiles
     * eventCheckInProfiles: A "2D Array" list of profileID and count, list[0][0] == "-1" if no profiles.
     */
    public String[][] getEventAllCheckInProfiles(DocumentSnapshot eventDocumentSnapshot) {
        if ((ArrayList<String>) eventDocumentSnapshot.get("eCheckInProfiles") == null) {
            String[][] data = new String[0][];
            data[0][0] = "-1";
            return data;
        }
        ArrayList<String> data = (ArrayList<String>) eventDocumentSnapshot.get("eCheckInProfiles");
        String[][] lst = new String[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            lst[i] = data.get(i).split("#");
        }
        return lst;
    }

    private String getEventCheckInProfileCount(DocumentSnapshot eventDocumentSnapshot,
                                             String profileID) {
        Log.d("Testo", eventDocumentSnapshot.get("eCheckInProfiles").toString());
        if ((ArrayList<String>) eventDocumentSnapshot.get("eCheckInProfiles") == null) {
            return "-1";
        }
        ArrayList<String> data = (ArrayList<String>) eventDocumentSnapshot.get("eCheckInProfiles");
        for (int i = 0; i < data.size(); i++) {
            if (Objects.equals(data.get(i).split("#")[0], profileID)) {
                return data.get(i).split("#")[1];
            }
        }
        return null;
    }

    /**
     * This is an adder used to add or update the given profileID to the event check in list.
     * @param eventID
     * eventID: An event's ID.
     * @param profileID
     * profileID: An profile's ID.
     */
    public void addEventCheckInProfile(String eventID, String profileID) {
        getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                String count = getEventCheckInProfileCount(docSns, profileID);
                if (Objects.equals(count, "-1")) {
                    count = "00000001";
                    String data = dt.constructIDCountString(profileID, count);
                    eColRef.document(eventID).update("eCheckInProfiles", FieldValue.arrayUnion(data));
                    ds.newCheckInProfileEvent(profileID, eventID, count);
                } else {
                    String data = dt.constructIDCountString(profileID, count);
                    String nextCount = dt.calculateAddOne(count);
                    String nextData = dt.constructIDCountString(profileID, nextCount);
                    eColRef.document(eventID).update("eCheckInProfiles", FieldValue.arrayRemove(data));
                    eColRef.document(eventID).update("eCheckInProfiles", FieldValue.arrayUnion(nextData));
                    ds.addCheckInProfileEvent(profileID, eventID, count, nextCount);
                }
            }
        });
    }
}
