package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.Objects;

/**
 * This class displays the users profile information on the screen.
 */
public class ProfileActivity extends AppCompatActivity {

    private String userID;
    private ProfileDatabaseControl pdc;

    BottomNavigationView navigationTabs;
    private ImageView pImageView;
    private TextView pNameTextView;
    private TextView pPhoneNumberTextView;
    private TextView pEmailTextView;
    private Button editProfileButton;
    private ImageView notificationIcon;
    private ImageView unreadDot;
    private Button myEventButton;
    private Button signedUpEvent;
    private Button adminFunctionsButton;

    /**
     * Initializes the views to be used in this class
     */
    private void initViews() {
        // Initialize views.
        pImageView = findViewById(R.id.iv_profile_image);
        pNameTextView = findViewById(R.id.text_profile_name);
        pPhoneNumberTextView = findViewById(R.id.text_phone_number);
        pEmailTextView = findViewById(R.id.text_email);
        editProfileButton = findViewById(R.id.btn_edit_profile);
        myEventButton = findViewById(R.id.btn_my_event);
        notificationIcon = findViewById(R.id.notification_icon);
        unreadDot = findViewById(R.id.unread_dot);
        unreadDot.setVisibility(View.GONE);
        signedUpEvent = findViewById(R.id.btn_sign_up_event);
    }

    /**
     * Generates user image based on name
     */
    private void generateInitialsAndDisplay() {
        // Get the first letter of the user's name
        String name = pNameTextView.getText().toString().trim();
        String initials = "";
        if (!name.isEmpty()) {
            initials = name.substring(0, 1).toUpperCase();
        }

        // Create a Bitmap with the initials on a plain white background
        Bitmap bitmap = Bitmap.createBitmap(120, 120, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawColor(Color.WHITE);
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setTextSize(48);
        paint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(initials, 60, 70, paint);

        // Set the generated Bitmap as the profile image
        pImageView.setImageBitmap(bitmap);
    }

    /**
     * Displays profile image on screen
     */
    private void displayProfileImage() {
        pdc.getProfileImage().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(pImageView);
                pdc.resetProfileImageUpdatedState();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // If there's an error loading profile image, display initials
                generateInitialsAndDisplay();
                pdc.resetProfileImageUpdatedState();
            }
        });
    }

    /**
     * Displays profile information
     */
    private void displayProfileInfo() {
        pdc.getProfile().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot docSns,
                                @Nullable FirebaseFirestoreException error) {
                // Load profile picture if available.
                pNameTextView.setText(pdc.getProfileName(docSns));
                pPhoneNumberTextView.setText(pdc.getProfilePhoneNumber(docSns));
                pEmailTextView.setText(pdc.getProfileEmail(docSns));
                if (pdc.getProfileImageUpdatedState(docSns))
                    displayProfileImage();

                setupUnreadDot();
            }
        });
    }

    /**
     * Creates listener for edit profile button
     */
    private void setupEditProfileButton() {
        // Handle click event for edit profile button.
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,
                        ProfileEditActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }

    /**
     * Creates listener for my events button
     */
    protected void setupMyEventButton() {
        myEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,
                        EventMyActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }

    /**
     * Creates listener for Signed Up Events button
     */
    protected void setupSignedUpEventButton() {
        signedUpEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this,
                        EventSignUpActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }

    /**
     * Creates listener for notifications icon
     */
    private void setupNotificationIcon() {
        notificationIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Launch NotificationBox activity to display notifications
                Intent intent = new Intent(ProfileActivity.this, NotificationsBox.class);
                // Pass necessary data, such as user ID, to fetch notifications
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }

    /**
     * Creates indicator that user has unread notifications
     */
    private void setupUnreadDot() {
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot doSns) {
                unreadDot.setVisibility(View.GONE);
                if(pdc.getProfileAllNotificationRecords(doSns) == null){
                    return;
                }
                for(int i = 0; i < pdc.getProfileAllNotificationRecords(doSns).length; i++){
                    if(!Objects.equals(pdc.getProfileAllNotificationRecords(doSns)[i][1],
                            pdc.getProfileAllNotificationRecords(doSns)[i][2])) {
                        unreadDot.setVisibility(View.VISIBLE);
                        break;
                    }
                }
            }
        });
    }

    /**
     * Handles bottom tab navigation
     */
    private void initNavigationTabs() {
        navigationTabs = findViewById(R.id.nav_profile);
        navigationTabs.setSelectedItemId(R.id.profile_tab);
        navigationTabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.events_tab) {
                    Intent newIntent = new Intent(ProfileActivity.this, EventAllActivity.class);
                    newIntent.putExtra("userID", userID);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(newIntent);
                    overridePendingTransition(0, 0);
                } else if (itemId == R.id.qr_scanner_tab) {
                    Intent newIntent = new Intent(ProfileActivity.this, QRScannerActivity.class);
                    newIntent.putExtra("userID", userID);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(newIntent);
                    overridePendingTransition(0, 0);
                } else if (itemId == R.id.profile_tab) {
                    assert Boolean.TRUE;
                } else {
                    throw new IllegalArgumentException("menu item ID does not exist");
                }
                return false;
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get profileID and connect to the database.
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);

        initNavigationTabs();
        initViews();
        displayProfileImage();
        displayProfileInfo();
        setupEditProfileButton();
        setupMyEventButton();
        setupSignedUpEventButton();
        setupNotificationIcon();
//        setupUnreadDot();

        adminFunctionsButton = findViewById(R.id.btn_admin_functions);
        adminFunctionsButton.setVisibility(View.INVISIBLE);
        checkIfAdmin();
        adminFunctionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to the ProfileManagementActivity
                Intent intent = new Intent(ProfileActivity.this, ProfileManagementActivity.class);
                intent.putExtra("USER_ID_KEY", userID);
                startActivity(intent);
            }
        });
    }

    /**
     * Checks if user is admin and gives priviledges
     */
    protected void checkIfAdmin() {
        pdc.getProfileSnapshot().addOnSuccessListener(docSns -> {
            String role = pdc.getProfileRole(docSns);
            if (role.equals("admin")) {
                adminFunctionsButton.setVisibility(View.VISIBLE);
            }
        });
    }

}
