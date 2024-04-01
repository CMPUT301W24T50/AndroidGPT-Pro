package com.example.androidgpt_pro;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;
public class AttendeeArrayAdapter extends ArrayAdapter<Attendee> {

    private final ArrayList<Attendee> attendees;
    private final Context context;

    /**
     * This is the constructor of the class AttendeeAdapter
     * @param context
     * context: The context.
     * @param attendees
     * events: A list of event.
     */
    public AttendeeArrayAdapter(Context context, ArrayList<Attendee> attendees) {
        super(context, 0, attendees);
        this.attendees = attendees;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @Nullable ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context)
                    .inflate(R.layout.attendee_list_content, parent, false);
        }

        Attendee attendee = attendees.get(position);

        TextView attendeeName = view.findViewById(R.id.attendee_list_name);
        TextView attendeeCheckedInCount = view.findViewById(R.id.checked_in_count);

        attendeeName.setText(attendee.getName());
        attendeeCheckedInCount.setText(attendee.getCheckedInCount());

        return view;
    }
}
