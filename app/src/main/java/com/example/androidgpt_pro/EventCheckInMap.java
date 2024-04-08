package com.example.androidgpt_pro;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.android.gms.maps.model.LatLng;

public class EventCheckInMap extends AppCompatActivity implements OnMapReadyCallback{
    GoogleMap gMap;
    private EventDatabaseControl edc;
    private String eventID;

    /**
     * Creates the pop up window to display the map fragment
     */
    private void PopUpWindow() {
        mapFragment();

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
     * Creates the map fragment
     */
    private void mapFragment() {
        SupportMapFragment supportMapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.google_map);
        supportMapFragment.getMapAsync(this);
    }


    /**
     * handler for attendee check in map
     * @param googleMap
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap = googleMap;

        edc.getEventSnapshot(eventID).addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                getLocation(edc.getEventAllCheckInLocations(docSns));
            }
        });
    }

    /**
     * Getter for attendee check-in locations
     * @param checkInLocation
     */
    private void getLocation(String[][] checkInLocation) {
        if(checkInLocation == null) {
            CharSequence text = "No one has checked in!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(EventCheckInMap.this, text, duration);
            toast.show();
        }
        else {
            for (int i = 0; i < checkInLocation.length; i++) {
                double checkInLatitude = Double.parseDouble(checkInLocation[i][0]);
                double checkInLongLatitude = Double.parseDouble(checkInLocation[i][1]);

                LatLng markerPosition = new LatLng(checkInLatitude, checkInLongLatitude);
                gMap.addMarker(new MarkerOptions().position(markerPosition));
            }

            double firstCheckInLatitude = Double.parseDouble(checkInLocation[0][0]);
            double firstCheckInLongLatitude = Double.parseDouble(checkInLocation[0][1]);

            LatLng markerFirstPosition = new LatLng(firstCheckInLatitude, firstCheckInLongLatitude);
            gMap.moveCamera(CameraUpdateFactory.newLatLng(markerFirstPosition));
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organizer_map);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        edc = new EventDatabaseControl();

        PopUpWindow();
    }
}
