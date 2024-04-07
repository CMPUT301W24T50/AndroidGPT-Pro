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

    public NotificationArrayAdapter(Context context, ArrayList<Notification> notifications) {
        super(context, 0, notifications);
        this.notifications = notifications;
        this.context = context;
    }

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
