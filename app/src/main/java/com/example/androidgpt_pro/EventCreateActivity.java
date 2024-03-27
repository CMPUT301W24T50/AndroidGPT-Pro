package com.example.androidgpt_pro;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
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
    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView selectedPicHint;
    private ImageButton backButton;
    private DatePickerDialog datePickerDialog;
    private Button eventConfirm;

    private void initViews() {
        // Initialize views.
        eventNameEditText = findViewById(R.id.edit_event_name);
        eventDateEditButton = findViewById(R.id.edit_event_date);
        eventLocationAddressEditText = findViewById(R.id.edit_street_address);
        eventLocationCityEditText = findViewById(R.id.edit_city_address);
        eventLocationProvinceEditText = findViewById(R.id.edit_province_address);
        eventTimeEditButton = findViewById(R.id.edit_event_time);
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
                eventDateEditButton.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

//        int style = android.R.style.Theme_Material_Light_Dialog_Alert;

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
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

//        int style = android.R.style.Theme_Material_Light_Dialog_Alert;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, onTimeSetListener, hour, minute, true);

        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
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



    public void setupEvent() {
        eName = eventNameEditText.getText().toString();
        eLocStreet = eventLocationAddressEditText.getText().toString();
        eLocCity = eventLocationCityEditText.getText().toString();
        eLocProvince = eventLocationProvinceEditText.getText().toString();

        // handel day picker
        initEventDatePicker();
        eventDateEditButton.setText(getTodaysDate());
        eDate = eventDateEditButton.getText().toString();

        // handel time picker
        String eventTime = hour + ":" + minute;
        eTime = eventTime.toString();

        // handel eventDescription
        eDescription = eventDescriptionEditText.getText().toString();

        // handel eventPoster
        eventSelectPicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }

        });
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
        setupEvent();

        Calendar calendar = Calendar.getInstance();
        int currentMinute = calendar.get(Calendar.MINUTE);
        //12 hour format
        int currentHour = calendar.get(Calendar.HOUR);
        //24 hour format

        eventConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // create a new event
                if(eName != null
                    && eLocStreet != null
                    && eLocCity != null
                    && eLocProvince != null
                    && eDate != null
                    && eTime != null
                    && eDescription != null
                    && eImageURI != null) {
                    newEvent();
                    // jump to next page
                    Intent newIntent = new Intent(EventCreateActivity.this, EventCreateQRActivity.class);
                    newIntent.putExtra("eventID", eID);
                    startActivity(newIntent);
                }
                else if(currentMinute <= minute && currentHour <= hour){
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
    }
}
