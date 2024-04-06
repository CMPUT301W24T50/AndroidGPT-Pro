package com.example.androidgpt_pro;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

/**
 * This is a class for working with GeoLocation.
 * Reference: https://github.com/Pritish-git/get-Current-Location
 */
public class GeoLocationActivity extends AppCompatActivity {

    private String eventID;
    private EventDatabaseControl edc;

    ImageButton btnBackButton;
    TextView tvGLTTitle;
    TextView tvGLTDescription;
    Button btnStart;
    ImageView ivRequestingPermission;
    TextView tvRequestingPermission;
    ImageView ivRequestingPermissionFail;
    TextView tvRequestingPermissionFail;
    ImageView ivDownloadingLocation;
    TextView tvDownloadingLocation;

    private LocationRequest locationRequest;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private final int REQUEST_TURN_ON_LOCATION_SERVICES = 2;
    private double latitude;
    private double longitude;


    private void initViews() {
        btnBackButton = findViewById(R.id.btn_glt_back_button);
        tvGLTTitle = findViewById(R.id.tv_glt_title);
        tvGLTDescription = findViewById(R.id.tv_glt_description);
        btnStart = findViewById(R.id.btn_glt_start);
        ivRequestingPermission = findViewById(R.id.iv_glt_requesting_permission);
        tvRequestingPermission = findViewById(R.id.tv_glt_requesting_permission);
        ivRequestingPermissionFail = findViewById(R.id.iv_glt_requesting_permission_fail);
        tvRequestingPermissionFail = findViewById(R.id.tv_glt_requesting_permission_fail);
        ivDownloadingLocation = findViewById(R.id.iv_glt_downloading_location);
        tvDownloadingLocation = findViewById(R.id.tv_glt_downloading_location);
        ivRequestingPermission.setVisibility(View.GONE);
        tvRequestingPermission.setVisibility(View.GONE);
        ivRequestingPermissionFail.setVisibility(View.GONE);
        tvRequestingPermissionFail.setVisibility(View.GONE);
        ivDownloadingLocation.setVisibility(View.GONE);
        tvDownloadingLocation.setVisibility(View.GONE);
    }

    private void setupBackButton() {
        btnBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setupStart() {
        btnStart.setEnabled(Boolean.TRUE);
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStart.setEnabled(Boolean.FALSE);
                ivRequestingPermissionFail.setVisibility(View.GONE);
                tvRequestingPermissionFail.setVisibility(View.GONE);
                locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                locationRequest.setInterval(5000);
                locationRequest.setFastestInterval(2000);
                getCurrentLocation();
            }
        });
    }


    private void getCurrentLocation() {
        getLocationPermission();
    }


    private void getLocationPermission() {
        ivRequestingPermission.setVisibility(View.VISIBLE);
        tvRequestingPermission.setVisibility(View.VISIBLE);
        if (ActivityCompat.checkSelfPermission(GeoLocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            checkLocationServices();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                checkLocationServices();
            } else {
                ivRequestingPermission.setVisibility(View.GONE);
                tvRequestingPermission.setVisibility(View.GONE);
                ivRequestingPermissionFail.setVisibility(View.VISIBLE);
                tvRequestingPermissionFail.setVisibility(View.VISIBLE);
                tvRequestingPermissionFail.setText(R.string.glt_fail_PNG);
                setupStart();
            }
        }
    }


    private void checkLocationServices() {
        LocationManager locationManager
            = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            requestLocation();
        } else {
            turnOnLocationServices();
        }
    }

    private void turnOnLocationServices() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices
            .getSettingsClient(getApplicationContext())
            .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                } catch (ApiException e) {
                    handleAPIException(e);
                }
            }
        });
    }

    private void handleAPIException(ApiException e) {
        switch (e.getStatusCode()) {
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                try {
                    ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                    resolvableApiException
                        .startResolutionForResult(GeoLocationActivity.this,
                            REQUEST_TURN_ON_LOCATION_SERVICES);
                } catch (IntentSender.SendIntentException ex) {
                    ex.printStackTrace();
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                ivRequestingPermission.setVisibility(View.GONE);
                tvRequestingPermission.setVisibility(View.GONE);
                ivRequestingPermissionFail.setVisibility(View.VISIBLE);
                tvRequestingPermissionFail.setVisibility(View.VISIBLE);
                tvRequestingPermissionFail.setText(R.string.glt_fail_NLS);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_TURN_ON_LOCATION_SERVICES) {
            if (resultCode == Activity.RESULT_OK) {
                requestLocation();
            } else {
                ivRequestingPermission.setVisibility(View.GONE);
                tvRequestingPermission.setVisibility(View.GONE);
                ivRequestingPermissionFail.setVisibility(View.VISIBLE);
                tvRequestingPermissionFail.setVisibility(View.VISIBLE);
                tvRequestingPermissionFail.setText(R.string.glt_fail_SNE);
                setupStart();
            }
        }
    }


    @SuppressLint("MissingPermission")
    private void requestLocation() {
        ivRequestingPermission.setVisibility(View.GONE);
        tvRequestingPermission.setVisibility(View.GONE);
        ivDownloadingLocation.setVisibility(View.VISIBLE);
        tvDownloadingLocation.setVisibility(View.VISIBLE);
        LocationServices.getFusedLocationProviderClient(GeoLocationActivity.this)
            .requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    LocationServices
                        .getFusedLocationProviderClient(GeoLocationActivity.this)
                        .removeLocationUpdates(this);
                    if (!locationResult.getLocations().isEmpty()) {
                        int index = locationResult.getLocations().size() - 1;
                        latitude = locationResult.getLocations().get(index).getLatitude();
                        longitude = locationResult.getLocations().get(index).getLongitude();
                        pushLocationThenBack();
                    }
                }
            }, Looper.getMainLooper());
    }


    private void pushLocationThenBack() {
        edc.addEventCheckInLocation(eventID, String.valueOf(latitude), String.valueOf(longitude));
        ivDownloadingLocation.setVisibility(View.GONE);
        tvDownloadingLocation.setVisibility(View.GONE);
        setResult(RESULT_OK);
        finish();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geolocation);

        Intent intent = getIntent();
        eventID = intent.getStringExtra("eventID");
        edc = new EventDatabaseControl();

        initViews();
        setupBackButton();
        setupStart();
    }
}
