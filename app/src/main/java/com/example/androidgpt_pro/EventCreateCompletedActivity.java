package com.example.androidgpt_pro;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Button;
import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

public class EventCreateCompletedActivity extends AppCompatActivity {

    private String userID;
    private String eventID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event_successful);

        // Retrieve the event ID passed from the previous activity
        userID = getIntent().getStringExtra("userID");
        eventID = getIntent().getStringExtra("eventID");

        // Generate QR codes
        Bitmap signUpQRCode = QRCodeGenerator.generateSignUpQRCodeBitmap(eventID, 400, 400);
        Bitmap checkInQRCode = QRCodeGenerator.generateCheckInQRCodeBitmap(eventID, 400, 400);

        // Display QR codes
        ImageView signUpQRCodeImageView = findViewById(R.id.sign_up_qr_code_image_view);
        ImageView checkInQRCodeImageView = findViewById(R.id.check_in_qr_code_image_view);
        signUpQRCodeImageView.setImageBitmap(signUpQRCode);
        checkInQRCodeImageView.setImageBitmap(checkInQRCode);

        // Back to Event Detail button
        Button backButton = findViewById(R.id.back_to_my_events_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Intent to navigate back to the main activity
                Intent intent = new Intent(EventCreateCompletedActivity.this,
                        ProfileActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        // Save Sign-Up QR button
        ImageButton saveSignUpQRButton = findViewById(R.id.ib_save_sign_up_qr);
        saveSignUpQRButton.setOnClickListener(v -> {
            saveImage(signUpQRCode, "sign_up_qr_code_" + eventID);
        });

        ImageButton shareSignUpQRButton = findViewById(R.id.ib_share_sign_up_qr);
        shareSignUpQRButton.setOnClickListener(v -> {
            shareImage(signUpQRCode);
        });

        // Save Check-In QR button
        ImageButton saveCheckInQRButton = findViewById(R.id.ib_save_check_in_qr);
        saveCheckInQRButton.setOnClickListener(v -> {
            saveImage(checkInQRCode, "check_in_qr_code_" + eventID);
        });

        ImageButton shareCheckInQRButton = findViewById(R.id.ib_share_check_in_qr);
        shareCheckInQRButton.setOnClickListener(v -> {
            shareImage(checkInQRCode);
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

    private void shareImage(Bitmap bitmap) {
        Uri uri = getImageToShare(bitmap);
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
}
