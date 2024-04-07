package com.example.androidgpt_pro;

public class Notification {

    private final String eID;
    private String eventName;
    private String notificationText;

    public Notification(String eventID, String eventName, String notificationText) {
        this.eID = eventID;
        this.eventName = eventName;
        this.notificationText = notificationText;
    }

    public String getEventID() {
        return eID;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getNotificationText() {
        return notificationText;
    }

    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }
}
