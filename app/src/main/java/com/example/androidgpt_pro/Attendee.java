package com.example.androidgpt_pro;

/**
 * This is a class representing Attendee(in organizer).
 */
public class Attendee {
    private final String pID;
    private String name;
    private String checkedInCount;

    /**
     * This is the constructor of class attendee.
     * @param profileID
     * this is the id of profile
     * @param name
     * this is the name of attendees
     * @param checkedInCount
     * this count the checked in number of each attendees
     */
    public Attendee( String profileID, String name, String checkedInCount) {
        this.pID = profileID;
        this.name = name;
        this.checkedInCount = checkedInCount;
    }

    /**
     * This is a getter of eventID.
     * @return eventID
     */

    /**
     * This is a getter of getProfileID.
     * @return profileID
     */
    public String getProfileID() {
        return pID;
    }

    /**
     * This is a getter of attendees' names
     * @return attendees' name
     */
    public String getName() {
        return name;
    }

    /**
     * this is a setter of attendees' name
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * this is a getter of counting of attendees' checked in
     * @return checkedInCount
     */
    public String getCheckedInCount() {
        return checkedInCount;
    }

    /**
     * this is a setter of counting of attendees' checked in
     * @param checkedInCount
     */
    public void setCheckedInCount(String checkedInCount) {
        this.checkedInCount = checkedInCount;
    }

}
