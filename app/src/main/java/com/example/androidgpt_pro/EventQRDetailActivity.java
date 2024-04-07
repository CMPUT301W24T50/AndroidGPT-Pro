package com.example.androidgpt_pro;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.Objects;

public class EventQRDetailActivity extends AppCompatActivity {

    private ProfileDatabaseControl pdc;
    private EventDatabaseControl edc;

    private String eventID;
    private String userID;
    private String userOp;

    private final int GEO_LOCATION_CONTROL = 1;

    private ImageButton backButton;
    private ImageButton announcementBoxButton;
    private CardView eventPosterCardView;
    private ImageView eventPosterImageView;
    private TextView eventNameTextView;
    private TextView eventTimeDateTextView;
    private TextView eventAddressTextView;
    private TextView eventDescriptionTextView;
    private Button signUpButton;
    private Button withdrawButton;
    private Button checkInButton;
    private Button deleteButton;
    private Button clearImageButton;

    Dialog dialog;
    private Button popBackBtn;

    private Boolean eGeoLocationTracking;
    private Boolean eventSignUpLimit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_qr_content);

        // Get profile & event information from intent
        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        userID = intent.getStringExtra("userID");
        userOp = intent.getStringExtra("userOp");
        edc = new EventDatabaseControl();
        pdc = new ProfileDatabaseControl(userID);

        //Initialize views
        eventPosterCardView = findViewById(R.id.card_event_image);
        announcementBoxButton = findViewById(R.id.notification_icon);
        eventPosterImageView = findViewById(R.id.iv_event_image);
        eventNameTextView = findViewById(R.id.event_name);
        eventTimeDateTextView = findViewById(R.id.event_time_date);
        eventAddressTextView = findViewById(R.id.event_address);
        eventDescriptionTextView = findViewById(R.id.event_description);
        signUpButton = findViewById(R.id.btn_sign_up);
        signUpButton.setVisibility(View.GONE);
        withdrawButton = findViewById(R.id.btn_withdraw);
        withdrawButton.setVisibility(View.GONE);
        checkInButton = findViewById(R.id.btn_check_in);
        checkInButton.setVisibility(View.GONE);
        deleteButton = findViewById(R.id.btn_delete);
        clearImageButton = findViewById(R.id.btn_clear_image);
        deleteButton.setVisibility(View.INVISIBLE);
        clearImageButton.setVisibility(View.INVISIBLE);

        // set the button function to back to eventList page
        backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
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
                eventTimeDateTextView.setText(edc.getEventTime(docSns));
                eventAddressTextView.setText(edc.getEventLocationCity(docSns));
                eventDescriptionTextView.setText(edc.getEventDescription(docSns));
                eGeoLocationTracking = edc.getEventGLTState(docSns);
                int alreadySignUp;
                if (edc.getEventAllSignUpProfiles(docSns) == null)
                    alreadySignUp = 0;
                else
                    alreadySignUp = edc.getEventAllSignUpProfiles(docSns).size();
                eventSignUpLimit = (alreadySignUp
                        >= Integer.parseInt(edc.getEventSignUpLimit(docSns)));
                fetchEventPoster();
                if (Objects.equals(userOp, "SignUp"))
                    eventSignUp();
                else
                    eventCheckIn();
                checkIfAdmin();
                setupDeleteButton();
                setupClearImageButton();
            }
        });
    }

    private void handleInvalidEvent() {
        eventNameTextView.setText(R.string.invalid_name_text);
        deleteButton.setVisibility(View.GONE);
        clearImageButton.setVisibility(View.GONE);
        eventPosterCardView.setVisibility(View.GONE);
        eventTimeDateTextView.setVisibility(View.GONE);
        eventAddressTextView.setVisibility(View.GONE);
        eventDescriptionTextView.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);
        withdrawButton.setVisibility(View.GONE);
        checkInButton.setVisibility(View.GONE);
    }

    private void fetchEventPoster() {
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
    }


    private void eventSignUp() {
        // check if the user has sign up the event
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                if (pdc.getProfileAllSignUpEvents(docSns) == null
                    || !pdc.getProfileAllSignUpEvents(docSns).contains(eventID)) {
                    if (eventSignUpLimit) {
                        signUpButton.setText(R.string.unavailable_text);
                        signUpButton.setEnabled(Boolean.FALSE);
                        signUpButton.setVisibility(View.VISIBLE);
                    } else {
                        signUpButton.setVisibility(View.VISIBLE);
                        setupSignUpButton();
                    }
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
                withdrawSuccess();
            }
        });
    }

    private void signUpSuccess() {
        dialog = new Dialog(EventQRDetailActivity.this);
        dialog.setContentView(R.layout.success_sign_up_content);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_box));
        dialog.setCancelable(false);
        popBackBtn = dialog.findViewById(R.id.back_list_button);

        popBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.show();
    }

    private void withdrawSuccess() {
        dialog = new Dialog(EventQRDetailActivity.this);
        dialog.setContentView(R.layout.success_withdraw_content);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_box));
        dialog.setCancelable(false);
        popBackBtn = dialog.findViewById(R.id.back_list_button);

        popBackBtn.setOnClickListener(new View.OnClickListener() {
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
                if (eGeoLocationTracking && pdc.getProfileGLTState(docSns)) {
                    Intent intent = new Intent(EventQRDetailActivity.this, GeoLocationActivity.class);
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
        dialog = new Dialog(EventQRDetailActivity.this);
        dialog.setContentView(R.layout.success_check_in_content);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_box));
        dialog.setCancelable(false);
        popBackBtn = dialog.findViewById(R.id.back_QR_button);

        popBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        dialog.show();
    }


    protected void checkIfAdmin(){
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                String role = pdc.getProfileRole(docSns);
                if (role.matches("admin")) {
                    deleteButton.setVisibility(View.VISIBLE);
                    clearImageButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setupDeleteButton() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edc.removeEvent(eventID);
                eventNameTextView.setText(R.string.invalid_name_text);
                deleteButton.setVisibility(View.GONE);
                clearImageButton.setVisibility(View.GONE);
                eventPosterCardView.setVisibility(View.GONE);
                eventTimeDateTextView.setVisibility(View.GONE);
                eventAddressTextView.setVisibility(View.GONE);
                eventDescriptionTextView.setVisibility(View.GONE);
                signUpButton.setVisibility(View.GONE);
                withdrawButton.setVisibility(View.GONE);
                checkInButton.setVisibility(View.GONE);
                CharSequence text = "Event Deleted";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(EventQRDetailActivity.this, text, duration);
                toast.show();
            }
        });
    }

    private void setupClearImageButton() {
        clearImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edc.delEventImage(eventID);
                Uri imageUri = (new Uri.Builder())
                        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                        .authority(getResources().getResourcePackageName(R.drawable.partyimage1))
                        .appendPath(getResources().getResourceTypeName(R.drawable.partyimage1))
                        .appendPath(getResources().getResourceEntryName(R.drawable.partyimage1))
                        .build();
                eventPosterImageView.setImageURI(imageUri);
                CharSequence text = "Image Initialized";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(EventQRDetailActivity.this, text, duration);
                toast.show();
            }
        });
    }

    private void setUpAnnouncementBoxButton() {
        announcementBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventQRDetailActivity.this, AnnouncementBox.class);
                intent.putExtra("eventID", eventID);
                startActivity(intent);
            }
        });
    }
}
