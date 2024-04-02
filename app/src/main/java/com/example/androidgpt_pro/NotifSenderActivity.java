package com.example.androidgpt_pro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nullable;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;

public class NotifSenderActivity extends Activity {
    private EditText notifTitle;
    private EditText notifEditText;
    private Button notifSendBtn;
    private EventDatabaseControl edc;
    private ProfileDatabaseControl pdc;
    private String eventID;
    private String userID;
    private static String BASE_URL = "https://fcm.googleapis.com/fcm/send";
    private static String SERVER_KEY = "AAAAskkkh7k:APA91bHiHsRX7JAbCfijCE-V9ALImnJ1sChVaxIzsqRl26zx9dBQ8Vrim7SK4nypS4rNv4f4wTFEiafTxr65D3LfDlwcLyDDkFrPLAYMxQ1lIhDO2cbzxlZjaXWJZLTreTNMwX1z-jVs";

    private void initViews() {
        notifTitle = findViewById(R.id.notif_title);
        notifEditText = findViewById(R.id.notif_text);
        notifSendBtn = findViewById(R.id.btn_send);
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
        notifSendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = notifTitle.getText().toString();
                String message = notifEditText.getText().toString();

                if(!title.isEmpty() && !message.isEmpty()) {
                    sendNotification(
                            NotifSenderActivity.this,
                            "636d1de22650e418",
                            title,
                            message);
                }
                else {
                    Toast.makeText(NotifSenderActivity.this, "Write Some Text!",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendNotification(Context context, String token, String notificationTitle, String notificationText) {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        RequestQueue queue = Volley.newRequestQueue(context);

        try{
            JSONObject json = new JSONObject();
            json.put("to", token);

            JSONObject notification = new JSONObject();
            notification.put("title", notificationTitle);
            notification.put("body", notificationText);
            json.put("notification", notification);

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest
                    (Request.Method.GET, BASE_URL, json, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println("FCM" + response);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError volleyError) {

                }
            }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("Content-Type","application/json");
                    params.put("Authorization", SERVER_KEY);
                    return params;
                }
            };

            queue.add(jsonObjectRequest);

        } catch (JSONException e) {
            e.printStackTrace();
        }


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
