package com.example.androidgpt_pro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;

public class AttendeeCountActivity extends AppCompatActivity {

    private String eventID;
    private EventDatabaseControl edc;
    private String userID;
    private ProfileDatabaseControl pdc;
    private ListView attendeesCountListView;
    private ArrayList<Attendee> attendees;
    private AttendeeArrayAdapter attendeeArrayAdapter;


    private void initAttendee() {
        attendees = new ArrayList<>();
    }

    private void initViews() {
        attendeesCountListView = findViewById(R.id.attendee_list);
        attendeeArrayAdapter = new AttendeeArrayAdapter(this, attendees);
        attendeesCountListView.setAdapter(attendeeArrayAdapter);
    }

    private void getAttendeeID(){
        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                if (edc.getEventAllCheckInProfiles(docSns) != null) {
                    getAttendeeName(edc.getEventAllCheckInProfiles(docSns));
                }
            }
        });
    }

    private void getAttendeeName(String[][] eventAttendeeNames) {
        for (int i = 0; i < eventAttendeeNames.size(); i++) {
            String attendeeID = eventAttendeeNames.get(i);
            edc.getEventSnapshot(eventID)
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot docSns) {
                            attendees.add(new Attendee(attendeeID,
//                                    edc.getEventAllCheckInProfiles(docSns),
                                    // edc.getEventCheckInProfileCount(docSns,userID)));
                        }
                    });
        }
    }


    private void getAttendeeCheckedInCount(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_list);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);
        edc = new EventDatabaseControl();
    }

}
