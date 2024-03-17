package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;

import java.util.ArrayList;

public class EventQRActivity extends AppCompatActivity {
    private ProfileDatabaseControl pdc;
    private EventDatabaseControl edc;
    private String eventID;
    private String userID;
    private TextView eventNameTextView;
    private TextView eventDateTextView;
    private TextView eventLocationAptTextView;
    private TextView eventLocationCityTextView;
    private TextView eventDescription;
    private ImageButton backButton;
    private Button signUpButton;
    private Button withdrawButton;
    private Button checkInButton;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_qr);

        // Get profile & event information from intent
        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        userID = intent.getStringExtra("userID");
        EventDatabaseControl edc = new EventDatabaseControl();
        ProfileDatabaseControl pdc = new ProfileDatabaseControl(userID);

        //Initialize views
        eventNameTextView = findViewById(R.id.event_name);
        eventDateTextView = findViewById(R.id.event_date);
        eventLocationAptTextView = findViewById(R.id.event_location1);
        eventLocationCityTextView = findViewById(R.id.event_location2);
        eventDescription = findViewById(R.id.event_description);
        signUpButton = findViewById(R.id.btn_sign_up);
        signUpButton.setVisibility(View.GONE);
        withdrawButton = findViewById(R.id.btn_withdraw);
        withdrawButton.setVisibility(View.GONE);
        checkInButton = findViewById(R.id.btn_check_in);
        checkInButton.setVisibility(View.GONE);

        edc.getEvent(eventID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot docSns,
                                @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Database", error.toString());
                }
                eventNameTextView.setText(edc.getEventName(docSns));
                eventDateTextView.setText(edc.getEventTime(docSns));
                eventLocationAptTextView.setText(edc.getEventLocation(docSns));
                eventLocationCityTextView.setText(edc.getEventSimplifiedLocation(docSns));
                eventDescription.setText(edc.getEventDescription(docSns));
            }
        });
    }

    public void eventSignUp() {
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                ArrayList<String> temp = pdc.getProfileAllSignUpEvents(docSns);
                if(temp.contains(eventID)) {
                    withdrawButton.setVisibility(View.VISIBLE);
                } else {
                    signUpButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    public void eventCheckIn() {
        checkInButton.setVisibility(View.VISIBLE);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdc.addProfileCheckInEvent(userID);
            }
        });
    }
}
