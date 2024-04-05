package com.example.androidgpt_pro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;

import javax.annotation.Nullable;

public class SendNotificationActivity extends Activity {
    private EditText ntfTitle;
    private EditText ntfEditText;
    private Button ntfSendBtn;
    private EventDatabaseControl edc;
    private ProfileDatabaseControl pdc;
    private String eventID;
    private String userID;
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY = "AAAAskkkh7k:APA91bHiHsRX7JAbCfijCE-V9ALImnJ1sChVaxIzsqRl26zx9dBQ8Vrim7SK4nypS4rNv4f4wTFEiafTxr65D3LfDlwcLyDDkFrPLAYMxQ1lIhDO2cbzxlZjaXWJZLTreTNMwX1z-jVs";

    private void initViews() {
        ntfTitle = findViewById(R.id.notif_title);
        ntfEditText = findViewById(R.id.notif_text);
        ntfSendBtn = findViewById(R.id.btn_send);
    }

    private void popUpWindow(){
        setReference();
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

    private void setReference() {
        ntfSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = ntfTitle.getText().toString();
                String message = ntfEditText.getText().toString();

                if(!title.isEmpty() && !message.isEmpty()) {
                    sendNotification(
                            SendNotificationActivity.this,
                            "636d1de22650e418",
                            title,
                            message);
                } else if (message.contains("#")) {
                    Toast.makeText(SendNotificationActivity.this, "Illegal Symbol Included.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SendNotificationActivity.this, "Write Some Text!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendNotification(Context context, String token, String notificationTitle, String notificationText) {

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


        initViews();
        popUpWindow();
    }
}
