package com.example.campusconnect;

// TODO Use Network Time Protocol / Time Zone

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static int getNumberOfDays(String month, String year) {
        int day = 0;
        if(month.equals("January") || month.equals("March") || month.equals("May") || month.equals("July") || month.equals("August") || month.equals("October") || month.equals("December")) {
            day = 31;
        }
        else if (month.equals("April") || month.equals("June") || month.equals("September") || month.equals("November")){
            day = 30;
        }
        else if (month.equals("February")){
            int yr = Integer.parseInt(year);
            // Check year if Leap Year
            if((yr % 400 == 0) || (yr % 4 == 0 && yr % 100 != 0)){
                day = 29;
            }
            else {
                day = 28;
            }
        }
        return day;
    }

    public static String getCurrentDate(){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentMonth(){
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getMonthName(String month) {
        int monthInt = Integer.parseInt(month);
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[monthInt-1];
    }

    public static String getCurrentDay(){
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentYear(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    public static String getCurrentTime(){
        DateFormat dateFormat = new SimpleDateFormat("hh:mm a");
        Date date = new Date();
        return dateFormat.format(date).toUpperCase();
    }
}