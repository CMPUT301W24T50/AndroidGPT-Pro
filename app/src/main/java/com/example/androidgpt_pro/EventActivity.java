package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

public class EventActivity extends AppCompatActivity{
    //TODO: this is the detail page of an event

    private CollectionReference colRef = FirebaseFirestore
            .getInstance()
            .collection("Event");

    private TextView eventNameTextView;
    private TextView eventDateTextView;
    private TextView eventLocationAptTextView;
    private TextView eventLocationCityTextView;
    private TextView eventDescription;
    private String eventID;
    BottomNavigationView navigationTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_content);

        // Retrieve the ID passed from EventBrowseActivity
        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        EventDatabaseControl edc = new EventDatabaseControl();

        //Initialize views
        eventNameTextView = findViewById(R.id.event_name);
        eventDateTextView = findViewById(R.id.event_date);
        eventLocationAptTextView = findViewById(R.id.event_location1);
        eventLocationCityTextView = findViewById(R.id.event_location2);
        eventDescription = findViewById(R.id.event_description);

        colRef.document(eventID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if(error != null){
                    Log.e("Database", error.toString());
                }
                eventNameTextView.setText(value.getString("eName"));
                eventDateTextView.setText(value.getString("eTime"));
                eventLocationAptTextView.setText(value.getString("eLocation"));
                eventLocationCityTextView.setText(value.getString("eSpfLocation"));
                eventDescription.setText(value.getString("eDescription"));
            }
        });


        // Get event info from intent or database
//        Intent intent = getIntent();
//        String eventID = intent.getStringExtra("eventID");
//        String eventID = EventDatabaseControl
//
//
//        EventDatabaseControl edc = new EventDatabaseControl(eventID);
//        eventNameTextView.setText(edc.getEventName());
//        eventDateTextView.setText(edc.getEventTime());
//        eventLocationAptTextView.setText(edc.getEventLocation());
//        eventLocationCityTextView.setText(edc.getEventSimplifiedLocation());
    }

}
