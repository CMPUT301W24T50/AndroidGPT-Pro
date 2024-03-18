package com.example.androidgpt_pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

/**
 * This class allows the user to edit their profile information.
 */
public class ProfileEditActivity extends AppCompatActivity {

    private String userID;
    private EditText editProfileNameEditText;
    private EditText editPhoneNumberEditText;
    private EditText editEmailEditText;
    private ImageButton backButton;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        // Initialize views
        editProfileNameEditText = findViewById(R.id.edit_text_edit_profile_name);
        editPhoneNumberEditText = findViewById(R.id.edit_text_edit_phone_number);
        editEmailEditText = findViewById(R.id.edit_text_edit_email);
        backButton = findViewById(R.id.back_button);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(ProfileEditActivity.this, ProfileActivity.class);
                newIntent.putExtra("userID", userID);
                startActivity(newIntent);
            }
        });

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

    /**
     * This method saves the changes to the profile
     */
    private void saveProfileChanges() {
        // Retrieve edited profile information from EditText fields
        String updatedProfileName = editProfileNameEditText.getText().toString();
        String updatedPhoneNumber = editPhoneNumberEditText.getText().toString();
        String updatedEmail = editEmailEditText.getText().toString();

        // replace this with firebase database update logic and update the profile information in firebase
        ProfileDatabaseControl pdc = new ProfileDatabaseControl(userID);
        pdc.setProfileName(updatedProfileName);
        pdc.setProfilePhoneNumber(updatedPhoneNumber);
        pdc.setProfileEmail(updatedEmail);

        // just display a toast message confirming the changes for now
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

        // Finish the activity and return to the profile screen
        finish();
    }
}

