package com.example.androidgpt_pro;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

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
        String data = dt.constructSharpString(eventID, count, null);
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
        String data = dt.constructSharpString(eventID, count, null);
        pColRef.document(profileID)
            .update("pCheckInEvents", FieldValue.arrayRemove(data));
        String nextData = dt.constructSharpString(eventID, nextCount, null);
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
        String data = dt.constructSharpString(profileID, count, null);
        eColRef.document(eventID)
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
        String data = dt.constructSharpString(profileID, count, null);
        eColRef.document(eventID)
            .update("eCheckInProfiles", FieldValue.arrayRemove(data));
        String nextData = dt.constructSharpString(profileID, nextCount, null);
        eColRef.document(eventID)
            .update("eCheckInProfiles", FieldValue.arrayUnion(nextData));
    }


    /**
     * This is a setter to synchronize the profile database for notification records.
     * @param eventID
     * eventID: An event's ID.
     * @param totalNotificationNumber
     * totalNotificationNumber: Total number of notifications for the specific event.
     */
    public void setNotificationEventProfile(String eventID, String totalNotificationNumber) {
        nepGetInfoFromED(eventID, totalNotificationNumber);
    }

    private void nepGetInfoFromED(String eventID, String tnn) {
        EventDatabaseControl edc = new EventDatabaseControl();
        edc.getEventSnapshot(eventID)
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot docSns) {
                    nepHandleInfo(edc, eventID, docSns, tnn);
                }
            });
    }

    private void nepHandleInfo(EventDatabaseControl edc,
                               String eventID,
                               DocumentSnapshot docSns,
                               String tnn) {
        if (edc.getEventAllCheckInProfiles(docSns) != null) {
            String[][] tmp = edc.getEventAllCheckInProfiles(docSns);
            for (String[] strings : tmp) {
                nepGetInfoFromPD(strings[0], eventID, tnn);
            }
        }
    }

    private void nepGetInfoFromPD(String profileID, String eventID, String tnn) {
        ProfileDatabaseControl pdc = new ProfileDatabaseControl(profileID);
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                nepSetNtfRec(pdc, profileID, docSns, eventID, tnn);
            }
        });
    }

    private void nepSetNtfRec(ProfileDatabaseControl pdc,
                              String profileID,
                              DocumentSnapshot docSns,
                              String eventID,
                              String tnn) {
        if (pdc.getProfileAllNotificationRecords(docSns) == null) {
            String newRecord = dt.constructSharpString(eventID, tnn, "-1");
            pColRef.document(profileID)
                    .update("pNotificationsRecords", FieldValue.arrayUnion(newRecord));
            return;
        }
        String[][] tmp = pdc.getProfileAllNotificationRecords(docSns);
        for (int i = 0; i < tmp.length; i++) {
            if (Objects.equals(tmp[i][0], eventID)) {
                String oldRecord = dt.constructSharpString(tmp[i][0], tmp[i][1], tmp[i][2]);
                tmp[i][1] = dt.calculateAddOne(tmp[i][1]);
                String newRecord = dt.constructSharpString(tmp[i][0], tnn, tmp[i][2]);
                pColRef.document(profileID)
                    .update("pNotificationsRecords", FieldValue.arrayRemove(oldRecord));
                pColRef.document(profileID)
                    .update("pNotificationsRecords", FieldValue.arrayUnion(newRecord));
                break;
            }
        }
    }
}
