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

import javax.annotation.Nullable;

public class SendNotificationActivity extends Activity {

    private EditText ntfEditText;
    private Button ntfSendBtn;
    private EventDatabaseControl edc;
    private String eventID;

    /**
     * Initializes the views to be used in this class
     */
    private void initViews() {
        ntfEditText = findViewById(R.id.notif_text);
        ntfSendBtn = findViewById(R.id.btn_send);
    }

    /**
     * Handler to pop out window
     */
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

    /**
     * Setter for reference
     */
    private void setReference() {
        ntfSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = ntfEditText.getText().toString();

                if (message.contains("#")) {
                    Toast.makeText(SendNotificationActivity.this, "Illegal Symbol Included.",
                            Toast.LENGTH_SHORT).show();
                } else if (!message.isEmpty()) {
                    edc.addEventNotification(eventID, message);
                    Toast.makeText(SendNotificationActivity.this, "Message Sent!",
                            Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(SendNotificationActivity.this, "Write Some Text!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif_sender);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        edc = new EventDatabaseControl();

        initViews();
        popUpWindow();
    }
}
