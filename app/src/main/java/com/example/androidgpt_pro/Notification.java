package com.example.androidgpt_pro;

public class Notification {

    private final String eID;
    private String eventName;
    private String notificationText;

    /**
     * this is the constructor for the notification
     * @param eventID
     * @param eventName
     * @param notificationText
     */
    public Notification(String eventID, String eventName, String notificationText) {
        this.eID = eventID;
        this.eventName = eventName;
        this.notificationText = notificationText;
    }

    /**
     * this is the getter of eventID
     * @return
     */
    public String getEventID() {
        return eID;
    }

    /**
     * this is the getter of event name
     * @return
     */
    public String getEventName() {
        return eventName;
    }

    /**
     * this is the setter of the event name
     * @param eventName
     */
    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    /**
     * this is the getter of notification
     * @return
     */
    public String getNotificationText() {
        return notificationText;
    }

    /**
     * this is the setter of the notification text
     * @param notificationText
     */
    public void setNotificationText(String notificationText) {
        this.notificationText = notificationText;
    }
}
