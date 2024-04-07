package com.example.androidgpt_pro;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

public class Notifications extends AppCompatActivity {
    private String userID;
    private ProfileDatabaseControl pdc;
    private EventDatabaseControl edc;
    private ArrayList<String> notificationList;
    private ArrayAdapter<String> NotificationListArrayAdapter;
    private ListView notificationListView;
    private Button readButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        notificationListView = findViewById(R.id.notification_list);
        readButton = findViewById(R.id.read_close_btn);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);
        edc = new EventDatabaseControl();

        getAllNotificationEventID();

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

    private void getAllNotificationEventID() {
        // get all eventID from pdc.getProfileAllNotificationRecords
        pdc.getProfileSnapshot().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {

                if(pdc.getProfileAllNotificationRecords(docSns) != null) {
                    String[][] profileAllNotificationRecord = pdc.getProfileAllNotificationRecords(docSns);
                    getALlNotification(profileAllNotificationRecord);
                    setUpReadButton(profileAllNotificationRecord);
                } else {
                    setUpReadButton();
                }
            }
        });
    }

    private void getALlNotification(String[][] allNotificationRecord) {

        notificationList = new ArrayList<>();
        for (int i = 0; i < allNotificationRecord.length; i++) {
            int finalI = i;
            edc.getEventSnapshot(allNotificationRecord[i][0]).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot docSns) {
                    if(edc.getEventAllNotifications(docSns) != null) {
                        for(int j = Integer.parseInt(allNotificationRecord[finalI][2]) + 1;
                            j <= Integer.parseInt(allNotificationRecord[finalI][1]);
                            j++) {
                            notificationList.add(edc.getEventAllNotifications(docSns).get(j));
                            NotificationListArrayAdapter = new ArrayAdapter<>(Notifications.this,
                                    android.R.layout.simple_list_item_1, notificationList);
                            notificationListView.setAdapter(NotificationListArrayAdapter);
                        }
                    }
                }
            });
        }
    }


    private void setUpReadButton() {
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpReadButton(String[][] allNotificationRecord) {
        readButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (String[] strings : allNotificationRecord) {
                    pdc.setProfileNotificationRead(strings[0]);
                }
                finish();
            }
        });
    }
}

