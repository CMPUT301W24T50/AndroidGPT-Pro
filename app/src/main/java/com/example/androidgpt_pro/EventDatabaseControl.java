package com.example.androidgpt_pro;

import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * This is a class that controls the interaction between event data and the database.
 */
@SuppressWarnings("unchecked")
public class EventDatabaseControl {

    private FirebaseFirestore db;
    private FirebaseStorage st;
    private CollectionReference eColRef;
    private StorageReference eStgRef;
    private DatabaseSynchronization ds;
    private DatabaseTools dt;

    private ArrayList<String> eSignUpProfiles;
    private ArrayList<String> eCheckInProfiles;
    private ArrayList<String> eCheckInLocations;
    private ArrayList<String> eNotifications;


    /**
     * This is the constructor of class EventDatabaseControl.
     */
    public EventDatabaseControl() {
        db = FirebaseFirestore.getInstance();
        eColRef = db.collection("Event");
        st = FirebaseStorage.getInstance();
        eStgRef = st.getReference().child("Event");
        ds = new DatabaseSynchronization();
        dt = new DatabaseTools();
    }


    /**
     * This is the initializer of an event.
     * @param eventID
     * eventID: The ID of the event.
     * @param eventOrganizerID
     * eventOrganizerID: The organizer's profileID.
     * @param eventName
     * eventName: An event's name.
     * @param eventLocationStreet
     * eventLocationStreet: An event's street location.
     * @param eventLocationCity
     * eventLocationCity: An event's city location.
     * @param eventLocationProvince
     * eventLocationProvince: An event's province location.
     * @param eventTime
     * eventTime: An event's time.
     * @param eventDate
     * eventDate: An event's date.
     * @param eventDescription
     * eventDescription: An event's description.
     * @param eventSignUpLimit
     * eventSignUpLimit: An limit for sign up.
     * @param eventGeoLocationTrackingState
     * eventGeoLocationTrackingState: A state of Geo-Location Tracking.
     * @param eventImageUri
     * eventImageUri: The URI of an image.
     */
    public void initEvent(String eventID,
                          String eventOrganizerID,
                          String eventName,
                          String eventLocationStreet,
                          String eventLocationCity,
                          String eventLocationProvince,
                          String eventTime,
                          String eventDate,
                          String eventDescription,
                          String eventSignUpLimit,
                          Boolean eventGeoLocationTrackingState,
                          Uri eventImageUri) {
        HashMap<String, Object> data = new HashMap<>();
        data.put("eOrganizerID", eventOrganizerID);
        data.put("eName", eventName);
        data.put("eLocStreet", eventLocationStreet);
        data.put("eLocCity", eventLocationCity);
        data.put("eLocProvince", eventLocationProvince);
        data.put("eTime", eventTime);
        data.put("eDate", eventDate);
        data.put("eDescription", eventDescription);
        data.put("eGLTState", eventGeoLocationTrackingState);
        data.put("eSignUpLimit", eventSignUpLimit);
        data.put("eSignUpProfiles", eSignUpProfiles);
        data.put("eCheckInProfiles", eCheckInProfiles);
        data.put("eCheckInLocations", eCheckInLocations);
        data.put("eNotifications", eNotifications);
        data.put("eImageUpdated", Boolean.FALSE);
        eColRef.document(eventID).set(data);
        setEventImage(eventID, eventImageUri);
        ds.addOrganizedProfileEvent(eventOrganizerID, eventID);
    }


    /**
     * This is an event deleter.
     * @param eventID
     * eventID: An ID of an event.
     */
    public void removeEvent(String eventID) {
        getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                ds.delSignUpAllProfileEvent(getEventAllSignUpProfiles(docSns), eventID);
                ds.delCheckInAllProfileEvent(getEventAllCheckInProfiles(docSns), eventID);
                ds.delOrganizedProfileEvent(getEventOrganizerID(docSns), eventID);
                eColRef.document(eventID).delete();
                eStgRef.child(eventID).delete();
            }
        });
    }


    /**
     * This is a getter for a snapshot of the EventStat.
     * @return eventStatSnapshotGetTask
     * eventStatSnapshotGetTask: A task for getting eventStatSnapshot.
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
     * This is a getter for Event Organizer Profile ID.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventOrganizerID
     * eventOrganizerID: The ID of the Event Organizer.
     */
    public String getEventOrganizerID(DocumentSnapshot eventDocumentSnapshot) {
        return eventDocumentSnapshot.getString("eOrganizerID");
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
     * This is a getter for Event Street Location.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventLocationStreet
     * eventLocationStreet: The street location of the event.
     */
    public String getEventLocationStreet(DocumentSnapshot eventDocumentSnapshot) {
        return eventDocumentSnapshot.getString("eLocStreet");
    }

    /**
     * This is a setter for Event Street Location.
     * @param eventID
     * eventID: An ID of an event.
     * @param eventLocationStreet
     * eventLocationStreet: An event's street location.
     */
    public void setEventLocationStreet(String eventID, String eventLocationStreet) {
        eColRef.document(eventID).update("eLocStreet", eventLocationStreet);
    }


    /**
     * This is a getter for Event City Location.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventLocationCity
     * eventLocationCity: The city location of the event.
     */
    public String getEventLocationCity(DocumentSnapshot eventDocumentSnapshot) {
        return eventDocumentSnapshot.getString("eLocCity");
    }

    /**
     * This is a setter for Event City Location.
     * @param eventID
     * eventID: An ID of an event.
     * @param eventLocationCity
     * eventLocationCity: An event's city location.
     */
    public void setEventLocationCity(String eventID, String eventLocationCity) {
        eColRef.document(eventID).update("eLocCity", eventLocationCity);
    }


    /**
     * This is a getter for Event Province Location.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventLocationProvince
     * eventLocationProvince: The province location of the event.
     */
    public String getEventLocationProvince(DocumentSnapshot eventDocumentSnapshot) {
        return eventDocumentSnapshot.getString("eLocProvince");
    }

    /**
     * This is a setter for Event Province Location.
     * @param eventID
     * eventID: An ID of an event.
     * @param eventLocationProvince
     * eventLocationProvince: An event's province location.
     */
    public void setEventLocationProvince(String eventID, String eventLocationProvince) {
        eColRef.document(eventID).update("eLocProvince", eventLocationProvince);
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
     * This is a getter for Event Geo-Location Tracking State.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventGLTState
     * eventGLTState: A state of Geo-Location Tracking.
     */
    public Boolean getEventGLTState(DocumentSnapshot eventDocumentSnapshot) {
        return eventDocumentSnapshot.getBoolean("eGLTState");
    }

    /**
     * This is a setter for Event Geo-Location Tracking State.
     * @param eventID
     * eventID: An ID of an event.
     * @param eventGLTState
     * eventGLTState: A state of Geo-Location Tracking.
     */
    public void setEventGLTState(String eventID, Boolean eventGLTState) {
        eColRef.document(eventID).update("eGLTState", eventGLTState);
    }


    /**
     * This is a getter for Event Image Updated State.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventImageUpdatedState
     * eventImageUpdatedState: A state of Image Updated.
     */
    public Boolean getEventImageUpdatedState(DocumentSnapshot eventDocumentSnapshot) {
        return eventDocumentSnapshot.getBoolean("eImageUpdated");
    }

    /**
     * This is a reset function for Event Image Updated State.
     */
    public void resetEventImageUpdatedState(String eventID) {
        eColRef.document(eventID).update("eImageUpdated", Boolean.FALSE);
    }

    /**
     * This is a getter for Event Image.
     * @param eventID
     * eventID: An ID of an event.
     * @return eventImageGetTask
     * eventImageGetTask: A task for getting eventImage.
     */
    public Task<Uri> getEventImage(String eventID) {
        File localFile = null;
        try {
            localFile = File.createTempFile("E" + eventID, "jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return eStgRef.child(eventID).getDownloadUrl();
    }

    /**
     * This is a setter for Event Image.
     * @param eventID
     * eventID: An ID of an event.
     * @param eventImageURI
     * eventImageURI: The URI of an image.
     */
    public void setEventImage(String eventID, Uri eventImageURI) {
        eStgRef.child(eventID).putFile(eventImageURI);
    }

    /**
     * This is a deleter for Event Image.
     */
    public void delEventImage(String eventID) {
        eStgRef.child(eventID).delete();
        eColRef.document(eventID).update("eImageUpdated", Boolean.TRUE);
    }


    /**
     * This is a getter for event sign up limit.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventSignUpLimit
     * eventSignUpLimit: The limit for sign up.
     */
    public String getEventSignUpLimit(DocumentSnapshot eventDocumentSnapshot) {
        if (Objects.equals(eventDocumentSnapshot.getString("eSignUpLimit"), ""))
            return "100000000";
        return eventDocumentSnapshot.getString("eSignUpLimit");
    }

    /**
     * This is a getter for Event SignUp Profiles.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventSignUpProfiles
     * eventSignUpProfiles: A list of profileID, null if no profile.
     */
    public ArrayList<String> getEventAllSignUpProfiles(DocumentSnapshot eventDocumentSnapshot) {
        if (eventDocumentSnapshot.get("eSignUpProfiles") == null)
            return null;
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
     * eventCheckInProfiles: A "2D Array" list of profileID and count, null if no profile.
     */
    public String[][] getEventAllCheckInProfiles(DocumentSnapshot eventDocumentSnapshot) {
        if (eventDocumentSnapshot.get("eCheckInProfiles") == null)
            return null;
        ArrayList<String> data = (ArrayList<String>) eventDocumentSnapshot.get("eCheckInProfiles");
        String[][] lst = new String[data.size()][];
        for (int i = 0; i < data.size(); i++)
            lst[i] = dt.splitSharpString(data.get(i));
        return lst;
    }

    private String getEventCheckInProfileCount(DocumentSnapshot eventDocumentSnapshot,
                                             String profileID) {
        if (eventDocumentSnapshot.get("eCheckInProfiles") == null)
            return null;
        ArrayList<String> data = (ArrayList<String>) eventDocumentSnapshot.get("eCheckInProfiles");
        for (int i = 0; i < data.size(); i++) {
            if (Objects.equals(dt.splitSharpString(data.get(i))[0], profileID))
                return dt.splitSharpString(data.get(i))[1];
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
                if (getEventCheckInProfileCount(docSns, profileID) == null) {
                    String count = "00000001";
                    String data = dt.constructSharpString(profileID, count, null);
                    eColRef.document(eventID).update("eCheckInProfiles",
                        FieldValue.arrayUnion(data));
                    ds.newCheckInProfileEvent(profileID, eventID, count);
                } else {
                    String count = getEventCheckInProfileCount(docSns, profileID);
                    String data = dt.constructSharpString(profileID, count, null);
                    String nextCount = dt.calculateAddOne(count);
                    String nextData = dt.constructSharpString(profileID, nextCount, null);
                    eColRef.document(eventID).update("eCheckInProfiles",
                        FieldValue.arrayRemove(data));
                    eColRef.document(eventID).update("eCheckInProfiles",
                        FieldValue.arrayUnion(nextData));
                    ds.addCheckInProfileEvent(profileID, eventID, count, nextCount);
                }
            }
        });
    }


    /**
     * This is a getter for Event Check In Locations.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventCheckInLocations
     * eventCheckInLocations: A "2D Array" list of Latitude and Longitude, null if no location.
     */
    public String[][] getEventAllCheckInLocations(DocumentSnapshot eventDocumentSnapshot) {
        if (eventDocumentSnapshot.get("eCheckInLocations") == null)
            return null;
        ArrayList<String> data = (ArrayList<String>) eventDocumentSnapshot.get("eCheckInLocations");
        String[][] lst = new String[data.size()][];
        for (int i = 0; i < data.size(); i++)
            lst[i] = dt.splitSharpString(data.get(i));
        return lst;
    }

    /**
     * This is an adder used to add the given location to the event check in list.
     * @param eventID
     * eventID: An event's ID.
     * @param latitude
     * latitude: A part of location.
     * @param longitude
     * longitude: A part of location.
     */
    public void addEventCheckInLocation(String eventID, String latitude, String longitude) {
        String location = dt.constructSharpString(latitude, longitude, null);
        eColRef.document(eventID).update("eCheckInLocations", FieldValue.arrayUnion(location));
    }


    /**
     * This is a getter for Event Notifications.
     * @param eventDocumentSnapshot
     * eventDocumentSnapshot: An event document snapshot.
     * @return eventNotifications
     * eventNotifications: A list of Notification, null if no notification.
     */
    public ArrayList<String> getEventAllNotifications(DocumentSnapshot eventDocumentSnapshot) {
        if (eventDocumentSnapshot.get("eNotifications") == null)
            return null;
        ArrayList<String> orgNtf = (ArrayList<String>) eventDocumentSnapshot.get("eNotifications");
        ArrayList<String> mdfNtf = new ArrayList<>();
        for (int i = 0; i < orgNtf.size(); i++)
            mdfNtf.add(dt.splitSharpString(orgNtf.get(i))[1]);
        return mdfNtf;
    }

    /**
     * This is an adder used to add the given notification.
     * @param eventID
     * eventID: An event's ID.
     * @param notification
     * notification: The notification that need to be sent.
     */
    public void addEventNotification(String eventID, String notification) {
        getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                String counter;
                if (docSns.get("eNotifications") == null) {
                    counter = dt.formatEightBits(0);
                } else {
                    int arraySize = (((ArrayList<String>) docSns.get("eNotifications")).size());
                    counter = dt.formatEightBits(arraySize);
                }
                String dbNtf = dt.constructSharpString(counter, notification, null);
                eColRef.document(eventID)
                        .update("eNotifications", FieldValue.arrayUnion(dbNtf));
                ds.setNotificationEventProfile(eventID, counter);
            }
        });
    }


    /**
     * This is a requester for all events.
     * @return eventsSnapshotGetTask
     * eventsSnapshotGetTask: A task for getting eventQueryDocumentSnapshots.
     */
    public Task<QuerySnapshot> requestAllEvents() {
        return db.collection("Event").get();
    }

    /**
     * This is a getter for all Event IDs.
     * @param eventQueryDocumentSnapshots
     * eventQueryDocumentSnapshots: The Event Query Document Snapshots.
     * @return allEventID
     * allEventID: A list contains all Event IDs.
     */
    public String[] getAllEventID(QuerySnapshot eventQueryDocumentSnapshots) {
        if (eventQueryDocumentSnapshots == null)
            return null;
        int colSize = eventQueryDocumentSnapshots.getDocuments().size();
        String[] allEventID = new String[colSize - 1];
        for (int i = 1; i < colSize; i++)
            allEventID[i - 1] = eventQueryDocumentSnapshots.getDocuments().get(i).getId();
        return allEventID;
    }

    /**
     * This is a getter for all Future Event IDs.
     * @param eventQueryDocumentSnapshots
     * eventQueryDocumentSnapshots: The Event Query Document Snapshots.
     * @return allFutureEventID
     * allFutureEventID: A list contains all Future Event IDs.
     */
    public String[] getAllFutureEventID(QuerySnapshot eventQueryDocumentSnapshots) {
        if (eventQueryDocumentSnapshots == null)
            return null;
        int colSize = eventQueryDocumentSnapshots.getDocuments().size();
        ArrayList<String> alAllFutureEventID = new ArrayList<>();
        for (int i = 1; i < colSize; i++) {
            String eventDate = eventQueryDocumentSnapshots
                    .getDocuments().get(i).getString("eDate");
            if (dt.compareDateBigOrEqual(eventDate, dt.getDateToday()))
                alAllFutureEventID.add(eventQueryDocumentSnapshots.getDocuments().get(i).getId());
        }
        return alAllFutureEventID.toArray(new String[0]);
    }
}
