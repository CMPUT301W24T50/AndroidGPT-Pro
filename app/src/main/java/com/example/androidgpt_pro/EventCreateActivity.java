package com.example.androidgpt_pro;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

import java.util.Calendar;
import java.util.Locale;


public class EventCreateActivity extends AppCompatActivity {

    private EventDatabaseControl edc;

    private String eID;
    private String eName;
    private String eLocStreet;
    private String eLocCity;
    private String eLocProvince;
    private String eTime;
    private String eDate;
    private String eDescription;
    private Uri eImageURI;
    private EditText eventNameEditText;
    private Button eventDateEditButton;
    private EditText eventLocationAddressEditText;
    private EditText eventLocationCityEditText;
    private EditText eventLocationProvinceEditText;
    private Button eventTimeEditButton;
    private int hour, minute;
    private EditText eventDescriptionEditText;
    private Switch geoLcationTracking;
    private Button eventSelectPicButton;
    private Button eventConfirm;
    private ImageButton backButton;
    private DatePickerDialog datePickerDialog;

    private void initViews() {
        // Initialize views.
        eventNameEditText = findViewById(R.id.edit_event_name);
        eventDateEditButton = findViewById(R.id.edit_event_date);
        eventLocationAddressEditText = findViewById(R.id.edit_street_address);
        eventLocationCityEditText = findViewById(R.id.edit_city_address);
        eventLocationProvinceEditText = findViewById(R.id.edit_province_address);
        eventTimeEditButton = findViewById(R.id.edit_event_time);
        eventSelectPicButton = findViewById(R.id.select_pic);
        eventConfirm = findViewById(R.id.confirm_create_event);
        backButton = findViewById(R.id.back_button);
        eventDescriptionEditText = findViewById(R.id.edit_event_description);

    }

    private void setupBackButton() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void initEventDatePicker() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month = month + 1;
                String date = makeDateString(day, month, year);
                eventDateEditButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        int style = AlertDialog.THEME_HOLO_LIGHT;

        datePickerDialog = new DatePickerDialog(this, style, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        if (month == 1)
            return "JAN";
        if (month == 2)
            return "FEB";
        if (month == 3)
            return "MAR";
        if (month == 4)
            return "APR";
        if (month == 5)
            return "MAY";
        if (month == 6)
            return "JUN";
        if (month == 7)
            return "JUL";
        if (month == 8)
            return "AUG";
        if (month == 9)
            return "SEP";
        if (month == 10)
            return "OCT";
        if (month == 11)
            return "NOV";
        if (month == 12)
            return "DEC";

        // this should never happen
        return "JAN";
    }

    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    public void openEventDatePicker(View view) {
        datePickerDialog.show();
    }

    public void popEventTimePicker(View view) {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute) {
                hour = selectedHour;
                minute = selectedMinute;
                eventTimeEditButton.setText(String.format(Locale.getDefault(), "%02d: %02d", hour, minute));
            }
        };

        int style = AlertDialog.THEME_HOLO_DARK;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }


    public void setupEvent() {
        eName = eventNameEditText.getText().toString();
        eLocStreet = eventLocationAddressEditText.getText().toString();
        eLocCity = eventLocationCityEditText.getText().toString();
        eLocProvince = eventLocationProvinceEditText.getText().toString();

        // handel day picker
        initEventDatePicker();
        eventDateEditButton.setText(getTodaysDate());
        eTime = eventDateEditButton.getText().toString();

        // handel time picker
        String eventTime = hour + ":" + minute;
        eTime = eventTime.toString();

        // handel eventDescription
        eDescription = eventDescriptionEditText.getText().toString();

//        eImageURI = eventImageURI;
    }


    public void newEvent() {
        edc.getEventStat()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot docSns) {
                        String lastEventID = edc.getLastEventID(docSns);
                        eID = edc.updateEventStat(lastEventID);
                        edc.initEvent(eID, eName, eLocStreet, eLocCity, eLocProvince,
                                eTime, eDate, eDescription, eImageURI);
                    }
                });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        edc = new EventDatabaseControl();

        initViews();
    }
}
