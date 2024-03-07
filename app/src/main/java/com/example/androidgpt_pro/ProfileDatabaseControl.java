package com.example.androidgpt_pro;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This is a class that controls the interaction between user profile data and the database.
 */
public class ProfileDatabaseControl {

    private FirebaseFirestore db;
    private DocumentReference pDocRef;

    private String pID;
    private String pName;
    private String pRole;
    private String pPhoneNumber;
    private String pEmail;
    private ArrayList<String> pEvents;


    /**
     * This is the constructor of class UserProfileDbCtrl.
     * @param profileID
     * profileID: String
     * A unique profile ID, which will index an entire user's profile.
     */
    public ProfileDatabaseControl(String profileID) {
        pID = profileID;
        db = FirebaseFirestore.getInstance();
        pDocRef = db.collection("Profile").document(pID);
    }


    /**
     * This is the initializer of user's profile.
     * @param profileRole
     * profileRole: The role of the user, one of Administrator/Attendee/Organizer.
     */
    public void initProfile(String profileRole) {
        pRole = profileRole;
        HashMap<String, Object> data = new HashMap<>();
        data.put("pName", "NewUser");
        data.put("pRole", pRole);
        data.put("pPhoneNumber", pPhoneNumber);
        data.put("pEmail", pEmail);
        data.put("pEvents", pEvents);
        pDocRef.set(data);
    }


    /**
     * This is a getter for Profile Name.
     * @return profileName
     * profileName: A user's name.
     */
    public String getProfileName() {
        downloadData();
        return pName;
    }

    /**
     * This is a getter for Profile Phone Number.
     * @return profilePhoneNumber
     * profilePhoneNumber: A user's phone number.
     */
    public String getProfilePhoneNumber() {
        downloadData();
        return pPhoneNumber;
    }

    /**
     * This is a getter for Profile Email.
     * @return profileEmail
     * profileEmail: A user's email.
     */
    public String getProfileEmail() {
        downloadData();
        return pEmail;
    }


    /**
     * This is a setter for Profile Name.
     * @param profileName
     * profileName: A user's name.
     */
    public void setProfileName(String profileName) {
        pDocRef.update("pName", profileName);
    }

    /**
     * This is a setter for Profile Phone Number.
     * @param profilePhoneNumber
     * profilePhoneNumber: A user's phone number.
     */
    public void setProfilePhoneNumber(String profilePhoneNumber) {
        pDocRef.update("pPhoneNumber", profilePhoneNumber);
    }

    /**
     * This is a setter for Profile Email.
     * @param profileEmail
     * profileEmail: A user's email.
     */
    public void setProfileEmail(String profileEmail) {
        pDocRef.update("pEmail", profileEmail);
    }


    /**
     * This is an adder used to add the given event ID to the user profile.
     * @param eventID
     * eventID: The ID of the event that needs to be added.
     */
    public void addProfileEvent(String eventID) {
        pDocRef.update("pEvents", FieldValue.arrayUnion(eventID));
    }

    /**
     * This is a deleter that can remove event that need to de-registration.
     * @param eventID
     * eventID: The ID of the event that needs to be deleted.
     */
    public void delProfileEvent(String eventID) {
        pDocRef.update("pEvents", FieldValue.arrayRemove(eventID));
    }


    private void downloadData() {
        pDocRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                pName = documentSnapshot.getString("pName");
                pPhoneNumber = documentSnapshot.getString("pPhoneNumber");
                pEmail = documentSnapshot.getString("pEmail");
            }
        });
    }
}
