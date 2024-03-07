package com.example.androidgpt_pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView profileNameTextView;
    private TextView phoneNumberTextView;
    private TextView emailTextView;

    private ToggleButton geolocationToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize views
        profileNameTextView = findViewById(R.id.text_profile_name);
        phoneNumberTextView = findViewById(R.id.text_phone_number);
        emailTextView = findViewById(R.id.text_email);
        geolocationToggle = findViewById(R.id.toggle_geolocation_tracking);

        // Get profile information from intent or database and display it
        // displayProfileInfo();

        // Handle click event for edit profile button
        Button editProfileButton = findViewById(R.id.btn_edit_profile);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        geolocationToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //  code here to start geolocation tracking
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

