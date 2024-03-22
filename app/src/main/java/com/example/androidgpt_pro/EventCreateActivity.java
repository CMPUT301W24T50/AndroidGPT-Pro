package com.example.androidgpt_pro;

import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

public class EventCreateActivity extends AppCompatActivity {

    private EventDatabaseControl edc;

    private String eID;
    private String eName;
    private String eLocStreet;
    private String eLocCity;
    private String eLocProvince;
    private String eTime;
    private String eDate;
    private String eDescription;
    private Uri eImageURI;


    public void setupEvent(String eventName,
                           String eventLocationStreet,
                           String eventLocationCity,
                           String eventLocationProvince,
                           String eventTime,
                           String eventDate,
                           String eventDescription,
                           Uri eventImageURI) {
        eName = eventName;
        eLocStreet = eventLocationStreet;
        eLocCity = eventLocationCity;
        eLocProvince = eventLocationProvince;
        eTime = eventTime;
        eDate = eventDate;
        eDescription = eventDescription;
        eImageURI = eventImageURI;
    }

    public void newEvent() {
        edc.getEventStat()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot docSns) {
                    String lastEventID = edc.getLastEventID(docSns);
                    eID = edc.updateEventStat(lastEventID);
                    edc.initEvent(eID, eName, eLocStreet, eLocCity, eLocProvince,
                            eTime, eDate, eDescription, eImageURI);
                }
            });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        edc = new EventDatabaseControl();
    }
}
