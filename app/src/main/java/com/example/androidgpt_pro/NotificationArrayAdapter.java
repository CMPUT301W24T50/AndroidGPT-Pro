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

public class NotificationArrayAdapter extends ArrayAdapter<Notification> {
    private final ArrayList<Notification> notifications;
    private final Context context;

    /**
     * this is the notification of the notification array adapter
     * @param context
     * @param notifications
     */
    public NotificationArrayAdapter(Context context, ArrayList<Notification> notifications) {
        super(context, 0, notifications);
        this.notifications = notifications;
        this.context = context;
    }

    /**
     * this function set the view of the notification content
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
                    .inflate(R.layout.notification_content, parent, false);
        }

        Notification notification = notifications.get(position);

        TextView eventName = view.findViewById(R.id.event_name);
        TextView notificationText = view.findViewById(R.id.event_notification);

        eventName.setText(notification.getEventName());
        notificationText.setText(notification.getNotificationText());

        return view;
    }
}
