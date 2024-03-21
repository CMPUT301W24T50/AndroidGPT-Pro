package com.example.androidgpt_pro;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class EventCreateActivity extends AppCompatActivity {

    private EventDatabaseControl edc;

    private String eID;
    private String eventName;
    private String eventLocation;
    private String eventSimplifiedLocation;
    private String eventDescription;
    private String eventTime;
    private String eventDate;


    public void newEvent() {
        edc.getEventStat()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot docSns) {
                    String lastEventID = edc.getLastEventID(docSns);
                    eID = edc.updateEventStat(lastEventID);
                    edc.initEvent(eID, eventName, eventLocation, eventSimplifiedLocation,
                            eventDescription, eventTime, eventDate);
                }
            });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edc = new EventDatabaseControl();
    }
}
