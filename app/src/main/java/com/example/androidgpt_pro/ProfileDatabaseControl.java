package com.example.androidgpt_pro;

import android.net.Uri;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

/**
 * This is a class that controls the interaction between profile data and the database.
 */
@SuppressWarnings("unchecked")
public class ProfileDatabaseControl {

    private FirebaseFirestore db;
    private FirebaseStorage st;
    private DocumentReference pDocRef;
    private StorageReference pStgRef;
    private DatabaseSynchronization ds;
    private DatabaseTools dt;

    private String pID;
    private String pRole;
    private String pName;
    private String pPhoneNumber;
    private String pEmail;
    private ArrayList<String> pSignUpEvents;
    private ArrayList<String> pCheckInEvents;
    private ArrayList<String> pOrganizedEvents;
    private ArrayList<String> pNotificationsRecords;


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
        st = FirebaseStorage.getInstance();
        pStgRef = st.getReference().child("Profile");
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
        data.put("pSignUpEvents", pSignUpEvents);
        data.put("pCheckInEvents", pCheckInEvents);
        data.put("pOrganizedEvents", pOrganizedEvents);
        data.put("pNotificationsRecords", pNotificationsRecords);
        data.put("pGLTState", Boolean.TRUE);
        data.put("pImageUpdated", Boolean.FALSE);
        pDocRef.set(data);
    }

    /**
     * This is a profile deleter.
     */
    public void delProfile() {
        getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                ds.delSignUpAllEventProfile(getProfileAllSignUpEvents(docSns), pID);
                ds.delCheckInAllEventProfile(getProfileAllCheckInEvent(docSns), pID);
                ds.delOrganizedAllEvent(getProfileOrganizedEvents(docSns));
                pDocRef.delete();
            }
        });
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
     * This is a getter for Profile Role.
     * @param profileDocumentSnapshot
     * profileDocumentSnapshot: A profile document snapshot.
     * @return profileRole
     * profileRole: A profile's role.
     */
    public String getProfileRole(DocumentSnapshot profileDocumentSnapshot) {
        return profileDocumentSnapshot.getString("pRole");
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
     * This is a getter for Profile Image Updated State.
     * @param profileDocumentSnapshot
     * profileDocumentSnapshot: A profile document snapshot.
     * @return profileImageUpdatedState
     * profileImageUpdatedState: A state of Image Updated.
     */
    public Boolean getProfileImageUpdatedState(DocumentSnapshot profileDocumentSnapshot) {
        return profileDocumentSnapshot.getBoolean("pImageUpdated");
    }

    /**
     * This is a reset function for Profile Image Updated State.
     */
    public void resetProfileImageUpdatedState() {
        pDocRef.update("pImageUpdated", Boolean.FALSE);
    }

    /**
     * This is a getter for Profile Image.
     * @return profileImageGetTask
     * profileImageGetTask: A task for getting profileImage.
     */
    public Task<Uri> getProfileImage() {
        File localFile = null;
        try {
            localFile = File.createTempFile("P" + pID, "jpg");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return pStgRef.child(pID).getDownloadUrl();
    }

    /**
     * This is a setter for Profile Image.
     * @param profileImageURI
     * profileImageURI: The URI of an image.
     */
    public void setProfileImage(Uri profileImageURI) {
        pStgRef.child(pID)
            .putFile(profileImageURI)
            .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    pDocRef.update("pImageUpdated", Boolean.TRUE);
                }
            });
    }

    /**
     * This is a deleter for Profile Image.
     */
    public void delProfileImage() {
        pStgRef.child(pID).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                pDocRef.update("pImageUpdated", Boolean.TRUE);
            }
        });
    }


    /**
     * This is a getter for Profile Organized Events.
     * @param profileDocumentSnapshot
     * profileDocumentSnapshot: A profile document snapshot.
     * @return profileOrganizedEvents
     * profileOrganizedEvents: A list of eventID.
     */
    public ArrayList<String> getProfileOrganizedEvents(DocumentSnapshot profileDocumentSnapshot) {
        return (ArrayList<String>) profileDocumentSnapshot.get("pOrganizedEvents");
    }

    /**
     * This is a deleter for Profile Organized Event.
     * @param eventID
     * eventID: The ID of the event that needs to be deleted.
     */
    public void delProfileOrganizedEvent(String eventID) {
        pDocRef.update("pOrganizedEvents", FieldValue.arrayRemove(eventID))
            .addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    ds.delOrganizedEvent(eventID);
                }
            });
    }


    /**
     * This is a getter for Profile SignUp Events.
     * @param profileDocumentSnapshot
     * profileDocumentSnapshot: A profile document snapshot.
     * @return profileSignUpEvents
     * profileSignUpEvents: A list of eventID.
     */
    public ArrayList<String> getProfileAllSignUpEvents(DocumentSnapshot profileDocumentSnapshot) {
        if (profileDocumentSnapshot.get("pSignUpEvents") == null)
            return null;
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
     * profileCheckInEvents: A "2D Array" list of eventID and count, null if no event.
     */
    public String[][] getProfileAllCheckInEvent(DocumentSnapshot profileDocumentSnapshot) {
        if (profileDocumentSnapshot.get("pCheckInEvents") == null)
            return null;
        ArrayList<String> data = (ArrayList<String>) profileDocumentSnapshot.get("pCheckInEvents");
        String[][] lst = new String[data.size()][];
        for (int i = 0; i < data.size(); i++)
            lst[i] = dt.splitSharpString(data.get(i));
        return lst;
    }

    private String getProfileCheckInEventCount(DocumentSnapshot profileDocumentSnapshot,
                                               String eventID) {
        if (profileDocumentSnapshot.get("pCheckInEvents") == null)
            return null;
        ArrayList<String> data = (ArrayList<String>) profileDocumentSnapshot.get("pCheckInEvents");
        for (int i = 0; i < data.size(); i++) {
            if (Objects.equals(dt.splitSharpString(data.get(i))[0], eventID))
                return dt.splitSharpString(data.get(i))[1];
        }
        return null;
    }

    /**
     * This is an adder used to add or update the given eventID to the profile check in list.
     * @param eventID
     * eventID: An event's ID.
     */
    public void addProfileCheckInEvent(String eventID) {
        getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                if (getProfileCheckInEventCount(docSns, eventID) == null) {
                    String count = "00000001";
                    String data = dt.constructSharpString(eventID, count, null);
                    pDocRef.update("pCheckInEvents", FieldValue.arrayUnion(data));
                    ds.newCheckInEventProfile(eventID, pID, count);
                } else {
                    String count = getProfileCheckInEventCount(docSns, eventID);
                    String data = dt.constructSharpString(eventID, count, null);
                    String nextCount = dt.calculateAddOne(count);
                    String nextData = dt.constructSharpString(eventID, nextCount, null);
                    pDocRef.update("pCheckInEvents", FieldValue.arrayRemove(data));
                    pDocRef.update("pCheckInEvents", FieldValue.arrayUnion(nextData));
                    ds.addCheckInEventProfile(eventID, pID, count, nextCount);
                }
            }
        });
    }


    /**
     * This is a getter for Profile Notification Records.
     * @param profileDocumentSnapshot
     * profileDocumentSnapshot: A profile document snapshot.
     * @return profileNotificationRecords
     * profileNotificationRecords: A "2D Array" list of eventID, totalNumNtf, ReadNtf.
     * Null if no notification.
     * ReadNtf == "-1" indicates that the user has not read any notifications.
     */
    public String[][] getProfileAllNotificationRecords(DocumentSnapshot profileDocumentSnapshot) {
        if (profileDocumentSnapshot.get("pNotificationsRecords") == null)
            return null;
        ArrayList<String> data = (ArrayList<String>) profileDocumentSnapshot
            .get("pNotificationsRecords");
        String[][] lst = new String[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            lst[i] = dt.splitSharpString(data.get(i));
        }
        return lst;
    }

    private void updateNotificationRecord(DocumentSnapshot docSns, String eventID) {
        if (docSns.get("pNotificationsRecords") == null)
            return;
        ArrayList<String> data = (ArrayList<String>) docSns.get("pNotificationsRecords");
        String[][] lst = new String[data.size()][];
        for (int i = 0; i < data.size(); i++) {
            String[] tmp = dt.splitSharpString(data.get(i));
            if (Objects.equals(tmp[0], eventID)) {
                String newRecord = dt.constructSharpString(tmp[0], tmp[1], tmp[1]);
                pDocRef.update("pNotificationsRecords", FieldValue.arrayRemove(data.get(i)));
                pDocRef.update("pNotificationsRecords", FieldValue.arrayUnion(newRecord));
            }
        }
    }

    /**
     * This is a setter allow user to mark notifications for specific event0 as read.
     * @param eventID
     * eventID: An event's ID.
     */
    public void setProfileNotificationRead(String eventID) {
        getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                updateNotificationRecord(docSns, eventID);
            }
        });
    }


    /**
     * This is a requester for all profiles.
     * @return profilesSnapshotGetTask
     * profilesSnapshotGetTask: A task for getting profileQueryDocumentSnapshots.
     */
    public Task<QuerySnapshot> requestAllProfiles() {
        return db.collection("Profile").get();
    }

    /**
     * This is a getter for all Profile IDs.
     * @param profileQueryDocumentSnapshots
     * profileQueryDocumentSnapshots: The profile query document snapshot.
     * @return allProfileID
     * allProfileID: A list contains all Profile IDs.
     */
    public String[] getAllProfileID(QuerySnapshot profileQueryDocumentSnapshots) {
        if (profileQueryDocumentSnapshots == null)
            return null;
        int colSize = profileQueryDocumentSnapshots.getDocuments().size();
        String[] allProfileID = new String[colSize];
        for (int i = 0; i < colSize; i++)
            allProfileID[i] = profileQueryDocumentSnapshots.getDocuments().get(i).getId();
        return allProfileID;
    }
}
