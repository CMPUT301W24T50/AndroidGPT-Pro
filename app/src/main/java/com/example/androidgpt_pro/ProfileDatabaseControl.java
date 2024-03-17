package com.example.androidgpt_pro;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a class that controls the interaction between profile data and the database.
 */
public class ProfileDatabaseControl {

    private FirebaseFirestore db;
    private DocumentReference pDocRef;
    private DatabaseSynchronization ds;
    private DatabaseTools dt;

    private String pID;
    private String pName;
    private String pRole;
    private String pPhoneNumber;
    private String pEmail;
    private Boolean pGLTState = Boolean.TRUE;
    private ArrayList<String> pSignUpEvents;
    private ArrayList<String> pCheckInEvents;


    /**
     * This is the constructor of class ProfileDatabaseControl.
     * @param profileID
     * profileID: String
     * A unique profile ID, which will index an entire profile.
     */
    public ProfileDatabaseControl(String profileID) {
        pID = profileID;
        db = FirebaseFirestore.getInstance();
        pDocRef = db.collection("Profile").document(pID);
        ds = new DatabaseSynchronization();
        dt = new DatabaseTools();
    }


    /**
     * This is the initializer of a profile.
     * @param profileRole
     * profileRole: The role of the profile, one of Administrator/Attendee/Organizer.
     */
    public void initProfile(String profileRole) {
        pRole = profileRole;
        HashMap<String, Object> data = new HashMap<>();
        data.put("pName", "NewUser");
        data.put("pRole", pRole);
        data.put("pPhoneNumber", pPhoneNumber);
        data.put("pEmail", pEmail);
        data.put("pGLTState", pGLTState);
        data.put("pSignUpEvents", pSignUpEvents);
        data.put("pCheckInEvents", pCheckInEvents);
        pDocRef.set(data);
    }


    /**
     * This is a getter for a profile snapshot.
     * @return profileSnapshotGetTask
     * profileSnapshotGetTask: A task for getting profileDocumentSnapshot.
     */
    public Task<DocumentSnapshot> getProfileSnapshot() {
        return pDocRef.get();
    }

    /**
     * This is a getter for a profile.
     * @return profile
     * profile: A profile document.
     */
    public DocumentReference getProfile() {
        return pDocRef;
    }


    /**
     * This is a getter for Profile Name.
     * @param profileDocumentSnapshot
     * profileDocumentSnapshot: A profile document snapshot.
     * @return profileName
     * profileName: A profile's name.
     */
    public String getProfileName(DocumentSnapshot profileDocumentSnapshot) {
        return profileDocumentSnapshot.getString("pName");
    }

    /**
     * This is a setter for Profile Name.
     * @param profileName
     * profileName: A profile's name.
     */
    public void setProfileName(String profileName) {
        pDocRef.update("pName", profileName);
    }


    /**
     * This is a getter for Profile Phone Number.
     * @param profileDocumentSnapshot
     * profileDocumentSnapshot: A profile document snapshot.
     * @return profilePhoneNumber
     * profilePhoneNumber: A profile's phone number.
     */
    public String getProfilePhoneNumber(DocumentSnapshot profileDocumentSnapshot) {
        return profileDocumentSnapshot.getString("pPhoneNumber");
    }

    /**
     * This is a setter for Profile Phone Number.
     * @param profilePhoneNumber
     * profilePhoneNumber: A profile's phone number.
     */
    public void setProfilePhoneNumber(String profilePhoneNumber) {
        pDocRef.update("pPhoneNumber", profilePhoneNumber);
    }


    /**
     * This is a getter for Profile Email.
     * @param profileDocumentSnapshot
     * profileDocumentSnapshot: A profile document snapshot.
     * @return profileEmail
     * profileEmail: A profile's email.
     */
    public String getProfileEmail(DocumentSnapshot profileDocumentSnapshot) {
        return profileDocumentSnapshot.getString("pEmail");
    }

    /**
     * This is a setter for Profile Email.
     * @param profileEmail
     * profileEmail: A profile's email.
     */
    public void setProfileEmail(String profileEmail) {
        pDocRef.update("pEmail", profileEmail);
    }


    /**
     * This is a getter for Profile Geo-Location Tracking State.
     * @param profileDocumentSnapshot
     * profileDocumentSnapshot: A profile document snapshot.
     * @return profileGLTState
     * profileGLTState: A state of Geo-Location Tracking.
     */
    public Boolean getProfileGLTState(DocumentSnapshot profileDocumentSnapshot) {
        return profileDocumentSnapshot.getBoolean("pGLTState");
    }

    /**
     * This is a setter for Profile Geo-Location Tracking State.
     * @param profileGLTState
     * profileGLTState: A state of Geo-Location Tracking.
     */
    public void setProfileGLTState(Boolean profileGLTState) {
        pDocRef.update("pGLTState", profileGLTState);
    }


    /**
     * This is a getter for Profile SignUp Events.
     * @param profileDocumentSnapshot
     * profileDocumentSnapshot: A profile document snapshot.
     * @return profileSignUpEvents
     * profileSignUpEvents: A list of eventID.
     */
    public ArrayList<String> getProfileAllSignUpEvents(DocumentSnapshot profileDocumentSnapshot) {
        return (ArrayList<String>) profileDocumentSnapshot.get("pSignUpEvents");
    }

    /**
     * This is an adder used to add the given event ID to the profile sign up list.
     * @param eventID
     * eventID: The ID of the event that needs to be added.
     */
    public void addProfileSignUpEvent(String eventID) {
        pDocRef.update("pSignUpEvents", FieldValue.arrayUnion(eventID));
        ds.addSignUpEventProfile(eventID, pID);
    }

    /**
     * This is a deleter that can remove event that need to cancel sign up.
     * @param eventID
     * eventID: The ID of the event that needs to be deleted.
     */
    public void delProfileSignUpEvent(String eventID) {
        pDocRef.update("pSignUpEvents", FieldValue.arrayRemove(eventID));
        ds.delSignUpEventProfile(eventID, pID);
    }


    /**
     * This is a getter for Profile CheckIn Events.
     * @param profileDocumentSnapshot
     * profileDocumentSnapshot: A profile document snapshot.
     * @return profileCheckInEvents
     * profileCheckInEvents: A "2D Array" list of eventID and count, null if no profile.
     */
    public String[][] getProfileAllCheckInEvent(DocumentSnapshot profileDocumentSnapshot) {
        ArrayList<String> data = (ArrayList<String>) profileDocumentSnapshot.get("pCheckInEvents");
        String[][] lst = new String[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            lst[i] = data.get(i).split("#");
        }
        return lst;
    }

    /**
     * This is an adder used to add the given eventID to the profile check in list.
     * @param eventID
     * eventID: An event's ID.
     * @param count
     * count: The number of times a given profile has been checked in.
     */
    public void addProfileCheckInEvent(String eventID, String count) {
        String data = dt.constructIDCountString(eventID, count);
        String nextCount = dt.calculateAddOne(count);
        String nextData = dt.constructIDCountString(eventID, nextCount);
        pDocRef.update("pCheckInEvents", FieldValue.arrayRemove(data));
        pDocRef.update("pCheckInEvents", FieldValue.arrayUnion(nextData));
        ds.addCheckInEventProfile(eventID, pID, count, nextCount);
    }
}
