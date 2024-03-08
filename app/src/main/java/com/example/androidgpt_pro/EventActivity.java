package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class EventActivity extends AppCompatActivity{
    //TODO: this is the detail page of an event
    private TextView eventNameTextView;
    private TextView eventDateTextView;
    private TextView eventLocationAptTextView;
    private TextView eventLocationCityTextView;
    private TextView eventDescription;
    BottomNavigationView navigationTabs;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_content);

        // Retrieve the ID passed from
        String objectID = getIntent().getStringExtra("EVENT_ID", -1);

        //Initialize views
        eventNameTextView = findViewById(R.id.event_name);
        eventDateTextView = findViewById(R.id.event_date);
        eventLocationAptTextView = findViewById(R.id.event_location1);
        eventLocationCityTextView = findViewById(R.id.event_location2);
        eventDescription = findViewById(R.id.event_description);

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
