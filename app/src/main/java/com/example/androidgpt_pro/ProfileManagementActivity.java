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

public class ProfileManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private ArrayList<Profile> profilesList;
    private String adminUserID;
    private ProfileDatabaseControl pdc;

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

    private void fetchProfiles() {
        pdc.requestAllProfiles().addOnSuccessListener(queryDocumentSnapshots -> {
            profilesList.clear();
            String[] lst = pdc.getAllProfileID(queryDocumentSnapshots);
            for (int i = 0; i < lst.length; i++) {
                String profileID = lst[i];
                ProfileDatabaseControl tempPdc = new ProfileDatabaseControl(profileID);
                tempPdc.getProfileSnapshot().addOnSuccessListener(profileDoc -> {
                    String name = tempPdc.getProfileName(profileDoc);
                    String imageUrl = profileDoc.getString("imageUrl");
                    Profile profile = new Profile(profileID, name, null, imageUrl);
                    profilesList.add(profile);
                    adapter.notifyDataSetChanged();
                });
            }
        });
    }


    private void deleteProfile(String profileID) {
        ProfileDatabaseControl tempPdc = new ProfileDatabaseControl(profileID);
        tempPdc.delProfile();
        fetchProfiles(); // Refresh the profile list immediately
    }


    public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
        private final List<Profile> profilesList;
        private final LayoutInflater inflater;

        public ProfileAdapter(List<Profile> profilesList, Context context) {
            this.profilesList = profilesList;
            this.inflater = LayoutInflater.from(context);
        }

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
        }


        @Override
        public int getItemCount() {
            return profilesList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            ImageView profileImageView;
            TextView nameTextView;
            Button deleteButton;

            ViewHolder(View itemView) {
                super(itemView);
                profileImageView = itemView.findViewById(R.id.profile_image);
                nameTextView = itemView.findViewById(R.id.profile_name);
                deleteButton = itemView.findViewById(R.id.delete_button);
            }
        }
    }
}