package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * This class allows the user to browse all events in the database.
 * The user can select an event to see the event details and sign up.
 */
public class EventMyActivity extends AppCompatActivity {

    private String userID;
    private ProfileDatabaseControl pdc;
    private EventDatabaseControl edc;
    BottomNavigationView navigationTabs;
    private FloatingActionButton createEventBtn;
    private ListView eventsListView;
    private ArrayList<Event> events;
    private EventArrayAdapter eventArrayAdapter;


    private void initEvents() {
        events = new ArrayList<>();
    }

    private void initViews() {
        createEventBtn = findViewById(R.id.event_create_btn);
        eventsListView = findViewById(R.id.event_list_view);
        eventArrayAdapter = new EventArrayAdapter(this, events);
        eventsListView.setAdapter(eventArrayAdapter);
    }


    private void getEvents() {
        getEventIDsFromProfile();
    }

    private void getEventIDsFromProfile() {
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                if (pdc.getProfileOrganizedEvents(docSns) != null)
                    getEventInfo(pdc.getProfileOrganizedEvents(docSns));
            }
        });
    }

    private void getEventInfo(ArrayList<String> organizedEvents) {
        for (int i = 0; i < organizedEvents.size(); i++) {
            String eventID = organizedEvents.get(i);
            edc.getEventSnapshot(eventID)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot docSns) {
                    getEventImage(eventID, docSns);
                }
            });
        }
    }

    private void getEventImage(String eventID, DocumentSnapshot documentSnapshot) {
        edc.getEventImage(eventID).addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                events.add(new Event(eventID,
                    edc.getEventName(documentSnapshot),
                    edc.getEventLocationCity(documentSnapshot),
                    edc.getEventLocationProvince(documentSnapshot),
                    edc.getEventTime(documentSnapshot),
                    edc.getEventDate(documentSnapshot),
                    uri));
                eventArrayAdapter.notifyDataSetChanged();
            }
        });
    }


    private void setupEventsListView() {
        // handle click action
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventMyActivity.this, EventOrganizerActivity.class);
                intent.putExtra("eventID", events.get(position).getEventID());
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }


    private void setupCreateEventButton() {
        // handle click event to open the creatingEvent
        createEventBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EventMyActivity.this, EventCreateActivity.class);
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);
        edc = new EventDatabaseControl();

        navigationTabs = findViewById(R.id.nav_event);
        navigationTabs.setSelectedItemId(R.id.profile_tab);
        navigationTabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.events_tab) {
                    Intent newIntent = new Intent(EventMyActivity.this, EventAllActivity.class);
                    newIntent.putExtra("userID", userID);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(newIntent);
                    overridePendingTransition(0,0);
                } else if (itemId == R.id.qr_scanner_tab) {
                    Intent newIntent = new Intent(EventMyActivity.this, QRScannerActivity.class);
                    newIntent.putExtra("userID", userID);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(newIntent);
                    overridePendingTransition(0,0);
                } else if (itemId == R.id.profile_tab) {
                    Intent newIntent = new Intent(EventMyActivity.this, ProfileActivity.class);
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

        initEvents();
        initViews();

        getEvents();
        setupEventsListView();

        setupCreateEventButton();
    }
}
