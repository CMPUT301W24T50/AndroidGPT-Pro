package com.example.androidgpt_pro;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class EventSignUpActivity extends AppCompatActivity {

    private String userID;
    private ProfileDatabaseControl pdc;
    private EventDatabaseControl edc;

    private ImageButton backButton;
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
        backButton = findViewById(R.id.back_button);
        eventsListView = findViewById(R.id.event_list_view);
        eventArrayAdapter = new EventArrayAdapter(this, events);
        eventsListView.setAdapter(eventArrayAdapter);
    }

    /**
     * Creates listener for back button
     */
    private void setupBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * Getter for events
     */
    private void getEvents() {
        getAllEventIDsFromEvent();
    }

    /**
     * Getter for event IDs
     */
    private void getAllEventIDsFromEvent() {
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                if (pdc.getProfileAllSignUpEvents(docSns) != null){
                    ArrayList<String> allSignUpEvent = pdc.getProfileAllSignUpEvents(docSns);
                    getEventInfo(allSignUpEvent);
                }
            }
        });
    }

    /**
     * Getter for event details
     * @param allSignUpEvent
     */
    private void getEventInfo(ArrayList<String> allSignUpEvent) {
        for (int i = 0; i < allSignUpEvent.size(); i++) {
            String signUpEventID = allSignUpEvent.get(i);
            edc.getEventSnapshot(signUpEventID).
                addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot docSns) {
                        if (edc.getEventName(docSns) == null) {
                            pdc.delProfileSignUpEvent(signUpEventID);
                            return;
                        }
                        getEventImage(signUpEventID, docSns);
                    }
                });
        }
    }

    /**
     * Getter for event poster
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
     * Creates listener for events list view
     */
    private void setupEventsListView() {
        // handle click action
        eventsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(EventSignUpActivity.this, EventAllDetailActivity.class);
                intent.putExtra("eventID", events.get(position).getEventID());
                intent.putExtra("userID", userID);
                startActivityForResult(intent, 0);
            }
        });
    }

    /**
     * Refreshes data
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
        setContentView(R.layout.activity_sign_up_events);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);
        edc = new EventDatabaseControl();

        initEvents();
        initViews();

        getEvents();
        setupEventsListView();

        setupBackButton();

    }
}
