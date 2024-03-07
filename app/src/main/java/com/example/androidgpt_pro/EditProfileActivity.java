package com.example.androidgpt_pro;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editProfileNameEditText;
    private EditText editPhoneNumberEditText;
    private EditText editEmailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        // Initialize views
        editProfileNameEditText = findViewById(R.id.edit_text_edit_profile_name);
        editPhoneNumberEditText = findViewById(R.id.edit_text_edit_phone_number);
        editEmailEditText = findViewById(R.id.edit_text_edit_email);

        // Get current profile information and pre-fill the EditText fields
        // displayCurrentProfileInfo();

        // Handle save button click event
        Button saveButton = findViewById(R.id.button_save_profile);
        saveButton.setOnClickListener(view -> saveProfileChanges());
    }

//    private void displayCurrentProfileInfo() {
//        // retrieve the current profile information
//
//        String currentProfileName = "Changhyun Lee";
//        String currentPhoneNumber = "123-456-7890";
//        String currentEmail = "changhyun@example.com";
//
//        // Pre-fill EditText fields with current profile information
//        editProfileNameEditText.setText(currentProfileName);
//        editPhoneNumberEditText.setText(currentPhoneNumber);
//        editEmailEditText.setText(currentEmail);
//    }

    private void saveProfileChanges() {
        // Retrieve edited profile information from EditText fields
        String updatedProfileName = editProfileNameEditText.getText().toString();
        String updatedPhoneNumber = editPhoneNumberEditText.getText().toString();
        String updatedEmail = editEmailEditText.getText().toString();


        // replace this with firebase database update logic and update the profile information in firebase


        // just display a toast message confirming the changes for now
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

        // Finish the activity and return to the profile screen
        finish();
    }
}

