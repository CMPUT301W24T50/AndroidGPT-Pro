package com.example.androidgpt_pro.attendee_event;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.androidgpt_pro.R;

import java.util.ArrayList;

public class EventMainActivity extends AppCompatActivity {
    private ListView eventList;
    private ArrayList<Event> eventDataList;
    private ArrayAdapter<Event> eventAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_event_list);

        eventList = findViewById(R.id.event_list);
        eventDataList = new ArrayList<>();
        eventAdapter = new EventArrayAdapter(this, eventDataList);
        eventList.setAdapter(eventAdapter);

        //click the event to open the detail page
        //TODO:if the attendee has sign up jump to withdraw page
        eventList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String eventSimple =
            }
        });

    }

    
}
