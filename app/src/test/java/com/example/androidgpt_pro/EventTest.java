package com.example.androidgpt_pro;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Test;

import java.io.File;
import java.util.Date;
public class EventTest {

    private EventDatabaseControl edc;
    String eventID;
    String userID;
    private void mockEvent() {
        edc = new EventDatabaseControl();

        // create the mock event in the firestore;
        edc.initEvent(eventID, userID, "MockTest", "MockStreet",
                "MockCity", "MockProvince", "0",
                "0", "MockDescription", "50",
                true, null);
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
                assertFalse(edc.getEventGLTState(docSns));
            }
        });
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
    }
}
