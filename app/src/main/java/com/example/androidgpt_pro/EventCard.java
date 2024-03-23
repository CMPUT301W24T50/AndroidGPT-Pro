package com.example.androidgpt_pro;

import android.widget.ImageView;

/**
 * This class handles the Event Cards to be placed in the Events List
 * in the Browse Events screen. Populates card with event poster and
 * basic event information
 */
public class EventCard {
    private String eventName;
    private String eventTime;
    private String eventDate;
    private String eventLocation;
    private int imageId;
    private Boolean signedUp;

    /**
     * Constructor for the EventCard class
     * @param eventName
     * String name of the event
     * @param eventTime
     * String time of the event
     * @param eventDate
     * String date of the event
     * @param eventLocation
     * String location of the event
     * @param imageId
     * int ID of the image to be used as the poster
     * @param signedUp
     * Boolean true if user has signed up for the event
     */
    public EventCard(String eventName, String eventTime, String eventDate, String eventLocation, int imageId, Boolean signedUp) {
        this.eventName = eventName;
        this.eventTime = eventTime;
        this.eventDate = eventDate;
        this.eventLocation = eventLocation;
        this.imageId = imageId;
        this.signedUp = signedUp;
    }

    /**
     * Getter for eventName
     * @return String eventName
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * Setter for eventName
     * @param eventName
     * String containing event name
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * Getter for EventTime
     * @return String eventTime
     */
    public String getEventTime() {
        return eventTime;
    }

    /**
     * Setter for eventTime
     * @param eventTime
     * String containing event time
     */
    public void setEventTime(String eventTime) {
        this.eventTime = eventTime;
    }

    /**
     * Getter for EventDate
     * @return String eventDate
     */
    public String getEventDate() {
        return eventDate;
    }

    /**
     * Setter for eventDate
     * @param eventDate
     * String containing event date
     */
    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    /**
     * Getter for EventLocation
     * @return String eventLocation
     */
    public String getEventLocation() {
        return eventLocation;
    }

    /**
     * Setter for eventLocation
     * @param eventLocation
     * String containing event location
     */
    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    /**
     * Getter for poster image Id
     * @return int imageId
     */
    public int getImageId() {
        return imageId;
    }

    /**
     * Setter for poster image ID
     * @param imageId
     * Int containing id of image to use as poster
     */
    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    /**
     * Getter for user sign up status
     * @return Boolean signedUp
     */
    public Boolean getSignedUp() {
        return signedUp;
    }

    /**
     * Setter for signedUp value
     * @param signedUp
     * Boolean value if user has signed up for this event
     */
    public void setSignedUp(Boolean signedUp) {
        this.signedUp = signedUp;
    }
}
