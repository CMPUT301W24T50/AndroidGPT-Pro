package com.example.androidgpt_pro;

import com.google.android.gms.tasks.OnSuccessListener;
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
     * This is a getter for Profile Name.
     * @return profileName
     * profileName: A profile's name.
     */
    public String getProfileName() {
        downloadData();
        return pName;
    }

    /**
     * This is a getter for Profile Phone Number.
     * @return profilePhoneNumber
     * profilePhoneNumber: A profile's phone number.
     */
    public String getProfilePhoneNumber() {
        downloadData();
        return pPhoneNumber;
    }

    /**
     * This is a getter for Profile Email.
     * @return profileEmail
     * profileEmail: A profile's email.
     */
    public String getProfileEmail() {
        downloadData();
        return pEmail;
    }

    /**
     * This is a getter for Profile Geo-Location Tracking State.
     * @return profileGLTState
     * profileGLTState: A state of Geo-Location Tracking.
     */
    public Boolean getProfileGLTState() {
        downloadData();
        return pGLTState;
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
     * This is a setter for Profile Phone Number.
     * @param profilePhoneNumber
     * profilePhoneNumber: A profile's phone number.
     */
    public void setProfilePhoneNumber(String profilePhoneNumber) {
        pDocRef.update("pPhoneNumber", profilePhoneNumber);
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
     * This is a setter for Profile Geo-Location Tracking State.
     * @param profileGLTState
     * profileGLTState: A state of Geo-Location Tracking.
     */
    public void setProfileGLTState(Boolean profileGLTState) {
        pDocRef.update("pGLTState", profileGLTState);
    }


    /**
     * This is an adder used to add the given event ID to the profile sign up list.
     * @param eventID
     * eventID: The ID of the event that needs to be added.
     */
    public void addProfileSignUpEvent(String eventID) {
        pDocRef.update("pSignUpEvents", FieldValue.arrayUnion(eventID));
    }

    /**
     * This is a deleter that can remove event that need to cancel sign up.
     * @param eventID
     * eventID: The ID of the event that needs to be deleted.
     */
    public void delProfileSignUpEvent(String eventID) {
        pDocRef.update("pSignUpEvents", FieldValue.arrayRemove(eventID));
    }

    /**
     * This is an adder used to add the given event ID to the profile check in list.
     * @param eventID
     * eventID: The ID of the event that needs to be added.
     */
    public void addProfileCheckInEvent(String eventID) {
        pDocRef.update("pCheckInEvents", FieldValue.arrayUnion(eventID));
    }


    private void downloadData() {
        pDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                pName = documentSnapshot.getString("pName");
                pPhoneNumber = documentSnapshot.getString("pPhoneNumber");
                pEmail = documentSnapshot.getString("pEmail");
                pGLTState = documentSnapshot.getBoolean("pGLTState");
            }
        });
    }
}
