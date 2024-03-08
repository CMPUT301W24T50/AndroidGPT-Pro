package com.example.androidgpt_pro;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.provider.Settings.Secure;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

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

        String uniqueID = Secure.getString(getBaseContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        ProfileDatabaseControl pdc = new ProfileDatabaseControl(uniqueID);
        Intent intent;

        if (pdc.getProfileExistence()) {
            intent = new Intent(MainActivity.this, ProfileActivity.class);
        } else {
            // Redirect to Opening Screen (Choose Role)
            intent = new Intent(MainActivity.this, OpeningScreenActivity.class);
        }
        intent.putExtra("userID", uniqueID);

        startActivity(intent);
    }
}
