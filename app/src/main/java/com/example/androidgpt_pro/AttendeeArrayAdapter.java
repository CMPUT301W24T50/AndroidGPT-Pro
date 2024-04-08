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

    /**
     * this get the view from the array content
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
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
