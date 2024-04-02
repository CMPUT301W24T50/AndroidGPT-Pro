package com.example.androidgpt_pro;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.provider.Settings.Secure;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

/**
 * This class manages the opening of the app and determines whether the user
 * profile needs to be created.
 */
public class MainActivity extends AppCompatActivity {

    private String uniqueID;


    /**
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        uniqueID = Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        ProfileDatabaseControl pdc = new ProfileDatabaseControl(uniqueID);

        pdc.getProfileSnapshot().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot doc = task.getResult();
                    Intent intent;
                    if (!doc.exists()){
                        pdc.initProfile("user");
                    }
                    intent = new Intent(MainActivity.this, ProfileActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    intent.putExtra("userID", uniqueID);
                    startActivity(intent);
                    overridePendingTransition(0,0);
                }
            }
        });

        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                String token = task.getResult();
                Log.i("My token", token);
            }
        });


    }
}
