package com.example.androidgpt_pro;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
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

    private ImageButton btnBackButton;
    private FloatingActionButton createEventBtn;
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
        btnBackButton = findViewById(R.id.back_button);
        createEventBtn = findViewById(R.id.event_create_btn);
        eventsListView = findViewById(R.id.event_list_view);
        eventArrayAdapter = new EventArrayAdapter(this, events);
        eventsListView.setAdapter(eventArrayAdapter);
    }

    /**
     * Creates listener for back button
     */
    private void setupBackButton() {
        btnBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Getter for user's created events
     */
    private void getEvents() {
        getEventIDsFromProfile();
    }

    /**
     * Getter for user's created event IDs
     */
    private void getEventIDsFromProfile() {
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                if (pdc.getProfileOrganizedEvents(docSns) != null)
                    getEventInfo(pdc.getProfileOrganizedEvents(docSns));
            }
        });
    }

    /**
     * Getter for event details
     * @param organizedEvents
     */
    private void getEventInfo(ArrayList<String> organizedEvents) {
        for (int i = 0; i < organizedEvents.size(); i++) {
            String eventID = organizedEvents.get(i);
            edc.getEventSnapshot(eventID)
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot docSns) {
                    if (edc.getEventName(docSns) == null) {
                        pdc.delProfileOrganizedEvent(eventID);
                        return;
                    }
                    getEventImage(eventID, docSns);
                }
            });
        }
    }

    /**
     * Getter for event poster, shows default image if no poster
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
    };

    /**
     * Creates listener for event list view
     */
    private void setupEventsListView() {
        // handle click action
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventMyActivity.this, EventMyDetailActivity.class);
                intent.putExtra("eventID", events.get(position).getEventID());
                intent.putExtra("userID", userID);
                startActivityForResult(intent, 0);
            }
        });
    }

    /**
     * Creates listener for Create Event button
     */
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

    /**
     * Refreshes data on activity result
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

    /**
     * Initialize
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_events);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);
        edc = new EventDatabaseControl();

        initEvents();
        initViews();
        setupBackButton();

        getEvents();
        setupEventsListView();

        setupCreateEventButton();
    }
}
