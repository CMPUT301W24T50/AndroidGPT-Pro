package com.example.androidgpt_pro;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.ComponentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.journeyapps.barcodescanner.ScanOptions;
import com.journeyapps.barcodescanner.ScanContract;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;

import com.journeyapps.barcodescanner.ScanOptions;

/**
 * This class is used to scan QR codes and navigates the user
 * to the event page
 */
public class QRScannerActivity extends ComponentActivity {

    BottomNavigationView navigationTabs;
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() == null) {
            Toast.makeText(QRScannerActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            String scannedData = result.getContents();
            Toast.makeText(QRScannerActivity.this, "Scanned: " + scannedData, Toast.LENGTH_LONG).show();

            // Determine if the QR code is for signup or check-in
            if (isSignupQRCode(scannedData)) {
                handleSignupQRCode(scannedData);
            } else if (isCheckInQRCode(scannedData)) {
                handleCheckInQRCode(scannedData);
            } else {
                Toast.makeText(QRScannerActivity.this, "Invalid QR Code", Toast.LENGTH_LONG).show();
            }
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrscanner);
        Button btnScanQR = findViewById(R.id.btnScanQR);
        btnScanQR.setOnClickListener(v -> barcodeLauncher.launch(new ScanOptions()));
        Intent intent = getIntent();
        String userID = intent.getStringExtra("userID");
        navigationTabs = findViewById(R.id.navigation);
        navigationTabs.setSelectedItemId(R.id.qr_scanner_tab);
        navigationTabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.events_tab) {
                    Intent newIntent = new Intent(QRScannerActivity.this, EventBrowseActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
                } else if (itemId == R.id.qr_scanner_tab) {
                    Intent newIntent = new Intent(QRScannerActivity.this, QRScannerActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
                } else if (itemId == R.id.profile_tab) {
                    Intent newIntent = new Intent(QRScannerActivity.this, ProfileActivity.class);
                    newIntent.putExtra("userID", userID);
                    startActivity(newIntent);
                } else {
                    throw new IllegalArgumentException("menu item ID does not exist");
                }
                return false;
            }
        });
    }

    private boolean isSignupQRCode(String data) {
        return data.startsWith("sign-up_");
    }

    private boolean isCheckInQRCode(String data) {
        return data.startsWith("check-in_");
    }

    private void handleSignupQRCode(String data) {/*  TBD
        String eventId = extractEventId(data);
        Intent intent = new Intent(QRScannerActivity.this, EventActivity(sign-up).class);
        intent.putExtra("EVENT_ID", eventId);
        startActivity(intent);
    */}

    private void handleCheckInQRCode(String data) {/*  TBD
        String eventId = extractEventId(data);
        Intent intent = new Intent(QRScannerActivity.this, EventActivity(check-in).class);
        intent.putExtra("EVENT_ID", eventId);
        startActivity(intent);
    */}

    private String extractEventId(String qrCodeData) {
        return qrCodeData.substring(qrCodeData.indexOf("_") + 1);
    }
}