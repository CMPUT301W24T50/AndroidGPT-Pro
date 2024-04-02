package com.example.androidgpt_pro;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Button;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;

public class EventCreateQRActivity extends AppCompatActivity {

    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_successful); // Using the updated layout

        // Retrieve the event ID passed from the previous activity
        eventID = getIntent().getStringExtra("eventID");

        // Generate QR codes
        Bitmap signUpQRCode = QRCodeGenerator.generateSignUpQRCodeBitmap(eventID, 400, 400);
        Bitmap checkInQRCode = QRCodeGenerator.generateCheckInQRCodeBitmap(eventID, 400, 400);

        // Display QR codes
        ImageView signUpQRCodeImageView = findViewById(R.id.sign_up_qr_code_image_view);
        ImageView checkInQRCodeImageView = findViewById(R.id.check_in_qr_code_image_view);
        signUpQRCodeImageView.setImageBitmap(signUpQRCode);
        checkInQRCodeImageView.setImageBitmap(checkInQRCode);

        // Back to Main button
        Button backButton = findViewById(R.id.back_to_main_button);
        backButton.setOnClickListener(v -> {
            // Navigate back to the main activity or appropriate screen
            startActivity(new Intent(EventCreateQRActivity.this, MainActivity.class));
        });

        // Save Sign-Up QR button
        Button saveSignUpQRButton = findViewById(R.id.save_sign_up_qr_button);
        saveSignUpQRButton.setOnClickListener(v -> {
            // Logic to save Sign-Up QR Code
        });

        // Save Check-In QR button
        Button saveCheckInQRButton = findViewById(R.id.save_check_in_qr_button);
        saveCheckInQRButton.setOnClickListener(v -> {
        });
    }
}

