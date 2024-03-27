package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * This class allows the user to browse all events in the database.
 * The user can select an event to see the event details and sign up.
 */
public class EventActivity extends AppCompatActivity {

    private String userID;
    private ProfileDatabaseControl pdc;

    BottomNavigationView navigationTabs;
    private ListView eventsListView;
    private FloatingActionButton createEventBtn;
    private CardAdapter adapter;

    private ArrayList<String> eventIDs;


    private void initViews() {
        eventsListView = findViewById(R.id.event_list_view);
        createEventBtn = findViewById(R.id.event_create_btn);
        adapter = new CardAdapter(this);
        eventsListView.setAdapter(adapter);
    }


    private void setupEventsListView() {
        // handle click action
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventActivity.this, EventDetailActivity.class);
                intent.putExtra("eventID", eventIDs.get(position));
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);

        navigationTabs = findViewById(R.id.nav_event);
        navigationTabs.setSelectedItemId(R.id.events_tab);
        navigationTabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.events_tab) {
                    assert Boolean.TRUE;
                } else if (itemId == R.id.qr_scanner_tab) {
                    Intent newIntent = new Intent(EventActivity.this, QRScannerActivity.class);
                    newIntent.putExtra("userID", userID);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(newIntent);
                    overridePendingTransition(0,0);
                } else if (itemId == R.id.profile_tab) {
                    Intent newIntent = new Intent(EventActivity.this, ProfileActivity.class);
                    newIntent.putExtra("userID", userID);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(newIntent);
                    overridePendingTransition(0,0);
                } else {
                    throw new IllegalArgumentException("menu item ID does not exist");
                }
                return false;
            }
        });

        initViews();
        setupEventsListView();


        // handle click event to open the creatingEvent
        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventActivity.this, EventCreateActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        // Sample cards made by Luke just for testing. Not communicating with db
        EventCard card = new EventCard("SampleEvent", "8:00 PM", "April 10, 2024", "Edmonton, AB", R.drawable.partyimage1,false);
        adapter.add(card);
        EventCard card2 = new EventCard("SampleEvent", "8:00 PM", "April 10, 2024", "Edmonton, AB", R.drawable.partyimage1,true);
        adapter.add(card2);
        adapter.notifyDataSetChanged();
    }
}
