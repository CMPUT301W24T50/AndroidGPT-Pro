package com.example.androidgpt_pro;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProfileManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private ArrayList<Profile> profilesList;
    private String adminUserID;
    private ProfileDatabaseControl pdc;
    private ArrayList<String> lastDeleteProfilePID = new ArrayList<>();
    private ArrayList<String> lastDeleteImagePID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        ImageButton backButton = findViewById(R.id.back_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish this activity and go back to the previous one
                finish();
            }
        });

        adminUserID = getIntent().getStringExtra("USER_ID_KEY");
        pdc = new ProfileDatabaseControl(adminUserID);

        recyclerView = findViewById(R.id.recyclerView_profiles);
        profilesList = new ArrayList<>();
        adapter = new ProfileAdapter(profilesList, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        fetchProfiles();
    }

    /**
     * Getter for user profiles
     */
    private void fetchProfiles() {
        pdc.requestAllProfiles().addOnSuccessListener(queryDocumentSnapshots -> {
            profilesList.clear();
            String[] lst = pdc.getAllProfileID(queryDocumentSnapshots);
            for (String profileID : lst) {
                ProfileDatabaseControl tempPdc = new ProfileDatabaseControl(profileID);
                tempPdc.getProfileSnapshot().addOnSuccessListener(profileDoc -> {
                    String profileName = tempPdc.getProfileName(profileDoc);
                    if (lastDeleteProfilePID.contains(profileID))
                        return;
                    if (!lastDeleteImagePID.contains(profileID)) {
                        handleHaveProfileImage(tempPdc, profileID, profileName);
                    } else {
                        handleNotHaveProfileImage(profileID, profileName);
                    }
                });
            }
        });
    }

    private void handleHaveProfileImage(ProfileDatabaseControl pdc, String profileID, String profileName) {
        pdc.getProfileImage().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("TESTO", String.valueOf(uri));
                updateProfile(profileID, profileName, uri);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.d("TESTO", "11111");
                handleNotHaveProfileImage(profileID, profileName);
            }
        });
    }

    private void handleNotHaveProfileImage(String profileID, String profileName) {
        Profile profile = new Profile(profileID, profileName, null);
        profilesList.add(profile);
        adapter.notifyDataSetChanged();
    }

    private void updateProfile(String profileID, String profileName, Uri imageUri) {
        Profile profile = new Profile(profileID, profileName, imageUri);
        profilesList.add(profile);
        adapter.notifyDataSetChanged();
    }


    /**
     * Handler for deleting profiles
     * @param profileID
     */
    private void deleteProfile(String profileID) {
        ProfileDatabaseControl tempPdc = new ProfileDatabaseControl(profileID);
        tempPdc.delProfile();
        lastDeleteProfilePID.add(profileID);
        fetchProfiles(); // Refresh the profile list immediately
    }

    /**
     * Handler for deleting a users profile picture
     * @param profileID
     */
    private void deleteProfilePicture(String profileID) {
        ProfileDatabaseControl pdc = new ProfileDatabaseControl(profileID);
        pdc.delProfileImage();
        lastDeleteImagePID.add(profileID);
        fetchProfiles(); // Refresh the profiles list
    }

    /**
     * Adapter for Profile list
     */
    public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
        private final List<Profile> profilesList;
        private final LayoutInflater inflater;

        /**
         * Constructor
         * @param profilesList - list of profiles
         * @param context - context
         */
        public ProfileAdapter(List<Profile> profilesList, Context context) {
            this.profilesList = profilesList;
            this.inflater = LayoutInflater.from(context);
        }

        /**
         *
         * @param parent The ViewGroup into which the new View will be added after it is bound to
         *               an adapter position.
         * @param viewType The view type of the new View.
         *
         * @return new view holder
         */
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.profile_item, parent, false);
            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            Profile currentProfile = profilesList.get(position);
            holder.nameTextView.setText(currentProfile.getName());

            if (currentProfile.getImageUri() != null)
                Picasso.get().load(currentProfile.getImageUri()).into(holder.profileImageView);
            else
                // If there's an error loading profile image, display initials or a default image
                holder.profileImageView.setImageResource(R.drawable.profile_icon);

            holder.deleteButton.setOnClickListener(v -> {
                deleteProfile(currentProfile.getProfileId());
            });
            holder.deleteProfilePictureButton.setOnClickListener(v -> deleteProfilePicture(currentProfile.getProfileId()));
        }

        /**
         * Getter for item count
         * @return number of profiles in list
         */
        @Override
        public int getItemCount() {
            return profilesList.size();
        }

        /**
         * Holder for views
         */
        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView profileImageView;
            TextView nameTextView;
            Button deleteButton;
            Button deleteProfilePictureButton;

            ViewHolder(View itemView) {
                super(itemView);
                profileImageView = itemView.findViewById(R.id.profile_image);
                nameTextView = itemView.findViewById(R.id.profile_name);
                deleteButton = itemView.findViewById(R.id.delete_button);
                deleteProfilePictureButton = itemView.findViewById(R.id.delete_profile_picture_button);
            }
        }
    }
}
