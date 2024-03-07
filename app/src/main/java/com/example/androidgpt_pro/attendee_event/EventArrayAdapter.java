package com.example.androidgpt_pro.attendee_event;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import com.example.androidgpt_pro.R;
import java.util.ArrayList;

public class EventArrayAdapter extends ArrayAdapter<Event> {
    public EventArrayAdapter(Context context, ArrayList<Event> events){
        super(context,0,events);
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent){
        View view;
        if(convertView == null){
            view = LayoutInflater.from(getContext()).inflate(R.layout.event_content_detail, parent, false);
        } else {
            view = convertView;
        }

        Event event = getItem(position);
        TextView eventName = view.findViewById(R.id.event_name);
        TextView eventDescription = view.findViewById(R.id.event_description);
        TextView eventDate = view.findViewById(R.id.event_date);
        TextView eventLocationApt = view.findViewById(R.id.event_location1);
        TextView eventLocationCity = view.findViewById(R.id.event_location2);
        ImageView eventImage = view.findViewById(R.id.event_image);

        eventName.setText(event.getEventName());
        eventDate.setText(event.getEventDate());
        eventLocationApt.setText(event.getEventLocationApt());
        eventLocationCity.setText(event.getEventLocationCity());
        eventDescription.setText(event.getEventDescription());
        Glide.with(view.getContext())
                .load(event.getEventImageUrl())
                .into(eventImage);


        return view;
    }
}
