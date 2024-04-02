package com.example.androidgpt_pro;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.messaging.FirebaseMessaging;

import javax.annotation.Nullable;

public class NotifSenderActivity extends AppCompatActivity {
    private EditText notifTitle;
    private EditText notifEditText;
    private Button notifSendBtn;
    private EventDatabaseControl edc;
    private ProfileDatabaseControl pdc;
    private String eventID;
    private String userID;
    private void initViews() {
        notifTitle = findViewById(R.id.notif_title);
        notifEditText = findViewById(R.id.notif_text);
        notifSendBtn = findViewById(R.id.btn_send);
    }

    private void setReference() {
        notifSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!notifEditText.getText().toString().isEmpty() && !notifTitle.getText().toString().isEmpty()) {
                    sendNotification(notifEditText.getText().toString(), notifTitle.getText().toString());
                }
                else {
                    Toast.makeText(NotifSenderActivity.this, "Write Some Text!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendNotification(String notification, String notificationTitle) {
        FcmNotificationsSender notificationsSender = new FcmNotificationsSender("/topics/all",
                notificationTitle,
                notification,
                getApplicationContext(), NotifSenderActivity.this);
        notificationsSender.SendNotifications();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_sender);
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);

        eventID = intent.getStringExtra("eventID");
        edc = new EventDatabaseControl();

        FirebaseMessaging.getInstance().subscribeToTopic("all");

        initViews();
        setReference();
    }
}
