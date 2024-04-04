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

/**
 * QRScannerActivity is an activity class responsible for handling QR code scanning.
 * It integrates with a QR code scanning library to scan QR codes and processes the results
 * to determine if they are related to event sign-up or check-in. Depending on the QR code content,
 * the activity navigates to different parts of the application.
 *
 * Outstanding issues:
 * - The actual activities for event sign-up and check-in (EventActivity for sign-up and check-in)
 *   are yet to be implemented and integrated.
 */
public class QRScannerActivity extends ComponentActivity {

    private String userID;

    BottomNavigationView navigationTabs;
    private final ActivityResultLauncher<ScanOptions> barcodeLauncher = registerForActivityResult(new ScanContract(), result -> {
        if(result.getContents() == null) {
            Toast.makeText(QRScannerActivity.this, "Cancelled", Toast.LENGTH_LONG).show();
        } else {
            String scannedData = result.getContents();
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

        // Setup scan button and bottom navigation view
        Button btnScanQR = findViewById(R.id.btnScanQR);
        btnScanQR.setOnClickListener(v -> barcodeLauncher.launch(new ScanOptions()));
        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");

        navigationTabs = findViewById(R.id.nav_qr_scanner);
        navigationTabs.setSelectedItemId(R.id.qr_scanner_tab);

        // Set listener for bottom navigation items
        navigationTabs.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int itemId = menuItem.getItemId();
                if (itemId == R.id.events_tab) {
                    Intent newIntent = new Intent(QRScannerActivity.this, EventAllActivity.class);
                    newIntent.putExtra("userID", userID);
                    newIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(newIntent);
                    overridePendingTransition(0,0);
                } else if (itemId == R.id.qr_scanner_tab) {
                    assert Boolean.TRUE;
                } else if (itemId == R.id.profile_tab) {
                    Intent newIntent = new Intent(QRScannerActivity.this, ProfileActivity.class);
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

    /**
     * Checks if the scanned data represents a sign-up QR code.
     *
     * @param data The scanned QR code data.
     * @return true if it's a sign-up QR code; false otherwise.
     */
    private boolean isSignupQRCode(String data) {
        return data.startsWith("AndroidGPT-Pro_SignUp_");
    }

    /**
     * Checks if the scanned data represents a check-in QR code.
     *
     * @param data The scanned QR code data.
     * @return true if it's a check-in QR code; false otherwise.
     */
    private boolean isCheckInQRCode(String data) {
        return data.startsWith("AndroidGPT-Pro_CheckIn_");
    }

    /**
     * This method is intended to handle the situation when a sign-up QR code is scanned.
     * Currently, it is a placeholder pending the implementation of the corresponding activity.
     * When completed, it should extract the event ID from the QR code, create an intent
     * for the sign-up activity (EventActivity for sign-up), and start that activity.
     *
     * @param data The scanned QR code data, which should include the event ID for sign-up.
     */
    private void handleSignupQRCode(String data) {
        String eventID = data.substring(22);
        Intent intent = new Intent(QRScannerActivity.this, EventQRActivity.class);
        intent.putExtra("eventID", eventID);
        intent.putExtra("userID", userID);
        intent.putExtra("userOp", "SignUp");
        startActivity(intent);
    }

    /**
     * This method is intended to handle the situation when a check-in QR code is scanned.
     * Like the sign-up handler, this is a placeholder pending the implementation of the
     * corresponding check-in activity. When implemented, it should extract the event ID
     * from the QR code, create an intent for the check-in activity (EventActivity for check-in),
     * and start that activity.
     *
     * @param data The scanned QR code data, which should include the event ID for check-in.
     */
    private void handleCheckInQRCode(String data) {
        String eventID = data.substring(23);
        Intent intent = new Intent(QRScannerActivity.this, EventQRActivity.class);
        intent.putExtra("eventID", eventID);
        intent.putExtra("userID", userID);
        intent.putExtra("userOp", "CheckIn");
        startActivity(intent);
    }
}