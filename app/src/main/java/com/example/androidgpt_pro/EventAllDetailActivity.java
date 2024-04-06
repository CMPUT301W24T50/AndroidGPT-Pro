package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This class represents a single event. It includes the event
 * information and the ability to sign in or check in
 * to an event
 */
@SuppressLint("SetTextI18n")
public class EventAllDetailActivity extends AppCompatActivity{

    private ProfileDatabaseControl pdc;
    private EventDatabaseControl edc;

    private String eventID;
    private String userID;

    private ImageButton backButton;

    private CardView eventPosterImageCard;
    private ImageView eventPosterImageView;
    private TextView eventNameTextView;
    private TextView eventTimeDateTextView;
    private TextView eventAddressTextView;
    private TextView eventDescription;

    private ImageButton announcementBoxButton;
    private Boolean eventSignUpLimit;
    private Button signUpButton;
    private Button withdrawButton;
    private Button deleteButton;
    private Button clearImageButton;

    Dialog dialog;
    private Button popBackBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_all_content);

        // Get profile & event information from intent
        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        userID = intent.getStringExtra("userID");
        edc = new EventDatabaseControl();
        pdc = new ProfileDatabaseControl(userID);


        //Initialize views
        eventPosterImageCard = findViewById(R.id.card_event_image);
        eventPosterImageView = findViewById(R.id.iv_event_image);
        eventNameTextView = findViewById(R.id.event_name);
        eventTimeDateTextView = findViewById(R.id.event_time_date);
        eventAddressTextView = findViewById(R.id.event_address);
        eventDescription = findViewById(R.id.event_description);
        signUpButton = findViewById(R.id.btn_sign_up);
        signUpButton.setVisibility(View.GONE);
        withdrawButton = findViewById(R.id.btn_withdraw);
        withdrawButton.setVisibility(View.GONE);
        deleteButton = findViewById(R.id.btn_delete);
        clearImageButton = findViewById(R.id.btn_clear_image);
        deleteButton.setVisibility(View.INVISIBLE);
        clearImageButton.setVisibility(View.INVISIBLE);
        announcementBoxButton = findViewById(R.id.notification_icon);


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
                eventTimeDateTextView.setText(edc.getEventTime(docSns)
                        + " - " + edc.getEventDate(docSns));
                eventAddressTextView.setText(edc.getEventLocationStreet(docSns)
                        + ", " + edc.getEventLocationCity(docSns)
                        + ", " + edc.getEventLocationProvince(docSns));
                eventDescription.setText(edc.getEventDescription(docSns));
                int alreadySignUp;
                if (edc.getEventAllSignUpProfiles(docSns) == null)
                    alreadySignUp = 0;
                else
                    alreadySignUp = edc.getEventAllSignUpProfiles(docSns).size();
                eventSignUpLimit = (alreadySignUp
                        >= Integer.parseInt(edc.getEventSignUpLimit(docSns)));
                fetchEventPoster();
                setupButtons();
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
        eventPosterImageCard.setVisibility(View.GONE);
        eventTimeDateTextView.setVisibility(View.GONE);
        eventAddressTextView.setVisibility(View.GONE);
        eventDescription.setVisibility(View.GONE);
        signUpButton.setVisibility(View.GONE);
        withdrawButton.setVisibility(View.GONE);
    }

    public void fetchEventPoster() {
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


    public void setupButtons() {
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                if (pdc.getProfileAllSignUpEvents(docSns) == null
                        || !pdc.getProfileAllSignUpEvents(docSns).contains(eventID)) {
                    if (eventSignUpLimit) {
                        signUpButton.setText("Unavailable");
                        signUpButton.setEnabled(Boolean.FALSE);
                    }
                    signUpButton.setVisibility(View.VISIBLE);
                } else {
                    withdrawButton.setVisibility(View.VISIBLE);
                }
            }
        });

        // handle the sign up events when clicking signUp button
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdc.addProfileSignUpEvent(eventID);
                signUpButton.setEnabled(Boolean.FALSE);
                signUpSuccess();
            }
        });

        // handle the withdraw events when clicking withdraw button
        withdrawButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pdc.delProfileSignUpEvent(eventID);
                withdrawButton.setEnabled(Boolean.FALSE);
                withdrawSuccess();
            }
        });
        checkIfAdmin();
        setupDeleteButton();
        setupClearImageButton();
        setupAnnouncementBox();

    }

    public void signUpSuccess() {
        dialog = new Dialog(EventAllDetailActivity.this);
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

    public void withdrawSuccess() {
        dialog = new Dialog(EventAllDetailActivity.this);
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
                eventPosterImageCard.setVisibility(View.GONE);
                eventTimeDateTextView.setVisibility(View.GONE);
                eventAddressTextView.setVisibility(View.GONE);
                eventDescription.setVisibility(View.GONE);
                signUpButton.setVisibility(View.GONE);
                withdrawButton.setVisibility(View.GONE);
                CharSequence text = "Event Deleted";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(EventAllDetailActivity.this, text, duration);
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
                Toast toast = Toast.makeText(EventAllDetailActivity.this, text, duration);
                toast.show();
            }
        });
    }

    private void setupAnnouncementBox() {
        announcementBoxButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventAllDetailActivity.this, AnnouncementBox.class);
                intent.putExtra("eventID", eventID);
                startActivity(intent);
            }
        });
    }

}
