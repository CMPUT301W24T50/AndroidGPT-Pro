package com.example.androidgpt_pro;

import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.ref.Reference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * This is a class that controls the interaction between user profile data and the database.
 */
public class UserProfileDbCtrl {

    private FirebaseFirestore db;
    private DocumentReference upfRef;

    private String upfID;
    private String upfName;
    private String upfRole;
    private String upfPhoneNumber;
    private String upfEmail;
    private ArrayList<String> upfEvents;


    /**
     * This is the constructor of class UserProfileDbCtrl.
     * @param profileID
     * profileID: String
     * A unique profile ID, which will index an entire user's profile.
     */
    public UserProfileDbCtrl(String profileID) {
        upfID = profileID;
        db = FirebaseFirestore.getInstance();
        upfRef = db.collection("UserProfile").document(upfID);
    }


    /**
     * This is the initializer of user's profile.
     * @param profileRole
     * profileRole: The role of the user, one of Administrator/Attendee/Organizer.
     */
    public void initProfile(String profileRole) {
        upfRole = profileRole;
        HashMap<String, Object> data = new HashMap<>();
        data.put("upfName", "NewUser");
        data.put("upfRole", upfRole);
        data.put("upfPhoneNumber", upfPhoneNumber);
        data.put("upfEmail", upfEmail);
        data.put("upfEvents", upfEvents);
        upfRef.set(data);
    }


    /**
     * This is a setter for Profile Name.
     * @param profileName
     * profileName: A user's name.
     */
    public void setProfileName(String profileName) {
        upfRef.update("upfName", profileName);
    }

    /**
     * This is a setter for Profile Phone Number.
     * @param profilePhoneNumber
     * profilePhoneNumber: A user's phone number.
     */
    public void setProfilePhoneNumber(String profilePhoneNumber) {
        upfRef.update("upfPhoneNumber", profilePhoneNumber);
    }

    /**
     * This is a setter for Profile Email.
     * @param profileEmail
     * profileEmail: A user's email.
     */
    public void setProfileEmail(String profileEmail) {
        upfRef.update("upfEmail", profileEmail);
    }


    /**
     * This is a getter for Profile Name.
     * @return profileName
     * profileName: A user's name.
     */
    public String getProfileName() {
        downloadData();
        return upfName;
    }

    /**
     * This is a getter for Profile Phone Number.
     * @return profilePhoneNumber
     * profilePhoneNumber: A user's phone number.
     */
    public String getProfilePhoneNumber() {
        downloadData();
        return upfPhoneNumber;
    }

    /**
     * This is a getter for Profile Email.
     * @return profileEmail
     * profileEmail: A user's email.
     */
    public String getProfileEmail() {
        downloadData();
        return upfEmail;
    }


    /**
     * This is an adder used to add the given event ID to the user profile.
     * @param eventID
     * eventID: The ID of the event that needs to be added.
     */
    public void addProfileEvent(String eventID) {
        upfRef.update("upfEvents", FieldValue.arrayUnion(eventID));
    }

    /**
     * This is a deleter that can remove event that need to de-registration.
     * @param eventID
     * eventID: The ID of the event that needs to be deleted.
     */
    public void delProfileEvent(String eventID) {
        upfRef.update("upfEvents", FieldValue.arrayRemove(eventID));
    }


    private void downloadData() {
        upfRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                upfName = documentSnapshot.getString("upfName");
                upfPhoneNumber = documentSnapshot.getString("upfPhoneNumber");
                upfEmail = documentSnapshot.getString("upfEmail");
            }
        });
    }
}
