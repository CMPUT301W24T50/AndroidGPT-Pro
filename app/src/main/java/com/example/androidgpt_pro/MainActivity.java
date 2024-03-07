package com.example.androidgpt_pro;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private CollectionReference profilesRef;

    /**
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        profilesRef = db.collection("Profile");

        String uniqueID = UUID.randomUUID().toString();
        Intent intent;
        intent = new Intent(MainActivity.this, OpeningScreenActivity.class);

        if ( checkIfUserExists(uniqueID) ) {
            intent = new Intent(MainActivity.this, ProfileActivity.class);
        }
        else {
            // Redirect to Opening Screen (Choose Role)
            intent = new Intent(MainActivity.this, OpeningScreenActivity.class);
        }
        intent.putExtra("userID", uniqueID);
        startActivity(intent);
    }

    /**
     * Checks to see if the user exists in the database
     * Ref: <a href="https://firebase.google.com/docs/firestore/query-data/get-data">...</a>
     * @param userID the unique userID tied to the user's device
     * @return true if user exists, false if not
     */
    private Boolean checkIfUserExists(String userID) {
        DocumentReference docRef = profilesRef.document(userID);
        final Boolean[] exists = {false};
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if( task.isSuccessful() ) {
                    DocumentSnapshot document = task.getResult();
                    if ( document.exists() ) {
                        exists[0] = true;
                    }
                }
                else {
                    Log.d("Firestore", "get failed with ", task.getException() );
                }
            }
        });
        return exists[0];
    }
}