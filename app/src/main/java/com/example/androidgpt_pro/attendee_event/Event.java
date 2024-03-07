package com.example.androidgpt_pro.attendee_event;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;
public class Event implements Parcelable {
    private String eventName;
    private String eventLocationApt;
    private String eventLocationCity;
    private String eventId;
    //  private ArrayList<Attendee> attendees;
    private String eventDate;
    private String eventDescription;
    private String eventImageUrl; // not being used for now

    // checkbox/signup/check in -> profile

    public String getEventImageUrl() {
        return eventImageUrl;
    }

    public void setEventImageUrl(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getEventLocationApt() {
        return eventLocationApt;
    }

    public void setEventLocationApt(String eventLocationApt) {
        this.eventLocationApt = eventLocationApt;
    }

    public String getEventLocationCity() {
        return eventLocationCity;
    }

    public void setEventLocationCity(String eventLocationCity) {
        this.eventLocationCity = eventLocationCity;
    }

    public String getEventId() {
        return eventId;
    }

    public void setEventId(String eventId) {
        this.eventId = eventId;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public void setEventDescription(String eventDescription) {
        this.eventDescription = eventDescription;
    }

    public Event(String eventName, String eventLocationApt, String eventLocationCity, String eventId, String eventDate, String eventDescription, String eventImageUrl) {
        this.eventName = eventName;
        this.eventLocationApt = eventLocationApt;
        this.eventLocationCity = eventLocationCity;
        this.eventId = eventId;
        this.eventDate = eventDate;
        this.eventDescription = eventDescription;
        this.eventImageUrl = eventImageUrl;
    }


}
