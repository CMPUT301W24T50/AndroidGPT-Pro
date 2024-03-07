package com.example.androidgpt_pro;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.activity.ComponentActivity;
import com.journeyapps.barcodescanner.ScanOptions;
import com.journeyapps.barcodescanner.ScanContract;
import androidx.activity.result.ActivityResultLauncher;

public class QRScannerActivity extends ComponentActivity {

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
        // TBD - Determine if it's a signup QR code
        return data.startsWith("SIGNUP_");
    }

    private boolean isCheckInQRCode(String data) {
        // TBD - Determine if it's a check-in QR code
        return data.startsWith("CHECKIN_");
    }

    private void handleSignupQRCode(String data) {
        // TBD -  Handle a signup QR code
        String eventId = extractEventId(data);
        Intent intent = new Intent(QRScannerActivity.this, EventxxxActivity.class);
        intent.putExtra("EVENT_ID", eventId);
        startActivity(intent);
    }

    private void handleCheckInQRCode(String data) {
        // TBD - Handle a check-in QR code
        String eventId = extractEventId(data);
        Intent intent = new Intent(QRScannerActivity.this, EventxxxActivity.class);
        intent.putExtra("EVENT_ID", eventId);
        startActivity(intent);
    }

    private String extractEventId(String qrCodeData) {
        // TBD -  Extracting the event ID from the QR code data
        return qrCodeData.split("_")[1];
    }
}
