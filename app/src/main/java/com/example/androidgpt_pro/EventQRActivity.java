package com.example.androidgpt_pro;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class EventQRActivity extends AppCompatActivity {

    private ProfileDatabaseControl pdc;
    private EventDatabaseControl edc;

    private String eventID;
    private String userID;
    private String userOp;

    private final int GEO_LOCATION_CONTROL = 1;

    private CardView eventPosterCardView;
    private ImageView eventPosterImageView;
    private TextView eventNameTextView;
    private TextView eventDateTextView;
    private TextView eventLocationAptTextView;
    private TextView eventLocationCityTextView;
    private TextView eventLocationProvinceTextView;
    private TextView eventDescriptionTitleTextView;
    private TextView eventDescriptionTextView;
    private ImageButton backButton;
    private Button signUpButton;
    private Button withdrawButton;
    private Button checkInButton;
    private Button backToQRScanner;
    Dialog dialog;

    Boolean eGeoLocationTracking;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_qr);

        // Get profile & event information from intent
        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        userID = intent.getStringExtra("userID");
        userOp = intent.getStringExtra("userOp");
        edc = new EventDatabaseControl();
        pdc = new ProfileDatabaseControl(userID);

        //Initialize views
        eventPosterCardView = findViewById(R.id.card_event_image);
        eventPosterImageView = findViewById(R.id.iv_event_image);
        eventNameTextView = findViewById(R.id.event_name);
        eventDateTextView = findViewById(R.id.event_date);
        eventLocationAptTextView = findViewById(R.id.event_location_street_name);
        eventLocationCityTextView = findViewById(R.id.event_location_city);
        eventLocationProvinceTextView = findViewById(R.id.event_location_province);
        eventDescriptionTitleTextView = findViewById(R.id.event_description_title);
        eventDescriptionTextView = findViewById(R.id.event_description);
        eventLocationProvinceTextView = findViewById(R.id.event_location_province);
        signUpButton = findViewById(R.id.btn_sign_up);
        signUpButton.setVisibility(View.GONE);
        withdrawButton = findViewById(R.id.btn_withdraw);
        withdrawButton.setVisibility(View.GONE);
        checkInButton = findViewById(R.id.btn_check_in);
        checkInButton.setVisibility(View.GONE);

        // set the button function to back to eventList page
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        // get event poster
        edc.getEventImage(eventID).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(eventPosterImageView);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // If there's an error loading poster or poster has been deleted by admin show nothing
                Picasso.get().load(R.drawable.partyimage1).into(eventPosterImageView);
            }
        });

        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                if (edc.getEventName(docSns) == null) {
                    handleInvalidEvent();
                    return;
                }
                eventNameTextView.setText(edc.getEventName(docSns));
                eventDateTextView.setText(edc.getEventTime(docSns));
                eventLocationAptTextView.setText(edc.getEventLocationStreet(docSns));
                eventLocationCityTextView.setText(edc.getEventLocationCity(docSns));
                eventLocationProvinceTextView.setText(edc.getEventLocationProvince(docSns));
                eventDescriptionTextView.setText(edc.getEventDescription(docSns));
                eGeoLocationTracking = edc.getEventGLTState(docSns);
                if (Objects.equals(userOp, "SignUp"))
                    eventSignUp();
                else
                    eventCheckIn();
            }
        });
    }


    private void handleInvalidEvent() {
        eventNameTextView.setText(R.string.invalid_name_text);
        eventPosterCardView.setVisibility(View.GONE);
        eventDateTextView.setVisibility(View.GONE);
        eventLocationAptTextView.setVisibility(View.GONE);
        eventLocationCityTextView.setVisibility(View.GONE);
        eventLocationProvinceTextView.setVisibility(View.GONE);
        eventDescriptionTitleTextView.setVisibility(View.GONE);
        eventDescriptionTextView.setVisibility(View.GONE);
    }


    private void eventSignUp() {
        // check if the user has sign up the event
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                if (pdc.getProfileAllSignUpEvents(docSns) == null
                    || !pdc.getProfileAllSignUpEvents(docSns).contains(eventID)) {
                    signUpButton.setVisibility(View.VISIBLE);
                    setupSignUpButton();
                } else {
                    withdrawButton.setVisibility(View.VISIBLE);
                    setupWithdrawButton();
                }
            }
        });
    }

    private void setupSignUpButton() {
        // handle the sign up events when clicking signUp button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdc.addProfileSignUpEvent(eventID);
                signUpButton.setVisibility(View.GONE);
                withdrawButton.setVisibility(View.VISIBLE);
                signUpSuccess();
            }
        });
    }

    private void setupWithdrawButton() {
        // handle the withdraw events when clicking withdraw button
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdc.delProfileSignUpEvent(eventID);
                withdrawButton.setVisibility(View.GONE);
                signUpButton.setVisibility(View.VISIBLE);
                withdrawSuccess();
            }
        });
    }

    private void signUpSuccess() {
        dialog = new Dialog(EventQRActivity.this);
        dialog.setContentView(R.layout.sign_up_success_content);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_box));
        dialog.setCancelable(false);
        backToQRScanner = dialog.findViewById(R.id.back_list_button);

        backToQRScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                finish();
            }
        });
        dialog.show();
    }


    private void eventCheckIn() {
        checkInButton.setVisibility(View.VISIBLE);
        checkInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkProfileToCheckIn();
            }
        });
    }

    private void checkProfileToCheckIn() {
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                if (pdc.getProfileGLTState(docSns)) {
                    Intent intent = new Intent(EventQRActivity.this, GeoLocationActivity.class);
                    intent.putExtra("eventID", eventID);
                    startActivityForResult(intent, GEO_LOCATION_CONTROL);
                } else {
                    pdc.addProfileCheckInEvent(eventID);
                    checkInSuccess();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GEO_LOCATION_CONTROL) {
            if (resultCode == Activity.RESULT_OK) {
                pdc.addProfileCheckInEvent(eventID);
                checkInSuccess();
            }
        }
    }

    private void checkInSuccess() {
        dialog = new Dialog(EventQRActivity.this);
        dialog.setContentView(R.layout.check_in_success_content);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_box));
        dialog.setCancelable(false);
        backToQRScanner = dialog.findViewById(R.id.back_QR_button);

        backToQRScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.show();
    }
}
