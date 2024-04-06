package com.example.androidgpt_pro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {
    private EventDatabaseControl edc;
    private ArrayList<String> notificationList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ListView notificationListView = findViewById(R.id.notification_list);

        Intent intent = getIntent();
        String eventID = intent.getStringExtra("eventID");
        EventDatabaseControl edc = new EventDatabaseControl();

        // Retrieve notifications for the event
        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                ArrayList<String> eventNotifications = edc.getEventAllNotifications(docSns);
                if (eventNotifications == null || eventNotifications.isEmpty()) {
                    Toast.makeText(Notifications.this, "There are no notifications for this event", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Notifications.this,
                            android.R.layout.simple_list_item_1, eventNotifications);
                    notificationListView.setAdapter(arrayAdapter);
                }
            }
        });
    }
}

