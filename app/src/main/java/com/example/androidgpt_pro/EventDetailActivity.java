package com.example.androidgpt_pro;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * This class represents a single event. It includes the event
 * information and the ability to sign in or check in
 * to an event
 */
public class EventDetailActivity extends AppCompatActivity{
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
    private Button signUpButton;
    private Button withdrawButton;
    private Button checkInButton;
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
//        checkInButton = findViewById(R.id.btn_checkin);
//        checkInButton.setVisibility();

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

        // set the button function to back to eventList page
        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // TODO: check if signup & check in to hide specific button

        // handle the sign up events when clicking signUp button
//        signUpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                pdc.addProfileSignUpEvent(eventID);
//                signUpButton.setVisibility(View.GONE);
//                withdrawButton.setVisibility(View.VISIBLE);
//                signUpSuccess();
//            }
//        });

        // handle the withdraw events when clicking withdraw button
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdc.delProfileSignUpEvent(userID);
                withdrawButton.setVisibility(View.GONE);
                signUpButton.setVisibility(View.VISIBLE);
                withdrawSuccess();
            }
        });

    }

//    public void signUpSuccess() {
//        dialog = new Dialog(EventDetailActivity.this);
//        dialog.setContentView(R.layout.sign_up_success_content);
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_box));
//        dialog.setCancelable(false);
//        backToBrowse = dialog.findViewById(R.id.back_list_button);
//
//        backToBrowse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent newIntent = new Intent(EventDetailActivity.this, EventActivity.class);
//                newIntent.putExtra("userID", userID);
//                newIntent.putExtra("eventID", eventID);
//                startActivity(newIntent);
//            }
//        });
//        dialog.show();
//    }

    public void withdrawSuccess() {
        dialog = new Dialog(EventDetailActivity.this);
        dialog.setContentView(R.layout.withdraw_success_content);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_box));
        dialog.setCancelable(false);
        backToBrowse = dialog.findViewById(R.id.back_list_button);

        backToBrowse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(EventDetailActivity.this, EventActivity.class);
                newIntent.putExtra("userID", userID);
                newIntent.putExtra("eventID", eventID);
                startActivity(newIntent);
            }
        });
        dialog.show();
    }
    public void eventCheckin() {

    }
}
