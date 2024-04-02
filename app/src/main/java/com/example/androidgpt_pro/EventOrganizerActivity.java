package com.example.androidgpt_pro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventOrganizerActivity extends AppCompatActivity {
    private String userID;
    private String eventID;
    private ProfileDatabaseControl pdc;
    private  EventDatabaseControl edc;
    private ImageButton backButton;
    private ImageView eventOrganizerPoster;
    private TextView eventOrganizerTitle;
    private TextView eventOrganizerTimeDate;
    private TextView eventOrganizerCityProvince;
    private TextView eventAttendeesNumber;
    private ImageButton eventSendNotification;
    private Button openMap;
    private ImageView checkInQRCode;

    private void initViews(){
        backButton = findViewById(R.id.back_button);
        eventOrganizerPoster = findViewById(R.id.iv_event_image);
        eventOrganizerTitle = findViewById(R.id.organizer_event_title);
        eventOrganizerTimeDate = findViewById(R.id.organizer_event_time_date);
        eventOrganizerCityProvince = findViewById(R.id.organizer_event_address_city_province);
        eventAttendeesNumber = findViewById(R.id.organizer_event_attendee);
        eventSendNotification = findViewById(R.id.organizer_notification_btn);
        openMap = findViewById(R.id.organizer_event_map_btn);
        checkInQRCode = findViewById(R.id.iv_event_qr_image);
    }

    private void setupBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void openMap() {
        openMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
    }

    private void openAttendees() {
        eventAttendeesNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventOrganizerActivity.this, AttendeeCountActivity.class);
                intent.putExtra("eventID", eventID);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }
    private void openSender(){
        eventSendNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventOrganizerActivity.this, NotifSenderActivity.class);
                intent.putExtra("eventID", eventID);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }

    private void fetchUserEvent() {
        // get event poster
        edc.getEventImage(eventID).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(eventOrganizerPoster);
            }
        });

        // get event time&Date and city&Province and attendee#
        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                eventOrganizerTitle.setText(edc.getEventName(docSns));
                String eventTimeDate = edc.getEventTime(docSns) + " - "+ edc.getEventDate(docSns);
                eventOrganizerTimeDate.setText(eventTimeDate);
                String eventCityProvince = edc.getEventLocationCity(docSns) + ", " + edc.getEventLocationProvince(docSns);
                eventOrganizerCityProvince.setText(eventCityProvince);
                if (edc.getEventAllSignUpProfiles(docSns) == null)
                    return;
                int eventAttendeeNumber = edc.getEventAllSignUpProfiles(docSns).size();
                if (eventAttendeeNumber != 0){
                    SpannableString underlineAttendeesNumber = new SpannableString("Attendees" + eventAttendeeNumber + "/∞");
                    underlineAttendeesNumber.setSpan(new UnderlineSpan(), 0, underlineAttendeesNumber.length(), 0);
                    eventAttendeesNumber.setText(underlineAttendeesNumber);
                }
                else{
                    eventAttendeesNumber.setText("0/∞");
                }
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.organizer_event_content);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);

        eventID = intent.getStringExtra("eventID");
        edc = new EventDatabaseControl();

        initViews();
        setupBackButton();
        fetchUserEvent();

        openAttendees();
        openSender();
    }

}
