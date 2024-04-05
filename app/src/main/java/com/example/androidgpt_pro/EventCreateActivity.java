package com.example.androidgpt_pro;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Locale;


public class EventCreateActivity extends AppCompatActivity {

    private String userID;
    private EventDatabaseControl edc;
    private String eID;
    private String eName;
    private String eLocStreet;
    private String eLocCity;
    private String eLocProvince;
    private String eTime;
    private String eDateFormat;
    private String eDate;
    private String eDescription;
    private Boolean eGLTState;
    private Uri eImageURI;
    private ImageButton backButton;
    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText eventNameEditText;
    private Button eventDateEditButton;
    private EditText eventLocationAddressEditText;
    private EditText eventLocationCityEditText;
    private EditText eventLocationProvinceEditText;
    private Button eventTimeEditButton;
    private EditText eventDescriptionEditText;
    private Switch eventGeoLocationTrackingSwitch;
    private Button eventSelectPicButton;
    private TextView selectedPicHint;
    private DatePickerDialog datePickerDialog;
    private Button eventConfirm;
    private int hour, minute;
    private long eTimestamp;


    private void initViews() {
        // Initialize views.
        eventNameEditText = findViewById(R.id.edit_event_name);
        eventDateEditButton = findViewById(R.id.edit_event_date);
        eventLocationAddressEditText = findViewById(R.id.edit_street_address);
        eventLocationCityEditText = findViewById(R.id.edit_city_address);
        eventLocationProvinceEditText = findViewById(R.id.edit_province_address);
        eventTimeEditButton = findViewById(R.id.edit_event_time);
        eventGeoLocationTrackingSwitch = findViewById(R.id.geo_location_switch);
        eventSelectPicButton = findViewById(R.id.select_pic);
        selectedPicHint = findViewById(R.id.selected_pic);
        backButton = findViewById(R.id.back_button);
        eventDescriptionEditText = findViewById(R.id.edit_event_description);
        eventConfirm = findViewById(R.id.confirm_create_event);
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
                eDateFormat = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month, day);
                eventDateEditButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;
    }

    private String getMonthFormat(int month) {
        String[] lst = {"JAN", "FEB", "MAR", "APR",
                "MAY", "JUN", "JUL", "AUG",
                "SEP", "OCT", "NOV", "DEC"};
        return lst[month - 1];
    }

    private String getToday() {
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

//        int style = android.R.style.Theme_Material_Light_Dialog_Alert;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }


    private void setupEventImageSelector() {
        // handel eventPoster
        eventSelectPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                PICK_IMAGE_REQUEST);
    }

    /**
     * This is a method to handle the result of image selection.
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode The integer result code returned by the child activity
     *                   through its setResult().
     * @param data An Intent, which can return result data to the caller
     *               (various data can be attached to Intent "extras").
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            // Store the selected image URI.
            eImageURI = data.getData();
            // Set the selected image to the profile image view.
            eventSelectPicButton.setText("Change Poster");
            selectedPicHint.setText("You Have Uploaded a Poster!");
        }
    }

    /**
     * This is a method to set all variables in the creating event part
     */
    public void setEvent() {
        eName = eventNameEditText.getText().toString();
        eLocStreet = eventLocationAddressEditText.getText().toString();
        eLocCity = eventLocationCityEditText.getText().toString();
        eLocProvince = eventLocationProvinceEditText.getText().toString();

        // handel day picker
        eDate = eventDateEditButton.getText().toString();

        // handel time picker
        String eventTime = String.format(Locale.getDefault(), "%02d:%02d", hour, minute);
        String eTimestampFormat = eDateFormat + " " + eventTime + ":00.000";
        // Parse the string to a Timestamp object
        Timestamp timestamp = Timestamp.valueOf(eTimestampFormat);
        eTimestamp = timestamp.getTime();

        eTime = eventTime;

        // handel eventDescription
        eDescription = eventDescriptionEditText.getText().toString();

        eGLTState = eventGeoLocationTrackingSwitch.isChecked();
    }

    /**
     * This is a method to set upload variables in the creating event part to the database
     */
    public void applyNewEvent() {
        edc.getEventStat().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot docSns) {
                String lastEventID = edc.getLastEventID(docSns);
                eID = edc.updateEventStat(lastEventID);
                edc.initEvent(eID, userID, eName, eLocStreet, eLocCity, eLocProvince,
                    eTime, eDate, eDescription, eGLTState, eImageURI);
                createCompleted();
            }
        });
    }

    private void createCompleted() {
        // jump to next page
        Intent newIntent = new Intent(EventCreateActivity.this, EventCreateQRActivity.class);
        newIntent.putExtra("eventID", eID);
        startActivity(newIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        Intent intent = getIntent();
        userID = intent.getStringExtra("userID");
        edc = new EventDatabaseControl();

        initViews();
        setupBackButton();
        setupEventImageSelector();

        // get current timestamp
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        long currentTimestamp = timestamp.getTime();
        initEventDatePicker();
        eventDateEditButton.setText(getToday());

        eventConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setEvent();
                // create a new event
                if(eName != null && eLocStreet != null
                        && eLocCity != null && eLocProvince != null
                        && eDate != null && eTime != null
                        && eDescription != null && eImageURI != null
                        && eTimestamp > currentTimestamp) {
                    applyNewEvent();
                }
                else if(eTimestamp < currentTimestamp){
                    CharSequence text = "Please Select a Valid Time";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(EventCreateActivity.this, text, duration);
                    toast.show();
                }
                else {
                    CharSequence text = "Please Fill up all Messages";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(EventCreateActivity.this, text, duration);
                    toast.show();
                }
            }
        });
        setupBackButton();
    }
}
