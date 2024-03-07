package com.example.androidgpt_pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class OpeningScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);

        CardView userCard = findViewById(R.id.first_card);
        CardView adminCard = findViewById(R.id.second_card);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        ProfileDatabaseControl newProfile = new ProfileDatabaseControl(userID);
        newProfile.setpGeoTracking(false);
        newProfile.setProfileEmail("");
        newProfile.setProfilePhoneNumber("");

        Intent newIntent = new Intent(OpeningScreenActivity.this, ProfileActivity.class);
        newIntent.putExtra("userID", userID);
        userCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Builds attendee profile and navigates to Profile activity
                newProfile.initProfile("user");
                startActivity(newIntent);
            }
        });

        adminCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Builds admin profile and navigates to Profile activity
                newProfile.initProfile("admin");
                startActivity(newIntent);
            }
        });

    }

}
