package com.example.androidgpt_pro;

import android.annotation.SuppressLint;

/**
 * This is a class that contains database tools.
 */
@SuppressLint("DefaultLocale")
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
     * This is a string constructor to merge two string.
     * @param stringA
     * stringA: A string.
     * @param stringB
     * stringB: A string.
     * @return sharpString
     * sharpString: A string with merged two strings.
     */
    public String constructSharpString(String stringA, String stringB) {
        return stringA + "#" + stringB;
    }

    /**
     * This is a string split function to split a sharp string.
     * @param sharpString
     * sharpString: A string with merged two strings.
     * @return strings
     * strings: A list of strings.
     */
    public String[] splitSharpString(String sharpString) {
        return sharpString.split("#");
    }
}
