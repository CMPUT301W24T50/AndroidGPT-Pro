package com.example.androidgpt_pro;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class SignedUpAttendee extends AppCompatActivity {

    private String eventID;
    private EventDatabaseControl edc;
    private String userID;
    private ProfileDatabaseControl pdc;
    private ListView attendeesSignedUpListView;
    private ArrayList<String> attendeesSignedUpList;
    private ArrayAdapter<String> attendeeSignedUpArrayAdapter;

    private void popUpWindow(){
        getSignedUpAttendee();
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

    private void initViews() {
        attendeesSignedUpListView = findViewById(R.id.attendee_signed_up_list);
        attendeesSignedUpList = new ArrayList<>();
    }

    private void getSignedUpAttendee() {
        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                getSignedUpAttendeeName(edc.getEventAllSignUpProfiles(docSns));
            }
        });
    }

    private void getSignedUpAttendeeName(ArrayList eventAllSignUpProfiles) {
        if(eventAllSignUpProfiles == null) {
            CharSequence text = "No one has signed in!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(SignedUpAttendee.this, text, duration);
            toast.show();
        } else {
            for (int i = 0; i < eventAllSignUpProfiles.size(); i++) {
                String attendeeID = (String) eventAllSignUpProfiles.get(i);

                ProfileDatabaseControl pdcTemp = new ProfileDatabaseControl(attendeeID);
                pdcTemp.getProfileSnapshot()
                        .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                            @Override
                            public void onSuccess(DocumentSnapshot docSns) {
                                String attendeeName = pdc.getProfileName(docSns);
                                attendeesSignedUpList.add(attendeeName);
                            }
                        });

            }
            attendeeSignedUpArrayAdapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_list_item_1, attendeesSignedUpList);
            attendeesSignedUpListView.setAdapter(attendeeSignedUpArrayAdapter);
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signed_up_attendee_list);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);

        eventID = intent.getStringExtra("eventID");
        edc = new EventDatabaseControl();

        initViews();
        popUpWindow();
    }
}
