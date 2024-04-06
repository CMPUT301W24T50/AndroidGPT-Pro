package com.example.androidgpt_pro;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ProfileManagementActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ProfileAdapter adapter;
    private ArrayList<Profile> profilesList;
    private ProfileDatabaseControl pdc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_management);

        recyclerView = findViewById(R.id.recyclerView_profiles);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        profilesList = new ArrayList<>();
        adapter = new ProfileAdapter(profilesList, this);
        recyclerView.setAdapter(adapter);

        pdc = new ProfileDatabaseControl();
        fetchProfiles();
    }

    private void fetchProfiles() {
        // Assuming ProfileDatabaseControl has a method to fetch all profiles
        pdc.getAllProfiles().addOnSuccessListener(queryDocumentSnapshots -> {
            profilesList.clear();
            for (DocumentSnapshot snapshot : queryDocumentSnapshots.getDocuments()) {
                Profile profile = snapshot.toObject(Profile.class);
                profilesList.add(profile);
            }
            adapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> Log.e("ProfileManagement", "Error fetching profiles", e));
    }

    public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.ViewHolder> {
        private final List<Profile> profilesList;
        private final LayoutInflater inflater;

        public ProfileAdapter(List<Profile> profilesList, Context context) {
            this.profilesList = profilesList;
            this.inflater = LayoutInflater.from(context);
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = inflater.inflate(R.layout.profile_item, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            Profile currentProfile = profilesList.get(position);
            holder.nameTextView.setText(currentProfile.getName());
            // Assuming Profile has a method to get image URL. Load image using your preferred method.
            // Example: Picasso.get().load(currentProfile.getImageUrl()).into(holder.profileImageView);

            holder.deleteButton.setOnClickListener(v -> deleteProfile(currentProfile.getProfileId(), position));
        }

        private void deleteProfile(String profileId, int position) {
            pdc.deleteProfile(profileId).addOnSuccessListener(aVoid -> {
                profilesList.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, profilesList.size());
            }).addOnFailureListener(e -> Log.e("ProfileAdapter", "Error deleting profile", e));
        }

        @Override
        public int getItemCount() {
            return profilesList.size();
        }

        public static class ViewHolder extends RecyclerView.ViewHolder {
            final ImageView profileImageView;
            final TextView nameTextView;
            final Button deleteButton;

            ViewHolder(View itemView) {
                super(itemView);
                profileImageView = itemView.findViewById(R.id.profile_image);
                nameTextView = itemView.findViewById(R.id.profile_name);
                deleteButton = itemView.findViewById(R.id.delete_button);
            }
        }
    }
}

