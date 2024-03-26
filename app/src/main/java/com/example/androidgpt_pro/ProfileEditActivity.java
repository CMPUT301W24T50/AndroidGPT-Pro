package com.example.androidgpt_pro;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

/**
 * This class allows the user to edit their profile information.
 */
public class ProfileEditActivity extends AppCompatActivity {

    private String userID;
    private ProfileDatabaseControl pdc;

    private static final int PICK_IMAGE_REQUEST = 1;
    private Boolean imageUpdate = Boolean.FALSE;
    private Uri imageUri;

    private ImageButton backButton;
    private ImageView editProfileImageView;
    private TextView editProfileImageText;
    private EditText editProfileNameEditText;
    private EditText editPhoneNumberEditText;
    private EditText editEmailEditText;
    private Button saveButton;


    private void initViews() {
        // Initialize views.
        backButton = findViewById(R.id.back_button);
        editProfileImageView = findViewById(R.id.iv_edit_profile_image);
        editProfileImageText = findViewById(R.id.tv_edit_profile_image);
        editProfileNameEditText = findViewById(R.id.edit_text_edit_profile_name);
        editPhoneNumberEditText = findViewById(R.id.edit_text_edit_phone_number);
        editEmailEditText = findViewById(R.id.edit_text_edit_email);
        saveButton = findViewById(R.id.button_save_profile);
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
                if (uri != null) {
                    Picasso.get().load(uri).into(editProfileImageView);
                } else {
                    editProfileImageView.setImageResource(android.R.drawable.sym_def_app_icon);
                }
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
            }
        });
    }

    private void setupProfileImageEditor() {
        // Set click listener for profile image.
        editProfileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        editProfileImageText.setOnClickListener(new View.OnClickListener() {
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
            imageUpdate = Boolean.TRUE;
        }
    }


    private void setupSaveButton() {
        // Handle save button click event.
        saveButton.setOnClickListener(view -> saveProfileChanges());
    }

    /**
     * This method saves the changes to the profile.
     */
    private void saveProfileChanges() {
        // Retrieve edited profile information from EditText fields.
        String updatedProfileName = editProfileNameEditText.getText().toString();
        String updatedPhoneNumber = editPhoneNumberEditText.getText().toString();
        String updatedEmail = editEmailEditText.getText().toString();

        //update the profile information in firebase.
        if (imageUpdate)
            pdc.setProfileImage(imageUri);
        pdc.setProfileName(updatedProfileName);
        pdc.setProfilePhoneNumber(updatedPhoneNumber);
        pdc.setProfileEmail(updatedEmail);

        // just display a toast message confirming the changes for now.
        Toast.makeText(this, "Profile updated successfully", Toast.LENGTH_SHORT).show();

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
        setupSaveButton();
    }
}
