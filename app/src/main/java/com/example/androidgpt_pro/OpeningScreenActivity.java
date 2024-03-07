package com.example.androidgpt_pro;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class OpeningScreenActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_opening_screen);

        CardView attendeeCard = findViewById(R.id.first_card);
        CardView organizerCard = findViewById(R.id.second_card);
        CardView adminCard = findViewById(R.id.third_card);

        attendeeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Builds new attendee profile and navigates to Events screen
            }
        });

//        organizerCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Builds new organizer profile and navigates to organizer main screen
//            }
//        });

//        adminCard.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Brings user to sign in screen
//            }
//        });
    }

}
