package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageButton;
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

    private TextView eventNameTextView;
    private TextView eventDateTextView;
    private TextView eventLocationAptTextView;
    private TextView eventLocationCityTextView;
    private TextView eventDescription;
    private String eventID;
    BottomNavigationView navigationTabs;
    private ImageButton backButton;

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

        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(EventActivity.this, EventBrowseActivity.class);
                startActivity(newIntent);
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
