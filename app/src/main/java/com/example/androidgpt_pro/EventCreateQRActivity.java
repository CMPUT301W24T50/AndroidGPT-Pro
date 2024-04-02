package com.example.androidgpt_pro;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.OutputStream;

public class EventCreateQRActivity extends AppCompatActivity {

    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_successful);

        // Retrieve the event ID passed from the previous activity
        eventID = getIntent().getStringExtra("eventID");

        // Generate QR codes
        Bitmap signUpQRCode = QRCodeGenerator.generateSignUpQRCodeBitmap("sign-up_" + eventID, 400, 400);
        Bitmap checkInQRCode = QRCodeGenerator.generateCheckInQRCodeBitmap("check-in_" + eventID, 400, 400);

        // Display QR codes
        ImageView signUpQRCodeImageView = findViewById(R.id.sign_up_qr_code_image_view);
        ImageView checkInQRCodeImageView = findViewById(R.id.check_in_qr_code_image_view);
        signUpQRCodeImageView.setImageBitmap(signUpQRCode);
        checkInQRCodeImageView.setImageBitmap(checkInQRCode);

        // Back to Event Detail button
        Button backButton = findViewById(R.id.back_to_main_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to navigate back to the main activity
                Intent intent = new Intent(EventCreateQRActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        // Save Sign-Up QR button
        Button saveSignUpQRButton = findViewById(R.id.save_sign_up_qr_button);
        saveSignUpQRButton.setOnClickListener(v -> {
            saveImage(signUpQRCode, "sign_up_qr_code_" + eventID);
        });

        // Save Check-In QR button
        Button saveCheckInQRButton = findViewById(R.id.save_check_in_qr_button);
        saveCheckInQRButton.setOnClickListener(v -> {
            saveImage(checkInQRCode, "check_in_qr_code_" + eventID);
        });
    }
    // Method to save the image
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
}

