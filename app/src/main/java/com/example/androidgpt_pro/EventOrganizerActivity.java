package com.example.androidgpt_pro;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Objects;

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
    private  TextView eventOrganizerDescription;
    private TextView eventAttendeesNumber;
    private ImageButton eventSendNotification;
    private Button openMap;
    private ImageView ivCheckInQRCode;
    private Button shareQRCodeButton;

    private void initViews(){
        backButton = findViewById(R.id.back_button);
        eventOrganizerPoster = findViewById(R.id.iv_event_image);
        eventOrganizerTitle = findViewById(R.id.organizer_event_title);
        eventOrganizerTimeDate = findViewById(R.id.organizer_event_time_date);
        eventOrganizerCityProvince = findViewById(R.id.organizer_event_address_city_province);
        eventOrganizerDescription = findViewById(R.id.organizer_event_description);
        eventAttendeesNumber = findViewById(R.id.organizer_event_attendee);
        eventSendNotification = findViewById(R.id.organizer_notification_btn);
        openMap = findViewById(R.id.organizer_event_map_btn);
        ivCheckInQRCode = findViewById(R.id.iv_event_qr_image);
        shareQRCodeButton = findViewById(R.id.share_qr_image_btn);
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
                eventOrganizerDescription.setText(edc.getEventDescription(docSns));
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

    private void showQRCode() {
        Bitmap checkInQRCode = QRCodeGenerator
            .generateCheckInQRCodeBitmap(eventID, 400, 400);
        ivCheckInQRCode.setImageBitmap(checkInQRCode);
    }

    private void setUpShareQRCode() {
        shareQRCodeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap checkInQRCode = QRCodeGenerator
                        .generateCheckInQRCodeBitmap(eventID, 400, 400);
                shareQRCode(checkInQRCode);
            }
        });
    }

    private void shareQRCode(Bitmap checkInQRCode) {
        Uri uri = getImageToShare(checkInQRCode);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_TEXT, "Image Text");
        intent.putExtra(Intent.EXTRA_SUBJECT, "Image Subject");
        intent.setType("image/*");
        startActivity(Intent.createChooser(intent, "Share via"));
    }
    private Uri getImageToShare(Bitmap bitmap) {
        File folder = new File(getCacheDir(), "images");
        Uri uri = null;
        try {
            folder.mkdir();
            File file = new File(folder, "image.jpg");
            FileOutputStream fileOutputStream = new FileOutputStream(file);

            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            uri = FileProvider.getUriForFile(this, "com.example.androidgpt_pro", file);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return uri;
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
        showQRCode();

        openAttendees();
        openSender();
        setUpShareQRCode();
    }

}
