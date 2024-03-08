package com.example.androidgpt_pro;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.example.androidgpt_pro.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class manages the array of events to be displayed in the EventBrowseActivity
 */
public class EventArrayAdapter extends ArrayAdapter<EventDatabaseControl>{

    private String eID;

    public EventArrayAdapter(Context context, ArrayList<EventDatabaseControl> events, String eventID){
        super(context,0,events);
        eID = eventID;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_content, parent, false);
        } else {
            view = convertView;
        }

        EventDatabaseControl edc = getItem(position);
        TextView eventName = view.findViewById(R.id.event_name);
        TextView eventDescription = view.findViewById(R.id.event_description);
        TextView eventDate = view.findViewById(R.id.event_date);
        TextView eventLocationApt = view.findViewById(R.id.event_location1);
        TextView eventLocationCity = view.findViewById(R.id.event_location2);
//        ImageView eventImage = view.findViewById(R.id.event_image);


        edc.getEventSnapshot(eID)
            .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot docSns) {
                    eventName.setText(edc.getEventName(docSns));
                    eventLocationApt.setText(edc.getEventLocation(docSns));
                    eventLocationCity.setText(edc.getEventSimplifiedLocation(docSns));
                    eventDescription.setText(edc.getEventDescription(docSns));
                    eventDate.setText(edc.getEventTime(docSns));
                }
            });
        return view;
    }
}
