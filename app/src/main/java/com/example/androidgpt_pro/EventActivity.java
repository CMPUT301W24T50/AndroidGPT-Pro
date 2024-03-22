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
import com.google.firebase.firestore.DocumentSnapshot;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

/**
 * This class allows the user to browse all events in the database.
 * The user can select an event to see the event details and sign up.
 */
public class EventActivity extends AppCompatActivity {
    // TODO: when the user click the event button in the Navigation bar, it jumps to this page with all events listed.
    BottomNavigationView navigationTabs;
    private CardAdapter adapter;
    private ListView listViewEvents;
    // private ArrayList<EventDatabaseControl> eventList;
    private String eID;

    private void createSampleEvent() {
        EventDatabaseControl edc = new EventDatabaseControl();
        // We're going to add a hardcoded ID for demonstration purposes
        edc.getEventStat()
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot docSns) {
                    String lastEventID = edc.getLastEventID(docSns);
                    eID = edc.updateEventStat(lastEventID);
                    edc.initEvent(eID, "Sample Event",
                            "123 Main St",
                            "Edmonton",
                            "AB",
                            "This is a sample event.",
                            "8:00 PM",
                            "April 10, 2024");
                    EventCard card = new EventCard("Sample Event",
                            "8:00 PM",
                            "April 10, 2024",
                            "Edmonton, AB",
                            R.drawable.partyimage1);
                    adapter.add(card);
                    adapter.notifyDataSetChanged();
                }
            });
    }
    @SuppressLint("MissingInflatedId")
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_list);
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");

        listViewEvents = (ListView) findViewById(R.id.event_list_view);
        adapter = new CardAdapter(this);
        listViewEvents.setAdapter(adapter);

//        EventCard card = new EventCard("SampleEvent", "8:00 PM", "April 10, 2024", "Edmonton, AB", R.drawable.partyimage1);
//        adapter.add(card);
//        adapter.notifyDataSetChanged();
        createSampleEvent();

        // handle click action
        listViewEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventActivity.this, EventDetailActivity.class);
                intent.putExtra("eventID",eID);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });

        // navigation bar
        navigationTabs = findViewById(R.id.navigation);
        navigationTabs.setSelectedItemId(R.id.events_tab);

        navigationTabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.events_tab) {
                    Intent newIntent = new Intent(EventActivity.this, EventActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
                } else if (itemId == R.id.qr_scanner_tab) {
                    Intent newIntent = new Intent(EventActivity.this, QRScannerActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
                } else if (itemId == R.id.profile_tab) {
                    Intent newIntent = new Intent(EventActivity.this, ProfileActivity.class);
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
