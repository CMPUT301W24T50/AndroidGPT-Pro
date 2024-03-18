package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
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
import java.util.Objects;

public class EventQRActivity extends AppCompatActivity {
    private ProfileDatabaseControl pdc;
    private EventDatabaseControl edc;
    private String eventID;
    private String userID;
    private String userOp;
    private TextView eventNameTextView;
    private TextView eventDateTextView;
    private TextView eventLocationAptTextView;
    private TextView eventLocationCityTextView;
    private TextView eventDescription;
    private ImageButton backButton;
    private Button signUpButton;
    private Button withdrawButton;
    private Button checkInButton;
    private Button backToQRScanner;
    Dialog dialog;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_qr);

        // Get profile & event information from intent
        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        userID = intent.getStringExtra("userID");
        userOp = intent.getStringExtra("userOp");
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

        // set the button function to back to eventList page
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        if (Objects.equals(userOp, "SignUp")) {
            eventSignUp();
        } else {
            eventCheckIn();
        }
    }
    /**
     * After scan the sign up qr code, the code leads user to signup page
     */
    public void eventSignUp() {

        // check if the user has sign up the event
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

        // handle the sign up events when clicking signUp button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdc.addProfileSignUpEvent(eventID);
                withdrawButton.setVisibility(View.VISIBLE);
                signUpButton.setVisibility(View.GONE);
                signUpSuccess();
            }
        });

        // handle the withdraw events when clicking withdraw button
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdc.delProfileSignUpEvent(userID);
                signUpButton.setVisibility(View.VISIBLE);
                withdrawButton.setVisibility(View.GONE);
                withdrawSuccess();
            }
        });
    }
    /**
     * After scan the check in qr code, the code leads user to check in page
     */
    public void eventCheckIn() {
        checkInButton.setVisibility(View.VISIBLE);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdc.addProfileCheckInEvent(eventID);
                withdrawButton.setVisibility(View.VISIBLE);
                checkInSuccess();

            }
        });
    }

    private void signUpSuccess() {
        dialog = new Dialog(EventQRActivity.this);
        dialog.setContentView(R.layout.sign_up_success_content);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_box));
        dialog.setCancelable(false);
        backToQRScanner = dialog.findViewById(R.id.back_QR_button);

        backToQRScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(EventQRActivity.this, QRScannerActivity.class);
                newIntent.putExtra("userID", userID);
                newIntent.putExtra("eventID", eventID);
                startActivity(newIntent);
            }
        });
        dialog.show();
    }

    private void withdrawSuccess() {
        dialog = new Dialog(EventQRActivity.this);
        dialog.setContentView(R.layout.withdraw_success_content);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_box));
        dialog.setCancelable(false);
        backToQRScanner = dialog.findViewById(R.id.back_QR_button);

        backToQRScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(EventQRActivity.this, QRScannerActivity.class);
                newIntent.putExtra("userID", userID);
                newIntent.putExtra("eventID", eventID);
                startActivity(newIntent);
            }
        });
        dialog.show();
    }

    private void checkInSuccess(){
        dialog = new Dialog(EventQRActivity.this);
        dialog.setContentView(R.layout.check_in_success_content);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_box));
        dialog.setCancelable(false);
        backToQRScanner = dialog.findViewById(R.id.back_QR_button);

        backToQRScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(EventQRActivity.this, QRScannerActivity.class);
                newIntent.putExtra("userID", userID);
                newIntent.putExtra("eventID", eventID);
                startActivity(newIntent);
            }
        });
        dialog.show();
    }
}

