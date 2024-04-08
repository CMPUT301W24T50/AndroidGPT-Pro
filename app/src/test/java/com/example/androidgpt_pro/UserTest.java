package com.example.androidgpt_pro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.Settings;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;

import java.io.File;
import java.util.Date;

public class UserTest extends AppCompatActivity {
    private ProfileDatabaseControl pdc;
    String eventID;
    String userID;

    private void mockUser() {
        eventID = "MockEventID";
        userID = Settings.Secure.getString(getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        pdc = new ProfileDatabaseControl(userID);

        pdc.getProfileSnapshot().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    if (!doc.exists()) {
                        pdc.initProfile("user");
                    }
                }
            }
        });
    }

    @Test
    public void SetProfile() {
        mockUser();
        pdc.setProfileName("Mocker");
        pdc.setProfilePhoneNumber("12345");
        pdc.setProfileEmail("mock@ua.ca");
        pdc.setProfileGLTState(true);

        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                assertEquals("Mocker", pdc.getProfileName(docSns));
                assertEquals("12345", pdc.getProfilePhoneNumber(docSns));
                assertEquals("mock@ua.ca", pdc.getProfileEmail(docSns));
                assertTrue(pdc.getProfileGLTState(docSns));
            }
        });
        pdc.delProfile();
    }

    @Test
    public void ProfileEvent() {
        mockUser();

        pdc.addProfileSignUpEvent(eventID);
        pdc.addProfileCheckInEvent(eventID);
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                assertEquals(1, pdc.getProfileAllSignUpEvents(docSns).size());
                assertEquals(1, pdc.getProfileAllCheckInEvent(docSns).length);
            }
        });
        pdc.delProfile();
    }
}
