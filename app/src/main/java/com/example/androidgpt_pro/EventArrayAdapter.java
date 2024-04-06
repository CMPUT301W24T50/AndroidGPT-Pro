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
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * This class is the adapter to be used to display the Event Cards in a listView.
 */
public class EventArrayAdapter extends ArrayAdapter<Event> {

    private final ArrayList<Event> events;
    private final Context context;


    /**
     * This is the constructor of the class EventArrayAdapter.
     *
     * @param context        context: The context.
     * @param events         events: A list of event.
     */
    public EventArrayAdapter(Context context, ArrayList<Event> events) {
        super(context, 0, events);
        this.events = events;
        this.context = context;
    }

    /**
     * This is a getter of a view.
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return view
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.event_array_adapter, parent, false);
        }

        Event event = events.get(position);

        TextView eNameTV = view.findViewById(R.id.event_name);
        TextView eLocationTV = view.findViewById(R.id.event_location);
        TextView eTimeTV = view.findViewById(R.id.event_time);
        TextView eDateTV = view.findViewById(R.id.event_date);
        ImageView eImageIV = view.findViewById(R.id.event_image);

        eNameTV.setText(event.getEventName());
        String eventCityProvince = event.getEventLocationCity()
                + ", " + event.getEventLocationProvince();
        eLocationTV.setText(eventCityProvince);
        eTimeTV.setText(event.getEventTime());
        eDateTV.setText(event.getEventDate());
        Picasso.get().load(event.getEventImageUri()).into(eImageIV);

        return view;
    }

}
