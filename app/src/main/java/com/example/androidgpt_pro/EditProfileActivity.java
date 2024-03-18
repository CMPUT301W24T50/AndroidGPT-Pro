package com.example.androidgpt_pro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

/**
 * This class allows the user to edit their profile information.
 */
public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    private ImageView profileImageView;
    private Button selectImageButton;
    private Uri imageUri;

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

        profileImageView = findViewById(R.id.image_edit_profile_picture);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(EditProfileActivity.this, ProfileActivity.class);
                newIntent.putExtra("userID", userID);
                startActivity(newIntent);
            }
        });

        // Set click listener for profile image
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        // Handle save button click event
        Button saveButton = findViewById(R.id.button_save_profile);
        saveButton.setOnClickListener(view -> saveProfileChanges());
    }
    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    // Method to handle the result of image selection
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Store the selected image URI
            imageUri = data.getData();
            // Set the selected image to the profile image view
            profileImageView.setImageURI(data.getData());
        }
    }

    /**
     * This method saves the changes to the profile
     */
    private void saveProfileChanges() {
        // Retrieve edited profile information from EditText fields
        String updatedProfileName = editProfileNameEditText.getText().toString();
        String updatedPhoneNumber = editPhoneNumberEditText.getText().toString();
        String updatedEmail = editEmailEditText.getText().toString();

        //update the profile information in firebase
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

