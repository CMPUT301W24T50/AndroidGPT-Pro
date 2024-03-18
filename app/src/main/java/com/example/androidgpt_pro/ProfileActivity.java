package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

/**
 * This class displays the users profile information on the screen
 */
public class ProfileActivity extends AppCompatActivity {

    private String userID;

    private TextView profileNameTextView;
    private TextView phoneNumberTextView;
    private TextView emailTextView;
    private ToggleButton geolocationToggle;
    BottomNavigationView navigationTabs;
    private ImageView profileImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Get profile information from intent or database and display it
        // displayProfileInfo();
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        ProfileDatabaseControl pdc = new ProfileDatabaseControl(userID);
//        test.setText(userID);

        // Initialize views
        profileNameTextView = findViewById(R.id.text_profile_name);
        phoneNumberTextView = findViewById(R.id.text_phone_number);
        emailTextView = findViewById(R.id.text_email);
        geolocationToggle = findViewById(R.id.toggle_geolocation_tracking);
//        TextView test = findViewById(R.id.test_text);
        profileImageView = findViewById(R.id.image_profile_picture);
        pdc.getProfile().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot docSns,
                                @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Database", error.toString());
                }
                profileNameTextView.setText(pdc.getProfileName(docSns));
                phoneNumberTextView.setText(pdc.getProfilePhoneNumber(docSns));
                emailTextView.setText(pdc.getProfileEmail(docSns));
                geolocationToggle.setChecked(pdc.getProfileGLTState(docSns));

                // Load profile picture if available
                String profilePictureUrl = pdc.getProfilePictureUrl(docSns);
                if (profilePictureUrl != null && !profilePictureUrl.isEmpty()) {
                    Picasso.get().load(profilePictureUrl).into(profileImageView);
                } else {
                    profileImageView.setImageResource(android.R.drawable.sym_def_app_icon);
                }
            }
        });

        // Handle click event for edit profile button
        Button editProfileButton = findViewById(R.id.btn_edit_profile);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
        navigationTabs = findViewById(R.id.navigation);
        navigationTabs.setSelectedItemId(R.id.profile_tab);
        navigationTabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.events_tab) {
                    Intent newIntent = new Intent(ProfileActivity.this, EventActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
                } else if (itemId == R.id.qr_scanner_tab) {
                    Intent newIntent = new Intent(ProfileActivity.this, QRScannerActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
                } else if (itemId == R.id.profile_tab) {
                    Intent newIntent = new Intent(ProfileActivity.this, ProfileActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
                } else {
                    throw new IllegalArgumentException("menu item ID does not exist");
                }
                return false;
            }
        });
//        geolocationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked) {
//                    //  code here to start geolocation tracking
//                } else {
//                    // code here to stop geolocation tracking
//                }
//            }
//        });
    }
    private void startGeolocationTracking() {

    }

    private void stopGeolocationTracking() {

    }


//    private void displayProfileInfo() {
//        // retrieve the profile information from a database or intent
//        //
//        String profileName = "Changhyun Lee";
//        String phoneNumber = "123-456-7890";
//        String email = "changhyun@example.com";
//
//        // Display profile information
//        profileNameTextView.setText(profileName);
//        phoneNumberTextView.setText(phoneNumber);
//        emailTextView.setText(email);
//    }
}