package com.example.androidgpt_pro;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class allows the user to edit their profile information.
 */
public class ProfileEditActivity extends AppCompatActivity {

    private String userID;
    private ProfileDatabaseControl pdc;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri imageUri;
    private ImageButton backButton;
    private ImageView editProfileImageView;
    private ImageButton ibEditProfileImage;
    private ImageButton ibDelProfileImage;
    private EditText editProfileNameEditText;
    private EditText editPhoneNumberEditText;
    private EditText editEmailEditText;
    private Button saveButton;
    private ToggleButton geolocationToggle;
    private RelativeLayout editScreen;


    private void initViews() {
        // Initialize views.
        backButton = findViewById(R.id.back_button);
        editProfileImageView = findViewById(R.id.iv_edit_profile_image);
        ibEditProfileImage = findViewById(R.id.ib_edit_profile_image);
        ibDelProfileImage = findViewById(R.id.ib_del_profile_image);
        editProfileNameEditText = findViewById(R.id.edit_text_edit_profile_name);
        editPhoneNumberEditText = findViewById(R.id.edit_text_edit_phone_number);
        editEmailEditText = findViewById(R.id.edit_text_edit_email);
        saveButton = findViewById(R.id.button_save_profile);
        geolocationToggle = findViewById(R.id.toggle_geolocation_tracking);
        editScreen = findViewById(R.id.profile_edit_layout);
    }


    private void setupBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    private void fetchOriginalProfile() {
        pdc.getProfileImage().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                imageUri = uri;
                Picasso.get().load(imageUri).into(editProfileImageView);
            }
        });

        pdc.getProfile().addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot docSns,
                                @Nullable FirebaseFirestoreException error) {
                // Load profile picture if available.
                editProfileNameEditText.setText(pdc.getProfileName(docSns));
                editPhoneNumberEditText.setText(pdc.getProfilePhoneNumber(docSns));
                editEmailEditText.setText(pdc.getProfileEmail(docSns));
                geolocationToggle.setChecked(pdc.getProfileGLTState(docSns));
            }
        });
    }

    private void setupProfileImageEditor() {
        // Set click listener for profile image.
        ibEditProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                               PICK_IMAGE_REQUEST);
    }

    /**
     * This is a method to handle the result of image selection.
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            // Store the selected image URI.
            imageUri = data.getData();
            // Set the selected image to the profile image view.
            editProfileImageView.setImageURI(imageUri);
            editProfileImageView.clearColorFilter();
        }
    }


    private void setupProfileImageDeleter() {
        ibDelProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageUri = null;
                editProfileImageView.setColorFilter(Color.WHITE);
            }
        });
    }


    private void setupSaveButton() {
        // Handle save button click event.
        saveButton.setOnClickListener(view -> saveProfileChanges());
    }

    // Email validation method
    public boolean isValidEmail(String email) {
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
        Pattern pattern = Pattern.compile(emailPattern);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    // Phone number validation method
    public boolean isValidPhoneNumber(String phoneNumber) {
        String phonePattern = "[0-9]{10}";
        return phoneNumber.matches(phonePattern);
    }

    /**
     * This method saves the changes to the profile.
     */
    private void saveProfileChanges() {
        // Retrieve edited profile information from EditText fields.
        String updatedProfileName = editProfileNameEditText.getText().toString();
        String updatedPhoneNumber = editPhoneNumberEditText.getText().toString();
        String updatedEmail = editEmailEditText.getText().toString();

        // Validate email format
        if (!isValidEmail(updatedEmail)) {
            Toast.makeText(this, "Please enter a valid email address", Toast.LENGTH_SHORT).show();
            return; // Stop further execution if email is invalid
        }

        // Validate phone number format
        if (!isValidPhoneNumber(updatedPhoneNumber)) {
            Toast.makeText(this, "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show();
            return; // Stop further execution if phone number is invalid
        }

        //update the profile information in firebase.
        if (imageUri == null)
            pdc.delProfileImage();
        else
            pdc.setProfileImage(imageUri);
        pdc.setProfileName(updatedProfileName);
        pdc.setProfilePhoneNumber(updatedPhoneNumber);
        pdc.setProfileEmail(updatedEmail);
        pdc.setProfileGLTState(geolocationToggle.isChecked());

        // just display a toast message confirming the changes for now.
        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();

        // Finish the activity and return to the profile screen.
        finish();
    }


    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.
     *     <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);

        initViews();
        setupBackButton();
        fetchOriginalProfile();
        setupProfileImageEditor();
        setupProfileImageDeleter();
        setupSaveButton();

        AnimationDrawable animationDrawable = (AnimationDrawable) editScreen.getBackground();
        animationDrawable.setEnterFadeDuration(2500);
        animationDrawable.setExitFadeDuration(5000);
        animationDrawable.start();
    }
}
