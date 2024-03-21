package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

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
    private ToggleButton geolocationToggle;


    private void initViews() {
        // Initialize views.
        pImageView = findViewById(R.id.image_profile_picture);
        pNameTextView = findViewById(R.id.text_profile_name);
        pPhoneNumberTextView = findViewById(R.id.text_phone_number);
        pEmailTextView = findViewById(R.id.text_email);
        geolocationToggle = findViewById(R.id.toggle_geolocation_tracking);
    }


    private void displayProfileImage() {
        pdc.getProfileImage().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                if (uri != null) {
                    Picasso.get().load(uri).into(pImageView);
                } else {
                    pImageView.setImageResource(android.R.drawable.sym_def_app_icon);
                }
            }
        });
    }

    private void displayProfileInfo() {
        pdc.getProfile().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot docSns,
                                @Nullable FirebaseFirestoreException error) {
                // Load profile picture if available.
                pNameTextView.setText(pdc.getProfileName(docSns));
                pPhoneNumberTextView.setText(pdc.getProfilePhoneNumber(docSns));
                pEmailTextView.setText(pdc.getProfileEmail(docSns));
                geolocationToggle.setChecked(pdc.getProfileGLTState(docSns));
            }
        });
    }


    private void setupEditProfileButton() {
        // Handle click event for edit profile button.
        editProfileButton = findViewById(R.id.btn_edit_profile);
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


    private void setupGLTButton() {
        geolocationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // code here to start geolocation tracking
                } else {
                    // code here to stop geolocation tracking
                }
            }
        });
    }

    private void startGeolocationTracking() {

    }

    private void stopGeolocationTracking() {

    }


    private void setupNavigationTabs() {
        navigationTabs = findViewById(R.id.navigation);
        navigationTabs.setSelectedItemId(R.id.profile_tab);
        navigationTabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.events_tab) {
                    Intent newIntent = new Intent(ProfileActivity.this,
                                                  EventActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
                } else if (itemId == R.id.qr_scanner_tab) {
                    Intent newIntent = new Intent(ProfileActivity.this,
                                                  QRScannerActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
                } else if (itemId == R.id.profile_tab) {
                    Intent newIntent = new Intent(ProfileActivity.this,
                                                  ProfileActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
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

        setupNavigationTabs();
        initViews();
        displayProfileImage();
        displayProfileInfo();
        setupEditProfileButton();
    }
}
