package com.example.androidgpt_pro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class AttendeeCountActivity extends Activity {

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

    private void popUpWindow(){
        getAttendee();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;
        int height = dm.heightPixels;
        getWindow().setLayout((int)(width*.8), (int)(height*.7));

        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.gravity = Gravity.CENTER;
        params.x = 0;
        params.y = -20;

        getWindow().setAttributes(params);
    }

    private void getAttendee(){
        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                getAttendeeName(edc.getEventAllCheckInProfiles(docSns));
            }
        });
    }

    private void getAttendeeName(String[][] eventAttendeeNamesCount) {
        if(eventAttendeeNamesCount == null) {
            CharSequence text = "No one has checked in!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(AttendeeCountActivity.this, text, duration);
            toast.show();
        } else {
            for (int i = 0; i < eventAttendeeNamesCount.length; i++) {
                String attendeeID = eventAttendeeNamesCount[i][0];
                String attendeeCheckInCount = eventAttendeeNamesCount[i][1];
                int attendeeCheckInNumber = Integer.parseInt(attendeeCheckInCount);
                String checkInNumber = Integer.toString(attendeeCheckInNumber);

                ProfileDatabaseControl pdcTemp = new ProfileDatabaseControl(attendeeID);
                pdcTemp.getProfileSnapshot()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot docSns) {
                            String attendeeName = pdc.getProfileName(docSns);
                            attendees.add(new Attendee(attendeeID,
                                    attendeeName, checkInNumber));
                            attendeeArrayAdapter.notifyDataSetChanged();
                        }
                    });
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.attendee_list);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);

        eventID = intent.getStringExtra("eventID");
        edc = new EventDatabaseControl();

        initAttendee();
        initViews();

        popUpWindow();
    }

}
