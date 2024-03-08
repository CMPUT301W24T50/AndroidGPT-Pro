package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.HashMap;

import android.widget.ArrayAdapter;

public class EventBrowseActivity extends AppCompatActivity {
    // TODO: when the user click the event button in the Navigation bar, it jumps to this page with all events listed.
    private CollectionReference colRef = FirebaseFirestore
            .getInstance()
            .collection("Event");
    BottomNavigationView navigationTabs;
    private ArrayList<String> eventNames = new ArrayList<>();
    private ArrayAdapter<String> eventArrayAdapter;
    private ListView listViewEvents;
    private String eID;

    private void createSampleEvent() {
        // We're going to add a hardcoded ID for demonstration purposes
        colRef.document("00000000").get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        EventDatabaseControl edc = new EventDatabaseControl();
                        String eLastEventID = documentSnapshot.getString("eLastEventID");
                        eID = edc.strAddOne(eLastEventID);
                        HashMap<String, String> data = new HashMap<>();
                        data.put("eLastEventID", eID);
                        colRef.document("00000000").set(data);
                        edc.initEvent(eID, "Sample Event",
                                "123 Main St",
                                "City Center",
                                "This is a sample event.",
                                "April 10, 2024 8:00 PM");
                        eventNames.add("Sample Event");
                        eventArrayAdapter.notifyDataSetChanged();
                    }
                });
    }
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        listViewEvents = findViewById(R.id.event_list_view);
        eventArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventNames);
        listViewEvents.setAdapter(eventArrayAdapter);

        createSampleEvent();

        // handle click action
        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventBrowseActivity.this, EventActivity.class);
                intent.putExtra("EVENT_ID",eID);
                startActivity(intent);
            }
        });
        // this is navigation bar
        navigationTabs = findViewById(R.id.navigation);
        navigationTabs.setSelectedItemId(R.id.profile_tab);

        navigationTabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.events_tab) {
                    Intent newIntent = new Intent(EventBrowseActivity.this, EventBrowseActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
                } else if (itemId == R.id.qr_scanner_tab) {
                    Intent newIntent = new Intent(EventBrowseActivity.this, QRScannerActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
                } else if (itemId == R.id.profile_tab) {
                    Intent newIntent = new Intent(EventBrowseActivity.this, ProfileActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
                } else {
                    throw new IllegalArgumentException("menu item ID does not exist");
                }
                return false;
            }
        });
    }
}
