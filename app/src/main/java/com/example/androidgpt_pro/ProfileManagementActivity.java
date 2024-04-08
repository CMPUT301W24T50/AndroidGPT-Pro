package com.example.androidgpt_pro;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileManagementActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private ArrayList<Profile> profilesList;
    private String adminUserID;
    private ProfileDatabaseControl pdc;
    private String lastDelete = "LastDelete";

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
                    if (Objects.equals(lastDelete, profileID))
                        return;
                    String name = tempPdc.getProfileName(profileDoc);
                    String imageUrl = profileDoc.getString("imageUrl");
                    Profile profile = new Profile(profileID, name, null, imageUrl);
                    profilesList.add(profile);
                    adapter.notifyDataSetChanged();
                });
            }
        });
    }

    /**
     * Handler for deleting profiles
     * @param profileID
     */
    private void deleteProfile(String profileID) {
        ProfileDatabaseControl tempPdc = new ProfileDatabaseControl(profileID);
        tempPdc.delProfile();
        lastDelete = profileID;
        fetchProfiles(); // Refresh the profile list immediately
    }

    /**
     * Handler for deleting a users profile picture
     * @param profileID
     */
    private void deleteProfilePicture(String profileID) {
        ProfileDatabaseControl pdc = new ProfileDatabaseControl(profileID);
        pdc.delProfileImage();
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

            // Fetch and load the profile image
            ProfileDatabaseControl tempPdc = new ProfileDatabaseControl(currentProfile.getProfileId());
            tempPdc.getProfileImage().addOnSuccessListener(uri -> {
                Picasso.get().load(uri).into(holder.profileImageView);
            }).addOnFailureListener(e -> {
                // If there's an error loading profile image, display initials or a default image
                holder.profileImageView.setImageResource(R.drawable.profile_icon);
            });

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
