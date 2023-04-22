package com.example.campusconnect;

// TODO Use Network Time Protocol / Time Zone

import android.util.Log;

import org.apache.commons.net.ntp.NTPUDPClient;
import org.apache.commons.net.ntp.TimeInfo;
import java.net.InetAddress;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {
    private static Date getCurrentTimeFromNTP() throws Exception {
        String[] hosts = new String[]{
                "pool.ntp.org",
                "asia.pool.ntp.org",
                "0.asia.pool.ntp.org",
                "1.asia.pool.ntp.org",
                "2.asia.pool.ntp.org",
                "3.asia.pool.ntp.org",
                "ph.pool.ntp.org"
        };
        NTPUDPClient client = new NTPUDPClient();
        client.setDefaultTimeout(5000);
        for (String host : hosts) {
            try {
                InetAddress hostAddr = InetAddress.getByName(host);
                client.open();
                client.setSoTimeout(5000);
                TimeInfo info = client.getTime(hostAddr);
                return new Date(info.getReturnTime());
            } catch (Exception e) {
                Log.d("TAG", "Failed to get time from " + host);
                e.printStackTrace();
            } finally {
                client.close();
            }
        }
        // If we're unable to get the time from any of the NTP servers,
        // fall back to using the device's local clock
        Log.d("TAG", "Failed to get time from all NTP servers. Using local time instead.");
        return new Date();
    }

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
        try {
            Date date = getCurrentTimeFromNTP();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm:ss a");
            return dateFormat.format(date);
        } catch (Exception e) {
            // Handle exception as per your requirement
            return null;
        }
    }

    public static String getCurrentMonth(){
        try {
            Date date = getCurrentTimeFromNTP();
            SimpleDateFormat dateFormat = new SimpleDateFormat("MM");
            return dateFormat.format(date);
        } catch (Exception e) {
            // Handle exception as per your requirement
            return null;
        }
    }

    public static String getMonthName(String month) {
        int monthInt = Integer.parseInt(month);
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[monthInt-1];
    }

    public static int getMonthNumber(String monthName){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        for(int i = 0 ; i < monthNames.length ; i++){
            if(monthName.equals(monthNames[i])){
                return i;
            }
        }
        return -1;
    }

    public static String getCurrentDay(){
        try {
            Date date = getCurrentTimeFromNTP();
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd");
            return dateFormat.format(date);
        } catch (Exception e) {
            // Handle exception as per your requirement
            return null;
        }
    }

    public static String getCurrentYear(){
        try {
            Date date = getCurrentTimeFromNTP();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy");
            return dateFormat.format(date);
        } catch (Exception e) {
            // Handle exception as per your requirement
            return null;
        }
    }

    public static String getCurrentTime(){
        try {
            Date date = getCurrentTimeFromNTP();
            SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
            return dateFormat.format(date);
        } catch (Exception e) {
            // Handle exception as per your requirement
            return null;
        }
    }
    public static String getCurrentTimeInMilitary(){
        try {
            Date date = getCurrentTimeFromNTP();
            SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
            return dateFormat.format(date);
        } catch (Exception e) {
            // Handle exception as per your requirement
            return null;
        }
    }
}