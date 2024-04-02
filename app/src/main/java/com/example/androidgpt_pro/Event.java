package com.example.androidgpt_pro;

import android.net.Uri;

/**
 * This is a class representing Event.
 */
public class Event {

    private final String eID;

    private String eName;
    private String eLocationCity;
    private String eLocationProvince;
    private String eTime;
    private String eDate;
    private Uri eImageUri;


    /**
     * This is the constructor of class Event.
     * @param eventID
     * eventID: The event's ID.
     * @param eventName
     * eventName: The event's name.
     * @param eventLocationCity
     * eventLocationCity: The event's city location.
     * @param eventLocationProvince
     * eventLocationProvince: The event's province location.
     * @param eventTime
     * eventTime: The event's time.
     * @param eventDate
     * eventDate: The event's date.
     * @param eventImageUri
     * eventImageUri: The event's image uri.
     */
    public Event(String eventID,
                 String eventName,
                 String eventLocationCity,
                 String eventLocationProvince,
                 String eventTime,
                 String eventDate,
                 Uri eventImageUri) {
        eID = eventID;
        eName = eventName;
        eLocationCity = eventLocationCity;
        eLocationProvince = eventLocationProvince;
        eTime = eventTime;
        eDate = eventDate;
        eImageUri = eventImageUri;
    }


    /**
     * This is a getter of eventID.
     * @return eventID
     * eventID: The event's ID.
     */
    public String getEventID() {
        return eID;
    }

    /**
     * This is a getter of eventName.
     * @return eventName
     * eventName: The event's name.
     */
    public String getEventName() {
        return eName;
    }

    /**
     * This is a getter of eventLocationCity.
     * @return eventLocationCity
     * eventLocationCity: The event's city location.
     */
    public String getEventLocationCity() {
        return eLocationCity;
    }

    /**
     * This is a getter of eventLocationProvince.
     * @return eventLocationProvince
     * eventLocationProvince: The event's province location.
     */
    public String getEventLocationProvince() {
        return eLocationProvince;
    }

    /**
     * This is a getter of eventTime.
     * @return eventTime
     * eventTime: The event's time.
     */
    public String getEventTime() {
        return eTime;
    }

    /**
     * This is a getter of eventDate.
     * @return eventDate
     * eventDate: The event's date.
     */
    public String getEventDate() {
        return eDate;
    }

    /**
     * This is a getter of eventImageUri.
     * @return eventImageUri
     * eventImageUri: The event's image uri.
     */
    public Uri getEventImageUri() {
        return eImageUri;
    }
}
