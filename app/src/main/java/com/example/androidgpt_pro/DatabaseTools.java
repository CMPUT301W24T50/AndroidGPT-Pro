package com.example.androidgpt_pro;

/**
 * This is a class that contains database tools.
 */
public class DatabaseTools {

    /**
     * This is a calculator to get the next string number.
     * @param value
     * value: An 8 bits string number.
     * @return valueAddOne
     * valueAddOne: 8-digit number after add 1.
     */
    public String calculateAddOne(String value) {
        int intValue = Integer.parseInt(value);
        return String.format("%08d", ++intValue);
    }

    /**
     * This is a calculator to get the previous string number.
     * @param value
     * value: An 8 bits string number.
     * @return valueMinusOne
     * valueMinusOne: 8-digit number after minus 1.
     */
    public String calculateMinusOne(String value) {
        int intValue = Integer.parseInt(value);
        return String.format("%08d", --intValue);
    }

    /**
     * This is a string constructor to merge an string ID and a string count.
     * @param someID
     * someID: A profileID or an eventID.
     * @param count
     * count: A string number.
     * @return idCountString
     * idCountString: A string with merged ID and count.
     */
    public String constructIDCountString(String someID, String count) {
        return someID + "#" + count;
    }
}
