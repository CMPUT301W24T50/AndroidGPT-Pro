package com.example.androidgpt_pro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
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
public class EventTest extends AppCompatActivity {

    EventDatabaseControl edc;
    ProfileDatabaseControl pdc;
    String eventID;
    String userID;
    private void mockEvent() {
        userID = Settings.Secure.getString(getBaseContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);

        edc = new EventDatabaseControl();
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

        edc.getEventStat().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                String lastEventID = edc.getLastEventID(docSns);
                eventID = edc.updateEventStat(lastEventID);

                // create the mock event in the firestore;
                edc.initEvent(eventID, userID, "MockTest", "MockStreet",
                        "MockCity", "MockProvince", "0",
                        "0", "MockDescription", "50",
                        true, null);
            }
        });
    }

    @Test
    public void testEventCreation() {
        mockEvent();
        //test the size of all event(confirm the event is create)
        //need to clear database before testing
        edc.requestAllEvents().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocSns) {
                assertEquals(1, edc.getAllEventID(queryDocSns).length);
            }
        });

        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                assertEquals("MockTest", edc.getEventName(docSns));
                assertEquals("MockStreet", edc.getEventLocationStreet(docSns));
                assertEquals("MockCity", edc.getEventLocationCity(docSns));
                assertEquals("MockProvince", edc.getEventLocationProvince(docSns));
                assertEquals("0", edc.getEventTime(docSns));
                assertEquals("0", edc.getEventDate(docSns));
                assertEquals("MockDescription", edc.getEventDescription(docSns));
                assertEquals("50", edc.getEventSignUpLimit(docSns));
                assertTrue(edc.getEventGLTState(docSns));
            }
        });

        edc.removeEvent(eventID);
        pdc.delProfile();
    }

    @Test
    public void testSignUpProfile() {
        mockEvent();
        edc.addEventSignUpProfile(eventID, "MockProfile");
        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                assertEquals(1, edc.getEventAllSignUpProfiles(docSns).size());
            }
        });

        edc.delEventSignUpProfile("eventID", "MockProfile");
        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                assertNull(edc.getEventAllSignUpProfiles(docSns));
            }
        });

        edc.removeEvent(eventID);
        pdc.delProfile();
    }

    @Test
    public void testCheckedInProfile() {
        mockEvent();
        edc.addEventCheckInProfile(eventID, "MockProfile");
        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                assertEquals(1, edc.getEventAllCheckInProfiles(docSns).length);
            }
        });

        edc.removeEvent(eventID);
        pdc.delProfile();
    }

    @Test
    public void testNotification() {
        mockEvent();
        edc.addEventNotification(eventID, "Mock Notification");
        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                assertEquals(1, edc.getEventAllNotifications(docSns).size());
            }
        });

        edc.removeEvent(eventID);
        pdc.delProfile();
    }
}
