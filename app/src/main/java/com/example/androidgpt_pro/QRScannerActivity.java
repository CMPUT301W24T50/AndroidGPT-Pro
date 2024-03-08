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