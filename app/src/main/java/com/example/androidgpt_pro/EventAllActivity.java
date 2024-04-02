package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;

public class EventAllActivity extends AppCompatActivity {
    private String userID;
    private ProfileDatabaseControl pdc;
    private EventDatabaseControl edc;
    BottomNavigationView navigationTabs;
    private FloatingActionButton createEventBtn;
    private ListView eventsListView;
    private ArrayList<Event> events;
    private EventArrayAdapter eventArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_future_events);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        pdc = new ProfileDatabaseControl(userID);
        edc = new EventDatabaseControl();

        navigationTabs = findViewById(R.id.nav_event);
        navigationTabs.setSelectedItemId(R.id.events_tab);
        navigationTabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.events_tab) {
                    assert Boolean.TRUE;
                } else if (itemId == R.id.qr_scanner_tab) {
                    Intent newIntent = new Intent(EventAllActivity.this, QRScannerActivity.class);
                    newIntent.putExtra("userID", userID);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(newIntent);
                    overridePendingTransition(0,0);
                } else if (itemId == R.id.profile_tab) {
                    Intent newIntent = new Intent(EventAllActivity.this, ProfileActivity.class);
                    newIntent.putExtra("userID", userID);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(newIntent);
                    overridePendingTransition(0,0);
                } else {
                    throw new IllegalArgumentException("menu item ID does not exist");
                }
                return false;
            }
        });
    }
}
