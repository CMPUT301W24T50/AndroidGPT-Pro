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

public class AnnouncementBox extends AppCompatActivity {
    private EventDatabaseControl edc;
    private ArrayList<String> announcementList;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_announcement_box);

        ListView announcementListView = findViewById(R.id.announcement_list);

        Intent intent = getIntent();
        String eventID = intent.getStringExtra("eventID");
        edc = new EventDatabaseControl();

        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                announcementList= edc.getEventAllNotifications(docSns);
            }
        });

        if (announcementList.isEmpty()) {
            Toast.makeText(AnnouncementBox.this, "There's no announcement for this event",
                    Toast.LENGTH_SHORT).show();
            finish();
        } else {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String> (this,
                    android.R.layout.simple_list_item_1, announcementList);
            announcementListView.setAdapter(arrayAdapter);
        }


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
