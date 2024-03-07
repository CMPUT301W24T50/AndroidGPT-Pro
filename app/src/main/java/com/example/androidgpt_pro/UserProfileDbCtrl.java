package com.example.androidgpt_pro;

import android.media.Image;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;

/**
 * This is a class that controls the interaction between user profile data and the database.
 */
public class UserProfileDbCtrl {

    private FirebaseFirestore db;
    private CollectionReference upfRef;

    private String upfID;
    private String upfName;
    private String upfPhoneNumber;
    private String upfEmail;


    /**
     * This is the constructor of class UserProfileDbCtrl.
     * @param profileID
     * profileID: String
     * A unique profile ID, which will index an entire user's profile.
     */
    public UserProfileDbCtrl(String profileID) {
        db = FirebaseFirestore.getInstance();
        upfRef = db.collection("UserProfile");
        upfID = profileID;
    }


    /**
     * This is a setter for Profile Name.
     * @param profileName
     * profileName: A user's name.
     */
    public void setProfileName(String profileName) {
        downloadData();
        upfName = profileName;
        uploadData();
    }

    /**
     * This is a setter for Profile Phone Number.
     * @param profilePhoneNumber
     * profilePhoneNumber: A user's phone number.
     */
    public void setProfilePhoneNumber(String profilePhoneNumber) {
        downloadData();
        upfPhoneNumber = profilePhoneNumber;
        uploadData();
    }

    /**
     * This is a setter for Profile Email.
     * @param profileEmail
     * profileEmail: A user's email.
     */
    public void setProfileEmail(String profileEmail) {
        downloadData();
        upfEmail = profileEmail;
        uploadData();
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


    private void uploadData() {
        HashMap<String, String> data = new HashMap<>();
        data.put("upfName", upfName);
        data.put("upfPhoneNumber", upfPhoneNumber);
        data.put("upfEmail", upfEmail);
        upfRef.document(upfID).set(data);
    }

    private void downloadData() {
        upfRef.document(upfID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                upfName = documentSnapshot.getString("upfName");
                upfPhoneNumber = documentSnapshot.getString("upfPhoneNumber");
                upfEmail = documentSnapshot.getString("upfEmail");
            }
        });
    }
}
