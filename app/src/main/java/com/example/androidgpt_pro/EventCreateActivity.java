package com.example.androidgpt_pro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;

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
    private EditText eventNameEditText;
    private EditText eventDateEditText;
    private EditText eventLocationAddressEditText;
    private EditText eventLocationCityEditText;
    private EditText eventLocationProvinceEditText;
    private Button eventSelectTime;
    private Switch geoLcationTracking;
    private Button eventSelectPicButton;
    private Button eventConfirm;
    private ImageButton backButton;


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

    private void setupBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
        });
    }

    private void initViews(){
        // Initialize views.
        eventNameEditText = findViewById(R.id.edit_event_name);
        eventDateEditText = findViewById(R.id.edit_event_date);
        eventLocationAddressEditText = findViewById(R.id.edit_street_address);
        eventLocationCityEditText = findViewById(R.id.edit_city_address);
        eventLocationProvinceEditText = findViewById(R.id.edit_province_address);
        eventSelectTime = findViewById(R.id.edit_event_time);
        eventSelectPicButton = findViewById(R.id.select_pic);
        eventConfirm = findViewById(R.id.confirm_create_event);
        backButton = findViewById(R.id.back_button);

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
        setContentView(R.layout.activity_create_event);
        edc = new EventDatabaseControl();
        initViews();
    }
}
