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
import java.util.ArrayList;
public class EventArrayAdapter extends ArrayAdapter<EventDatabaseControl>{
    public EventArrayAdapter(Context context, ArrayList<EventDatabaseControl> events){
        super(context,0,events);
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

        EventDatabaseControl event = getItem(position);
        TextView eventName = view.findViewById(R.id.event_name);
        TextView eventDescription = view.findViewById(R.id.event_description);
        TextView eventDate = view.findViewById(R.id.event_date);
        TextView eventLocationApt = view.findViewById(R.id.event_location1);
        TextView eventLocationCity = view.findViewById(R.id.event_location2);
//        ImageView eventImage = view.findViewById(R.id.event_image);

        // not sure...
        String eventID = event.createEvent();

        eventName.setText(event.getEventName(eventID));
        eventDate.setText(event.getEventTime(eventID));
        eventLocationApt.setText(event.getEventLocation(eventID));
        eventLocationCity.setText(event.getEventSimplifiedLocation(eventID));
        eventDescription.setText(event.getEventDescription(eventID));
        return view;

    }
}
