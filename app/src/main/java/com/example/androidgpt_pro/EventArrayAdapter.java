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

public class EventArrayAdapter extends ArrayAdapter<EventDatabaseControl>{

    private CollectionReference colRef;
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

        EventDatabaseControl event = getItem(position);
        TextView eventName = view.findViewById(R.id.event_name);
        TextView eventDescription = view.findViewById(R.id.event_description);
        TextView eventDate = view.findViewById(R.id.event_date);
        TextView eventLocationApt = view.findViewById(R.id.event_location1);
        TextView eventLocationCity = view.findViewById(R.id.event_location2);
//        ImageView eventImage = view.findViewById(R.id.event_image);


        colRef.document(eID).get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot doc) {
                        eventName.setText(doc.getString("eName"));
                        eventDate.setText(doc.getString("eTime"));
                        eventLocationApt.setText(doc.getString("eLocation"));
                        eventLocationCity.setText(doc.getString("eSpfLocation"));
                        eventDescription.setText(doc.getString("eDescription"));
                    }
                });
        return view;
    }
}
