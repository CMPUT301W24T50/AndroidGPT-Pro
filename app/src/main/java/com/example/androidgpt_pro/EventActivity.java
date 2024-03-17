package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Printer;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * This class represents a single event. It includes the event
 * information and the ability to sign in or check in
 * to an event
 */
public class EventActivity extends AppCompatActivity{
    //TODO: this is the detail page of an event

    private TextView eventNameTextView;
    private TextView eventDateTextView;
    private TextView eventLocationAptTextView;
    private TextView eventLocationCityTextView;
    private TextView eventDescription;
    private String eventID;
    private String userID;
    private ProfileActivity pa;
    private ImageButton backButton;
    private Button SignUpButton;
    private Button WithdrawButton;
    private Button CheckInButton;
    Dialog dialog;
    private Button backToBrowse;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_content);

        // Get profile & event information from intent
        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        userID = intent.getStringExtra("userID");
        EventDatabaseControl edc = new EventDatabaseControl();
        // ProfileDatabaseControl pdc = new ProfileDatabaseControl(userID);


        //Initialize views
        eventNameTextView = findViewById(R.id.event_name);
        eventDateTextView = findViewById(R.id.event_date);
        eventLocationAptTextView = findViewById(R.id.event_location1);
        eventLocationCityTextView = findViewById(R.id.event_location2);
        eventDescription = findViewById(R.id.event_description);
        SignUpButton = findViewById(R.id.btn_sign_up);
//        CheckInButton = findViewById(R.id.btn_checkin);
//        WithdrawButton = findViewById(R.id.btn_withdraw);

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

        // set the button function to back to eventList page
        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // TODO: check if signup & check in to hide specific button
        SignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edc.addEventSignUpProfile(eventID, userID);
                SignUpSuccess();
            }
        });


    }

    public void SignUpSuccess() {
        dialog = new Dialog(EventActivity.this);
        dialog.setContentView(R.layout.sign_up_success_content);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_box));
        dialog.setCancelable(false);
        backToBrowse = dialog.findViewById(R.id.back_list_button);
        backToBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(EventActivity.this, EventBrowseActivity.class);
                newIntent.putExtra("userID", userID);
                newIntent.putExtra("eventID", eventID);
                startActivity(newIntent);
            }
        });


        dialog.show();
    }
    public void eventCheckin(){

    }
}
