package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventAllActivity extends AppCompatActivity {
    private String userID;
    private String eventID;
    private ProfileDatabaseControl pdc;
    private EventDatabaseControl edc;
    BottomNavigationView navigationTabs;
    private FloatingActionButton createEventBtn;
    private ListView eventsListView;
    private ArrayList<Event> events;
    private EventArrayAdapter eventArrayAdapter;
    private ImageButton previous_btn;
    private ImageButton next_btn;


    private void initEvents() {
        events = new ArrayList<>();
    }

    private void initViews() {
        previous_btn = findViewById(R.id.event_previous_page);
        next_btn = findViewById(R.id.event_next_page);
        eventsListView = findViewById(R.id.event_list_view);
        eventArrayAdapter = new EventArrayAdapter(this, events);
        eventsListView.setAdapter(eventArrayAdapter);
    }

    private void getEvents() {
        getAllEventIDsFromEvent();
    }

    private void getAllEventIDsFromEvent() {
        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                if (edc.getLastEventID(docSns) != "00000000") {
                    String lastEventID = edc.getLastEventID(docSns);
                    String trimmedString = lastEventID.replaceFirst("^0+(?!$)", "");
                    int lastEventIDValue = Integer.parseInt(trimmedString);
                    // search all id
                    for (int i = 1; i <= lastEventIDValue; i++) {

                    }
                }
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
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // If there's an error loading poster or poster has been deleted by admin show nothing
                Uri imageUri = (new Uri.Builder())
                        .scheme(ContentResolver.SCHEME_ANDROID_RESOURCE)
                        .authority(getResources().getResourcePackageName(R.drawable.partyimage1))
                        .appendPath(getResources().getResourceTypeName(R.drawable.partyimage1))
                        .appendPath(getResources().getResourceEntryName(R.drawable.partyimage1))
                        .build();
                events.add(new Event(eventID,
                        edc.getEventName(documentSnapshot),
                        edc.getEventLocationCity(documentSnapshot),
                        edc.getEventLocationProvince(documentSnapshot),
                        edc.getEventTime(documentSnapshot),
                        edc.getEventDate(documentSnapshot),
                        imageUri));
                eventArrayAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupEventsListView() {
        // handle click action
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventAllActivity.this, EventDetailActivity.class);
                intent.putExtra("eventID", events.get(position).getEventID());
                intent.putExtra("userID", userID);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_future_events);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);

        eventID = intent.getStringExtra("eventID");
        edc = new EventDatabaseControl();

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
                    Intent newIntent = new Intent(EventAllActivity.this, QRScannerActivity.class);
                    newIntent.putExtra("userID", userID);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(newIntent);
                    overridePendingTransition(0,0);
                } else if (itemId == R.id.profile_tab) {
                    Intent newIntent = new Intent(EventAllActivity.this, ProfileActivity.class);
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
    }
}
