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
import java.util.Arrays;

/**
 * This class represents the Event announcement box
 */
public class AnnouncementBox extends AppCompatActivity {
    private EventDatabaseControl edc;
    private String eventID;
    private ArrayList<String> announcementList;
    private ArrayAdapter<String> announcementListArrayAdapter;
    private ListView announcementListView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_announcement_box);

        announcementListView = findViewById(R.id.announcement_list);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        edc = new EventDatabaseControl();

        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                if (edc.getEventAllNotifications(docSns) == null) {
                    Toast.makeText(AnnouncementBox.this, "No Announcement",
                            Toast.LENGTH_SHORT).show();
                } else {
                    announcementList = new ArrayList<>();
                    announcementList.addAll(edc.getEventAllNotifications(docSns));
                    announcementListArrayAdapter = new ArrayAdapter<>(AnnouncementBox.this,
                            android.R.layout.simple_list_item_1, announcementList);
                    announcementListView.setAdapter(announcementListArrayAdapter);
                }
            }
        });


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
}
