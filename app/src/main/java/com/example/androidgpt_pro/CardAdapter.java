package com.example.androidgpt_pro;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Inspiration taken from https://github.com/bel3arabyapps/ListView-and-CardView
 * This class is the adapter to be used to display the Event Cards in a listView
 */
public class CardAdapter extends ArrayAdapter<EventCard> {

    /**
     * This is the constructor for the CardAdapter class
     * @param context
     * context of constructor being called
     */
    public CardAdapter(Context context) {
        super(context, R.layout.event_card);
    }

    /**
     * Inflates the card view
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return convertView
     * the view to display
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            convertView = inflater.inflate(R.layout.event_card, parent, false);
            holder = new ViewHolder(convertView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        EventCard card = getItem(position);
        holder.eventPoster.setImageResource(card.getImageId());
        holder.eventName.setText(card.getEventName());
        holder.eventTime.setText(card.getEventTime());
        holder.eventDate.setText(card.getEventDate());
        holder.eventLocation.setText(card.getEventLocation());

        return convertView;
    }

    /**
     * This class holds all the Views to be used within the card
     */
    static class ViewHolder {
        ImageView eventPoster;
        TextView eventName;
        TextView eventTime;
        TextView eventDate;
        TextView eventLocation;

        /**
         * Constructor for the ViewHolder class
         * @param view
         * the view to place children views within
         */
        ViewHolder(View view) {
            eventPoster = view.findViewById(R.id.event_image);
            eventName = view.findViewById(R.id.event_name);
            eventTime = view.findViewById(R.id.event_time);
            eventDate = view.findViewById(R.id.event_date);
            eventLocation = view.findViewById(R.id.event_location);
        }
    }
}
