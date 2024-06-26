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
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class EventAllActivity extends AppCompatActivity {
    private String userID;
    private String eventID;
    private ProfileDatabaseControl pdc;
    private EventDatabaseControl edc;
    BottomNavigationView navigationTabs;
    private ListView eventsListView;
    private ArrayList<Event> events;
    private EventArrayAdapter eventArrayAdapter;


    private void initEvents() {
        events = new ArrayList<>();
    }

    /**
     * Initializes the views to be used in this class
     */
    private void initViews() {
        eventsListView = findViewById(R.id.event_list_view);
        eventArrayAdapter = new EventArrayAdapter(this, events);
        eventsListView.setAdapter(eventArrayAdapter);
    }

    /**
     * Getter for all events
     */
    private void getEvents() {
        getAllEventIDsFromEvent();
    }

    /**
     * Getter for event IDs
     */
    private void getAllEventIDsFromEvent() {

        edc.requestAllEvents().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                String[] allFutureEventID = edc.getAllFutureEventID(queryDocumentSnapshots);
                getEventInfo(allFutureEventID);
            }
        });
    }

    /**
     * Gets event details from event IDs
     * @param allFutureEventID
     */
    private void getEventInfo(String[] allFutureEventID) {
        for (String futureEventID : allFutureEventID) {
            edc.getEventSnapshot(futureEventID).
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot docSns) {
                        getEventImage(futureEventID, docSns);
                    }
                });
        }
    }

    /**
     * Getter for the event poster, uses stock image if unsuccessful
     * @param eventID
     * @param documentSnapshot
     */
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

    /**
     * Handles placing events in list view
     */
    private void setupEventsListView() {
        // handle click action
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventAllActivity.this, EventAllDetailActivity.class);
                intent.putExtra("eventID", events.get(position).getEventID());
                intent.putExtra("userID", userID);
                startActivityForResult(intent, 0);
            }
        });
    }


    /**
     * Calls extra functions to refresh data
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     *
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        initEvents();
        initViews();
        getEvents();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_events);

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

        initEvents();
        initViews();

        getEvents();
        setupEventsListView();
    }
}
