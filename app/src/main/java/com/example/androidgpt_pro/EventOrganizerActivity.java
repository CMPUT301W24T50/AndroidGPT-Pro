package com.example.androidgpt_pro;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.FileProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class EventOrganizerActivity extends AppCompatActivity {

    private String userID;
    private String eventID;
    private ProfileDatabaseControl pdc;
    private  EventDatabaseControl edc;

    private ImageButton backButton;
    private CardView eventOrganizerPosterCard;
    private ImageView eventOrganizerPoster;
    private TextView eventOrganizerTitle;
    private TextView eventOrganizerTimeDate;
    private TextView eventOrganizerCityProvince;
    private  TextView eventOrganizerDescription;
    private TextView eventAttendeesNumber;
    private ImageButton eventSendNotification;
    private TextView tvOpenMap;
    private Button openMap;
    private TextView tvSignUpQRCode;
    private ImageView ivSignUpQRCode;
    private ImageButton ibSignUpQRCodeSave;
    private ImageButton ibSignUpQRCodeShare;
    private TextView tvCheckInQRCode;
    private ImageView ivCheckInQRCode;
    private ImageButton ibCheckInQRCodeSave;
    private ImageButton ibCheckInQRCodeShare;
    private Button deleteButton;
    private Button clearImageButton;
    private Boolean eventGeoLocation;

    private Bitmap signUpQRCode;
    private Bitmap checkInQRCode;


    private void initViews(){
        backButton = findViewById(R.id.back_button);
        eventOrganizerPosterCard = findViewById(R.id.card_event_image);
        eventOrganizerPoster = findViewById(R.id.iv_event_image);
        eventOrganizerTitle = findViewById(R.id.organizer_event_title);
        eventOrganizerTimeDate = findViewById(R.id.organizer_event_time_date);
        eventOrganizerCityProvince = findViewById(R.id.organizer_event_address_city_province);
        eventOrganizerDescription = findViewById(R.id.organizer_event_description);
        eventAttendeesNumber = findViewById(R.id.organizer_event_attendee);
        eventSendNotification = findViewById(R.id.organizer_notification_btn);
        tvOpenMap = findViewById(R.id.organizer_event_map);
        openMap = findViewById(R.id.organizer_event_map_btn);
        tvSignUpQRCode = findViewById(R.id.organizer_event_sign_up_qr_code);
        ivSignUpQRCode = findViewById(R.id.iv_event_sign_up_qr_image);
        ibSignUpQRCodeSave = findViewById(R.id.ib_save_oe_sign_up_qr);
        ibSignUpQRCodeShare = findViewById(R.id.ib_share_oe_sign_up_qr);
        tvCheckInQRCode = findViewById(R.id.organizer_event_check_in_qr_code);
        ivCheckInQRCode = findViewById(R.id.iv_event_check_in_qr_image);
        ibCheckInQRCodeSave = findViewById(R.id.ib_save_oe_check_in_qr);
        ibCheckInQRCodeShare = findViewById(R.id.ib_share_oe_check_in_qr);
        deleteButton = findViewById(R.id.btn_delete);
        clearImageButton = findViewById(R.id.btn_clear_image);
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
        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                eventGeoLocation = edc.getEventGLTState(docSns);
            }
        });

        if (eventGeoLocation == Boolean.FALSE ) {
            openMap.setEnabled(false);
        }
        else {
            openMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(EventOrganizerActivity.this, EventCheckInMap.class);
                    intent.putExtra("eventID", eventID);
                    startActivity(intent);
                }
            });
        }
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
                Intent intent = new Intent(EventOrganizerActivity.this, SendNotificationActivity.class);
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // If there's an error loading poster or poster has been deleted by admin show nothing
                Picasso.get().load(R.drawable.partyimage1).into(eventOrganizerPoster);
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
        signUpQRCode = QRCodeGenerator.generateSignUpQRCodeBitmap(eventID, 400, 400);
        ivSignUpQRCode.setImageBitmap(signUpQRCode);
        checkInQRCode = QRCodeGenerator.generateCheckInQRCodeBitmap(eventID, 400, 400);
        ivCheckInQRCode.setImageBitmap(checkInQRCode);
    }

    private void setupSaveQRCode() {
        ibSignUpQRCodeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(signUpQRCode, "sign_up_qr_code_" + eventID);
            }
        });
        ibCheckInQRCodeSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveImage(checkInQRCode, "check_in_qr_code_" + eventID);
            }
        });
    }

    private void saveImage(Bitmap bitmap, String fileName) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/YourAppName"); // Use an appropriate folder name

        Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        try {
            OutputStream outputStream = getContentResolver().openOutputStream(uri);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            outputStream.close();
            Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Toast.makeText(this, "Error saving image", Toast.LENGTH_SHORT).show();
        }
    }

    private void setupShareQRCode() {
        ibSignUpQRCodeShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shareQRCode(signUpQRCode);
            }
        });
        ibCheckInQRCodeShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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


    protected void checkIfAdmin(){
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                String role = pdc.getProfileRole(docSns);
                if (!role.matches("admin")) {
                    deleteButton.setVisibility(View.INVISIBLE);
                    clearImageButton.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    private void setupDeleteButton() {
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edc.removeEvent(eventID);
                eventOrganizerTitle.setText(R.string.invalid_name_text);
                deleteButton.setVisibility(View.GONE);
                clearImageButton.setVisibility(View.GONE);
                eventOrganizerPosterCard.setVisibility(View.GONE);
                eventOrganizerTimeDate.setVisibility(View.GONE);
                eventOrganizerCityProvince.setVisibility(View.GONE);
                eventOrganizerDescription.setVisibility(View.GONE);
                eventAttendeesNumber.setVisibility(View.GONE);
                eventSendNotification.setVisibility(View.GONE);
                tvOpenMap.setVisibility(View.GONE);
                openMap.setVisibility(View.GONE);
                tvSignUpQRCode.setVisibility(View.GONE);
                ivSignUpQRCode.setVisibility(View.GONE);
                ibSignUpQRCodeSave.setVisibility(View.GONE);
                ibSignUpQRCodeShare.setVisibility(View.GONE);
                tvCheckInQRCode.setVisibility(View.GONE);
                ivCheckInQRCode.setVisibility(View.GONE);
                ibCheckInQRCodeSave.setVisibility(View.GONE);
                ibCheckInQRCodeShare.setVisibility(View.GONE);
                CharSequence text = "Event Deleted";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(EventOrganizerActivity.this, text, duration);
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
                eventOrganizerPoster.setImageURI(imageUri);
                CharSequence text = "Image Initialized";
                int duration = Toast.LENGTH_SHORT;
                Toast toast = Toast.makeText(EventOrganizerActivity.this, text, duration);
                toast.show();
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
        showQRCode();

        openMap();
        openAttendees();
        openSender();
        setupSaveQRCode();
        setupShareQRCode();
        checkIfAdmin();
        setupDeleteButton();
        setupClearImageButton();
    }
}
