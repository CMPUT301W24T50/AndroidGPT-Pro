package com.example.androidgpt_pro;

import android.annotation.SuppressLint;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

/**
 * This is a class that contains database tools.
 */
@SuppressLint("DefaultLocale")
public class DatabaseTools {

    /**
     * This is a formatter to format the given integer to an 8 bits string number..
     * @param value
     * value: An integer.
     * @return formattedValue
     * formattedValue: An 8 bits string number.
     */
    public String formatEightBits(int value) {
        return String.format("%08d", value);
    }

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
     * @param stringC
     * stringC: A string.
     * @return sharpString
     * sharpString: A string with merged two strings.
     */
    public String constructSharpString(String stringA, String stringB, String stringC) {
        if (stringC == null)
            return stringA + "#" + stringB;
        return stringA + "#" + stringB + "#" + stringC;
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

    /**
     * This is a compare function to compare two dates.
     * @param dateA
     * dateA: A date.
     * @param dateB
     * dateB: A date.
     * @return isBigOrEqual
     * isBigOrEqual: True if dateA is big or equal to dateB, False otherwise.
     */
    public Boolean compareDateBigOrEqual(String dateA, String dateB) {
        String[] lstDateA = dateA.split(" ");
        String[] lstDateB = dateB.split(" ");
        List<String> month = Arrays.asList("JAN", "FEB", "MAR", "APR",
                "MAY", "JUN", "JUL", "AUG",
                "SEP", "OCT", "NOV", "DEC");
        if (Integer.parseInt(lstDateA[2]) >= Integer.parseInt(lstDateB[2])
                && month.indexOf(lstDateA[0]) >= month.indexOf(lstDateB[0])
                && Integer.parseInt(lstDateA[2]) >= Integer.parseInt(lstDateB[2]))
            return Boolean.TRUE;
        return Boolean.FALSE;
    }

    public String getDateToday() {
        Calendar cal = Calendar.getInstance();
        String[] monthList = {"JAN", "FEB", "MAR", "APR",
                "MAY", "JUN", "JUL", "AUG",
                "SEP", "OCT", "NOV", "DEC"};
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return monthList[month - 1] + " " + day + " " + year;
    }
}
